package com.partybox;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.common.collect.Lists;
import com.partyboxAPI.Item;
import com.partyboxAPI.OrderInfo;
import com.partyboxAPI.Party;
import com.partyboxAPI.PartyFactory;
import com.partyboxAPI.ShoppingCart;
import com.partyboxAPI.Store;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class FoodListActivity extends BaseActivity {
    private int[] images = {R.drawable.burger, R.drawable.hotdog, R.drawable.corndog, R.drawable.wrap, R.drawable.wings, R.drawable.chips, R.drawable.icecream,
                            R.drawable.nachos, R.drawable.popcorn, R.drawable.soda, R.drawable.beer, R.drawable.vodka};
    private String[] foods = {"Burgers", "Hot Dogs","Corn Dog", "Chicken Wrap", "Chicken Wings", "Chips", "Ice Cream", "Nachos", "Popcorn", "Soda", "Beer", "Vodka"};
    private String[] price = {"$10", "$5", "$2", "$4", "$3", "$2", "$3", "$3", "$4", "$1", "$3", "$6"};
    private List<Item> items = Lists.newArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);
        Store.init(foods,  price, images);
        items = Lists.newArrayList(Store.getMasterItemSet());
        Collections.sort(items);

        ListView listView = (ListView) findViewById(R.id.listView);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToActivity(v, ActivityNewPartyFirst.class, Direction.RIGHT);
            }
        });

        Button nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Party party = PartyFactory.getNewOrCurrentParty();
                if (party.getOrderInfo() == null) {
                    PartyFactory.getNewOrCurrentParty().setOrderInfo(new OrderInfo());
                }

                switchToActivity(v, CheckoutActivity.class, Direction.LEFT);
            }
        });

    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_layout, null);
            final Item item = items.get(i);
            ShoppingCart cart = ShoppingCart.getInstance();

            ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
            TextView textView_name = (TextView)view.findViewById(R.id.textView_name);
            TextView textView_price = (TextView)view.findViewById(R.id.textView_description);
            final TextView itemQuantityView = (TextView) view.findViewById(R.id.cart_quantity);
            itemQuantityView.setText(Integer.toString(cart.getQuantity(item)));

            /* Set static information */
            imageView.setImageResource(item.getImage());
            textView_name.setText(item.getTitle());
            textView_price.setText(String.format(Locale.ENGLISH, "$%.2f", item.getPrice()));

            /* Set +/- button listeners */
            Button cartAddButton = (Button) view.findViewById(R.id.cart_add);
            cartAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCart cart = ShoppingCart.getInstance();
                    cart.increaseQuantity(item);
                    itemQuantityView.setText(Integer.toString(cart.getQuantity(item)));
                }
            });

            Button cartRemoveButton = (Button) view.findViewById(R.id.button);
            cartRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCart cart = ShoppingCart.getInstance();
                    cart.decreaseQuantity(item);
                    itemQuantityView.setText(Integer.toString(cart.getQuantity(item)));
                }
            });

            return view;
        }
    }
}
