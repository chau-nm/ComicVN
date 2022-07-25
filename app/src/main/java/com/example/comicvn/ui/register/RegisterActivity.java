package com.example.comicvn.ui.register;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.comicvn.R;
import com.example.comicvn.obj.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEt, passwordEt, confirmPasswordEt, fnameEt, ageEt;
    private AppCompatButton registerBtn;
    private DatabaseReference databaseReference;
    private boolean sameUsername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        initialize();
        enterUsername();
        pressRegisterBtn();
    }

    private void enterUsername(){
        usernameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkUsername(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void pressRegisterBtn(){
        registerBtn.setOnClickListener(view -> {
            register();
        });
    }

    private void checkUsername(String username){
        Query query = databaseReference.orderByChild("username").equalTo(username);
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null) {
                    sameUsername = true;
                    Toast.makeText(RegisterActivity.this, snapshot.toString(), Toast.LENGTH_SHORT).show();
                }else{
                    sameUsername = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void initialize(){
        usernameEt = findViewById(R.id.username_et);
        passwordEt = findViewById(R.id.password_et);
        confirmPasswordEt = findViewById(R.id.confirm_password_et);
        fnameEt = findViewById(R.id.fname_et);
        ageEt = findViewById(R.id.age_et);
        registerBtn = findViewById(R.id.register_btn);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    private void register(){
        if (sameUsername){
            Toast.makeText(RegisterActivity.this, "Tên đăng nhập trùng", Toast.LENGTH_SHORT).show();
        }
        if(passwordEt.getText().equals(confirmPasswordEt.getText())){
            Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
        }
        else{
            String id = databaseReference.push().getKey();
            String username = usernameEt.getText().toString();
            String password = passwordEt.getText().toString();
            String fname = fnameEt.getText().toString();
            int age = Integer.parseInt(ageEt.getText().toString());
            User user = new User(id, username, password, fname, age, 1);
            databaseReference.child(id).setValue(user)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
