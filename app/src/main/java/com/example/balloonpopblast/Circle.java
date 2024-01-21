package com.example.balloonpopblast;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Circle {
    public static final float RADIUS = 1;
    private float x; // Координата x центра окружности
    private float y; // Координата y центра окружности
    private float radius; // Радиус окружности
    private float speedX; // Скорость перемещения по оси X
    private float speedY; // Скорость перемещения по оси Y
    private int screenWidth; // Ширина экрана
    private int screenHeight; // Высота экрана
    private Paint paint; // Кисть для отрисовки окружности

    public Circle(float x, float y, float radius, int screenWidth, int screenHeight, int color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        speedX = 0; // Начальная скорость по оси X
        speedY = 0; // Начальная скорость по оси Y

        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, paint);
    }

    // Геттеры и сеттеры для координат и радиуса окружности
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return radius;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    // Метод для обновления состояния окружности
    public void update() {
        // Логика обновления состояния окружности
        // Например, перемещение окружности
        x += speedX;
        y += speedY;

        // Можно добавить условия или ограничения для перемещения окружности
        // Например, проверка границ экрана и отражение от них
        if (x < radius || x > screenWidth - radius) {
            speedX *= -1; // изменяем направление по оси X
        }
        if (y < radius || y > screenHeight - radius) {
            speedY *= -1; // изменяем направление по оси Y
        }
    }

    public boolean contains(float x, float y) {
        // Вычисляем расстояние от переданных координат (x, y) до центра окружности
        float distance = (float) Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));

        // Если расстояние меньше радиуса окружности, значит точка находится внутри окружности
        return distance < radius;
    }
}