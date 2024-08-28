package com.example.burgertemp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Product> itemList;
    private ImageView user;
    private MainAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView searchProdView;
    private TextView[] categories;
    private SQLiteHelper db;
    private int userId;
    private TextView title;
    private SharedPreferences sharedPreferences;
    HashMap<Integer, Cursor> dbList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        adapter = new MainAdapter(this);
        categories = new TextView[]{findViewById(R.id.pizzaCat), findViewById(R.id.burgerCat), findViewById(R.id.tacosCat), findViewById(R.id.saladCat)};
        db = new SQLiteHelper(MainActivity.this);
        int count =db.getCount();
        if(db.getCount()==0){
            instantiateData();
            resetLoginId();
        }
        userId = getLoginId();
        //
        recyclerView = findViewById(R.id.recView);
        title = findViewById(R.id.textView2);
        searchProdView = findViewById(R.id.searchView);
        searchProdView.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);
        user = findViewById(R.id.user_img);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        dbList = db.getCategories();
        setProductsList(R.id.pizzaCat);
       searchProdView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(userId>0) {
                    intent = new Intent(MainActivity.this, CartActivity.class);
                }else{
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                MainActivity.this.startActivity(intent);

            }
        });

        for (TextView cat : categories) {
            cat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setProductsList(v.getId());
                }
            });
        }
    }

    public void filter(String text){
        ArrayList<Product> filteredList = new ArrayList<>();
        for (Product prod:itemList){
           if(prod.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(prod);
            }
        }
        adapter.setFilteredList(filteredList);
        recyclerView.setAdapter(adapter);
    }

    public void setProductsList(int id) {
        int index = -1;
        try {
            if (id == R.id.pizzaCat) {
                index = 1;
            } else if (id == R.id.burgerCat) {
                index = 2;
            } else if (id == R.id.tacosCat) {
                index = 3;
            } else if (id == R.id.saladCat) {
                index = 4;
            }

            Cursor cursor = dbList.get(index);
            cursor.moveToPosition(-1);
            itemList = new ArrayList<>();
            if (cursor.getCount() == 0) {
            }
            else {
                while (cursor.moveToNext()) {
                    itemList.add(new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getInt(10)));
                }
                adapter.setProducts(itemList);
                recyclerView.setAdapter(adapter);
            }
        } catch (Exception e) {
        }
    }


   public int getLoginId(){
        int id =sharedPreferences.getInt("USER_ID", -1);
        return id;
   }

   public void resetLoginId(){
       SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.clear();
       editor.apply();
   }

   public void instantiateData(){
       itemList = new ArrayList<>();
       itemList.add(new Product(1, "Pizza Margherita", R.drawable.margherita,  "tomato, cheese, basil", "80.5", 150, "200 g", "250  cal", "4.3", "15-20 min",1 ));
       itemList.add(new Product(2, "Pizza Veggie", R.drawable.veggie, "bell peppers, olives, cheese", "90.0", 120, "210 g", "230 cal", "4.2","16-20 min",1 ));
       itemList.add(new Product(3, "Pizza Rome", R.drawable.rome, "tomato, mozzarella, mushrooms", "100.0", 90, "220 g", "260 cal", "4.4", "18-25 min", 1));
       itemList.add(new Product(4, "Pizza California", R.drawable.california, "tomato, avocado, cheese","90.0", 80, "200 g","240 cal","4.1","15-25 min", 1));
       itemList.add(new Product(5, "Pizza Pepperoni", R.drawable.pepperoni, "pepperoni, cheese, tomato","85.5", 180, "230 g","270 cal","4.5","16-21 min", 1));
       itemList.add(new Product(6, "Pizza Spicy", R.drawable.spicy, "jalapenos, cheese, tomato", "75.0", 100, "210 g","250 cal","4.0", "15-20 min", 1));
       itemList.add(new Product(7, "Pizza Thunder", R.drawable.thunder, "bacon, cheese, tomato","92.5", 70, "220 g","280 cal","4.3", "18-20 min", 1));
       itemList.add(new Product(8, "Big Tasty", R.drawable.bigtasty, "beef, cheese, tomato, barbeque sauce", "60.5", 200, "250 g","650 cal","4.4","12-17 min",2));
       itemList.add(new Product(9, "Quarter Pound", R.drawable.quarterpound, "beef, cheese, pickles","50.5", 220, "230 g", "600 cal","4.3", "11-14 min", 2));
       itemList.add(new Product(10, "Big Mac", R.drawable.bigtasty, "beef, lettuce, cheese", "50.0", 250, "200 g","550 cal", "4.5", "16-18 min", 2));
       itemList.add(new Product(11, "Royal Deluxe", R.drawable.royaldelux, "beef, cheese, lettuce", "62.0", 190, "240 g", "620 cal","4.4","19-25 min", 2));
       itemList.add(new Product(12, "McCrispy", R.drawable.grispyburger, "chicken, lettuce, mayo", "58.5", 210, "220 g", "540 cal","325.5 g","11-17 min",2));
       itemList.add(new Product(13, "McChicken", R.drawable.mchicken, "chicken, lettuce, mayo","45.0", 230, "210 g","510 cal","4.3","14-16 min",2));
       itemList.add(new Product(14, "Tacos Chicken", R.drawable.chickentacos, "chicken, cheese, lettuce","25.5", 180, "150 g","400 cal","4.2", "12-17 min",3));
       itemList.add(new Product(15, "Tacos Nugget", R.drawable.nuggettacos, "nuggets, cheese, lettuce","30.0", 170, "140 g","380 cal","4.1", "10-15 min",3));
       itemList.add(new Product(16, "Tacos mixte", R.drawable.largetacos, "beef, chicken, cheese","45.0", 160, "160 g","420 cal","4.3", "12-16 min",3));
       itemList.add(new Product(17, "Chicken Salad", R.drawable.chickensalad, "chicken, lettuce, tomatoes","35.0", 140, "180 g","300 cal","4.2", "8-13 min",4));
       itemList.add(new Product(18, "Coleslaw Salad", R.drawable.coleslawsalad, "cabbage, carrots, mayo","45.0", 130, "150 g","200 cal","4.0", "5-10 min",4));

       for(Product prod : itemList){
           db.addProduct(prod.getId(), prod.getName(), prod.getImg_url(), prod.getIngredients(), prod.getPrice(), prod.getOrders(),prod.getWeight(),prod.getCalories(),prod.getRating(),prod.getDelay(),prod.getCategory());
       }
   }


}
/*

*/