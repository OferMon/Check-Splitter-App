package com.example.splinter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ConnectActivity extends AppCompatActivity {

    private static final String TAG = "ConnectActivity";
    private TableLayout mLayout;
    private TableRow mTableRow;
    private SharedPreferences sp;
    private SharedPreferences.Editor sedt;
    private String[][] dishes_to_splitters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        sp = getSharedPreferences("Splinter", 0);

        mLayout = (TableLayout) findViewById(R.id.con_container);
        mTableRow = (TableRow) findViewById(R.id.connect_row_head);

        List<String> list_of_dishes = new ArrayList<>();
        List<String> list_of_splitters = new ArrayList<>();
        final int num_of_dishes = sp.getInt("dishes_length", 0);
        final int num_of_splitters = sp.getInt("splitters_length", 0);
        dishes_to_splitters = new String[num_of_dishes][num_of_splitters + 1];
        for (int i = 0; i < num_of_dishes; i++) {
            list_of_dishes.add(sp.getString("dish_name" + (i + 1), null));
        }
        for (int i = 0; i < num_of_splitters; i++) {
            list_of_splitters.add(sp.getString("splitter_name" + (i + 1), null));
        }

        List<KeyPairBoolData> list_of_pairs_splitters = new ArrayList<>();
        for (int i = 0; i < num_of_splitters; i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(list_of_splitters.get(i));
            h.setSelected(false);
            list_of_pairs_splitters.add(h);
        }

        for (int i = 0; i < num_of_dishes; i++) {
            mLayout.addView(createNewTableRow(i, list_of_dishes.get(i), list_of_pairs_splitters), i + 1);
        }

        findViewById(R.id.next_CS_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count;
                Set<String> temp;
                for (int i = 0; i < num_of_dishes; i++) {
                    if (dishes_to_splitters[i][dishes_to_splitters[i].length - 1] == null || Integer.parseInt(dishes_to_splitters[i][dishes_to_splitters[i].length - 1]) == 0) {
                        Toast.makeText(getApplicationContext(), "Please assign all the dishes!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                sedt = sp.edit();
                for (int i = 0; i < num_of_dishes; i++) {
                    count = Integer.parseInt(dishes_to_splitters[i][dishes_to_splitters[i].length - 1]);
                    temp = new HashSet<>();
                    for (int j = 0; j < count; j++) {
                        temp.add(dishes_to_splitters[i][j]);
                    }
                    sedt.putStringSet("dish_split" + (i + 1), temp);
                }
                sedt.apply();
                Intent intent = new Intent(ConnectActivity.this, calcLoadActivity.class);
                startActivity(intent);
            }
        });

    }

    private TableRow createNewTableRow(int rowIndx, String dishName, List<KeyPairBoolData> list_of_pairs_splitters) {
        final TextView textView1 = new TextView(this);
        final TextView mHead = findViewById(R.id.con_dish_head_table);
        TableRow tbrow0 = new TableRow(this);
        MultiSpinnerSearch multiSelectSpinnerWithSearch = new MultiSpinnerSearch(this);

        // Pass true If you want searchView above the list. Otherwise false. default = true.
        multiSelectSpinnerWithSearch.setSearchEnabled(true);

        // A text that will display in search hint.
        multiSelectSpinnerWithSearch.setSearchHint("Select splitters");

        // Set text that will display when search result not found...
        multiSelectSpinnerWithSearch.setEmptyTitle("Splitter not found!");

        // If you will set the limit, this button will not display automatically.
        multiSelectSpinnerWithSearch.setShowSelectAllButton(true);

        //A text that will display in clear text button
        multiSelectSpinnerWithSearch.setClearText("Close & Clear");

        // Removed second parameter, position. Its not required now..
        // If you want to pass preselected items, you can do it while making listArray,
        // pass true in setSelected of any item that you want to preselect
        multiSelectSpinnerWithSearch.setItems(list_of_pairs_splitters, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                int count = 0;
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i(TAG, i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                        dishes_to_splitters[rowIndx][count++] = items.get(i).getName();
                    }
                    items.get(i).setSelected(false);
                }
                dishes_to_splitters[rowIndx][dishes_to_splitters[rowIndx].length - 1] = "" + count;
            }
        });

        tbrow0.setLayoutParams(mTableRow.getLayoutParams());
        tbrow0.setPadding(mTableRow.getPaddingLeft(), mTableRow.getPaddingTop(), mTableRow.getPaddingRight(), mTableRow.getPaddingBottom());
        tbrow0.setDividerDrawable(mTableRow.getDividerDrawable());
        tbrow0.setDividerPadding(mTableRow.getDividerPadding());
        tbrow0.setShowDividers(mTableRow.getShowDividers());
        textView1.setWidth(mHead.getWidth());
        textView1.setHeight(mHead.getHeight());
        textView1.setLayoutParams(mHead.getLayoutParams());
        textView1.setTextAlignment(mHead.getTextAlignment());
        textView1.setText(dishName);
        tbrow0.addView(textView1);
        tbrow0.addView(multiSelectSpinnerWithSearch);

        return tbrow0;
    }
}