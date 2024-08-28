package com.example.burgertemp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;

public class SignUpActivity extends AppCompatActivity {
    private SQLiteHelper db;
    private TextInputEditText userEmail;
    private TextInputEditText userName;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private Button submit;
    private TextView login;
    AlertDialog.Builder builder;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById((R.id.confirmPassword));
        submit = findViewById(R.id.create_account);
        login = findViewById(R.id.login);
        db = new SQLiteHelper(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePassword() && validateConfirmPassword() && validateEmail()) {
                    if (password.getText().toString().compareTo(confirmPassword.getText().toString()) != 0) {
                        password.setError("passwords dont match");
                        confirmPassword.setError("passwords dont match");
                    } else if (db.checkEmail(userEmail.getText().toString())>0) {
                            userEmail.setError("email already used");
                    } else {
                        password.setError(null);
                        confirmPassword.setError(null);
                        userEmail.setError(null);
                        db.addUser(userName.getText().toString(),userEmail.getText().toString(), password.getText().toString());
                        showSuccessDialog(R.layout.signup_success_dialog);
                    }
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), LoginActivity.class);
                SignUpActivity.this.startActivity(i);
            }
        });
    }

    public boolean validateUserName(){
        String name = userName.getText().toString();
            if(name.isEmpty()){
                userName.setError("field is empty");
                return false;
            }
        userName.setError(null);
        return true;
    }

    public boolean validateEmail() {
        String val = userEmail.getText().toString();
        if (val.isEmpty()) {
            userEmail.setError("field is empty");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(val).matches()) {
            userEmail.setError("invalid email");
        } else {
            userEmail.setError(null);
            return true;
        }
        return false;
    }

    public void showSuccessDialog(int layout) {
        builder = new AlertDialog.Builder(this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        Button btn = layoutView.findViewById(R.id.login);
        builder.setView(layoutView);
        dialog = builder.create();
        dialog.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                SignUpActivity.this.startActivity(intent);
            }
        });
    }

    public boolean validatePassword() {
        String val = password.getText().toString();
        if (val.isEmpty()) {
            password.setError("field is empty");
        } else {
            password.setError(null);
            return true;
        }
        return false;
    }

    public boolean validateConfirmPassword() {
        String val = confirmPassword.getText().toString();
        if (val.isEmpty()) {
            confirmPassword.setError("field is empty");
        } else {
            confirmPassword.setError(null);
            return true;
        }
        return false;
    }
}