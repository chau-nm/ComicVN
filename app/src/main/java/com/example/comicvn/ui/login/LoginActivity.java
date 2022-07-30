package com.example.comicvn.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.comicvn.R;
import com.example.comicvn.obj.User;
import com.example.comicvn.ui.MainActivity;
import com.example.comicvn.ui.register.RegisterActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {

    public static final String LOGIN_NAME = "Login";
    private EditText usernameEt, passwordEt;
    private AppCompatButton loginBtn, registerBtn;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        initialize();
        pressLoginBtn();
        pressRegisterBtn();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initialize(){
        usernameEt = findViewById(R.id.username_et);
        passwordEt = findViewById(R.id.password_et);
        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.register_btn);
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    private void pressRegisterBtn(){
        registerBtn.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void pressLoginBtn(){
        loginBtn.setOnClickListener(view -> {
            String username = usernameEt.getText().toString();
            String password = passwordEt.getText().toString();
            login(username, password);
        });
    }

    private void login(String username, String password){
        Query query = databaseReference.orderByChild("username").equalTo(username);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                User user = iterator.hasNext() ? iterator.next().getValue(User.class) : null;
                if (user == null || !user.getPassword().equals(password)){
                    Toast.makeText(LoginActivity.this, "Login fail", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USERID", user.getId());
                    editor.putString("FNAME", user.getName());
                    editor.putInt("ROLE", user.getRole());
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
