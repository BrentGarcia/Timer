package com.example.android.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
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
    private boolean reloading;

    private static final int HOURS_IN_WEEK = 168 * 3600000;
    private static final int REST_HOURS_IN_WEEK = 56 * 3600000;

    int savedWorkTime, savedRestTime, savedFreeTime;
    int loadWorkTime, loadRestTime, loadFreeTime;
    int count;

    private String currentWorkTimeText, currentRestTimeText, currentFreeTimeText;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String CURRENT_WORK_TIME = "work time";
    public static final String CURRENT_WORK_TIME_TEXT = "work time text";
    public static final String CURRENT_REST_TIME = "rest time";
    public static final String CURRENT_REST_TIME_TEXT = "rest time text";
    public static final String CURRENT_FREE_TIME = "free time";
    public static final String CURRENT_FREE_TIME_TEXT = "free time text";
    public static final String RELOADING = "reloading";

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


        //Start Button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checks if the clock is currently running or is paused

                Log.d("INSIDE CLICK", "onClick: " + reloading);
                Log.d("INSIDE CLICK", "savedwork time: " + savedWorkTime);
                    if (startButton.getText() == "Pause"){
                        clockIsRunning = true;
                    } else if (startButton.getText() == "Start"){
                        clockIsRunning = false;
                    }

                startButton.setText("Pause");

                if(clockIsRunning == false || reloading == true){

                    // convert intial work hours
                    int workHours = 0;

                    // Set free hours on first click & save work hours after that
                    if (savedWorkTime == 0) {
                        String startTimeText = editStartText.getText().toString();
                        workHours = Integer.valueOf(startTimeText) * 3600000;
                        savedFreeTime = HOURS_IN_WEEK - workHours - REST_HOURS_IN_WEEK;
                    } else if (savedWorkTime != 0) {
                        workHours = savedWorkTime;
//                        savedFreeTime = HOURS_IN_WEEK - workHours - REST_HOURS_IN_WEEK;
                    }

                    Log.d("WORKHOURS CALCULATED", "onClick: " + workHours);
                    // start work timer!
                    startWorkTimer(workHours);
                }
            }
        });

        //Rest Button
        restButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Checks if the clock is currently running or paused
                if (restButton.getText() == "Pause"){
                    restClockIsRunning = true;
                } else if (restButton.getText() == "Start"){
                    restClockIsRunning = false;
                }

                restButton.setText("Pause");

                if(restClockIsRunning == false || reloading == true){
                    // Set rest hours or load remaining rest hours
                    int restHours = 0;
                    Log.d("TAG", "onClick: savedRestTime = " + savedRestTime);
                    if (savedRestTime == 0) {
                        restHours = 56 * 3600000;
                    } else if (savedRestTime != 0) {
                        Log.d("TAG", "onClick: IM IN SAVED REST TIME");
                        restHours = savedRestTime;
                    }

                    // start rest timer!
                    startRestTimer(restHours);
                }
            }
        });

        editWorkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editWorkButton.getText().equals("Edit Work Time")){
                    editWorkButton.setText("Set New Work Time");
                    editWorkText.setVisibility(View.VISIBLE);
                } else if (editWorkButton.getText().equals("Set New Work Time")){
                    String tempText = editWorkText.getText().toString();
                    savedWorkTime = Integer.valueOf(tempText) * 3600000;
                    editWorkButton.setText("Edit Work Time");
                    editWorkText.setVisibility(View.INVISIBLE);
                }
            }
        });

        editRestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editRestButton.getText().equals("Edit Rest Time")){
                    editRestButton.setText("Set New Rest Time");
                    editRestText.setVisibility(View.VISIBLE);
                } else if (editRestButton.getText().equals("Set New Rest Time")){
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

        loadData();
        updateViews();
        Log.d("Checking data persistance", "onClick: " + reloading);
    } // END OF PROGRAM

    // Starts Free Timer
    private void startFreeTimer(int freeHours) {
        CountDownTimer countDownTimer = new CountDownTimer(freeHours,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                count++;

                if (count == 60){
                    saveData();
                    count = 0;
                }

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

    //Starts work timer
    private void startWorkTimer(int workHours){
        CountDownTimer countDownTimer = new CountDownTimer(workHours,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                count++;
                if (count == 60){
                    saveData();
                    count = 0;
                }

                //Translate Hours/Min/Seconds from millis
                int h = (int) ((millisUntilFinished/(1000*60*60)));
                int s = (int)(millisUntilFinished/1000) % 60;
                int m = (int)(millisUntilFinished/(1000*60)) % 60;

                //Print Time Remaining
                workTextView.setText("Work Week Hours Remaining: \n" + h + "h " + m + "m " + s + "s" );

                savedWorkTime = (int)millisUntilFinished;
                //Check if clock is already running
                if (clockIsRunning == true){
                    savedWorkTime = (int)millisUntilFinished;
                    startButton.setText("Start");
//                    saveData();
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

    //Starts rest timer
    private void startRestTimer(int restHours) {
        CountDownTimer countDownTimer = new CountDownTimer(restHours,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                count++;
                if (count == 60){
                    saveData();
                    count = 0;
                }

                //Translate Hours/Min/Seconds from millis
                int h = (int) ((millisUntilFinished/(1000*60*60)));
                int s = (int)(millisUntilFinished/1000) % 60;
                int m = (int)(millisUntilFinished/(1000*60)) % 60;

                //Print Time Remaining
                restTextView.setText("Rest Hours Remaining: \n" + h + "h " + m + "m " + s + "s" );

                savedRestTime = (int)millisUntilFinished;
                //Check if clock is already running
                if (restClockIsRunning == true){
                    savedRestTime = (int)millisUntilFinished;
                    restButton.setText("Start");
//                    saveData();
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

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


//        Log.d("savedWorkTime", "saveData: " + savedWorkTime);
        editor.putInt(CURRENT_WORK_TIME, savedWorkTime);
        editor.putString(CURRENT_WORK_TIME_TEXT, workTextView.getText().toString());
        editor.putBoolean(RELOADING, true);

        editor.putInt(CURRENT_REST_TIME, savedRestTime);
        editor.putString(CURRENT_REST_TIME_TEXT, restTextView.getText().toString());

        editor.putInt(CURRENT_FREE_TIME, savedFreeTime);
        editor.putString(CURRENT_FREE_TIME_TEXT, freeTextView.getText().toString());

        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

//        text = sharedPreferences.getString(TEXT, "");
//        completeYesNo = sharedPreferences.getBoolean(COMPLETE, false);
//        currentStreakText = sharedPreferences.getString(CURRENT_STREAK_TEXT, "");
//        loadStreak = sharedPreferences.getInt(CURRENT_STREAK, 0);

        currentWorkTimeText = sharedPreferences.getString(CURRENT_WORK_TIME_TEXT, "nothing");
        loadWorkTime = sharedPreferences.getInt(CURRENT_WORK_TIME, 0);

        currentRestTimeText = sharedPreferences.getString(CURRENT_REST_TIME_TEXT, "can't sit here");
        loadRestTime = sharedPreferences.getInt(CURRENT_REST_TIME, 0);

        currentFreeTimeText = sharedPreferences.getString(CURRENT_FREE_TIME_TEXT, "seats taken");
        loadFreeTime = sharedPreferences.getInt(CURRENT_FREE_TIME, 0);

        reloading = sharedPreferences.getBoolean(RELOADING, false);

    }

    private void updateViews() {
        workTextView.setText(currentWorkTimeText);
        restTextView.setText(currentRestTimeText);
        freeTextView.setText(currentFreeTimeText);
        savedWorkTime = loadWorkTime;
        savedRestTime = loadRestTime;
        savedFreeTime = loadFreeTime;
    }

}
