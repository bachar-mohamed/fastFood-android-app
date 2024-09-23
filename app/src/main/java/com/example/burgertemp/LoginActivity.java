package com.example.burgertemp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText userEmail;
    private TextInputEditText password;
    private Button login;
    private SQLiteHelper db;
    private TextView signUpBtn;
    private int userId;
    private String userName;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userId=-1;
        userEmail = findViewById(R.id.userEmail);
        password = findViewById(R.id.userPassword);
        login = findViewById(R.id.loginBtn);
        signUpBtn = findViewById(R.id.signup);

        db = new SQLiteHelper(this);
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEmail()&&validatePassword()) {
                    Cursor users = db.getUsers();
                    while (users.moveToNext()) {
                        if (users.getString(2).compareToIgnoreCase(userEmail.getText().toString()) == 0 && users.getString(3).compareTo(password.getText().toString()) == 0) {
                            userId = users.getInt(0);
                            userName=users.getString(1);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("USER_ID", userId);
                            editor.putString("userName",userName);
                            editor.apply();
                            showSuccessDialog();
                            return;
                        }
                    }
                    showFailedDialog();

                }
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                LoginActivity.this.startActivity(i);
            }
        });
    }

    public void showSuccessDialog(){
        dialog= new Dialog(this);
        dialog.setContentView(R.layout.login_success_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_layout));
        dialog.setCancelable(false);
        Button btn = dialog.findViewById(R.id.home);
        dialog.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });
    }

    public void showFailedDialog(){
        dialog= new Dialog(this);
        dialog.setContentView(R.layout.login_failed_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_layout));
        dialog.setCancelable(false);
        Button button = dialog.findViewById(R.id.retry);
        dialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }



    public boolean validateEmail(){
        String val = userEmail.getText().toString();
        if(val.isEmpty()){
            userEmail.setError("field is empty");
        }else{
            userEmail.setError(null);
            return true;
        }
        return false;
    }

    public boolean validatePassword(){
        String val = password.getText().toString();
        if(val.isEmpty()){
            password.setError("field is empty");
        }else{
            password.setError(null);
            return true;
        }
        return false;
    }
}