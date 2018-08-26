package com.edgar_avc.morena;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.edgar_avc.morena.model.usuarios;
import com.edgar_avc.morena.views.administrador.ContainerActivity;
import com.edgar_avc.morena.views.rc.ContainerActivity2;
import com.edgar_avc.morena.views.rg.ContainerActivity3;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("usuario");
    TextInputEditText curp, contra;
    Button sesion;
    String usuario, contrasena;

    String tipoUsuario;


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
    protected void onStart() {
        super.onStart();

        //VERIFICAR USUARIO
        String vUsuario,vContraseña;

        SharedPreferences sharedPref = getSharedPreferences("LoginPreferences",Context.MODE_PRIVATE);
        vUsuario = sharedPref.getString("spUser","null");
        vContraseña = sharedPref.getString("spPassword","null");
        tipoUsuario = sharedPref.getString("spTipo","null");

        if(!vUsuario.equals("null"))
        {
            Intent intent = new Intent(LoginActivity.this, ContainerActivity.class);
            startActivity(intent);
            IniciarSesion(vUsuario,vContraseña);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        curp = (TextInputEditText) findViewById(R.id.TIusername);
        contra = (TextInputEditText) findViewById(R.id.TIpassword);
        sesion = (Button) findViewById(R.id.Blogin);

        sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Query q= bbdd.orderByChild("clave_elec").equalTo(curp.getText().toString());
               q.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                           usuarios miusuario = dataSnapshot1.getValue(usuarios.class);
                           usuario = miusuario.getClave_elec().toString();
                           contrasena = miusuario.getPassword().toString();
                           tipoUsuario = miusuario.getTipoEstructura();
                       }
                       if (curp.getText().toString().equals(usuario) && contra.getText().toString().equals(contrasena)){

                           IniciarSesion(usuario,contrasena);

                       }
                       else
                           Toast.makeText(LoginActivity.this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();

                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });
            }
        });
    }

    public void IniciarSesion(String user, String pass)
    {
        SaveLogin(user,pass);

        Toast.makeText(LoginActivity.this,"Sesión iniciada correctamente",Toast.LENGTH_SHORT).show();

        if(tipoUsuario.equals("Administrador"))
        {
            Log.i("entro","aqui1");
            Intent registro = new Intent(LoginActivity.this, ContainerActivity.class);
            startActivity(registro);
            finish();
        }

        if(tipoUsuario.equals("RC"))
        {
            Log.i("entro","aqui2");
            Intent registro = new Intent(LoginActivity.this, ContainerActivity2.class);
            startActivity(registro);
            finish();
        }

        if(tipoUsuario.equals("RG"))
        {
            Log.i("entro","aqui3");
            Intent registro = new Intent(LoginActivity.this, ContainerActivity3.class);
            startActivity(registro);
            finish();
        }


    }

    public void SaveLogin(String user, String password )
    {
        SharedPreferences sharedPref = getSharedPreferences("LoginPreferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("spUser",user);
        editor.putString("spPassword",password);
        editor.putString("spTipo",tipoUsuario);
        editor.apply();
    }

}

