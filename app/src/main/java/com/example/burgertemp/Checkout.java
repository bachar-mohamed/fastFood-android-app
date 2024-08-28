package com.example.burgertemp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Checkout extends AppCompatActivity {
    private TextView userName;
    private TextView userAddress;
    private TextView phoneNumber;
    private TextView country;
    private TextView city;
    private TextView zipCode;
    private CheckoutAdapter adapter;
    private RecyclerView recyclerView;
    private TextView totalPrice;
    private ArrayList<Product> checkoutList;
    private Cursor cursor;
    private SQLiteHelper db;
    private SharedPreferences sharedPreferences;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userName = findViewById(R.id.userName);
        userAddress = findViewById(R.id.userAddress);
        phoneNumber = findViewById(R.id.phoneNumber);
        country = findViewById(R.id.country);
        city=findViewById(R.id.city);
        zipCode=findViewById(R.id.zipcode);
        totalPrice=findViewById(R.id.total_price);
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        id = sharedPreferences.getInt("USER_ID", -1);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.cart_item_spacing);
        db = new SQLiteHelper(this);
        adapter = new CheckoutAdapter(this);
        recyclerView = findViewById(R.id.shopping_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        setUserAddress();
        setProductsList();
        totalPrice.setText("$"+setTotalPrice());
    }

    public void setUserAddress(){
        ArrayList<String> data = (ArrayList<String>) getIntent().getSerializableExtra("address");
        userName.setText(sharedPreferences.getString("userName","none"));
        userAddress.setText(data.get(0));
        phoneNumber.setText(data.get(1));
        country.setText(data.get(2));
        city.setText(data.get(3));
        zipCode.setText(data.get(4));
    }

    public void setProductsList() {
        try {
            checkoutList = (ArrayList<Product>) getIntent().getSerializableExtra("cartList");
                adapter.setProducts(checkoutList);
                recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            Log.w("exception", e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public float setTotalPrice(){
        float price=0.0f;
        for(Product list:checkoutList){
            price+=Float.valueOf(list.getPrice());
        }
        return price;
    }
}