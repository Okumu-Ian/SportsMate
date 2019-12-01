package com.example.sportsmate.ui.creategame;

import android.app.TimePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.sportsmate.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class CreateGame extends Fragment {

    private CreateGameViewModel createGameViewModel;
    TextInputEditText title, date,price,time;
    Button submit;
    SQLiteDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        database = getActivity().openOrCreateDatabase("GAMES", Context.MODE_PRIVATE,null);
        createGameViewModel =
                ViewModelProviders.of(this).get(CreateGameViewModel.class);
        View root = inflater.inflate(R.layout.fragment_create_game, container, false);

        title = root.findViewById(R.id.game_title);
        date = root.findViewById(R.id.edt_game_date);
        price = root.findViewById(R.id.edt_game_price);
        time = root.findViewById(R.id.edt_game_time);
        submit = root.findViewById(R.id.button);


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selector;
                        if(selectedMinute < 10){
                            selector = "0"+selectedMinute;
                        }else{
                            selector = ""+selectedMinute;
                        }
                        time.setText( selectedHour + ":" + selector);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitGame(title.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        date.getText().toString(),time.getText().toString(),price.getText().toString());
            }
        });
        return root;
    }

    private void submitGame(String title_s,String user_s,String date_s,String time_s, String price_s){
        String inserQuery = "INSERT into games(game_title,game_date,game_creator,game_joined,game_time,game_price) VALUES (?,?,?,?,?,?)";
        database.execSQL(inserQuery,new Object[]{title_s,date_s,user_s,"No",time_s,price_s});
        title.setText("");
        date.setText("");
        price.setText("");
        time.setText("");
        Toast.makeText(getActivity(), "Successfully created game!", Toast.LENGTH_SHORT).show();
    }
}