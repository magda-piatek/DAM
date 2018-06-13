package com.example.user.puzzle;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Random;


public class MemoryActivity extends AppCompatActivity implements View.OnClickListener{

    private int numberOfelements;

    private MemoryButton[] buttons;

    private int[] buttonGraphicLocations;

    private int[] buttonGraphics;

    private  MemoryButton selectedButton1;
    private MemoryButton selectedButton2;

    private  boolean isBusy= false;

    private Button buttonBack;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        buttonBack=(Button)findViewById(R.id.buttonBack);


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent(MemoryActivity.this,MainActivity.class);
                startActivity(intentBack);
            }
        });

        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);



        Display display = getWindowManager().getDefaultDisplay();
        int widthScreen = display.getWidth();
        int heightScreen= display.getHeight();


        int numColumns = gridLayout.getColumnCount();
        int numRows = gridLayout.getRowCount();

        numberOfelements = numColumns * numRows;

        buttons = new MemoryButton[numberOfelements];

        buttonGraphics = new int[numberOfelements / 2];

        buttonGraphics[0] = R.drawable.button_1;
        buttonGraphics[1] = R.drawable.button_2;
        buttonGraphics[2] = R.drawable.button_3;
        buttonGraphics[3] = R.drawable.button_4;
        buttonGraphics[4] = R.drawable.button_5;
        buttonGraphics[5] = R.drawable.button_6;
        buttonGraphics[6] = R.drawable.button_7;
        buttonGraphics[7] = R.drawable.button_8;

        buttonGraphicLocations = new int[numberOfelements];



        shuffleButtonGraphics();

        for(int r=0;r<numRows;r++){

            for(int c=0;c<numColumns;c++){

                MemoryButton tempButton= new MemoryButton(this,r,c,buttonGraphics[buttonGraphicLocations[r*numColumns+c]],widthScreen,heightScreen);
                //TU MOZE BYC PROBLEM
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tempButton.setId(View.generateViewId());
                }
                tempButton.setOnClickListener(this);
                buttons[r*numColumns+c]= tempButton;
                gridLayout.addView(tempButton);

            }

        }





    }

    protected void shuffleButtonGraphics() {

        Random rand=new Random();

        for(int i=0;i<numberOfelements;i++) {

            buttonGraphicLocations[i]= i%(numberOfelements/2);
        }

        for(int i=0;i<numberOfelements;i++){

            int temp=buttonGraphicLocations[i];

            int swapIndex= rand.nextInt(16);

            buttonGraphicLocations[i]=buttonGraphicLocations[swapIndex];
            buttonGraphicLocations[swapIndex]=temp;
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {

        if(isBusy)
            return;
        MemoryButton button=(MemoryButton) v;

        if(button.isMatched)
            return;

        if(selectedButton1==null)
        {
            selectedButton1=button;
            selectedButton1.flip();
            return;
        }

        if(selectedButton1.getId()==button.getId()){
            return;
        }

        if(selectedButton1.getFrontDrawableId()==button.getFrontDrawableId()){
            button.flip();

            button.setMatched(true);
            selectedButton1.setMatched(true);

            selectedButton1.setEnabled(false);
            button.setEnabled(false);

            selectedButton1=null;

            return;
        }
        else {

            selectedButton2=button;
            selectedButton2.flip();
            isBusy=true;

            final Handler handler= new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    selectedButton2.flip();
                    selectedButton1.flip();
                    selectedButton1=null;
                    selectedButton2=null;
                    isBusy=false;
                }
            },500);
        }
    }
}