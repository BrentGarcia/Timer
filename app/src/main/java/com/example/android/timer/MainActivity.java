package com.example.android.timer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    EditText editWorkText;
    EditText editRestText;

    TextView textView;
    TextView restTextView;
    TextView freeTextView;

    Button button;
    Button restButton;
    Button editWorkButton;
    Button editRestButton;

    boolean clockIsRunning = false;
    boolean restClockIsRunning = false;

    private static final int HOURS_IN_WEEK = 168 * 3600000;
    private static final int REST_HOURS_IN_WEEK = 56 * 3600000;

    int savedTime = 0;
    int freeHours = 0;
    int savedRestTime = 0;
    int savedFreeTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        editWorkText = (EditText) findViewById(R.id.editWorkText);
        editRestText = (EditText) findViewById(R.id.editRestText);

        button = (Button) findViewById(R.id.button);
        restButton = (Button) findViewById(R.id.restButton);
        editWorkButton = (Button) findViewById(R.id.editWorkButton);
        editRestButton = (Button) findViewById(R.id.editRestButton);


        textView = (TextView) findViewById(R.id.textView);
        restTextView = (TextView) findViewById(R.id.restTextView);
        freeTextView = (TextView) findViewById(R.id.freeTextView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Checks if the clock is currently running
                if (button.getText() == "Pause"){
                    clockIsRunning = true;
                }

                // Checks if timer has been canceled
                if (button.getText() == "Start"){
                    clockIsRunning = false;
                }

                button.setText("Pause");
                String text = editText.getText().toString();
                int workHours = Integer.valueOf(text) * 3600000;
//                int freeHours = HOURS_IN_WEEK - workHours;

                if(!text.equalsIgnoreCase("") && (clockIsRunning == false)){

                    // Set work Hours
                    int hours = Integer.valueOf(text) * 3600000;
                    freeHours = HOURS_IN_WEEK - hours - REST_HOURS_IN_WEEK;

                    if (savedTime != 0){
                        hours = savedTime;
                        freeHours = savedFreeTime;
                    }


                    CountDownTimer countDownTimer = new CountDownTimer(hours,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                            //Translate Hours/Min/Seconds from millis
                            int h = (int) ((millisUntilFinished/(1000*60*60)));
                            int s = (int)(millisUntilFinished/1000) % 60;
                            int m = (int)(millisUntilFinished/(1000*60)) % 60;

                            //Print Time Remaining
                            textView.setText("Work Week Hours Remaining: \n" + h + "h " + m + "m " + s + "s" );


                            //Check if clock is already running
                            if (clockIsRunning == true){
                                savedTime = (int)millisUntilFinished;
                                startFreeTimer(freeHours);
                                button.setText("Start");
                                cancel();
                            }
                        }

                        @Override
                        public void onFinish() {
                            textView.setText("Congratulations! You've Finished Your Motherfuckin Work Week!");
                        }
                    }.start();
                }
            }
        });

        //Rest Button
        restButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Checks if the clock is currently running
                if (restButton.getText() == "Pause"){
                    restClockIsRunning = true;
                }

//                 Checks if timer has been canceled
                if (restButton.getText() == "Start"){
                    restClockIsRunning = false;
                }

                restButton.setText("Pause");

                if(restClockIsRunning == false){

                    // Set work Hours
                    int restHours = 56 * 3600000;

                    if (savedRestTime != 0){
                        restHours = savedRestTime;
                        freeHours = savedFreeTime;
                    }


                    CountDownTimer countDownTimer = new CountDownTimer(restHours,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                            //Translate Hours/Min/Seconds from millis
                            int h = (int) ((millisUntilFinished/(1000*60*60)));
                            int s = (int)(millisUntilFinished/1000) % 60;
                            int m = (int)(millisUntilFinished/(1000*60)) % 60;

                            //Print Time Remaining
                            restTextView.setText("Rest Hours Remaining: \n" + h + "h " + m + "m " + s + "s" );


                            //Check if clock is already running
                            if (restClockIsRunning == true){
                                savedRestTime = (int)millisUntilFinished;
                                startFreeTimer(freeHours);
                                restButton.setText("Start");
                                cancel();
                            }
                        }

                        @Override
                        public void onFinish() {
                            restTextView.setText("No more rest time!");
                        }
                    }.start();
                }
            }
        });

        editWorkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editWorkButton.getText().toString().equalsIgnoreCase("Edit Work Time")){
                    editWorkButton.setText("Set New Work Time");
                    editWorkText.setVisibility(View.VISIBLE);
                } else if (editWorkButton.getText().toString().equalsIgnoreCase("Set New Work Time")){
                    String tempText = editWorkText.getText().toString();
                    Log.d("tempText", tempText);
                    savedTime = Integer.valueOf(tempText) * 3600000;
                    editWorkButton.setText("Edit Work Time");
                    editWorkText.setVisibility(View.INVISIBLE);
                }

            }
        });

        editRestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editRestButton.getText().toString().equalsIgnoreCase("Edit Rest Time")){
                    editRestButton.setText("Set New Rest Time");
                    editRestText.setVisibility(View.VISIBLE);
                } else if (editRestButton.getText().toString().equalsIgnoreCase("Set New Rest Time")){
                    String tempText = editRestText.getText().toString();
                    savedRestTime = Integer.valueOf(tempText) * 3600000;
                    editRestButton.setText("Edit Rest Time");
                    editRestText.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void startFreeTimer(int freeHours) {
        CountDownTimer countDownTimer = new CountDownTimer(freeHours,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                //Translate Hours/Min/Seconds from millis
                int h = (int) ((millisUntilFinished/(1000*60*60)));
                int s = (int)(millisUntilFinished/1000) % 60;
                int m = (int)(millisUntilFinished/(1000*60)) % 60;

                //Print Time Remaining
                freeTextView.setText("Free Hours Remaining: \n" + h + "h " + m + "m " + s + "s" );

                savedFreeTime = (int) millisUntilFinished;
                //Check if clock is already running
                if (button.getText() == "Pause" || restButton.getText() == "Pause") {
                    savedFreeTime = (int) millisUntilFinished;
                    cancel();
                }

            }

            @Override
            public void onFinish() {
                freeTextView.setText("No More play time!");
            }
        }.start();
    }
}
