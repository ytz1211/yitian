package com.example.politicgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.politicgame.UserActivity.LoginActivity.LoggedInActivity;

public class PauseActivity extends GameActivity{

    private final int DEFAULT_CODE = 0;
    private final int RESUME_CODE = 1;
    private final int QUIT_CODE = 2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);

        setTitle("Paused");

        //Resume button
        final Button resumeB = findViewById(R.id.resume);
        resumeB.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        returnRequest(RESUME_CODE);
                    }
                });

        //Quit to main menu button
        final Button quitB = findViewById(R.id.sign_out);
        quitB.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        returnRequest(QUIT_CODE);
                    }
                });

        //Quit to logged in menu button
        final Button loggedInB = findViewById(R.id.pause_menu_load_screen);
        loggedInB.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        openLoggedInActivity();
                    }
                });

    }


    /**
     * Open LoggedInActivity
     */
    private void openLoggedInActivity(){
        Intent LoggedInActivityIntent = new Intent(this, LoggedInActivity.class);
        startActivity(LoggedInActivityIntent);
        finish();
    }


    /**
     * Return to the previous menu
     *
     * @param requestCode
     */
    private void returnRequest(int requestCode){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("result", requestCode);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
