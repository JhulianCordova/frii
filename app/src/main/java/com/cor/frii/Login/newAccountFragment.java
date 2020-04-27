package com.cor.frii.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cor.frii.R;
import com.google.android.material.textfield.TextInputLayout;

public class newAccountFragment extends Fragment implements View.OnClickListener {

    private static TextInputLayout name,DNI,Correo,Telefono,Direccion,contraseña,confirmarContraseña;
    private static Button crearCuenta;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.newaccount_fragment, container, false);

        name=view.findViewById(R.id.Name);
        DNI=view.findViewById(R.id.DNI);
        Correo=view.findViewById(R.id.Correo);
        Telefono=view.findViewById(R.id.Telefono);
        Direccion=view.findViewById(R.id.Direccion);
        contraseña=view.findViewById(R.id.Password);
        confirmarContraseña=view.findViewById(R.id.ConfirmarPassword);
        crearCuenta=view.findViewById(R.id.CrearCuenta);




        // init views

        return view;
    }





    @Override
    public void onClick(View v) {

    }
}
