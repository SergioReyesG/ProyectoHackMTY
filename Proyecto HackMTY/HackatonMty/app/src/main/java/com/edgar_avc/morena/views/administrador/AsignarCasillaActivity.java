package com.edgar_avc.morena.views.administrador;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.edgar_avc.morena.R;
import com.edgar_avc.morena.model.RelacionCasilla;
import com.edgar_avc.morena.model.usuarios;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AsignarCasillaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spUsuarios;
    Spinner spCasillas;
    ArrayAdapter comboAdapter;

    List<String> listaUsuarioNombre;
    List<String> listaUsuarioClave;

    List<String> listaCasillaClave;

    DatabaseReference databaseReference;
    DatabaseReference dbRefCas;

    String AgregarUsuario, AgregarCasilla;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_casilla);

        showToolbar("Asignar casilla",true);


        spUsuarios = (Spinner) findViewById(R.id.sp_usuario);
        spUsuarios.setOnItemSelectedListener(this);

        spCasillas = (Spinner) findViewById(R.id.sp_casilla);
        spCasillas.setOnItemSelectedListener(this);




        listaUsuarioNombre = new ArrayList<>();
        listaUsuarioClave = new ArrayList<>();
        listaCasillaClave = new ArrayList<>();




        //databaseReference = FirebaseDatabase.getInstance().getReference("usuario");
        //Query Uref2 = databaseReference.orderByChild("clave_elec");


        databaseReference = FirebaseDatabase.getInstance().getReference("usuario");
        Query Uref2= databaseReference.orderByChild("tipoEstructura").equalTo("RC");
        Uref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    usuarios user = dataSnapshot1.getValue(usuarios.class);
                    String usuario = user.getClave_elec().toString();
                    String nombreUser= user.getNombre().toString() +" "+ user.getAp_pat().toString()+" "+ user.getAp_mat().toString();

                    //Log.i("SALIDA",usuario);

                    listaUsuarioNombre.add(nombreUser);
                    listaUsuarioClave.add(usuario);

                }

                comboAdapter = new ArrayAdapter<String>(AsignarCasillaActivity.this,android.R.layout.simple_spinner_item, listaUsuarioNombre);
                spUsuarios.setAdapter(comboAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        dbRefCas = FirebaseDatabase.getInstance().getReference("casilla");
        Query Cref = dbRefCas.orderByChild("asignada").equalTo(null);
        Cref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshotX) {
                listaCasillaClave.clear();
                for (DataSnapshot dataSnapshotX2: dataSnapshotX.getChildren()){

                    String k= dataSnapshotX2.getKey().toString();

                    Log.d("key",k);

                    listaCasillaClave.add(k);
                }

                comboAdapter = new ArrayAdapter<String>(AsignarCasillaActivity.this,android.R.layout.simple_spinner_item, listaCasillaClave);
                spCasillas.setAdapter(comboAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.sp_usuario:
                String name = listaUsuarioNombre.get(i);
                String id = listaUsuarioClave.get(i);

                //Toast.makeText(this, "Nombre: " + name, Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, "Clave: " + id, Toast.LENGTH_LONG).show();

                AgregarUsuario=id;

                break;

            case R.id.sp_casilla:

                String key = listaCasillaClave.get(i);
                //Toast.makeText(this, "Key: " + key, Toast.LENGTH_SHORT).show();

                AgregarCasilla = key;

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void GoAgregar (View view)
    {
        if(listaCasillaClave.size()>0)
        {
            RelacionCasilla relacionCasilla = new RelacionCasilla(AgregarUsuario, AgregarCasilla);
            databaseReference = FirebaseDatabase.getInstance().getReference("RelacionCasillaColaborador");

            String key = databaseReference.push().getKey();

            Log.d("KEY", key);

            databaseReference.child(key).child("colaborador").setValue(AgregarUsuario);
            databaseReference.child(key).child("casilla").setValue(AgregarCasilla);

            databaseReference = FirebaseDatabase.getInstance().getReference("casilla").child(AgregarCasilla);
            databaseReference.child("asignada").setValue("true");

            Toast.makeText(this, "Casilla asignada", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "No hay casillas disponibles", Toast.LENGTH_SHORT).show();

    }

    public void showToolbar(String title, boolean upButton)
    {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

}
