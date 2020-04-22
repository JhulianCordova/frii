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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Cart extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        CartdetailFragment.OnFragmentInteractionListener,
        CategoriesFragment.OnFragmentInteractionListener,
        ProcesarpedidoFragment.OnFragmentInteractionListener,
        BrandsFragment.OnFragmentInteractionListener,
        GasFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener,
        ProductsFragment.OnFragmentInteractionListener,
        SettingFragment.OnFragmentInteractionListener {


    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cart);
        super.onCreate(savedInstanceState);

        toolbar = findViewById(R.id.navigationToolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.navigationDrawerCart);
        navigationView = findViewById(R.id.navigationView);

        // estaclecer el evento onclick de navigation
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.navigationContainer, new CartdetailFragment());
        fragmentTransaction.commit();

        /*
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.CartContainer,new CartdetailFragment()).commit();
         */
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        Intent intent;
        if (menuItem.getItemId()==R.id.home){

             intent=new Intent(this,MainActivity.class);
            //transaction.replace(R.id.navigationContainer,new MainFragment());
            startActivity(intent);
            finish();
            //transaction.commit();
            Toast.makeText(this, "ingreso a home", Toast.LENGTH_SHORT).show();
        }
        if (menuItem.getItemId()==R.id.account){
             intent=new Intent(getBaseContext(),PerfilActivity.class);
            intent.putExtra("id",R.id.account);
            startActivity(intent);
            Toast.makeText(this,"ingreso a account",Toast.LENGTH_SHORT).show();
        }
        if(menuItem.getItemId()==R.id.Perfil){
            transaction.replace(R.id.navigationContainer,new SettingFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            Toast.makeText(this, "ingreso a configuracion", Toast.LENGTH_SHORT).show();
        }
        if (menuItem.getItemId()==R.id.MisPedidos){
            intent=new Intent(this,PedidosActivity.class);
            startActivity(intent);
        }
        return false;
    }


}
