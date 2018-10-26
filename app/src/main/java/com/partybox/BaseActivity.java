package com.partybox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Put stuff here that every activity needs to use
 */
public abstract class BaseActivity extends AppCompatActivity {
    /**
     * Switch from current activity to another
     * @param v
     * @param activity
     */
    protected void switchToActivity(View v, Class<? extends BaseActivity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
