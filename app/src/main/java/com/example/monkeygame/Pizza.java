package com.example.monkeygame;
import android.graphics.Bitmap;
import android.content.Context;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Pizza {

    Bitmap pizza[] = new Bitmap[3];
    int pizzaFrame = 0;
    int pizzaX, pizzaY, pizzaVelocity;
    Random random;

    public Pizza(Context context){
        pizza[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.pizza);
        pizza[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.pizza);
        pizza[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.pizza);
        random = new Random();
        resetPosition();
    }

    public Bitmap getPizza(int pizzaFrame){
        return pizza[pizzaFrame];
    }

    public int getPizzaWidth(){
        return pizza[0].getWidth();
    }

    public int getPizzaHeight(){
        return pizza[0].getHeight();
    }

    public void resetPosition(){
        pizzaX = random.nextInt(GameView.dWidth - getPizzaWidth());
        pizzaY = -200 + random.nextInt(600) * -1;
        pizzaVelocity = 10 + random.nextInt(16);

    }
}
