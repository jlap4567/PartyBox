package com.partybox.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.partybox.R;
import com.partyboxAPI.Item;
import com.partyboxAPI.ShoppingCart;

import java.util.ArrayList;

/**
 * Adapter for item list on checkout screen
 */
public class ShoppingCartItemAdapter extends ArrayAdapter<Item> {
    public ShoppingCartItemAdapter(Context context, ArrayList<Item> summaries) {
        super(context, 0, summaries);
    }

    public ShoppingCartItemAdapter(Context context, int view) {
        super(context, view, Lists.newArrayList(ShoppingCart.getInstance().getItemToQuantityMap().keySet())); //ew
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Item item = getItem(position);
        Integer quantity = ShoppingCart.getInstance().getQuantity(item);
        if (quantity == null){
            Log.d("ERROR", "Item present in view not in cart");
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.checkout_item_entry, parent, false);
        }

        // Lookup view for data population
        TextView titleDisplay = (TextView) convertView.findViewById(R.id.checkout_item_title);
        TextView quantityDisplay = (TextView) convertView.findViewById(R.id.checkout_item_quantity);
        TextView priceDisplay = (TextView) convertView.findViewById(R.id.checkout_item_price);
        ImageView imageDisplay = (ImageView) convertView.findViewById(R.id.checkout_item_img);

        // Populate the data into the template view using the data object
        titleDisplay.setText(item.getTitle());
        priceDisplay.setText(String.format("$%.2f", item.getPrice()));
        quantityDisplay.setText(quantity.toString() + "x");
        imageDisplay.setImageResource(item.getImage());

        // Return the completed view to render on screen
        return convertView;
    }
}
