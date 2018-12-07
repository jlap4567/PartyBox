package com.partyboxAPI;

import com.google.common.collect.ImmutableList;
import com.partyboxAPI.exceptions.PartyBoxException;
import com.partyboxAPI.exceptions.SerializationException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class PaymentInfo extends BaseBO {
    private static String JSON_CARD_TYPE = "cardType";
    private static String JSON_CARD_NUMBER = "cardNumber";
    private static String JSON_CARD_NAME = "cardName";
    private static String JSON_CARD_SECURITY_CODE = "cardSecurityCode";
    private static String JSON_CARD_EXPIRE = "cardExpire";

    private static int CC_LENGTH = 16;
    private static String DATE_DELIM = "/";

    public static enum CardType {
        VISA, MASTER_CARD, AMEX, DISCOVER
    };

    private CardType cardType;
    private String cardNumber;
    private String cardName;
    private String cardExpire;
    private int cardSecurityCode;

    public boolean verify() {
        if (cardNumber == null || cardNumber.isEmpty() || !isNumericString(cardNumber) || cardNumber.length() != CC_LENGTH) {
            return false;
        }

        if (cardName == null || cardName.isEmpty()) {
            return false;
        }

        if (cardType == null) {
            return false;
        }

        return true;
    }

    private boolean isNumericString(String str) {
        for (Character c: str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    private boolean isCCDateFormat(String str) {
        String[] tokens = str.split(DATE_DELIM);
        if (tokens.length != 2) {
            return false;
        }

        if (tokens[0].isEmpty() || !isNumericString(tokens[0])) {
            return false;
        }

        if (tokens[1].isEmpty() || !isNumericString(tokens[1])) {
            return false;
        }

        return true;
    }

    /**
     * @return VISA *1234, AMEX *1234
     */
    public String getCensoredCardNumber() {
        String censored = "";
        if (cardNumber != null && this.verify()) {
            censored = cardNumber.substring(12, 16);
        }

        return censored;
    }

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
            jsonObject.put(JSON_CARD_EXPIRE, cardExpire);

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
            cardExpire = jsonObject.getString(JSON_CARD_EXPIRE);
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

    public String getCardExpire() {
        return cardExpire;
    }

    public void setCardExpire(String cardExpire) {
        this.cardExpire = cardExpire;
    }

    @Override
    public int hashCode() {
        return hashObjects(ImmutableList.of(cardName, cardNumber, cardSecurityCode, cardType, cardExpire));
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s\n%s\n%s\n%d\n%s", cardName, cardNumber, cardType.name(), cardSecurityCode, cardExpire);
    }
}
