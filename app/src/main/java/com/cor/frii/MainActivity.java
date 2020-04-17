package com.cor.frii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


//   AppCompatActivity

/* fragment implements

CategoriesFragment.OnFragmentInteractionListener,
    BrandsFragment.OnFragmentInteractionListener,ProductsFragment.OnFragmentInteractionListener,
    GasFragment.OnFragmentInteractionListener,CartdetailFragment.OnFragmentInteractionListener
 */


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        MainFragment.OnFragmentInteractionListener,
        CategoriesFragment.OnFragmentInteractionListener,
        BrandsFragment.OnFragmentInteractionListener,
        ProductsFragment.OnFragmentInteractionListener,
        GasFragment.OnFragmentInteractionListener,
        SettingFragment.OnFragmentInteractionListener {

    //Probando commit

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;


    FloatingActionButton flo_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.navigationToolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.navigationDrawer);
        navigationView = findViewById(R.id.navigationView);

        // estaclecer el evento onclick de navigation
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        cargarFragments();

        //carito de comprar
        flo_cart = findViewById(R.id.fad_cart_order);
        flo_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Cart.class);
                startActivity(intent);
                //finish();
            }
        });

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (menuItem.getItemId() == R.id.home) {
            transaction.replace(R.id.navigationContainer, new MainFragment());
            transaction.commit();
            Toast.makeText(this, "ingreso a home", Toast.LENGTH_SHORT).show();
        }
        if (menuItem.getItemId() == R.id.account) {
            Toast.makeText(this, "en implementacion", Toast.LENGTH_SHORT).show();
        }
        if (menuItem.getItemId() == R.id.setting) {
            transaction.replace(R.id.navigationContainer, new SettingFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            Toast.makeText(this, "en implementacion", Toast.LENGTH_SHORT).show();
        }


        return false;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void cargarFragments() {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        //inicializando al fragment que contendra alos fragments categories y brands
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.navigationContainer, new MainFragment());
        fragmentTransaction.commit();


        /*
        // inicializando los fragment de categories y brands en el app
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.CategoriesSection,new CategoriesFragment()).commit();

        FragmentTransaction tras=getSupportFragmentManager().beginTransaction();
        tras.add(R.id.mainContainer,new BrandsFragment()).commit();

         */
    }


}
