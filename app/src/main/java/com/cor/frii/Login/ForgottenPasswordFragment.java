package com.cor.frii.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cor.frii.R;


public class ForgottenPasswordFragment extends Fragment implements View.OnClickListener {


    private static TextView Email, atras,enviar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.forgottenpassword_layout, container, false);

        atras=view.findViewById(R.id.BackLogin);
        Email=view.findViewById(R.id.EmailLogin);
        enviar=view.findViewById(R.id.EnviarForgottenPassword);



        /*
        ForgottenPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });
        NewAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });
         */

        // init views

        return view;
    }





    @Override
    public void onClick(View v) {

    }
}