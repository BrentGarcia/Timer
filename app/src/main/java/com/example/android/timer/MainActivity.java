package com.example.android.timer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText editText;

    TextView textView;

    TextView restTextView;

    Button button;

    Button restButton;

    boolean clockIsRunning = false;
    boolean restClockIsRunning = false;

    int savedTime = 0;

    int savedRestTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        restButton = (Button) findViewById(R.id.restButton);
        textView = (TextView) findViewById(R.id.textView);
        restTextView = (TextView) findViewById(R.id.restTextView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Checks if the clock is currently running
                if (button.getText() == "Pause"){
                    clockIsRunning = true;
                }

//                 Checks if timer has been canceled
                if (button.getText() == "Start"){
                    clockIsRunning = false;
                }

                button.setText("Pause");
                String text = editText.getText().toString();
                int workHours = Integer.valueOf(text) * 3600000;

                if(!text.equalsIgnoreCase("") && (clockIsRunning == false)){

                    // Set work Hours
                    int hours = Integer.valueOf(text) * 3600000;

                    if (savedTime != 0){
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
                            textView.setText("Work Week Hours Remaining: \n" + h + "h " + m + "m " + s + "s" );
//                            workTextView.setText("Work Week Hours Remaining: \n" + h2 + "h " + m2 + "m " + s2 + "s" );
//                            restTextView.setText("Sleep Hours Remaining: \n" + h3 + "h " + m3 + "m " + s3 + "s" );


                            //Check if clock is already running
                            if (clockIsRunning == true){
                                savedTime = (int)millisUntilFinished;
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
    }
}
