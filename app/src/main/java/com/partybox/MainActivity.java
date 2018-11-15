package com.partybox;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button newPartyButton = findViewById(R.id.new_party);
        newPartyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switchToActivity(v, ActivityNewPartyFirst.class, Direction.LEFT);
            }
        });

        final Button existingPartyButton = findViewById(R.id.made_party);
        existingPartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToActivity(v, MadePartyListActivity.class, Direction.LEFT);
            }
        });
    }
}
