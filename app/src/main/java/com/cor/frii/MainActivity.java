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

import android.widget.TextView;
import android.widget.Toast;


import com.cor.frii.persistence.DatabaseClient;
import com.cor.frii.persistence.DatabaseHelper;
import com.cor.frii.persistence.Session;
import com.cor.frii.persistence.entity.Acount;
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
        SettingFragment.OnFragmentInteractionListener,
        MisPedidosFragment.OnFragmentInteractionListener {


    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    //--
    TextView lblUsername, lblEmail;

    //e
    String tokenTemp = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoyLCJ1c2VybmFtZSI6ImFsZGFlbHZpc0Bob3RtYWlsLmNvbSIsImV4cCI6MTU5MDQyODEwNiwiZW1haWwiOiIiLCJvcmlnX2lhdCI6MTU4NzgzNjEwNn0.pPDXXuhyer6GaxozPFqcaPsSkl0ANnuXROiIuxQViQw";
    FloatingActionButton flo_cart;
    FloatingActionButton flo_order_pedido;

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
        llenarInfoUsuario();

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

        flo_order_pedido = findViewById(R.id.fad_order_pedido);
        flo_order_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), PedidosActivity.class);
                startActivity(intent);
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
            Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
        }
        if (menuItem.getItemId() == R.id.account) {
            //transaction.replace(R.id.navigationContainer,new AccountFragment());

            Intent intent = new Intent(getBaseContext(), PerfilActivity.class);
            intent.putExtra("id", R.id.account);
            startActivity(intent);

            Toast.makeText(this, "Account", Toast.LENGTH_SHORT).show();
        }
        if (menuItem.getItemId() == R.id.Perfil) {

            Intent intent = new Intent(getBaseContext(), PerfilActivity.class);
            String title = menuItem.getTitle().toString();
            intent.putExtra("name", title);
            intent.putExtra("id", R.id.Perfil);
            startActivity(intent);

            /*
            transaction.replace(R.id.navigationContainer, new SettingFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();

             */
        }
        if (menuItem.getItemId() == R.id.MisPedidos) {

            Intent intent = new Intent(getBaseContext(), PedidosActivity.class);
            startActivity(intent);
            /*
            transaction.replace(R.id.navigationContainer, new MisPedidosFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            Toast.makeText(this, "Mis Pedidos", Toast.LENGTH_SHORT).show();

             */

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

    private void llenarInfoUsuario() {
        Session session = new Session(getApplicationContext());
        session.setToken(tokenTemp);
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        Acount acount = helper.login(tokenTemp);
        if (acount != null) {
            Toast.makeText(getApplicationContext(), "Esta loggeado", Toast.LENGTH_LONG).show();


        } else {
            Toast.makeText(getApplicationContext(), "Error de  loggeado", Toast.LENGTH_LONG).show();
        }

    }


}
