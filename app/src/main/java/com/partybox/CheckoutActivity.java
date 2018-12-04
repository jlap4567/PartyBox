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

public class CheckoutActivity extends BaseActivity {
    private TextView cardDisplay;
    private TextView addressDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Button enterCardButton = findViewById(R.id.button_add_payment);
        enterCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToActivity(v, CommitAndPayActivity.class, Direction.RIGHT);
            }
        });

        Button enterAddressButton = findViewById(R.id.button_add_address);
        enterAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nothing for now
            }
        });

        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change to store interface when Mike finishes it
                switchToActivity(v, ActivityNewPartyFirst.class, Direction.LEFT);
            }
        });

        cardDisplay = findViewById(R.id.payment_summary);
        addressDisplay = findViewById(R.id.address_summary);

        Party party = PartyFactory.getNewOrCurrentParty();
        OrderInfo orderInfo = party.getOrderInfo();
        if (orderInfo != null) {
            PaymentInfo paymentInfo = orderInfo.getPaymentInfo();
            if (paymentInfo != null) {
                String summaryString = paymentInfo.getCardType().name() + " *" + paymentInfo.getCensoredCardNumber();
                cardDisplay.setText(summaryString);
            }
        }
    }
}
