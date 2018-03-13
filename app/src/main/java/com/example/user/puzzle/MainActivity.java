package com.example.user.puzzle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button buttonUkladanka,buttonPuzzle,buttonMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonUkladanka=(Button)findViewById(R.id.buttonUkladanka);
        buttonUkladanka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(MainActivity.this,UkladankaActivity.class);
                startActivity(intent1);
            }
        });


        buttonPuzzle=(Button)findViewById(R.id.buttonPuzzle);
        buttonPuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this,PuzzleActivity.class);
                startActivity(intent2);
            }
        });


        buttonMemory=(Button)findViewById(R.id.buttonMemory);
        buttonMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(MainActivity.this,MemoryActivity.class);
                startActivity(intent3);
            }
        });

    }
}
