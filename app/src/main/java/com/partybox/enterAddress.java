package com.partybox;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.partybox.BaseActivity;
import com.partybox.R;
import com.partyboxAPI.OrderInfo;
import com.partyboxAPI.Party;
import com.partyboxAPI.PartyFactory;
import com.partyboxAPI.PaymentInfo;

import java.util.Locale;

public class enterAddress extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_address);
    }


}
