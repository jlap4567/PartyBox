package com.partyboxAPI;

import com.google.common.collect.ImmutableList;
import com.partyboxAPI.exceptions.PartyBoxException;
import com.partyboxAPI.exceptions.SerializationException;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentInfo extends BaseBO {
    private static String JSON_CARD_TYPE = "cardType";
    private static String JSON_CARD_NUMBER = "cardNumber";
    private static String JSON_CARD_NAME = "cardName";
    private static String JSON_CARD_SECURITY_CODE = "cardSecurityCode";

    public static enum CardType {
        VISA, MASTER_CARD, AMEX, DISCOCER
    };

    private CardType cardType;
    private String cardNumber;
    private String cardName;
    private int cardSecurityCode;

    @Override
    public JSONObjectWrapper toJSON() throws PartyBoxException {
        JSONObjectWrapper jsonObjectWrapper = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put(JSON_CARD_NAME, cardName);
            jsonObject.put(JSON_CARD_NUMBER, cardNumber);
            jsonObject.put(JSON_CARD_TYPE, cardType.name());
            jsonObject.put(JSON_CARD_SECURITY_CODE, cardSecurityCode);
            jsonObjectWrapper = new JSONObjectWrapper(jsonObject);
        } catch (JSONException e) {
            throw new SerializationException(getClass().getName(), e.getMessage());
        }

        return jsonObjectWrapper;
    }

    @Override
    public void fromJSON(JSONObjectWrapper jsonObjectWrapper) throws PartyBoxException {
        JSONObject jsonObject = jsonObjectWrapper.getObject();
        if (jsonObject == null) {
            throw new SerializationException(getClass().getName(), "Expected JSONObject from JSONObjectWrapper");
        }

        try {
            cardType = CardType.valueOf(jsonObject.getString(JSON_CARD_TYPE));
            cardName = jsonObject.getString(JSON_CARD_NAME);
            cardNumber = jsonObject.getString(JSON_CARD_NUMBER);
            cardSecurityCode = jsonObject.getInt(JSON_CARD_SECURITY_CODE);
        } catch (JSONException e) {
            throw new SerializationException(getClass().getName(), e.getMessage());
        }
    }

    public PaymentInfo() {
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getCardSecurityCode() {
        return cardSecurityCode;
    }

    public void setCardSecurityCode(int cardSecurityCode) {
        this.cardSecurityCode = cardSecurityCode;
    }

    @Override
    public int hashCode() {
        return hashObjects(ImmutableList.of(cardName, cardNumber, cardSecurityCode, cardType));
    }
}
