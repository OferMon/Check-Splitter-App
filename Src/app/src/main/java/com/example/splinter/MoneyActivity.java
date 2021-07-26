package com.example.splinter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class MoneyActivity extends AppCompatActivity {

    private TableLayout mLayout;
    private TableRow mTableRow;
    private EditText mDishCell;
    private EditText mPriceCell;
    private EditText mCountCell;
    private String num;
    private static final String tagsArr[] = {"DishInput", "PriceInput", "CountInput"};
    private static final String DBArr[] = {"dish_name", "dish_price", "dish_count"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        SharedPreferences sp = getSharedPreferences("Splinter", 0);
        SharedPreferences.Editor sedt = sp.edit();

        mLayout = (TableLayout) findViewById(R.id.bill_container);
        mTableRow = (TableRow) findViewById(R.id.bill_row1);
        mPriceCell = (EditText) findViewById(R.id.price1);
        mDishCell = (EditText) findViewById(R.id.dish1);
        mCountCell = (EditText) findViewById(R.id.count1);

        findViewById(R.id.next_MC_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numOfRows = mLayout.getChildCount() - 1;
                TableRow tableRow;
                EditText editText1;
                for (int j = numOfRows; j > 0; j--) {
                    tableRow = (TableRow) mLayout.getChildAt(j);
                    for (int i = 0; i < 2; i++) {
                        editText1 = (EditText) tableRow.getChildAt(i);
                        if (editText1.getText().toString().equals("")) {
                            mLayout.removeViewAt(j);
                            break;
                        }
                    }
                }
                ArrayList<View> views;
                String name;
                EditText editText;
                int childCount = 0;
                for (int j = 0; j < tagsArr.length; j++) {
                    views = getViewsByTag(mLayout, tagsArr[j]);
                    childCount = views.size();
                    for (int i = 0; i < childCount; i++) {
                        editText = (EditText) views.remove(0);
                        name = editText.getText().toString();
                        sedt.putString(DBArr[j] + (i + 1), name);
                    }
                }
                sedt.putInt("dishes_length", childCount);
                sedt.apply();
                Intent intent = new Intent(MoneyActivity.this, ConnectActivity.class);
                startActivity(intent);
            }
        });

        mDishCell.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                mLayout.addView(createNewTableRow(mLayout.getChildCount()), mLayout.getChildCount());
                v.setOnFocusChangeListener(null);
            }
        });

        mCountCell.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean gainFocus) {
                EditText editText = (EditText) findViewById(R.id.count1);
                if (gainFocus) {
                    num = editText.getText().toString();
                    editText.setText("");
                } else if (editText.getText().toString().equals(""))
                    editText.setText(num);
            }
        });
    }

    private TableRow createNewTableRow(int rowNum) {
        final EditText editText1 = new EditText(this);
        final EditText editText2 = new EditText(this);
        final EditText editText3 = new EditText(this);

        TableRow tbrow0 = new TableRow(this);
        tbrow0.setLayoutParams(mTableRow.getLayoutParams());
        tbrow0.setPadding(mTableRow.getPaddingLeft(), mTableRow.getPaddingTop(), mTableRow.getPaddingRight(), mTableRow.getPaddingBottom());
        tbrow0.setDividerDrawable(mTableRow.getDividerDrawable());
        tbrow0.setDividerPadding(mTableRow.getDividerPadding());
        tbrow0.setShowDividers(mTableRow.getShowDividers());
        editText1.setWidth(mDishCell.getWidth());
        editText1.setHeight(mDishCell.getHeight());
        editText1.setHint(mDishCell.getHint());
        editText1.setTag("DishInput");
        editText1.setInputType(mDishCell.getInputType());
        //editText1.setLayoutParams(mDishCell.getLayoutParams());
        editText1.setTextAlignment(mDishCell.getTextAlignment());
        editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                mLayout.addView(createNewTableRow(mLayout.getChildCount()), mLayout.getChildCount());
                v.setOnFocusChangeListener(null);
            }
        });
        tbrow0.addView(editText1);
        editText2.setWidth(100);
        editText2.setHeight(40);
        editText2.setTag("PriceInput");
        editText2.setHint(mPriceCell.getHint());
        editText2.setInputType(mPriceCell.getInputType());
        editText2.setTextAlignment(mPriceCell.getTextAlignment());
        tbrow0.addView(editText2);
        editText3.setWidth(100);
        editText3.setHeight(40);
        editText3.setTag("CountInput");
        editText3.setText("1");
        editText3.setId(rowNum);

        editText3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean gainFocus) {
                EditText editText = (EditText) findViewById(rowNum);
                if (gainFocus) {
                    num = editText.getText().toString();
                    editText.setText("");
                } else if (editText.getText().toString().equals(""))
                    editText.setText(num);
            }
        });
        editText3.setInputType(mCountCell.getInputType());
        editText3.setTextAlignment(mCountCell.getTextAlignment());
        tbrow0.addView(editText3);
        return tbrow0;
    }

    private static ArrayList<View> getViewsByTag(ViewGroup root, String tag) {
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        View child;
        Object tagObj;
        for (int i = 0; i < childCount; i++) {
            child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }
            tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }
        }
        return views;
    }
}