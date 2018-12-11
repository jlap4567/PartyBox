package com.partyboxAPI;

import com.partyboxAPI.exceptions.PartyBoxException;
import com.partyboxAPI.exceptions.SerializationException;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderInfo extends BaseBO {
    private static String JSON_CNAME_FIRST = "firstName";
    private static String JSON_CNAME_LAST = "lastName";
    private static String JSON_ADDRESS = "address";
    private static String JSON_ZIP = "zip";
    private static String JSON_CITY = "city";
    private static String JSON_CART = "cart";
    private static String JSON_PAYMENT = "payment";
    private static String JSON_DATE = "deliveryDate";
    private static String JSON_TIME = "arriveBy";

    private String firstName;
    private String lastName;
    private String address;
    private String zip;
    private String city;
    private ShoppingCart cart;
    private PaymentInfo paymentInfo;
    private String date;
    private String deliverBy;

    public OrderInfo() {
        cart = ShoppingCart.getInstance();
        // Party should not be null since should only be called after NewPartyFirst
        if (PartyFactory.hasInstance()) {
            Party party = PartyFactory.getNewOrCurrentParty();
            date = party.getDate();
            deliverBy = party.getStartTime();
        }

        // hard code address/name for now until Jacob is done with address screen
    }

    @Override
    public JSONObjectWrapper toJSON() throws PartyBoxException {
        JSONObjectWrapper jsonObjectWrapper = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(JSON_ADDRESS, address);
            jsonObject.put(JSON_CITY, city);
            jsonObject.put(JSON_CNAME_FIRST, firstName);
            jsonObject.put(JSON_CNAME_LAST, lastName);
            jsonObject.put(JSON_ZIP, zip);
            jsonObject.put(JSON_DATE, date);
            jsonObject.put(JSON_TIME, deliverBy);

//            JSONObjectWrapper paymentObject = paymentInfo.toJSON();
//            jsonObject.put(JSON_PAYMENT, paymentObject.getObject());

            JSONObjectWrapper carObject = cart.toJSON();
            jsonObject.put(JSON_CART, carObject.getArray());

            jsonObjectWrapper = new JSONObjectWrapper(jsonObject);
        } catch (JSONException e) {
            throw new SerializationException(getClass().getName(), e.getMessage());
        } catch (PartyBoxException e) {
            throw new SerializationException(PaymentInfo.class.getName(), e.getMessage());
        }

        return jsonObjectWrapper;
    }

    @Override
    public void fromJSON(JSONObjectWrapper jsonObjectWrapper) throws PartyBoxException {
        JSONObject jsonObject = jsonObjectWrapper.getObject();
        if (jsonObject == null) {
            throw new SerializationException(getClass().getName(), "Expected JSONObject from wrapper");
        }

        try {
            firstName = jsonObject.getString(JSON_CNAME_FIRST);
            lastName = jsonObject.getString(JSON_CNAME_LAST);
            address = jsonObject.getString(JSON_ADDRESS);
            zip = jsonObject.getString(JSON_ZIP);
            city = jsonObject.getString(JSON_CITY);
            date = jsonObject.getString(JSON_DATE);
            deliverBy = jsonObject.getString(JSON_TIME);

            JSONObject cartObject = jsonObject.getJSONObject(JSON_CART);
            cart = ShoppingCart.getInstance();
            cart.fromJSON(new JSONObjectWrapper(cartObject));

            JSONObject paymentObejct = jsonObject.getJSONObject(JSON_PAYMENT);
            paymentInfo = new PaymentInfo();
            paymentInfo.fromJSON(new JSONObjectWrapper(paymentObejct));
        } catch (JSONException e) {
            throw new SerializationException(getClass().getName(), e.getMessage());
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeliverBy() {
        return deliverBy;
    }

    public void setDeliverBy(String deliverBy) {
        this.deliverBy = deliverBy;
    }
}
