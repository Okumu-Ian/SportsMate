package com.example.sportsmate.ui.findgames;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsmate.GameAdapter;
import com.example.sportsmate.GameModel;
import com.example.sportsmate.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class FindGames extends Fragment {

    private FindGamesViewModel findGamesViewModel;
    SQLiteDatabase database;
    private List<GameModel> games;
    private GameAdapter adapter;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        database = getActivity().openOrCreateDatabase("GAMES", Context.MODE_PRIVATE,null);
        createTable();
        games = new ArrayList<>();
        findGamesViewModel =
                ViewModelProviders.of(this).get(FindGamesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_find_games, container, false);

        recyclerView =root.findViewById(R.id.games_list);
        fetchGames();
        return root;
    }

    private void createTable(){
        String createTable = "CREATE TABLE IF NOT EXISTS games(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "game_title VARCHAR(20) NOT NULL, " +
                "game_date VARCHAR(10) NOT NULL," +
                "game_creator VARCHAR(30) NOT NULL,"+
                "game_joined VARCHAR(30) NOT NUll)";
        database.execSQL(createTable);
    }

    private void fetchGames(){
        Cursor cursorGames = database.rawQuery("SELECT * FROM games",null);
        if(cursorGames.moveToFirst()){
            do{
                GameModel model = new GameModel();
                model.set_ID(cursorGames.getInt(0));
                model.setCreatorID(cursorGames.getString(3));
                model.setDate(cursorGames.getString(2));
                model.setTitle(cursorGames.getString(1));
                model.setJoined(cursorGames.getString(4));
                model.setTime(cursorGames.getString(5));
                model.setPrice(cursorGames.getString(6));
                games.add(model);
            }while (cursorGames.moveToNext());
        }
        cursorGames.close();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        adapter = new GameAdapter(games,getActivity(),uid,1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }
}