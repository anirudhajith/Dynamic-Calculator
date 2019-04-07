package com.example.dynamiccalculator;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView display = findViewById(R.id.display);
        final LinearLayout buttonGrid = findViewById(R.id.buttonGrid);
        final ViewTreeObserver observer = buttonGrid.getViewTreeObserver();

        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        populateButtonGrid();
                    }
                });
    }

    private void populateButtonGrid() {
        final LinearLayout buttonGrid = findViewById(R.id.buttonGrid);
        final TextView display = findViewById(R.id.display);
        int gridHeight = buttonGrid.getHeight();
        int gridWidth = buttonGrid.getWidth();

        final char numList[][] = {
                {'7', '8', '9'},
                {'4', '5', '6'},
                {'1', '2', '3'}
        };

        for(int row = 0; row < 3; row++) {

            LinearLayout l = new LinearLayout(this);
            l.setLayoutParams(new LinearLayout.LayoutParams(gridWidth, gridHeight/4));
            l.setBackgroundResource(R.color.colorPrimaryDark);
            //l.setLayoutParams(new LinearLayout.LayoutParams(250,50));
            l.setOrientation(LinearLayout.HORIZONTAL);

            for (int col = 0; col < 3; col++) {

                Button b = new Button(this);
                b.setWidth(gridWidth / 4);
                b.setHeight(gridHeight / 4);
                b.setText(String.valueOf(numList[row][col]));
                b.setTextSize(40);
                b.setBackgroundResource(R.color.buttonWhite);

                final int r = row;
                final int c = col;
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        display.setText(display.getText() + String.valueOf(numList[r][c]));
                    }
                });

                l.addView(b);
                Log.v("VERBOSE", "Populated col " + String.valueOf(col));
            }

            buttonGrid.addView(l);
            Log.v("VERBOSE", "Populated row " + String.valueOf(row));
        }
    }
}
