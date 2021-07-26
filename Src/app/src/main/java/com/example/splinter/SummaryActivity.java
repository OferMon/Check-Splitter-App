package com.example.splinter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SummaryActivity extends AppCompatActivity {

    SeekBar seekBar;
    private LinearLayout mLayout1;
    private LinearLayout mLayout2;
    private TextView mTipNumber;
    private SharedPreferences sp;
    private float[] price_per_splitter;
    private List<String> list_of_splitters;
    private float total_price = 0;
    private float tip;
    private int num_of_splitters;
    private int num_of_dishes;
    private int non_splitters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        sp = getSharedPreferences("Splinter", 0);
        seekBar = (SeekBar) findViewById(R.id.tip_seek);
        mLayout1 = (LinearLayout) findViewById(R.id.summ_names_container);
        mLayout2 = (LinearLayout) findViewById(R.id.summ_price_container);
        mTipNumber = (TextView) findViewById(R.id.tip_number_box);
        num_of_splitters = sp.getInt("splitters_length", 0);
        num_of_dishes = sp.getInt("dishes_length", 0);
        seekBar.setProgress(10);
        mTipNumber.setText("" + 10 + "%");

        calculateBill();
        tip = calculateTip();
        printSplitters(false);

        CheckBox checkBox = findViewById(R.id.tip_all_check);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                printSplitters(isChecked);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                mTipNumber.setText("" + progress + "%");
                tip = calculateTip();
                TextView textView1;
                CheckBox checkBox = (CheckBox) findViewById(R.id.tip_all_check);
                boolean tip_non_splitters = checkBox.isChecked();
                for (int i = 0; i < mLayout2.getChildCount(); i++) {
                    textView1 = (TextView) findViewById(i + 1);
                    if (price_per_splitter[i] == 0) {
                        if (!tip_non_splitters)
                            textView1.setText(String.format("%.2f", price_per_splitter[i]));
                        else
                            textView1.setText(String.format("%.2f", price_per_splitter[i] + tip / num_of_splitters));
                    } else if (!tip_non_splitters)
                        textView1.setText(String.format("%.2f", price_per_splitter[i] + tip / (num_of_splitters - non_splitters)));
                    else
                        textView1.setText(String.format("%.2f", price_per_splitter[i] + tip / num_of_splitters));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Spread the love!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Thank you!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.next_SS_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SummaryActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    private void printSplitters(boolean tip_non_splitters) {
        mLayout1.removeAllViews();
        mLayout2.removeAllViews();
        TextView textView;
        TextView textView2;
        for (int i = 0; i < list_of_splitters.size(); i++) {
            textView = new TextView(this);
            textView2 = new TextView(this);
            textView.setTextSize(30);
            textView2.setTextSize(30);
            textView.setGravity(Gravity.CENTER);
            textView2.setGravity(Gravity.CENTER);
            textView2.setId(i + 1);
            textView.setText(list_of_splitters.get(i));
            if (price_per_splitter[i] == 0) {
                if (!tip_non_splitters)
                    textView2.setText(String.format("%.2f", price_per_splitter[i]));
                else
                    textView2.setText(String.format("%.2f", price_per_splitter[i] + tip / num_of_splitters));
            } else if (!tip_non_splitters)
                textView2.setText(String.format("%.2f", price_per_splitter[i] + tip / (num_of_splitters - non_splitters)));
            else
                textView2.setText(String.format("%.2f", price_per_splitter[i] + tip / num_of_splitters));
            mLayout1.addView(textView);
            mLayout2.addView(textView2);
        }
    }

    private float calculateTip() {
        return total_price * Integer.parseInt(mTipNumber.getText().toString().split("%")[0]) / 100;
    }

    private void calculateBill() {
        list_of_splitters = new ArrayList<>();
        for (int i = 0; i < num_of_splitters; i++) {
            list_of_splitters.add(sp.getString("splitter_name" + (i + 1), null));
        }
        price_per_splitter = new float[num_of_splitters];
        Arrays.fill(price_per_splitter, 0);
        Set<String> temp;
        int dish_count;
        float dish_price;
        float dish_price_per_splitter;
        for (int i = 0; i < num_of_dishes; i++) {
            temp = sp.getStringSet("dish_split" + (i + 1), null);
            dish_count = Integer.parseInt(sp.getString("dish_count" + (i + 1), null));
            dish_price = Float.parseFloat(sp.getString("dish_price" + (i + 1), null));
            dish_price_per_splitter = dish_price * dish_count / temp.size();
            for (String splitter : temp) {
                price_per_splitter[list_of_splitters.indexOf(splitter)] += dish_price_per_splitter;
            }
            total_price += dish_count * dish_price;
        }
        for (float v : price_per_splitter)
            if (v == 0)
                non_splitters++;
    }
}