package com.edgar_avc.morena.views.administrador;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.edgar_avc.morena.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateAccountActivity extends AppCompatActivity {
    EditText ap_pat,ap_mat,nombre,tel,referente,clave_e,curp,ocr,contra,calle,no_int,no_ext,localidad,municipio,confirmar, cp;
    Button guardar;
    RadioButton rg, rc;

    DatabaseReference bbdd;

    boolean esRC=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        showToolbar("Crear usuario",true);

        ap_pat= (EditText) findViewById(R.id.TextView1);
        ap_mat= (EditText) findViewById(R.id.TextView2);
        nombre= (EditText) findViewById(R.id.TextView3);
        //tel= (EditText) findViewById(R.id.TextView4);
        //referente= (EditText) findViewById(R.id.TextView5);
        clave_e= (EditText) findViewById(R.id.TextView6);
        //curp= (EditText) findViewById(R.id.TextView7);
        //ocr= (EditText) findViewById(R.id.TextView8);
        //calle= (EditText) findViewById(R.id.TextView10);
        //no_ext= (EditText) findViewById(R.id.TextView11);
        //no_int=(EditText) findViewById(R.id.TextView12);
        //localidad = (EditText) findViewById(R.id.TextView13);
        //municipio = (EditText) findViewById(R.id.TextView14);
        contra = (EditText) findViewById(R.id.TextView15);
        confirmar = (EditText) findViewById(R.id.TextView16);
        //cp = (EditText) findViewById(R.id.TextView17);
        guardar= (Button) findViewById(R.id.button);

        bbdd = FirebaseDatabase.getInstance().getReference("usuario");

        guardar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //obtener total de registros en usuarios
                bbdd.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int bandera = 0;
                        long c;
                        String ap_paterno= ap_pat.getText().toString();
                        String ap_materno= ap_mat.getText().toString();
                        String nomb= nombre.getText().toString();
                        //String telefono= tel.getText().toString();
                        //String ref= referente.getText().toString();
                        String clav_elec= clave_e.getText().toString();
                        //String cur= curp.getText().toString();
                        //String oc= ocr.getText().toString();
                        //String street = calle.getText().toString();
                        //String Noint = no_int.getText().toString();
                        //String Noext = no_ext.getText().toString();
                        //String loca = localidad.getText().toString();
                        //String mun = municipio.getText().toString();
                        String cont= contra.getText().toString();
                        String confirm = confirmar.getText().toString();
                        //String codpos = cp.getText().toString();
                        String tipo = "";
                        rg = (RadioButton) findViewById(R.id.radioButton2);
                        rc = (RadioButton) findViewById(R.id.radioButton3);

                        if (rg.isChecked() && rc.isChecked()){
                            Toast.makeText(CreateAccountActivity.this, "Debes elegir solo un tipo de usuario", Toast.LENGTH_SHORT).show();
                        }
                        else if (rg.isChecked() && !rc.isChecked()){
                            tipo = rg.getText().toString();
                            bandera = 1;
                        }
                        else if (!rg.isChecked() && rc.isChecked()){
                            tipo = rc.getText().toString();
                            esRC=true;
                            bandera = 1;
                        }
                        else if (!rg.isChecked() && !rc.isChecked()){
                            Toast.makeText(CreateAccountActivity.this, "Debes marcar un tipo de usuario", Toast.LENGTH_SHORT).show();
                        }

                        if (ap_pat.getText().toString().equals("")||ap_mat.getText().toString().equals("")||nombre.getText().toString().equals("")||
                                clave_e.getText().toString().equals("")||
                                contra.getText().toString().equals("")||confirmar.getText().toString().equals("") ){
                            Toast.makeText(CreateAccountActivity.this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
                            bandera=0;
                        }

                        if (!contra.getText().toString().equals(confirmar.getText().toString())){
                            Toast.makeText(CreateAccountActivity.this, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
                            bandera=0;
                        }

                        c=dataSnapshot.getChildrenCount()+1;

                        if (bandera == 1)
                        {
                            //bbdd.child("Colaborador"+c).setValue(new usuarios(ap_materno,ap_paterno,street,clav_elec,codpos,cur,Noext,Noint,
                            //        loca,mun,nomb,oc,cont,ref,telefono,tipo));

                            bbdd.child("Colaborador"+c).child("ap_mat").setValue(ap_materno);
                            bbdd.child("Colaborador"+c).child("ap_pat").setValue(ap_paterno);
                            bbdd.child("Colaborador"+c).child("nombre").setValue(nomb);
                            bbdd.child("Colaborador"+c).child("clave_elec").setValue(clav_elec);
                            bbdd.child("Colaborador"+c).child("password").setValue(cont);
                            bbdd.child("Colaborador"+c).child("tipoEstructura").setValue(tipo);

                            //SE AGREGA PARA SABER CUANDO UN RC ESTA ASIGNADO
                            if(esRC)
                                bbdd.child("Colaborador"+c).child("estructura_asignacion").setValue("rc_false");

                            Toast.makeText(CreateAccountActivity.this, "Usuario ingresado correctamente", Toast.LENGTH_SHORT).show();
                            ap_pat.setText("");
                            ap_mat.setText("");
                            nombre.setText("");
                            //tel.setText("");
                            //referente.setText("");
                            clave_e.setText("");
                            //curp.setText("");
                            //ocr.setText("");
                            //calle.setText("");
                            //no_int.setText("");
                            //no_ext.setText("");
                            //localidad.setText("");
                            //municipio.setText("");
                            contra.setText("");
                            confirmar.setText("");
                            //cp.setText("");
                            rg.setChecked(false);
                            rc.setChecked(false);
                            esRC=false;

                        }
                        else{
                            Toast.makeText(CreateAccountActivity.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
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


