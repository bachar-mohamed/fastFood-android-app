package com.example.burgertemp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CartActivity extends AppCompatActivity {

    private CartAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView logoutBtn;
    private TextView counter;
    private ArrayList<Product> cartList;
    private Cursor cursor;
    private TextView price;
    private SQLiteHelper db;
    private SharedPreferences sharedPreferences;
    private AlertDialog.Builder builder;
    private ArrayList<String> userAddress;
    private Dialog dialog;
    private boolean hasAddress;
    private int id;

    private TextView order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        id = sharedPreferences.getInt("USER_ID", -1);
        db = new SQLiteHelper(CartActivity.this);
        userAddress = new ArrayList<>();
        hasAddress = db.checkUserAddress(id);
        if (hasAddress) {
            Cursor cursor = db.getUserAddress(id);
            Toast.makeText(CartActivity.this,Integer.toString(id),Toast.LENGTH_SHORT).show();
            while (cursor.moveToNext()) {
                Toast.makeText(CartActivity.this,"inside while",Toast.LENGTH_SHORT).show();
                userAddress.addAll(Arrays.asList(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }
        }
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.cart_item_spacing);
        price = findViewById(R.id.total_price);
        order = findViewById(R.id.order_btn);
        adapter = new CartAdapter(this);
        recyclerView = findViewById(R.id.recView);
        counter = findViewById(R.id.counter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        setProductsList();
        logoutBtn = findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog(R.layout.logout_dialog);
            }
        });
        price.setText("$" + getPrice());
        ItemTouchHelper helper = new ItemTouchHelper(simpleCallback);
        helper.attachToRecyclerView(recyclerView);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (!hasAddress) {
                    intent = new Intent(CartActivity.this, AddressActivity.class);
                } else {
                    intent = new Intent(CartActivity.this, Checkout.class);
                    intent.putExtra("address",userAddress);
                }
                intent.putExtra("cartList", cartList);
                CartActivity.this.startActivity(intent);

            }
        });

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getBindingAdapterPosition();
            int itemId = cartList.get(position).getId();
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    db.deleteFromCart(itemId);
                    cartList.remove(position);
                    adapter.notifyItemRemoved(position);
                    counter.setText(Integer.toString(cartList.size()));
                    price.setText("¥" + getPrice());
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(CartActivity.this, R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public void showLogoutDialog(int layout) {
        dialog= new Dialog(this);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_layout));
        dialog.setCancelable(false);
        TextView logout = dialog.findViewById(R.id.confirm_logout);
        TextView exit = dialog.findViewById(R.id.abort);
        dialog.show();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                CartActivity.this.startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void setProductsList() {
        cartList = new ArrayList<>();
        cursor = db.getUserCart(id);
        try {
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    cartList.add(new Product(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getInt(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getInt(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getString(8),
                            cursor.getString(9),
                            cursor.getInt(10)
                    ));
                }
                adapter.setProducts(cartList);
                recyclerView.setAdapter(adapter);
                counter.setText(Integer.toString(cartList.size()));
            }
        } catch (Exception e) {
            Log.w("exception", e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public float getPrice() {
        float total = 0;
        for (Product prod : cartList) {
            total += Float.parseFloat(prod.getPrice());
        }
        return total;
    }
}
