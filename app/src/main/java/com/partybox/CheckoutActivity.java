package com.partybox;

import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.partybox.adapters.ShoppingCartItemAdapter;
import com.partyboxAPI.MessageThread;
import com.partyboxAPI.OrderInfo;
import com.partyboxAPI.Party;
import com.partyboxAPI.PartyFactory;
import com.partyboxAPI.PaymentInfo;
import com.partyboxAPI.ShoppingCart;
import com.partyboxAPI.exceptions.PartyBoxException;

import java.util.Locale;

public class CheckoutActivity extends BaseActivity {
    private TextView cardDisplay;
    private TextView addressDisplay;
    private TextView cartTotalDisplay;

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
                switchToActivity(v, enterAddress.class, Direction.LEFT);
            }
        });

        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToActivity(v, FoodListActivity.class, Direction.LEFT);
            }
        });

        cardDisplay = findViewById(R.id.payment_summary);
        addressDisplay = findViewById(R.id.address_summary);
        cartTotalDisplay = findViewById(R.id.price);

        ShoppingCartItemAdapter adapter = new ShoppingCartItemAdapter(this, R.layout.checkout_item_entry);
        ListView listView = findViewById(R.id.checkout_item_list);
        listView.setAdapter(adapter);

        final Party party = PartyFactory.getNewOrCurrentParty();
        OrderInfo orderInfo = party.getOrderInfo();
        if (orderInfo != null) {
            PaymentInfo paymentInfo = orderInfo.getPaymentInfo();
            if (paymentInfo != null) {
                String summaryString = paymentInfo.getCardType().name() + " *" + paymentInfo.getCensoredCardNumber();
                cardDisplay.setText(summaryString);
            }

            cartTotalDisplay.setText(String.format(Locale.ENGLISH, "%.2f", orderInfo.getCart().getTotalPrice()));

            if (orderInfo.getAddress() != null) {
                TextView addressView = findViewById(R.id.address_summary);
                addressView.setText(orderInfo.getAddress() + " \n" + orderInfo.getCity() + " " + orderInfo.getZip());
            }
        }

        //TODO show dialog if order info is not complete
        Button orderButton = findViewById(R.id.order);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (party.getOrderInfo() == null || party.getOrderInfo().getPaymentInfo() == null || party.getOrderInfo().getFirstName() == null) {
                    showAlertDialog("Missing Payment Info or Address", "Missing Payment Info or Address");
                    return;
                }
                Thread thread = new Thread(new MessageThread());
                thread.start();
                switchToActivity(v, MainActivity.class, Direction.LEFT);
                /*
                try {
                    Log.d("xxxxxxxxx", party.getOrderInfo().toJSON().toString());
                } catch (PartyBoxException e) {
                    Log.d("xxxxxxxxx", "FAILED: " + e.getMessage());
                }
                */
            }
        });
    }
}
