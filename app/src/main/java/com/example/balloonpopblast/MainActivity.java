package com.example.balloonpopblast;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements CircleSurfaceView.OnCirclePopListener {

    private CircleSurfaceView circleSurfaceView;
    private TextView scoreTextView;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleSurfaceView = findViewById(R.id.circleSurfaceView);
        scoreTextView = findViewById(R.id.scoreTextView);
        Button resetButton = findViewById(R.id.resetButton);

        circleSurfaceView.setOnCirclePopListener(this);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        circleSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        circleSurfaceView.onPause();
    }

    @Override
    public void onCirclePopped(int score) {
        this.score = score;
        updateScoreText();
    }

    private void resetGame() {
        score = 0;
        updateScoreText();
    }

    private void updateScoreText() {
        scoreTextView.setText(getString(R.string.score_label, score));
    }
}