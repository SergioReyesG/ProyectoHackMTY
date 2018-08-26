package com.edgar_avc.morena.views.rg;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.edgar_avc.morena.R;
import com.edgar_avc.morena.views.administrador.fragments.PerfilFragment;
import com.edgar_avc.morena.views.rc.fragments.InformacionRcFragment;
import com.edgar_avc.morena.views.rc.fragments.InicioRcFragment;
import com.edgar_avc.morena.views.rg.fragments.InformacionRgFragment;
import com.edgar_avc.morena.views.rg.fragments.InicioRgFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class ContainerActivity3 extends AppCompatActivity {

    @Override
    public void onBackPressed() {

        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Desea salir de la app?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                /*finish();
                System.exit(0);*/
            }
        });
        dialogo1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //cancelar();
            }
        });
        dialogo1.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        BottomBar bottomBar = findViewById(R.id.bottombar);

        int tab=1;

        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            tab = extras.getInt("tab");
        }

        switch (tab)
        {
            case 1:
                bottomBar.setDefaultTab(R.id.TabHome);
                break;
            case 2:
                bottomBar.setDefaultTab(R.id.Tab2);
                break;
            case 3:
                bottomBar.setDefaultTab(R.id.Tab3);
                break;
            default:
                bottomBar.setDefaultTab(R.id.TabHome);
                break;
        }

        bottomBar.setOnTabSelectListener(new OnTabSelectListener()
        {
            @Override
            public void onTabSelected(int tabId)
            {
                switch (tabId)
                {
                    case R.id.TabHome:
                        InicioRgFragment inicioRgFragment = new InicioRgFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerPrincipal, inicioRgFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;

                    case R.id.Tab2:
                        InformacionRgFragment inicioRgFragment1 = new InformacionRgFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerPrincipal, inicioRgFragment1)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;

                    case R.id.Tab3:
                        PerfilFragment perfilFragment = new PerfilFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerPrincipal, perfilFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;

                }
            }
        });
    }
}
