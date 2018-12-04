package com.partybox;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CommitAndPayActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_and_pay);

        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToActivity(v, ActivityNewPartyFirst.class, Direction.RIGHT);
            }
        });
    }
}