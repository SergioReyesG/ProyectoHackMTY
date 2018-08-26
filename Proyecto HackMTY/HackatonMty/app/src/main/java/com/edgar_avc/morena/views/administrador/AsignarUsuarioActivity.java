package com.edgar_avc.morena.views.administrador;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.edgar_avc.morena.R;
import com.edgar_avc.morena.model.usuarios;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AsignarUsuarioActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spUsuariosRG;
    Spinner spUsuariosRC;
    ArrayAdapter comboAdapter;

    List<String> listaRGnombre;
    List<String> listaRGclave;

    List<String> listaRCnombre;
    List<String> listaRCclave;
    List<String> listaRCid;

    DatabaseReference databaseReference;
    DatabaseReference dbRefCas;

    String AgregarUsuarioRG, AgregarUsuarioRC,UsuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_usuario);

        showToolbar("Asignar usuario",true);

        //asignar controles e inicializar variables y listas

        spUsuariosRG = (Spinner) findViewById(R.id.sp_usuarioRG);
        spUsuariosRG.setOnItemSelectedListener(this);

        spUsuariosRC = (Spinner) findViewById(R.id.sp_usuarioRC);
        spUsuariosRC.setOnItemSelectedListener(this);



        listaRGnombre = new ArrayList<>();
        listaRGclave = new ArrayList<>();
        listaRCnombre = new ArrayList<>();
        listaRCclave = new ArrayList<>();
        listaRCid= new ArrayList<>();


        //OBTENER USUARIOS RG

        databaseReference = FirebaseDatabase.getInstance().getReference("usuario");
        Query Uref2= databaseReference.orderByChild("tipoEstructura").equalTo("RG");

        Uref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    usuarios user = dataSnapshot1.getValue(usuarios.class);
                    String nombreUser= user.getNombre().toString() +" "+ user.getAp_pat().toString()+" "+ user.getAp_mat().toString();

                    //Log.i("SALIDA",usuario);

                    listaRGnombre.add(nombreUser);
                    listaRGclave.add(user.getClave_elec().toString());
                }

                comboAdapter = new ArrayAdapter<String>(AsignarUsuarioActivity.this,android.R.layout.simple_spinner_item, listaRGnombre);
                spUsuariosRG.setAdapter(comboAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //OBTENER USUARIOS RC
        databaseReference = FirebaseDatabase.getInstance().getReference("usuario");

        Query Uref= databaseReference.orderByChild("estructura_asignacion").equalTo("rc_false");
        Uref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaRCclave.clear();
                listaRCnombre.clear();
                listaRCid.clear();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){


                    usuarios user = dataSnapshot1.getValue(usuarios.class);
                    String nombreUser= user.getNombre().toString() +" "+ user.getAp_pat().toString()+" "+ user.getAp_mat().toString();

                    //Log.i("SALIDA",usuario);

                    String CLAVE = dataSnapshot1.getKey().toString();

                    listaRCnombre.add(nombreUser);
                    listaRCclave.add(user.getClave_elec().toString());

                    listaRCid.add(CLAVE);

                }
                comboAdapter = new ArrayAdapter<String>(AsignarUsuarioActivity.this, android.R.layout.simple_spinner_item, listaRCnombre);
                spUsuariosRC.setAdapter(comboAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    //CUANDO SE SELECCIONA ALGUN NOMBRE DE LAS LISTAS
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.sp_usuarioRG:
                String name = listaRGnombre.get(i);
                String id = listaRGclave.get(i);

                //Toast.makeText(getApplication(), "Nombre: " + name, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplication(), "Clave: " + id, Toast.LENGTH_SHORT).show();

                AgregarUsuarioRG = id;
                break;

            case R.id.sp_usuarioRC:

                String name2 = listaRCnombre.get(i);
                String id2 = listaRCclave.get(i);
                UsuarioID = listaRCid.get(i);

                //Toast.makeText(getApplication(), "Nombre: " + name2, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplication(), "Clave: " + id2, Toast.LENGTH_SHORT).show();

                AgregarUsuarioRC = id2;

                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void GoAsignarUsuario(View view)
    {
        if (listaRCnombre.size()>0)
        {
            databaseReference = FirebaseDatabase.getInstance().getReference("RelacionRG_RC");

            String key = databaseReference.push().getKey();

            databaseReference.child(key).child("RG").setValue(AgregarUsuarioRG);
            databaseReference.child(key).child("RC").setValue(AgregarUsuarioRC);

            databaseReference = FirebaseDatabase.getInstance().getReference("usuario").child(UsuarioID);
            databaseReference.child("estructura_asignacion").setValue("rc_true");

            Toast.makeText(this, "RC asignado", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "No hay RC disponible", Toast.LENGTH_SHORT).show();
    }




    public void showToolbar(String title, boolean upButton)
    {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }


}
