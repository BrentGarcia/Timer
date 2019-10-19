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

    EditText editStartText, editWorkText, editRestText, editFreeText;

    TextView workTextView, restTextView, freeTextView;

    Button startButton, restButton, editWorkButton, editRestButton, editFreeButton;

    boolean clockIsRunning = false;
    boolean restClockIsRunning = false;

    private static final int HOURS_IN_WEEK = 168 * 3600000;
    private static final int REST_HOURS_IN_WEEK = 56 * 3600000;

    int savedTime = 0;
    int savedRestTime = 0;
    int savedFreeTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editStartText = (EditText) findViewById(R.id.editStartText);
        editWorkText = (EditText) findViewById(R.id.editWorkText);
        editRestText = (EditText) findViewById(R.id.editRestText);
        editFreeText = (EditText) findViewById(R.id.editFreeText);

        startButton = (Button) findViewById(R.id.startButton);
        restButton = (Button) findViewById(R.id.restButton);
        editWorkButton = (Button) findViewById(R.id.editWorkButton);
        editRestButton = (Button) findViewById(R.id.editRestButton);
        editFreeButton = (Button) findViewById(R.id.editFreeButton);


        workTextView = (TextView) findViewById(R.id.workTextView);
        restTextView = (TextView) findViewById(R.id.restTextView);
        freeTextView = (TextView) findViewById(R.id.freeTextView);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Checks if the clock is currently running
                if (startButton.getText() == "Pause"){
                    clockIsRunning = true;
                }

                // Checks if timer has been canceled
                if (startButton.getText() == "Start"){
                    clockIsRunning = false;
                }

                startButton.setText("Pause");
                String text = editStartText.getText().toString();

                if((text.length() > 0) && (clockIsRunning == false)){

                    // Set work Hours
                    int hours = Integer.valueOf(text) * 3600000;

                    if (savedTime == 0) {
                        savedFreeTime = HOURS_IN_WEEK - hours - REST_HOURS_IN_WEEK;
                    } else if (savedTime != 0) {
                        hours = savedTime;
                    }


                    CountDownTimer countDownTimer = new CountDownTimer(hours,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                            //Translate Hours/Min/Seconds from millis
                            int h = (int) ((millisUntilFinished/(1000*60*60)));
                            int s = (int)(millisUntilFinished/1000) % 60;
                            int m = (int)(millisUntilFinished/(1000*60)) % 60;

                            //Print Time Remaining
                            workTextView.setText("Work Week Hours Remaining: \n" + h + "h " + m + "m " + s + "s" );


                            //Check if clock is already running
                            if (clockIsRunning == true){
                                savedTime = (int)millisUntilFinished;
                                startButton.setText("Start");
                                startFreeTimer(savedFreeTime);
                                cancel();
                            }
                        }

                        @Override
                        public void onFinish() {
                            workTextView.setText("Congratulations! You've Finished Your Motherfuckin Work Week!");
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
                                restButton.setText("Start");
                                startFreeTimer(savedFreeTime);
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

        editFreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editFreeButton.getText().toString().equalsIgnoreCase("Edit Free Time")){
                    editFreeButton.setText("Set New Free Time");
                    editFreeText.setVisibility(View.VISIBLE);
                } else if (editFreeButton.getText().toString().equalsIgnoreCase("Set New Free Time")){
                    String tempText = editFreeText.getText().toString();
                    savedFreeTime = Integer.valueOf(tempText) * 3600000;
                    Log.d("tempText", tempText);
                    editFreeButton.setText("Edit Free Time");
                    editFreeText.setVisibility(View.INVISIBLE);
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
                if (startButton.getText() == "Pause" || restButton.getText() == "Pause") {
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
