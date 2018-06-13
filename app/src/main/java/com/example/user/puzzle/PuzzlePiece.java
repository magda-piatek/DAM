package com.example.user.puzzle;

import android.content.Context;

/**
 * Created by User on 13.06.2018.
 */
public class PuzzlePiece extends android.support.v7.widget.AppCompatImageView {
    public int wspX;
    public int wspY;
    public int pieceWidth;
    public int pieceHeight;
    public boolean canMove = true;

    public PuzzlePiece(Context context) {
        super(context);
    }
}