package com.example.user.puzzle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        final RelativeLayout layout = findViewById(R.id.layout);
        final ImageView imageView = findViewById(R.id.imageView);
        final ImageView sampleImage = findViewById(R.id.sampleImage);

        final ImageButton showBtn = (ImageButton) findViewById(R.id.showBtn);
        final ImageButton loadBtn = (ImageButton) findViewById(R.id.loadBtn);
        final ImageButton exitBtn = (ImageButton) findViewById(R.id.exitBtn);


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
            public void onClick(View v) {
                for (PuzzlePiece piece : parts) {

                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                    lParams.leftMargin = new Random().nextInt(layout.getWidth() - piece.pieceWidth);
                    lParams.topMargin = layout.getHeight() - piece.pieceHeight;
                    piece.setLayoutParams(lParams);
                }
            }
            ;
        });


        imageView.post(new Runnable() {
            @Override
            public void run() {
                parts = split();
                TouchListener touchListener = new TouchListener(PuzzleActivity.this);

                Collections.shuffle(parts);
                for (PuzzlePiece piece : parts) {
                    piece.setOnTouchListener(touchListener);
                    layout.addView(piece);

                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                    lParams.leftMargin = new Random().nextInt(layout.getWidth() - piece.pieceWidth);
                    lParams.topMargin = layout.getHeight() - piece.pieceHeight;
                    piece.setLayoutParams(lParams);
                }
            }
        });
    }

    private ArrayList<PuzzlePiece> split() {
        int piecesNumber = 12;
        int rows = 4;
        int cols = 3;

        ImageView imageView = findViewById(R.id.imageView);
        ArrayList<PuzzlePiece> pieces = new ArrayList<>(piecesNumber);

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int[] dimensions = position(imageView);
        int scaledBitmapLeft = dimensions[0];
        int scaledBitmapTop = dimensions[1];
        int scaledBitmapWidth = dimensions[2];
        int scaledBitmapHeight = dimensions[3];

        int croppedImageWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft);
        int croppedImageHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true);
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, abs(scaledBitmapLeft), abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight);

        int pieceWidth = croppedImageWidth/cols;
        int pieceHeight = croppedImageHeight/rows;

        int y = 0;
        for (int row = 0; row < rows; row++) {
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {
                int odlX = 0;
                int odlY = 0;
                if (col > 0) {
                    odlX = pieceWidth / 8;
                }
                if (row > 0) {
                    odlY = pieceHeight / 8;
                }

            Bitmap pieceBitmap = Bitmap.createBitmap(croppedBitmap, xCoord - odlX, y - odlY, pieceWidth + odlX, pieceHeight + odlY);
            PuzzlePiece piece = new PuzzlePiece(getApplicationContext());
            piece.setImageBitmap(pieceBitmap);
            piece.x = xCoord - odlX + imageView.getLeft();
            piece.y = y - odlY + imageView.getTop();
            piece.pieceWidth = pieceWidth + odlX;
            piece.pieceHeight = pieceHeight + odlY;
            pieces.add(piece);
            xCoord += pieceWidth;
            }
            y += pieceHeight;
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

        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH)/2;
        int left = (int) (imgViewW - actW)/2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }


    public void finish() {
        if (GameOver()) {
            Toast.makeText(PuzzleActivity.this,"Wygrałeś",Toast.LENGTH_SHORT).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            finish();
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
}
