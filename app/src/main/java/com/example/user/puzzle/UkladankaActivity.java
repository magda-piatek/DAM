package com.example.user.puzzle;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class UkladankaActivity extends AppCompatActivity implements SensorEventListener {

    //akcelerometr
    private Sensor mySensor;
    private SensorManager SM;
    //image
    BitmapDrawable drawable;
    Bitmap srcBmp;
    ImageView Puzzle;
    Bitmap[] bitmapList = new Bitmap[9];
    Bitmap[] imgs = new Bitmap[9];
    Bitmap[] imgs_zapas = new Bitmap[9];
    Bitmap[] zmiana = new Bitmap[2];
    ImageView[] ImageList =new ImageView[9];
    int[] tab = new int[9];
    int poziom=0, pion=0, width, height;
    boolean res=true, hide=true;
    Uri imageUri;
    private static final int PICK_IMAGE=100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ukladanka);
        hideNavigation();
}