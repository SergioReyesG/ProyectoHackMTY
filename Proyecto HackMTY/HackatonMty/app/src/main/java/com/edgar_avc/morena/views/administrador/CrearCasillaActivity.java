package com.edgar_avc.morena.views.administrador;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.edgar_avc.morena.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CrearCasillaActivity extends  AppCompatActivity {
    EditText tipo,tipo_cas,seccion,list_nom, estado, num_mun, localid,ubi, cp;
    Button guardar;
    String cas="";
    DatabaseReference bbdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_casilla);

        showToolbar("Agregar Casilla",true);

        tipo = (EditText) findViewById(R.id.TextView1);
        tipo_cas = (EditText) findViewById(R.id.TextView9);
        seccion = (EditText) findViewById(R.id.TextView2);
        list_nom = (EditText) findViewById(R.id.TextView3);
        //estado = (EditText) findViewById(R.id.TextView4);
        //num_mun = (EditText) findViewById(R.id.TextView5);
        //localid = (EditText) findViewById(R.id.TextView6);
        ubi = (EditText) findViewById(R.id.TextView7);
        cp = (EditText) findViewById(R.id.TextView8);
        guardar = (Button) findViewById(R.id.button);


        bbdd = FirebaseDatabase.getInstance().getReference("casilla");

        guardar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //obtener total de registros en usuarios
                bbdd.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int bandera = 1;
                        String tip= tipo.getText().toString();
                        String tip_cas= tipo_cas.getText().toString();
                        String secc= seccion.getText().toString();
                        String lisnom= list_nom.getText().toString();
                        String ubicac= ubi.getText().toString();
                        String codpos= cp.getText().toString();


                        if (tipo.getText().toString().equals("")||tipo_cas.getText().toString().equals("")||seccion.getText().toString().equals("")||list_nom.getText().toString().equals("")||
                                ubi.getText().toString().equals("")||cp.getText().toString().equals("")){
                            Toast.makeText(CrearCasillaActivity.this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
                            bandera=0;
                        }

                        if (tip_cas.equals("Basica")||tip_cas.equals("Básica")||tip_cas.equals("basica")||tip_cas.equals("básica")){
                            cas = "B";
                        }
                        else if (tip_cas.equals("Contigua1")||tip_cas.equals("contigua1")){
                            cas = "C1";
                        }
                        else{
                            cas = "C2";
                        }


                        if (bandera == 1){

                            bbdd = FirebaseDatabase.getInstance().getReference("casilla").child("Casilla"+secc+cas);

                            bbdd.child("codigoPostal").setValue(codpos);
                            bbdd.child("listaNominal").setValue(lisnom);
                            bbdd.child("seccion").setValue(secc);
                            bbdd.child("tipo").setValue(tip);
                            bbdd.child("tipo_cas").setValue(tip_cas);
                            bbdd.child("ubicacion").setValue(ubicac);


                            //bbdd.child("Casilla"+secc+cas).setValue(new casillasclass(tip,tip_cas,secc,lisnom,ubicac,codpos));

                            Toast.makeText(CrearCasillaActivity.this, "Casilla ingresada correctamente", Toast.LENGTH_SHORT).show();
                            tipo.setText("");
                            tipo_cas.setText("");
                            seccion.setText("");
                            list_nom.setText("");
                            ubi.setText("");
                            cp.setText("");

                        }
                        else{
                            Toast.makeText(CrearCasillaActivity.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }


    public void showToolbar(String title, boolean upButton)
    {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

}