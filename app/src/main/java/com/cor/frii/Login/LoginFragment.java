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
import androidx.recyclerview.widget.GridLayoutManager;

import com.cor.frii.R;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private static Button buttonEntrar;
    private static TextView Email,ForgottenPassword,NewAccount;
    private static TextInputLayout password;
    private static ImageView loginImage;
    private static ProgressBar progressBarLogin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_brands, container, false);

        buttonEntrar=view.findViewById(R.id.ButonEntrarLogin);
        Email=view.findViewById(R.id.EmailLogin);
        ForgottenPassword=view.findViewById(R.id.ForgottenPasswordLogin);
        NewAccount=view.findViewById(R.id.NewAccountLogin);
        password=view.findViewById(R.id.LoginPassword);
        progressBarLogin=view.findViewById(R.id.progressBarLogin);


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
