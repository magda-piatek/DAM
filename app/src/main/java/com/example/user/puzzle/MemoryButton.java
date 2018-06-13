package com.example.user.puzzle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatDrawableManager;
import android.widget.Button;
import android.widget.GridLayout;

public class MemoryButton extends android.support.v7.widget.AppCompatButton {


    protected int row;
    protected int column;
    protected int frontDrawableId;

    protected boolean isFlipped= false;
    protected boolean isMatched=false;

    protected Drawable front;
    protected Drawable back;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("RestrictedApi")
    public MemoryButton(Context context, int r, int c, int frontImageDrawable, int widthScreen,int heightScreen)
    {
        super(context);

        row=r;
        column=c;
        frontDrawableId=frontImageDrawable;

        front= AppCompatDrawableManager.get().getDrawable(context,frontImageDrawable);
        back=AppCompatDrawableManager.get().getDrawable(context,R.drawable.button_question);

        setBackground(back);

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r),GridLayout.spec(c));





        tempParams.width =(int) getResources().getDisplayMetrics().density+(widthScreen/4)-12;
        //  tempParams.width =(int) getResources().getDisplayMetrics().density+(widthScreen/4);
        tempParams.height =(int) getResources().getDisplayMetrics().density+(heightScreen/6);

        tempParams.setMargins(5,5,5,5);



        setLayoutParams(tempParams);

    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public int getFrontDrawableId() {
        return frontDrawableId;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void flip()
    {
        if(isMatched)
            return;

        if(isFlipped){

            setBackground(back);
            isFlipped=false;
        }
        else {
            setBackground(front);
            isFlipped=true;
        }

    }
}
