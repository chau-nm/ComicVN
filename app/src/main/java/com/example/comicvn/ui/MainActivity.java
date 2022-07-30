package com.example.comicvn.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.comicvn.R;
import com.example.comicvn.databinding.ActivityMainBinding;
import com.example.comicvn.ui.admin.view.AdminActivity;
import com.example.comicvn.ui.category.ChoseCategoryActivity;
import com.example.comicvn.ui.login.LoginActivity;
import com.example.comicvn.ui.search.SearchActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;
    private DatabaseReference databaseReference;
    private TextView welcomMessage;
    private NavigationView navigationView;
    private NavController navController;
    private DrawerLayout drawer;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        drawer = binding.drawerLayout;
        navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_category, R.id.nav_admin, R.id.nav_history)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        int menuID = getIntent().getIntExtra("CATEGORY_MENU", -1);
        if(menuID == R.layout.fragment_category) navController.navigate(R.id.nav_category);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        welcomMessage = navigationView.getHeaderView(0).findViewById(R.id.welcom_message);

        setUpNav();
        handleNavigation();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // getSupportActionBar().hide();
    }

    private void handleNavigation(){
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_logout:
                    sharedPreferences.edit().clear().apply();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    return true;
                case R.id.nav_login:
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    return true;
                case R.id.nav_category:
                    startActivity(new Intent(MainActivity.this, ChoseCategoryActivity.class));
                    return true;
                case R.id.nav_admin:
                    startActivity(new Intent(MainActivity.this, AdminActivity.class));
                    return true;
                case R.id.nav_home:
                    navController.navigate(R.id.nav_home);
                    drawer.close();
                    return true;
                case R.id.nav_history:
                    navController.navigate(R.id.nav_history);
                    drawer.close();
                    return true;
                default:
                    return false;
            }
        });
    }

    private void setUpNav(){
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("USERID", null);
        String userFname = sharedPreferences.getString("FNAME", "");
        int role = sharedPreferences.getInt("ROLE", 1);
        Log.d("Tagg", userFname);
        if (userId == null) return;
        else{
            welcomMessage.setText("Xin chào " + userFname);
            if (role == 1) {
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.menu_reader);
            }else{
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.activity_main_drawer);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        menu.findItem(R.id.action_search).setChecked(true);
        menu.findItem(R.id.action_search).setOnMenuItemClickListener(menuItem -> {

            switch (menuItem.getItemId()){
                case R.id.action_search:
                    startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return true;
            }

            return false;
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}