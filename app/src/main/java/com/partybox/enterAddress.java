package com.partybox;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.partyboxAPI.OrderInfo;
import com.partyboxAPI.Party;
import com.partyboxAPI.PartyFactory;
import com.partyboxAPI.PaymentInfo;

public class enterAddress extends BaseActivity {
    private EditText streetAddress;
    private EditText aptNum;
    private EditText state;
    private EditText city;
    private EditText zipCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_address);
        Party party = PartyFactory.getNewOrCurrentParty();
        final OrderInfo orderInfo = party.getOrderInfo();

        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToActivity(v, CheckoutActivity.class, Direction.RIGHT);
            }
        });

        Button nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ask what to put here
                setOrderInfo();
                if (orderInfo.getAddress() == null) {
                    //show dialog
                } else {
                    Party currentPartry = PartyFactory.getNewOrCurrentParty();
                    if (currentPartry.getOrderInfo() == null) {
                        currentPartry.setOrderInfo(new OrderInfo());
                    }
                    switchToActivity(v, CheckoutActivity.class, Direction.RIGHT);
                }
            }
        });

        findInputs();
    }

    private void findInputs() {
        streetAddress = findViewById(R.id.s_address);
        aptNum = findViewById(R.id.apt_num);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        zipCode = findViewById(R.id.zipCode);
    }

    private void setOrderInfo() {

        Party party = PartyFactory.getNewOrCurrentParty();
        if (party.getOrderInfo() == null) {
            party.setOrderInfo(new OrderInfo());
        }

        OrderInfo orderInfo = party.getOrderInfo();
        if(aptNum != null){
            orderInfo.setAddress(streetAddress.getText().toString() + " apt num:" + aptNum.getText().toString());
        }
        else{
            orderInfo.setAddress(streetAddress.getText().toString());
        }
        orderInfo.setCity(city.getText().toString() + " " + state.getText().toString());
        orderInfo.setZip(zipCode.getText().toString());


    }


}
