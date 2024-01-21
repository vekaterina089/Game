package com.example.balloonpopblast;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoopThread extends Thread {

    private static final int FPS = 30; // Число кадров в секунду
    private boolean isRunning = false; // Флаг для запуска/остановки игрового цикла
    private SurfaceHolder surfaceHolder; // Объект для управления отображением на экране
    private CircleSurfaceView circleSurfaceView; // Ссылка на объект CircleSurfaceView

    public GameLoopThread(CircleSurfaceView circleSurfaceView) {
        this.surfaceHolder = surfaceHolder;
        this.circleSurfaceView = circleSurfaceView;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void run() {
        long startTime; // Время начала выполнения итерации игрового цикла
        long timeMillis; // Время выполнения итерации игрового цикла
        long waitTime; // Время ожидания перед следующей итерацией игрового цикла

        long targetTime = 1000 / FPS; // Ожидаемое время выполнения одной итерации игрового цикла

        while (isRunning) {
            startTime = System.nanoTime();

            // Обновление игровой логики
            circleSurfaceView.update();

            // Отрисовка игровых объектов на экране
            circleSurfaceView.draw((Canvas) surfaceHolder);

            // Вычисление времени, затраченного на итерацию игрового цикла
            timeMillis = (System.nanoTime() - startTime) / 1000000;

            // Ожидание перед следующей итерацией, чтобы поддерживать заданную частоту кадров
            waitTime = targetTime - timeMillis;
            try {
                sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSurfaceSize(int width, int height) {
    }
}
