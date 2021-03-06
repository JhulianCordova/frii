package com.cor.frii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.cor.frii.Login.LoginActivity;
import com.cor.frii.persistence.Session;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class PedidosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        MisPedidosFragment.OnFragmentInteractionListener,
        MapsPerdidos.OnFragmentInteractionListener,
        AccountFragment.OnFragmentInteractionListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        //Validar informacion del usuario
        Session session = new Session(getApplicationContext());
        final int token = session.getToken();
        if (token == 0 || token < 0) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            System.out.println("LAS CREDENCIALES SON INVALIDAS");
        }
        //--


        toolbar = findViewById(R.id.navigationToolbar);
        setSupportActionBar(toolbar);

        /*
        drawerLayout = findViewById(R.id.navigationDrawerPedidos);
        navigationView = findViewById(R.id.navigationView);

        // estaclecer el evento onclick de navigation
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

         */

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navigationContainer, new MisPedidosFragment())
                    .commit();
        }

        /*FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.navigationContainer, new MisPedidosFragment());
        transaction.commit();
*/

        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            Intent intent;
            switch (menuItem.getItemId()) {
                case R.id.HomeBottom:
                    intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.MapaBottom:
                    Toast.makeText(getBaseContext(), "Por favor haga click en un pedido", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.PedidosBottom:
                    transaction.replace(R.id.navigationContainer, new MisPedidosFragment());
                    transaction.commit();
                    break;

            }
            return false;
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
