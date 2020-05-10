package com.fattiger.animproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fattiger.emotionrain.EmotionRainView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EmotionRainView emotion_rain_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emotion_rain_view = findViewById(R.id.emotion_rain_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRain();
    }

    public void startRain() {
        emotion_rain_view.start(getBitmaps());
    }

    public List<Bitmap> getBitmaps() {
        List<Bitmap> bitmaps = new ArrayList<>();
        bitmaps.add(BitmapFactory.decodeResource(getResources(),R.mipmap.pic1));
        bitmaps.add(BitmapFactory.decodeResource(getResources(),R.mipmap.pic2));
        bitmaps.add(BitmapFactory.decodeResource(getResources(),R.mipmap.pic3));
        return bitmaps;
    }

}
