package com.example.monkeygame;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Random;


public class GameView extends View {

    Bitmap background, ground, monkey;
    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 5;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    float TEXT_SIZE = 120;
    int points = 0;
    int life = 3;
    static int dWidth, dHeight;
    Random random;
    float monkeyX, monkeyY;
    float oldX;
    float oldY;
    float oldMonkeyX;
    float oldMonkeyY;
    ArrayList<Pizza> pizzas;

    public GameView(Context context) {
        super(context);
        this.context = context;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.jungle);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        monkey = BitmapFactory.decodeResource(getResources(), R.drawable.apina);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rectBackground = new Rect(0, 0, dWidth, dHeight);
        rectGround = new Rect(0, dHeight - ground.getHeight(), dWidth, dHeight);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        textPaint.setColor(Color.rgb(255, 165, 0));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        healthPaint.setColor(Color.GREEN);
        random = new Random();
        monkeyX = dWidth / 2 - monkey.getWidth() / 2;
        monkeyY = dHeight - ground.getHeight() - monkey.getHeight();
        pizzas = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Pizza pizza = new Pizza(context);
            pizzas.add(pizza);
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground, null, rectGround, null);
        canvas.drawBitmap(monkey, monkeyX, monkeyY, null);
        for (int i = 0; i < pizzas.size(); i++) {
            canvas.drawBitmap(pizzas.get(i).getPizza(pizzas.get(i).pizzaFrame), pizzas.get(i).pizzaX, pizzas.get(i).pizzaY, null);
            pizzas.get(i).pizzaFrame++;
            if (pizzas.get(i).pizzaFrame > 2) {
                pizzas.get(i).pizzaFrame = 0;
            }
            pizzas.get(i).pizzaY += pizzas.get(i).pizzaVelocity;
            if (pizzas.get(i).pizzaY + pizzas.get(i).getPizzaHeight() >= dHeight - ground.getHeight()) {
                life--;
                pizzas.get(i).resetPosition();
                if (life == 0) {
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        }

        for (int i = 0; i < pizzas.size(); i++) {
            if (pizzas.get(i).pizzaX + pizzas.get(i).getPizzaWidth() >= monkeyX
                    && pizzas.get(i).pizzaX <= monkeyX + monkey.getWidth()
                    && pizzas.get(i).pizzaY + pizzas.get(i).getPizzaWidth() >= monkeyY
                    && pizzas.get(i).pizzaY + pizzas.get(i).getPizzaWidth() <= monkeyY + monkey.getHeight()) {
                points += 1;
                pizzas.get(i).resetPosition();

            }
        }
        if (life == 2) {
            healthPaint.setColor(Color.YELLOW);
        } else if (life == 1) {
            healthPaint.setColor(Color.RED);
        }
        canvas.drawRect(dWidth - 200, 30, dWidth - 200 + 60 * life, 80, healthPaint);
        canvas.drawText("" + points, 20, TEXT_SIZE, textPaint);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if (touchY >= monkeyY) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                oldX = event.getX();
                oldMonkeyX = monkeyX;
            }
            if (action == MotionEvent.ACTION_MOVE) {
                float shift = oldX - touchX;
                float newMonkeyX = oldMonkeyX - shift;
                if (newMonkeyX <= 0)
                    monkeyX = 0;
                else if (newMonkeyX >= dWidth - monkey.getWidth())
                    monkeyX = dWidth - monkey.getWidth();
                else
                    monkeyX = newMonkeyX;
            }
        }
        return true;
    }
}