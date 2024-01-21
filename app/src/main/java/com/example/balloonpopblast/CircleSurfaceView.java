package com.example.balloonpopblast;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CircleSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private GameLoopThread gameLoopThread;
    private List<Circle> circles;
    private int score;

    public interface OnCirclePopListener {
        void onCirclePopped(int score);
    }

    private OnCirclePopListener onCirclePopListener;

    public CircleSurfaceView(Context context) {
        super(context);
        init();
    }

    public CircleSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        circles = new ArrayList<>();
        score = 0;

        gameLoopThread = new GameLoopThread(this);
    }

    public void setOnCirclePopListener(OnCirclePopListener listener) {
        onCirclePopListener = listener;
    }

    public void onResume() {
        gameLoopThread.setRunning(true);
        gameLoopThread.start();
    }

    public void onPause() {
        boolean retry = true;
        gameLoopThread.setRunning(false);
        while (retry) {
            try {
                gameLoopThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        spawnCircle();
        // Убедитесь, что gameLoopThread не запущен до его инициализации
        if (gameLoopThread == null) {
            gameLoopThread = new GameLoopThread(this);
            gameLoopThread.start();
        } else {
            gameLoopThread.setSurfaceSize(getWidth(), getHeight());
            gameLoopThread.setRunning(true);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        gameLoopThread.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameLoopThread.setRunning(false);
        while (retry) {
            try {
                gameLoopThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            synchronized (surfaceHolder) {
                for (Circle circle : circles) {
                    if (circle.contains(x, y)) {
                        // Шарик лопается, удаляем его и увеличиваем счет
                        circles.remove(circle);
                        score++;
                        if (onCirclePopListener != null) {
                            onCirclePopListener.onCirclePopped(score);
                        }
                        break;
                    }
                }
            }
        }
        return true;
    }

    private void spawnCircle() {
        int maxX = (int) (getWidth() - Circle.RADIUS * 2);
        int maxY = (int) (getHeight() - Circle.RADIUS * 2);

        // Проверка валидности значения ширины и высоты
        if (maxX <= 0 || maxY <= 0) {
            // Некорректные размеры, возврат без создания нового круга
            return;
        }

        Random random = new Random();
        float x = random.nextInt(maxX);
        float y = random.nextInt(maxY);

        int color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        float radius = Circle.RADIUS;
        int screenWidth = getWidth();
        int screenHeight = getHeight();

        circles.add(new Circle(x, y, radius, screenWidth, screenHeight, color));
    }
    public void update() {
        synchronized (surfaceHolder) {
            for (Circle circle : circles) {
                circle.update();
            }
            if (circles.size() < 10) {
                // Спавн нового шарика при необходимости
                spawnCircle();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            synchronized (surfaceHolder) {
                for (Circle circle : circles) {
                    circle.draw(canvas);
                }
            }
        }
    }
}