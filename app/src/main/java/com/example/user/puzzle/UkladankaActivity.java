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

        //wsadzanie do tablicy wszystkich ImageView
        for (int i =0 ; i < 9; i++) {
            int res = getResources().getIdentifier("imageView"+i, "id", getPackageName());
            ImageList[i] = (ImageView) findViewById(res);
            tab[i]=i;
        }

        //akcelerometr
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);


        //Screen Size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        //image
        Puzzle = (ImageView) findViewById(R.id.Puzzle);
        Puzzle.setVisibility(View.INVISIBLE);
        drawable = (BitmapDrawable) Puzzle.getDrawable();
        srcBmp = drawable.getBitmap();
        bitmapList = splitBitmap(srcBmp, width, height);

        //mix
        Button mix = (Button) findViewById(R.id.button_mix);
        mix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zmiana[0]=imgs[8 - poziom - (pion*3)];
                zmiana[1]=imgs[8];
                imgs[8]=zmiana[0];
                imgs[8 - poziom - (pion*3)]=zmiana[1];
                poziom=0;
                pion=0;
                shake();
            }
        });

        //choose
        Button choose = (Button) findViewById(R.id.button_image);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        //show
        final Button show = (Button) findViewById(R.id.button_show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hide) {
                    Puzzle.setVisibility(View.VISIBLE);
                    hide=false;
                }
                else if(!hide) {
                    Puzzle.setVisibility(View.INVISIBLE);
                    hide=true;
                }

            }
        });

        //exit
        Button exit = (Button) findViewById(R.id.button_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.os.Process.killProcess(android.os.Process.myPid());
                Intent intentBack = new Intent(UkladankaActivity.this,MainActivity.class);
                startActivity(intentBack);
            }
        });


    }




    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.values[1]>=-3 && event.values[1]<3 && event.values[0]>=-3 && event.values[0]<3) {
            res=true;
        }

        /////////////////////////////////////////////////////////
        //left
        if(event.values[0]>=4) {
            if(poziom==2 && res==true) {
                zmiana[0]=imgs[6-(pion*3)];
                zmiana[1]=imgs[7-(pion*3)];
                ImageList[6-(pion*3)].setImageBitmap(zmiana[1]);
                ImageList[7-(pion*3)].setImageBitmap(zmiana[0]);
                imgs[6-(pion*3)]=zmiana[1];
                imgs[7-(pion*3)]=zmiana[0];
                poziom--;
                res=false;
            }
            if(poziom==1 && res==true) {
                zmiana[0]=imgs[7-(pion*3)];
                zmiana[1]=imgs[8-(pion*3)];
                ImageList[7-(pion*3)].setImageBitmap(zmiana[1]);
                ImageList[8-(pion*3)].setImageBitmap(zmiana[0]);
                imgs[7-(pion*3)]=zmiana[1];
                imgs[8-(pion*3)]=zmiana[0];
                poziom--;
                res=false;
            }

        }

        /////////////////////////////////////////////////////////
        //right - ok
        if(event.values[0]<=-4 && res==true){
            if(poziom < 2) {
                zmiana[0] = imgs[8-poziom-(pion*3)];
                zmiana[1] = imgs[7-poziom-(pion*3)];
                ImageList[8-poziom-(pion*3)].setImageBitmap(zmiana[1]);
                ImageList[7-poziom-(pion*3)].setImageBitmap(zmiana[0]);
                imgs[8-poziom-(pion*3)]=zmiana[1];
                imgs[7-poziom-(pion*3)]=zmiana[0];

                poziom++;
                res=false;
            }
        }

        //////////////////////////////////////////////////////////
        //down
        if(event.values[1]>=4 && res==true) {
            zmiana[0] = imgs[8 - poziom - (pion*3)];
            zmiana[1] = imgs[8 - poziom - ((pion+1)*3)];
            ImageList[8 - poziom - (pion*3)].setImageBitmap(zmiana[1]);
            ImageList[8 - poziom - ((pion+1)*3)].setImageBitmap(zmiana[0]);
            imgs[8 - poziom - (pion*3)]=zmiana[1];
            imgs[8 - poziom - ((pion+1)*3)]=zmiana[0];
            pion++;
            res = false;
        }

        /////////////////////////////////////////////////////////

        //up
        if(event.values[1]<=-4) {
            if(pion == 2 && res==true) {
                zmiana[0] = imgs[8 - poziom - (pion*3)];
                zmiana[1] = imgs[8 - poziom - ((pion-1)*3)];
                ImageList[8 - poziom - (pion*3)].setImageBitmap(zmiana[1]);
                ImageList[8 - poziom - ((pion-1)*3)].setImageBitmap(zmiana[0]);
                imgs[8 - poziom - (pion*3)]=zmiana[1];
                imgs[8 - poziom - ((pion-1)*3)]=zmiana[0];
                pion--;
                res = false;
            }

            if(pion == 1 && res==true) {
                zmiana[0] = imgs[8 - poziom - (pion*3)];
                zmiana[1] = imgs[8 - poziom - ((pion-1)*3)];
                ImageList[8 - poziom - (pion*3)].setImageBitmap(zmiana[1]);
                ImageList[8 - poziom - ((pion-1)*3)].setImageBitmap(zmiana[0]);
                imgs[8 - poziom - (pion*3)]=zmiana[1];
                imgs[8 - poziom - ((pion-1)*3)]=zmiana[0];
                pion--;
                res = false;
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not use
    }


    //siatka
    public Bitmap[] splitBitmap(Bitmap picture, int width, int height)
    {
        int width_piece = width/3;
        int height_piece = height/3;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture, width, height, true);
        imgs[0] = Bitmap.createBitmap(scaledBitmap, 0, 0, width_piece , height_piece);
        ImageList[0].setImageBitmap(imgs[0]);

        imgs[1] = Bitmap.createBitmap(scaledBitmap, width_piece, 0, width_piece, height_piece);
        ImageList[1].setImageBitmap(imgs[1]);

        imgs[2] = Bitmap.createBitmap(scaledBitmap,width_piece*2, 0, width_piece,height_piece);
        ImageList[2].setImageBitmap(imgs[2]);

        imgs[3] = Bitmap.createBitmap(scaledBitmap, 0, height_piece, width_piece, height_piece);
        ImageList[3].setImageBitmap(imgs[3]);

        imgs[4] = Bitmap.createBitmap(scaledBitmap, width_piece, height_piece, width_piece,height_piece);
        ImageList[4].setImageBitmap(imgs[4]);

        imgs[5] = Bitmap.createBitmap(scaledBitmap, width_piece*2, height_piece, width_piece,height_piece);
        ImageList[5].setImageBitmap(imgs[5]);

        imgs[6] = Bitmap.createBitmap(scaledBitmap, 0, height_piece*2, width_piece,height_piece);
        ImageList[6].setImageBitmap(imgs[6]);

        imgs[7] = Bitmap.createBitmap(scaledBitmap, width_piece, height_piece*2, width_piece,height_piece);
        ImageList[7].setImageBitmap(imgs[7]);

        // imgs[8] = Bitmap.createBitmap(scaledBitmap, width_piece*2,height_piece*2, width_piece,height_piece);
        imgs[8]=imgs_zapas[8];
        ImageList[8].setImageBitmap(imgs[8]);
        poziom=0;
        pion=0;
        return imgs;
    }

    //mix
    public void shake() {
        tab[0]=6;
        int n;
        Random rand = new Random();
        for(int i=0;i<8;i++) {
            n = rand.nextInt(8);
            for (int ile = 0; ile <i; ile++) {
                if (tab[ile] != n) tab[i] = n;
                else {
                    i--;
                    ile--;
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            imgs_zapas[i]=imgs[i];
        }

        for (int i = 0; i < 9; i++) {
            ImageList[i].setImageBitmap(imgs_zapas[tab[i]]);
            imgs[i]=imgs_zapas[tab[i]];
        }
    }

    //gallery
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requeastCode, int resultCode, Intent data) {
        super.onActivityResult(requeastCode, resultCode,data);
        if(resultCode==RESULT_OK && requeastCode == PICK_IMAGE) {
            imageUri = data.getData();
            Puzzle.setImageURI(imageUri);
            //załadowanie nowego obrazku
            BitmapDrawable drawable = (BitmapDrawable) Puzzle.getDrawable();
            Bitmap srcBmp = drawable.getBitmap();
            bitmapList = splitBitmap(srcBmp, width, height);
        }
    }

}