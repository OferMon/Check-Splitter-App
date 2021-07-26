package com.example.splinter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class NamesActivity extends AppCompatActivity {

    private LinearLayout mLayout;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);

        SharedPreferences mPreferences = getSharedPreferences("Splinter", 0);
        SharedPreferences.Editor sedt = mPreferences.edit();
        sedt.clear();

        mLayout = (LinearLayout) findViewById(R.id.names_container);
        mEditText = (EditText) findViewById(R.id.name1);

        findViewById(R.id.next_NM_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numOfRows = mLayout.getChildCount() - 1;
                EditText editText1;
                for (int j = numOfRows - 1; j >= 0; j--) {
                    editText1 = (EditText) mLayout.getChildAt(j);
                    if (editText1.getText().toString().equals(""))
                        mLayout.removeViewAt(j);
                }

                final ArrayList<View> views = getViewsByTag(mLayout, "UserInput");
                final int childCount = views.size();
                String name;
                EditText editText;
                for (int i = 0; i < childCount; i++) {
                    editText = (EditText) views.remove(0);
                    name = editText.getText().toString();
                    sedt.putString("splitter_name" + (i + 1), name);
                }
                sedt.putInt("splitters_length", childCount);
                sedt.apply();
                Intent intent = new Intent(NamesActivity.this, MoneyActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.add_names_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.addView(createNewEditText(mEditText.getHint().toString()), mLayout.getChildCount() - 1);
            }
        });

    }

    private EditText createNewEditText(String text) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final EditText editText = new EditText(this);
        editText.setLayoutParams(lparams);
        editText.setHint(text);
        editText.setTag("UserInput");
        return editText;
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