package com.example.user.puzzle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static java.lang.Math.abs;

public class PuzzleActivity extends AppCompatActivity {

    ArrayList<PuzzlePiece> parts;
    int click = 0;
    ImageView img,sampleImage;
    private Sensor mySensor;
    public static  Uri imageUri;
    private static final int PICK_IMAGE=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        final RelativeLayout layout = findViewById(R.id.layout);
        final ImageView sampleImage = findViewById(R.id.sampleImage);

        final ImageButton showBtn = (ImageButton) findViewById(R.id.showBtn);
        final ImageButton loadBtn = (ImageButton) findViewById(R.id.loadBtn);
        final ImageButton exitBtn = (ImageButton) findViewById(R.id.exitBtn);
        final Button load = (Button) findViewById(R.id.load);

        img = findViewById(R.id.imageView);
//zdarzenia dla przycisków
        showBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (click == 0) {
                    sampleImage.bringToFront();
                    sampleImage.setVisibility(View.VISIBLE);
                    click = 1;
                } else {
                    sampleImage.setVisibility(View.INVISIBLE);
                    click = 0;
                }
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(PuzzleActivity.this, MainActivity.class);
                startActivity(intentBack);


            }
        });

        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                for (PuzzlePiece piece : parts) {

                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                    lParams.leftMargin = new Random().nextInt(layout.getWidth() - piece.pieceWidth);
                    lParams.topMargin = layout.getHeight() - piece.pieceHeight;
                    piece.setLayoutParams(lParams);
                }
            }                                       ;
        });


        load.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (click == 0) {

                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, PICK_IMAGE);
                    load.setText("Play");
                    click = 2;
                } else if(click ==2){
                    img.post(new Runnable() {
                        @Override
                        public void run() {
                            sampleImage.setImageURI(imageUri);

                            parts = split();

                            TouchListener touchListener = new TouchListener(PuzzleActivity.this);
//wymieszanie puzzlo
                            Collections.shuffle(parts);
                            for (PuzzlePiece piece : parts) {
                                piece.setOnTouchListener(touchListener);
                                layout.addView(piece);
//umieszczenie puzzli na dole ekranu
                                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                                lParams.leftMargin = new Random().nextInt(layout.getWidth() - piece.pieceWidth);
                                lParams.topMargin = layout.getHeight() - piece.pieceHeight;
                                piece.setLayoutParams(lParams);
                            }
                            ;
                        }

                        ;
                    });
                    load.setText("Again");

                    click = 3;

                }
                else{
                    Intent intentBack = new Intent(PuzzleActivity.this, PuzzleActivity.class);
                    startActivity(intentBack);

                    click = 0;
                    load.setText("Load");
                }
                ;

            }

            ;

//start gry, wywołanie metod

        });
    };
    public ArrayList<PuzzlePiece> split() {
        int piecesNumber = 12;
        int rows = 4;
        int cols = 3;

        ArrayList<PuzzlePiece> pieces = new ArrayList<>(piecesNumber);

        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int[] wym = position(img);
        int puzzleLeft = wym[0];
        int puzzleTop = wym[1];
        int puzzleWidth = wym[2];
        int puzzleHeight = wym[3];

        int croppedImageWidth = puzzleWidth - 2 * abs(puzzleLeft);
        int croppedImageHeight = puzzleHeight - 2 * abs(puzzleTop);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, puzzleWidth, puzzleHeight, true);
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, abs(puzzleLeft), abs(puzzleTop), croppedImageWidth, croppedImageHeight);
//obl wys i szer
        int pieceWidth = croppedImageWidth/cols;
        int pieceHeight = croppedImageHeight/rows;
//dodanie częśći puzzli do tablicy
        int wspY = 0;
        for (int row = 0; row < rows; row++) {
            int wspX = 0;
            for (int col = 0; col < cols; col++) {
                int odlX = 0;
                int odlY = 0;
                if (col > 0) {
                    odlX = pieceWidth / 12;
                }
                if (row > 0) {
                    odlY = pieceHeight / 12;
                }

                Bitmap pieceBitmap = Bitmap.createBitmap(croppedBitmap, wspX - odlX, wspY - odlY, pieceWidth + odlX, pieceHeight + odlY);
                PuzzlePiece piece = new PuzzlePiece(getApplicationContext());
                piece.setImageBitmap(pieceBitmap);
                piece.wspX = wspX - odlX + img.getLeft();
                piece.wspY = wspY - odlY + img.getTop();
                piece.pieceWidth = pieceWidth + odlX;
                piece.pieceHeight = pieceHeight + odlY;
                pieces.add(piece);
                wspX += pieceWidth;
            }
            wspY += pieceHeight;
        }

        return pieces;
    }


    private int[] position(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        final int actualWidth = Math.round(origW * scaleX);
        final int actualHeight = Math.round(origH * scaleY);

        ret[2] = actualWidth;
        ret[3] = actualHeight;
//pozycja obrazka
        int horizontal = imageView.getWidth();
        int vertical = imageView.getHeight();

        int top = (int) (vertical - actualHeight)/2;
        int left = (int) (horizontal - actualWidth)/2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }


    public void finish() {
//spr czy gameover
        if (GameOver()) {
            Toast.makeText(PuzzleActivity.this,"Wygrałeś!",Toast.LENGTH_SHORT).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            Intent intentBack = new Intent(PuzzleActivity.this, MainActivity.class);
                            startActivity(intentBack);
                        }
                    },
                    1000);

        }
    }

    private boolean GameOver() {
        for (PuzzlePiece piece : parts) {
            if (piece.canMove) {
                return false;
            }
        }

        return true;
    }




    @Override
    protected void onActivityResult(int requeastCode, int resultCode, Intent data) {
        super.onActivityResult(requeastCode, resultCode,data);
        if(resultCode==RESULT_OK && requeastCode == PICK_IMAGE) {
            imageUri = data.getData();
            img.setImageURI(imageUri);
        }
    }
}
