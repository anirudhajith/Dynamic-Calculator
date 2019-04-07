package com.example.dynamiccalculator;

import android.app.ActionBar;
import android.os.Build;
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
                        initializeMetaButtons();

                        if (Build.VERSION.SDK_INT > 16) {
                            buttonGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            buttonGrid.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                });
    }

    private void initializeMetaButtons() {

        final TextView display = findViewById(R.id.display);
        final LinearLayout buttonGrid = findViewById(R.id.buttonGrid);
        final Button acButton = findViewById(R.id.AC);
        final Button delButton = findViewById(R.id.DEL);

        acButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display.setText("0.");
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expression = display.getText().toString();
                String newExpression;

                if (expression.length() > 0) {
                    newExpression = expression.substring(0, expression.length() - 1);
                } else {
                    newExpression = expression;
                }
                display.setText(newExpression);
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
                {'1', '2', '3'},
                {'0', '.', '='}
        };

        final char operatorList[] = {
                '/', 'x', '+', '-'
        };

        for(int row = 0; row < 4; row++) {

            LinearLayout l = new LinearLayout(this);
            l.setLayoutParams(new LinearLayout.LayoutParams(gridWidth, gridHeight/4));
            l.setBackgroundResource(R.color.colorPrimaryDark);
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
                        if (display.getText().equals("0."))
                            display.setText(String.valueOf(numList[r][c]));
                        else
                            display.setText(display.getText() + String.valueOf(numList[r][c]));
                    }
                });

                l.addView(b);
                Log.v("VERBOSE", "Populated col " + String.valueOf(col));
            }

            Button b = new Button(this);
            b.setWidth(gridWidth / 4);
            b.setHeight(gridHeight / 4);
            b.setText(String.valueOf(operatorList[row]));
            b.setTextSize(40);
            b.setBackgroundResource(R.color.buttonGrey);

            final int r = row;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String expression = display.getText().toString();
                    String newExpression;
                    int length = expression.length();

                    switch (expression.charAt(length - 1)) {
                        case '/':
                        case 'x':
                        case '+':
                        case '-':
                            newExpression = expression.substring(0,length-1) + operatorList[r];
                            display.setText(newExpression);
                            break;

                        default:
                            newExpression = expression + operatorList[r];
                            display.setText(newExpression);
                    }

                }
            });

            l.addView(b);
            Log.v("VERBOSE", "Populated col " + String.valueOf(operatorList[row]));

            buttonGrid.addView(l);
            Log.v("VERBOSE", "Populated row " + String.valueOf(row));
        }

        LinearLayout lastRow = (LinearLayout) buttonGrid.getChildAt(3);
        Button equalsButton = (Button) lastRow.getChildAt(2);

        equalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expression = display.getText().toString();
                String newExpression = evaluate(expression);
                display.setText(newExpression);
            }
        });

    }

    private String evaluate(String expression) {
        try {
            if (expression.contains("/")) {
                String operands[] = expression.split("/");
                float result = Float.valueOf(operands[0]) / Float.valueOf(operands[1]);
                return String.valueOf(result);
            } else if (expression.contains("x")) {
                String operands[] = expression.split("x");
                float result = Float.valueOf(operands[0]) * Float.valueOf(operands[1]);
                return String.valueOf(result);
            } else if (expression.contains("+")) {
                String operands[] = expression.split("+");
                float result = Float.valueOf(operands[0]) + Float.valueOf(operands[1]);
                return String.valueOf(result);
            } else if (expression.contains("-")) {
                String operands[] = expression.split("-");
                float result = Float.valueOf(operands[0]) - Float.valueOf(operands[1]);
                return String.valueOf(result);
            } else {
                return "0.";
            }
        } catch (Exception e) {
            Log.e("ERROR", expression);
        }
        return "0.";
    }
}
