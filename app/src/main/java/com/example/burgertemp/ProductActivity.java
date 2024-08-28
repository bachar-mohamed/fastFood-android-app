package com.example.burgertemp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProductActivity extends AppCompatActivity {

    private TextView prodName ;
    private TextView prodWeight;
    private TextView prodCalories;
    private TextView prodRating;
    private boolean added;
    private TextView prodDelay;
    private TextView prodPrice;
    private Button addToCart;
    private Intent intent;
    private ImageView prodImg;
    private TextView ingredients;
    private SQLiteHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        added=false;
        db = new SQLiteHelper(ProductActivity.this);
        prodName= findViewById(R.id.productName);
        prodWeight=findViewById(R.id.weight);
        prodCalories=findViewById(R.id.calories);
        prodDelay=findViewById(R.id.delay);
        prodRating=findViewById(R.id.rating);
        prodPrice = findViewById(R.id.price);
        addToCart=findViewById(R.id.button);
        prodImg = findViewById(R.id.imageView);
        ingredients=findViewById(R.id.ingredients);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId =getLoginId();
                Handler handler=null;
                if(userId>0){
                    if(!added) {
                        db.addToUserCart(userId, Integer.parseInt(getIntent().getStringExtra("id")), 1);
                        disableButton();
                       handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                addedButton();
                            }
                        },2000);


                    }else{
                        db.deleteFromCart(Integer.parseInt(getIntent().getStringExtra("id")));

                        disableButton();
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                removedButton();
                            }
                        },2000);
                    }
                }else{
                    intent = new Intent(ProductActivity.this,LoginActivity.class);
                    ProductActivity.this.startActivity(intent);
                }
            }
        });
        getIntentData();
        if(checkProd()) {
            addedButton();
        }
    }

    public void getIntentData(){

            prodName.setText(getIntent().getStringExtra("name"));
            prodWeight.setText(getIntent().getStringExtra("weight"));
            prodCalories.setText(getIntent().getStringExtra("calories"));
            prodDelay.setText(getIntent().getStringExtra("time"));
            prodPrice.setText("¥"+getIntent().getStringExtra("price"));
            prodRating.setText(getIntent().getStringExtra("reviews"));
            prodImg.setImageResource(Integer.parseInt(getIntent().getStringExtra("url")));
            ingredients.setText(getIntent().getStringExtra("ingredients"));

    }

    public void disableButton(){
        addToCart.setBackgroundColor(getResources().getColor(R.color.grey));
        addToCart.setText("please wait...");
        addToCart.setEnabled(false);
    }

    public boolean checkProd(){
        Cursor data = db.getUserCart(getLoginId());
        int prodId =  Integer.parseInt(getIntent().getStringExtra("id"));
        while(data.moveToNext()){
            if(data.getInt(0)==prodId){
                added=true;
                return added ;
            }
        }
        //data.close();
        return added;
    }

    public void addedButton(){
        added=true;
        addToCart.setBackgroundColor(getResources().getColor(R.color.green));
        addToCart.setText("added to cart");
        addToCart.setEnabled(true);
    }
    public void removedButton(){
        added=false;
        addToCart.setBackgroundColor(Color.BLACK);
        addToCart.setText("add to cart");
        addToCart.setEnabled(true);
    }

    public int getLoginId(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("USER_ID", -1);
        return userId;
    }
}

/*
SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();*/