package com.example.sportsmate;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameHolder>{
    class GameHolder extends RecyclerView.ViewHolder{

        private TextView title, date, joined;
        private AppCompatButton button;
        private ImageView imageView;
         GameHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_game_title);
            date = itemView.findViewById(R.id.txt_game_date);
            joined = itemView.findViewById(R.id.txt_game_joined);
            button = itemView.findViewById(R.id.btn_joined);
            imageView = itemView.findViewById(R.id.exit_games);
        }
    }

    private List<GameModel> gameModels;
    private Context context;
    private String userID;
    private boolean joined = false;
    private SQLiteDatabase database;
    private int fragment;

    public GameAdapter(List<GameModel> gameModels, Context context, String userID, int fragment) {
        this.gameModels = gameModels;
        this.context = context;
        this.userID = userID;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public GameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GameHolder(LayoutInflater.from(context).inflate(R.layout.game_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GameHolder holder, int position) {
        final GameModel model = gameModels.get(position);

        if(fragment == 0){
            holder.imageView.setVisibility(View.VISIBLE);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = context.openOrCreateDatabase("GAMES",Context.MODE_PRIVATE,null);
                String updateQuery = "UPDATE games SET game_joined = ? WHERE id = ?";
                database.execSQL(updateQuery,new Object[]{"No",model.get_ID()});
                reload("User");
            }
        });

        holder.title.setText(model.getTitle());
        holder.date.setText("CREATED: "+model.getDate());
        joined = checkMember(model.getJoined());
        if(joined){
        holder.joined.setText("Already member");
        holder.button.setText("Joined");
        holder.button.setBackgroundResource(android.R.color.holo_red_light);
        holder.button.setEnabled(false);
        }
        else {
            holder.joined.setText("Not A member");
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = context.openOrCreateDatabase("GAMES",Context.MODE_PRIVATE,null);
                String updateQuery = "UPDATE games SET game_joined = ? WHERE id = ?";
                database.execSQL(updateQuery,new Object[]{"Yes",model.get_ID()});
                reload("Nothing");
            }
        });
    }



    private boolean checkMember(String model){
        return model.equals("Yes");
    }

    @Override
    public int getItemCount() {
        return gameModels.size();
    }

    private void reload(String reloadFrom){
        Cursor cursorGames;
        if(reloadFrom.equals("User")){
            cursorGames = database.rawQuery("SELECT * FROM games WHERE game_joined = ?",new String[]{"Yes"});
        }else{
            cursorGames = database.rawQuery("SELECT * FROM games",null);
        }
        gameModels.clear();

        if(cursorGames.moveToFirst()){
            do{
                GameModel model = new GameModel();
                model.set_ID(cursorGames.getInt(0));
                model.setCreatorID(cursorGames.getString(3));
                model.setDate(cursorGames.getString(2));
                model.setTitle(cursorGames.getString(1));
                model.setJoined(cursorGames.getString(4));
                model.setPrice(cursorGames.getString(6));
                model.setTime(cursorGames.getString(5));
                gameModels.add(model);
            }while (cursorGames.moveToNext());
        }
        cursorGames.close();
        notifyDataSetChanged();
    }

}
