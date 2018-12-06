package com.partybox;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.partyboxAPI.Party;
import com.partyboxAPI.PartyFactory;
import com.partybox.exceptions.InvalidUserInputException;
import com.partyboxAPI.exceptions.PartyBoxException;
import com.partyboxAPI.PartyFactory;

public class FoodListActivity extends BaseActivity {
    int[] images = {R.drawable.burger, R.drawable.hotdog, R.drawable.corndog, R.drawable.wrap, R.drawable.wings, R.drawable.chips, R.drawable.icecream,
                            R.drawable.nachos, R.drawable.popcorn, R.drawable.soda, R.drawable.beer, R.drawable.vodka};
    String[] foods = {"Burgers", "Hot Dogs","Corn Dog", "Chicken Wrap", "Chicken Wings", "Chips", "Ice Cream", "Nachos", "Popcorn", "Soda", "Beer", "Vodka"};
    String[] price = {"$10", "$5", "$2", "$4", "$3", "$2", "$3", "$3", "$4", "$1", "$3", "$6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);

        ListView listView = (ListView) findViewById(R.id.listView);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String food = String.valueOf(adapterView.getItemAtPosition(i));
//                Toast.makeText(FoodListActivity.this, food, Toast.LENGTH_LONG).show();
                switchToActivity(view, CheckoutActivity.class, Direction.LEFT);
            }
        });

//        Button backButton = findViewById(R.id.back);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switchToActivity(v, ActivityNewPartyFirst.class, Direction.RIGHT);
//            }
//        });
//
//        Button nextButton = findViewById(R.id.next);
//        nextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switchToActivity(v, CheckoutActivity.class, Direction.LEFT);
//            }
//        });

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

            ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
            TextView textView_name = (TextView)view.findViewById(R.id.textView_name);
            TextView textView_price = (TextView)view.findViewById(R.id.textView_description);

            imageView.setImageResource(images[i]);
            textView_name.setText(foods[i]);
            textView_price.setText(price[i]);

            return view;
        }
    }
}
