package com.example.burgertemp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

public class AddressActivity extends AppCompatActivity {

    private String[] countries;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;
    private SQLiteHelper db;
    private String country;
    private TextInputEditText address ;
    private CountryCodePicker ccp ;
    private TextInputEditText phoneNumber;
    private TextInputEditText zipCode;
    private TextInputEditText city;
    private Button submitButton;
    private SharedPreferences sharedPreferences;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.address_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        id =sharedPreferences.getInt("USER_ID", -1);
        db=new SQLiteHelper(this);
        address = findViewById(R.id.addressInput);
        ccp =findViewById(R.id.ccp);
        phoneNumber = findViewById(R.id.phoneNumberInput);
        city = findViewById(R.id.city);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        adapter = new ArrayAdapter<>(this,R.layout.drop_down_item,getCountries());
        zipCode = findViewById(R.id.zipCode);
        submitButton = findViewById(R.id.submit);
        ccp.registerCarrierNumberEditText(phoneNumber);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                country = parent.getItemAtPosition(position).toString();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validInput()){
                db.setUserAddress(id,address.getText().toString(),country,city.getText().toString(),phoneNumber.getText().toString(),zipCode.getText().toString());
                Intent intent = new Intent(AddressActivity.this,Checkout.class);
                ArrayList<Product> cartList=null;
                ArrayList<String> addressData=new ArrayList<>(Arrays.asList(address.getText().toString(),phoneNumber.getText().toString(),country,city.getText().toString(),zipCode.getText().toString()));
                try {
                    cartList = (ArrayList<Product>) getIntent().getSerializableExtra("cartList");
                }catch (ClassCastException  exp){
                    exp.printStackTrace();
                }
                intent.putExtra("address",addressData);
                intent.putExtra("cartList",cartList);
                AddressActivity.this.startActivity(intent);
                }

            }
        });
    }

    public ArrayList<String> getCountries(){
        SortedSet<String> countries = new TreeSet<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            if (!TextUtils.isEmpty(locale.getDisplayCountry())) {
                countries.add(locale.getDisplayCountry());
            }
        }
        return new ArrayList<>(countries);
    }

    public boolean validInput(){
        if(address.getText().toString().isEmpty()){
            address.setError("field is empty");
            return false;
        }else if(phoneNumber.getText().toString().isEmpty()){
            phoneNumber.setError("field is empty");
            return false;
        } else if (city.getText().toString().isEmpty()){
            city.setError("field is empty");
            return false;
        }else if(zipCode.getText().toString().isEmpty()){
            zipCode.setError("field is empty");
            return false;
        }
        return true;
    }
}