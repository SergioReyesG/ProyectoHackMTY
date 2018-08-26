package com.edgar_avc.morena.views.administrador.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.edgar_avc.morena.R;
import com.edgar_avc.morena.views.administrador.AsignarCasillaActivity;
import com.edgar_avc.morena.views.administrador.AsignarUsuarioActivity;
import com.edgar_avc.morena.views.administrador.CrearCasillaActivity;
import com.edgar_avc.morena.views.administrador.CreateAccountActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    Button registrar, casilla, BTasignarcasilla;
    Button BTasignarusuario,BTtotales1;

    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        showToolbar("Inicio",false,view);


        registrar = view.findViewById(R.id.button2);
        casilla = view.findViewById(R.id.button3);
        BTasignarcasilla= view.findViewById(R.id.button4);
        BTasignarusuario= view.findViewById(R.id.buttonAsignarUsuarios);
        BTtotales1= view.findViewById(R.id.button5);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registro = new Intent(getActivity(), CreateAccountActivity.class);
                startActivity(registro);
            }
        });

        casilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent casi = new Intent(getActivity(), CrearCasillaActivity.class);
                startActivity(casi);
            }
        });

        BTasignarcasilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent casi = new Intent(getActivity(), AsignarCasillaActivity.class);
                startActivity(casi);
            }
        });

        BTasignarusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent casi = new Intent(getActivity(), AsignarUsuarioActivity.class);
                startActivity(casi);
            }
        });

        BTtotales1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent casi = new Intent(getActivity(), TotalMunicipiosActivity.class);
                startActivity(casi);
            }
        });



        return view;
    }



    public void showToolbar(String title, boolean upButton, View view )
    {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
