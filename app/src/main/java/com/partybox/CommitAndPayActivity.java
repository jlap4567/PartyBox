package com.partybox;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.partyboxAPI.OrderInfo;
import com.partyboxAPI.Party;
import com.partyboxAPI.PartyFactory;
import com.partyboxAPI.PaymentInfo;

public class CommitAndPayActivity extends BaseActivity {
    private EditText cardName;
    private EditText cardNumber;
    private EditText cardExpMonth;
    private EditText cardExpYear;
    private EditText cardSecurity;
    private RadioGroup cardTypeSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_and_pay);


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
                PaymentInfo paymentInfo = fillOutPaymentInfo();
                if (paymentInfo == null || getFirstLastName(paymentInfo.getCardName()) == null) {
                    showAlertDialog("Invalid Payment", "Please enter valid card information");
                } else {
                    Party currentPartry = PartyFactory.getNewOrCurrentParty();
                    if (currentPartry.getOrderInfo() == null) {
                        currentPartry.setOrderInfo(new OrderInfo());
                    }

                    currentPartry.getOrderInfo().setPaymentInfo(paymentInfo);
                    String[] firstLastName = getFirstLastName(paymentInfo.getCardName());
                    currentPartry.getOrderInfo().setFirstName(firstLastName[0]);
                    currentPartry.getOrderInfo().setLastName(firstLastName[1]);
                    switchToActivity(v, CheckoutActivity.class, Direction.RIGHT);
                }
            }
        });

        findInputs();
    }

    private void findInputs() {
        cardName = findViewById(R.id.c_name);
        cardNumber = findViewById(R.id.c_number);
        cardExpMonth = findViewById(R.id.c_expire_month);
        cardExpYear = findViewById(R.id.c_expire_year);
        cardSecurity = findViewById(R.id.c_security);
        cardTypeSelector = findViewById(R.id.cardTypeGroup);
    }


    private PaymentInfo fillOutPaymentInfo() {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCardName(cardName.getText().toString());
        paymentInfo.setCardNumber(cardNumber.getText().toString());
        paymentInfo.setCardSecurityCode(Integer.parseInt(cardSecurity.getText().toString()));
        paymentInfo.setCardExpire(cardExpMonth.getText() + "/" + cardExpYear.getText());
        switch (cardTypeSelector.getCheckedRadioButtonId()) {
            case R.id.visaButton:
                paymentInfo.setCardType(PaymentInfo.CardType.VISA);
                break;
            case R.id.mastercardButton:
                paymentInfo.setCardType(PaymentInfo.CardType.MC);
                break;
            case R.id.amexButton:
                paymentInfo.setCardType(PaymentInfo.CardType.AMEX);
                break;
        }

        if (paymentInfo.verify()) {
            return paymentInfo;
        }

        return null;
    }

    private String[] getFirstLastName(String cardName) {
        if (cardName == null) {
            return null;
        }

        String[] tokens = cardName.split(" ");
        if (tokens.length < 2) {
            return null;
        } else if (tokens.length == 2) {
            return tokens;
        } else {
            String[] arr = new String[2];
            arr[0] = tokens[0];
            arr[1] = tokens[2];
            return arr;
        }
    }
}