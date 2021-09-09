package dev.haguel.xo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import dev.haguel.xo.R;

public class WinnerActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvMessage;
    private TextView tvBackToMenu;
    private ConstraintLayout top;


    private boolean win;
    private String role;
    private String winningRole;
    private String winningTitle;
    private String message;
    private boolean isOnline;
    private String winner;

    private Animation topAnim, middleAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        tvTitle = findViewById(R.id.tvWinnerTitle);
        tvMessage = findViewById(R.id.tvMessage);
        tvBackToMenu = findViewById(R.id.tvBackToMenu);
        top = findViewById(R.id.layoutTopWinner);

        Intent intent = getIntent();
        isOnline = intent.getBooleanExtra("isOnline", isOnline);
        if (isOnline) {
            win = intent.getBooleanExtra("win", false);
            role = intent.getStringExtra("role");
        } else {
            winner = intent.getStringExtra("winner");
        }
        setWinnerScreen();

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_in_animation);
        middleAnim = AnimationUtils.loadAnimation(this, R.anim.middle_in_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_in_animation);


        tvBackToMenu.setOnClickListener(v->{
            startActivity(new Intent(this, MainActivity.class));
        });

    }



    private void setWinnerScreen(){
        if (isOnline) {
            if (win) {
                // if you won
                winningTitle = "YOU WON";
                message = "Congratulations,\nYou are the\nchampion.";
            } else {
                // if you lost
                if (role.equals("X")) {
                    winningRole = "O";
                } else {
                    winningRole = "X";
                }
                winningTitle = "YOU LOST";
                message = "Too bad...\n" + winningRole + "Won,\nTry again.";
            }

        } else {
            winningTitle = winner + " Wins";
            message = "You should\nplay again.";
        }
        tvTitle.setText(winningTitle);
        tvMessage.setText(message);

    }

    @Override
    protected void onResume() {
        super.onResume();
        top.startAnimation(topAnim);
        tvMessage.startAnimation(middleAnim);
        tvBackToMenu.startAnimation(bottomAnim);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}