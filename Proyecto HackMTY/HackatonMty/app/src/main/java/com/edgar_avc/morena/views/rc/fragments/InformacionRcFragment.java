package com.edgar_avc.morena.views.rc.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.edgar_avc.morena.R;
import com.edgar_avc.morena.model.usuarios;
import com.edgar_avc.morena.views.rc.ContainerActivity2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InformacionRcFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public InformacionRcFragment() {
        // Required empty public constructor
    }

    Spinner spCasillasUsuario;
    ArrayAdapter comboAdapter;

    List<String> listaCasillaClave;
    List<String> listaCasillaContadas;

    List<String> partidos;
    List<String> opciones;

    DatabaseReference dbRefCas;

    String CasillaActual;

    String vUsuario,vContraseña;

    EditText editTextVotos[];
    int votos[];

    Button button;

    AlertDialog.Builder dialogo1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_informacion_rc, container, false);
        showToolbar("Información casillas",false,view);

        //OBTENER INFORMACION DEL USUARIO
        ObtenerUsuario();

        //OBTENER CASILLAS CONTADAS
        listaCasillaContadas = new ArrayList<>();

        editTextVotos =  new EditText[120];
        votos =  new int[120];

        partidos = new ArrayList<>();
        opciones = new ArrayList<>();

        IniciarEditText(view);

        //ASIGNAR SPINNER

        spCasillasUsuario = (Spinner) view.findViewById(R.id.sp_casillasUsuario);
        spCasillasUsuario.setOnItemSelectedListener(this);

        listaCasillaClave = new ArrayList<>();


        //OBTENER CASILLAS CONTADAS

        dbRefCas = FirebaseDatabase.getInstance().getReference("CasillasContadas");
        Query q = dbRefCas.orderByKey();

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaCasillaContadas.clear();
                for (DataSnapshot dataSnapshotX2: dataSnapshot.getChildren()){

                    String k= dataSnapshotX2.getKey().toString();

                    listaCasillaContadas.add(k);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




        //CONSULTA OBTENER CASILLAS DEL USUARIO

        dbRefCas = FirebaseDatabase.getInstance().getReference("RelacionCasillaColaborador");
        Query Cref = dbRefCas.orderByChild("colaborador").equalTo(vUsuario);
        Cref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshotX) {
                listaCasillaClave.clear();
                for (DataSnapshot dataSnapshotX2: dataSnapshotX.getChildren()){

                    String casilla= dataSnapshotX2.child("casilla").getValue().toString();

                    listaCasillaClave.add(casilla);
                }


                if(listaCasillaClave.isEmpty())
                    Toast.makeText(getActivity(), "No tienes casillas asignadas", Toast.LENGTH_SHORT).show();

                comboAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, listaCasillaClave);
                spCasillasUsuario.setAdapter(comboAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }


    public void ObtenerUsuario()
    {
        //OBTENER USUARIO ACTUAL
        SharedPreferences sharedPref = getActivity().getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
        vUsuario = sharedPref.getString("spUser","null");
        vContraseña = sharedPref.getString("spPassword","null");
    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.sp_casillasUsuario:

                String id = listaCasillaClave.get(i);
                CasillaActual = id;

                Consultar();

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void Consultar()
    {
        if( !listaCasillaClave.isEmpty() ) {

            dbRefCas = FirebaseDatabase.getInstance().getReference("CasillasContadas").child(CasillaActual);
            Query q= dbRefCas;
            q.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int puntosPre[] = new int[partidos.size()];
                    int puntosSen[] = new int[partidos.size()];
                    int puntosGob[] = new int[partidos.size()];
                    int puntosDipFed[] = new int[partidos.size()];
                    int puntosDipLoc[] = new int[partidos.size()];

                    if(dataSnapshot.getValue()!=null)
                    {
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                            String tipo = dataSnapshot1.getKey().toString();

                            for(int i=0; i<partidos.size();i++)
                            {
                                String puntos= dataSnapshot1.child(partidos.get(i)).getValue().toString();

                                if(tipo.equals("Presidente"))
                                    puntosPre[i]= Integer.parseInt(puntos);
                                if(tipo.equals("Senador"))
                                    puntosSen[i]= Integer.parseInt(puntos);
                                if(tipo.equals("Gobernador"))
                                    puntosGob[i]= Integer.parseInt(puntos);
                                if(tipo.equals("Dip_fed"))
                                    puntosDipFed[i]= Integer.parseInt(puntos);
                                if(tipo.equals("Dip_loc"))
                                    puntosDipLoc[i]= Integer.parseInt(puntos);
                            }
                        }

                        for(int i=0,j=0; i<partidos.size();i++,j+=5)
                        {
                            editTextVotos[j].setText( String.valueOf(puntosPre[i]) );
                            editTextVotos[j+1].setText( String.valueOf(puntosSen[i]) );
                            editTextVotos[j+2].setText( String.valueOf(puntosGob[i]));
                            editTextVotos[j+3].setText( String.valueOf(puntosDipFed[i]));
                            editTextVotos[j+4].setText( String.valueOf(puntosDipLoc[i]));
                        }
                    }
                    else
                    {
                        for(int i=0; i<editTextVotos.length;i++)
                            editTextVotos[i].setText("");

                        Toast.makeText(getActivity(), "Aun no hay información de esta casilla", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // ...
                }
            });

        }
        else
            Toast.makeText(getActivity(), "No tienes casillas disponibles", Toast.LENGTH_SHORT).show();
    }

    public void IngresarAbase()
    {
        //dbRefCas = FirebaseDatabase.getInstance().getReference("casilla").child(CasillaActual);
        //dbRefCas.child("contada").setValue("true");

        if( !listaCasillaClave.isEmpty() ) {
            dbRefCas = FirebaseDatabase.getInstance().getReference("CasillasContadas").child(CasillaActual);

            int c = 0;
            for (int i = 0; i < partidos.size(); i++) {
                dbRefCas.child(opciones.get(0)).child(partidos.get(i)).setValue(votos[c]);
                dbRefCas.child(opciones.get(1)).child(partidos.get(i)).setValue(votos[c + 1]);
                dbRefCas.child(opciones.get(2)).child(partidos.get(i)).setValue(votos[c + 2]);
                dbRefCas.child(opciones.get(3)).child(partidos.get(i)).setValue(votos[c + 3]);
                dbRefCas.child(opciones.get(4)).child(partidos.get(i)).setValue(votos[c + 4]);
                c += 5;
            }

            Toast.makeText(getActivity(), "Conteo agregado", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getActivity(), "No tienes casillas disponibles", Toast.LENGTH_SHORT).show();

        for (int i=0; i<115 ;i++)
            editTextVotos[i].setText("");

        Intent intent = new Intent(getActivity(),ContainerActivity2.class);
        startActivity(intent);

    }

    public void IniciarEditText(View view)
    {

        editTextVotos[0] = view.findViewById(R.id.EditTextVotos1);
        editTextVotos[1] = view.findViewById(R.id.EditTextVotos2);
        editTextVotos[2] = view.findViewById(R.id.EditTextVotos3);
        editTextVotos[3] = view.findViewById(R.id.EditTextVotos4);
        editTextVotos[4] = view.findViewById(R.id.EditTextVotos5);
        editTextVotos[5] = view.findViewById(R.id.EditTextVotos6);
        editTextVotos[6] = view.findViewById(R.id.EditTextVotos7);
        editTextVotos[7] = view.findViewById(R.id.EditTextVotos8);
        editTextVotos[8] = view.findViewById(R.id.EditTextVotos9);
        editTextVotos[9] = view.findViewById(R.id.EditTextVotos10);
        editTextVotos[10] = view.findViewById(R.id.EditTextVotos11);
        editTextVotos[11] = view.findViewById(R.id.EditTextVotos12);
        editTextVotos[12] = view.findViewById(R.id.EditTextVotos13);
        editTextVotos[13] = view.findViewById(R.id.EditTextVotos14);
        editTextVotos[14] = view.findViewById(R.id.EditTextVotos15);
        editTextVotos[15] = view.findViewById(R.id.EditTextVotos16);
        editTextVotos[16] = view.findViewById(R.id.EditTextVotos17);
        editTextVotos[17] = view.findViewById(R.id.EditTextVotos18);
        editTextVotos[18] = view.findViewById(R.id.EditTextVotos19);
        editTextVotos[19] = view.findViewById(R.id.EditTextVotos20);
        editTextVotos[20] = view.findViewById(R.id.EditTextVotos21);
        editTextVotos[21] = view.findViewById(R.id.EditTextVotos22);
        editTextVotos[22] = view.findViewById(R.id.EditTextVotos23);
        editTextVotos[23] = view.findViewById(R.id.EditTextVotos24);
        editTextVotos[24] = view.findViewById(R.id.EditTextVotos25);
        editTextVotos[25] = view.findViewById(R.id.EditTextVotos26);
        editTextVotos[26] = view.findViewById(R.id.EditTextVotos27);
        editTextVotos[27] = view.findViewById(R.id.EditTextVotos28);
        editTextVotos[28] = view.findViewById(R.id.EditTextVotos29);
        editTextVotos[29] = view.findViewById(R.id.EditTextVotos30);
        editTextVotos[30] = view.findViewById(R.id.EditTextVotos31);
        editTextVotos[31] = view.findViewById(R.id.EditTextVotos32);
        editTextVotos[32] = view.findViewById(R.id.EditTextVotos33);
        editTextVotos[33] = view.findViewById(R.id.EditTextVotos34);
        editTextVotos[34] = view.findViewById(R.id.EditTextVotos35);
        editTextVotos[35] = view.findViewById(R.id.EditTextVotos36);
        editTextVotos[36] = view.findViewById(R.id.EditTextVotos37);
        editTextVotos[37] = view.findViewById(R.id.EditTextVotos38);
        editTextVotos[38] = view.findViewById(R.id.EditTextVotos39);
        editTextVotos[39] = view.findViewById(R.id.EditTextVotos40);
        editTextVotos[40] = view.findViewById(R.id.EditTextVotos41);
        editTextVotos[41] = view.findViewById(R.id.EditTextVotos42);
        editTextVotos[42] = view.findViewById(R.id.EditTextVotos43);
        editTextVotos[43] = view.findViewById(R.id.EditTextVotos44);
        editTextVotos[44] = view.findViewById(R.id.EditTextVotos45);
        editTextVotos[45] = view.findViewById(R.id.EditTextVotos46);
        editTextVotos[46] = view.findViewById(R.id.EditTextVotos47);
        editTextVotos[47] = view.findViewById(R.id.EditTextVotos48);
        editTextVotos[48] = view.findViewById(R.id.EditTextVotos49);
        editTextVotos[49] = view.findViewById(R.id.EditTextVotos50);
        editTextVotos[50] = view.findViewById(R.id.EditTextVotos51);
        editTextVotos[51] = view.findViewById(R.id.EditTextVotos52);
        editTextVotos[52] = view.findViewById(R.id.EditTextVotos53);
        editTextVotos[53] = view.findViewById(R.id.EditTextVotos54);
        editTextVotos[54] = view.findViewById(R.id.EditTextVotos55);
        editTextVotos[55] = view.findViewById(R.id.EditTextVotos56);
        editTextVotos[56] = view.findViewById(R.id.EditTextVotos57);
        editTextVotos[57] = view.findViewById(R.id.EditTextVotos58);
        editTextVotos[58] = view.findViewById(R.id.EditTextVotos59);
        editTextVotos[59] = view.findViewById(R.id.EditTextVotos60);
        editTextVotos[60] = view.findViewById(R.id.EditTextVotos61);
        editTextVotos[61] = view.findViewById(R.id.EditTextVotos62);
        editTextVotos[62] = view.findViewById(R.id.EditTextVotos63);
        editTextVotos[63] = view.findViewById(R.id.EditTextVotos64);
        editTextVotos[64] = view.findViewById(R.id.EditTextVotos65);
        editTextVotos[65] = view.findViewById(R.id.EditTextVotos66);
        editTextVotos[66] = view.findViewById(R.id.EditTextVotos67);
        editTextVotos[67] = view.findViewById(R.id.EditTextVotos68);
        editTextVotos[68] = view.findViewById(R.id.EditTextVotos69);
        editTextVotos[69] = view.findViewById(R.id.EditTextVotos70);
        editTextVotos[70] = view.findViewById(R.id.EditTextVotos71);
        editTextVotos[71] = view.findViewById(R.id.EditTextVotos72);
        editTextVotos[72] = view.findViewById(R.id.EditTextVotos73);
        editTextVotos[73] = view.findViewById(R.id.EditTextVotos74);
        editTextVotos[74] = view.findViewById(R.id.EditTextVotos75);
        editTextVotos[75] = view.findViewById(R.id.EditTextVotos76);
        editTextVotos[76] = view.findViewById(R.id.EditTextVotos77);
        editTextVotos[77] = view.findViewById(R.id.EditTextVotos78);
        editTextVotos[78] = view.findViewById(R.id.EditTextVotos79);
        editTextVotos[79] = view.findViewById(R.id.EditTextVotos80);
        editTextVotos[80] = view.findViewById(R.id.EditTextVotos81);
        editTextVotos[81] = view.findViewById(R.id.EditTextVotos82);
        editTextVotos[82] = view.findViewById(R.id.EditTextVotos83);
        editTextVotos[83] = view.findViewById(R.id.EditTextVotos84);
        editTextVotos[84] = view.findViewById(R.id.EditTextVotos85);
        editTextVotos[85] = view.findViewById(R.id.EditTextVotos86);
        editTextVotos[86] = view.findViewById(R.id.EditTextVotos87);
        editTextVotos[87] = view.findViewById(R.id.EditTextVotos88);
        editTextVotos[88] = view.findViewById(R.id.EditTextVotos89);
        editTextVotos[89] = view.findViewById(R.id.EditTextVotos90);
        editTextVotos[90] = view.findViewById(R.id.EditTextVotos91);
        editTextVotos[91] = view.findViewById(R.id.EditTextVotos92);
        editTextVotos[92] = view.findViewById(R.id.EditTextVotos93);
        editTextVotos[93] = view.findViewById(R.id.EditTextVotos94);
        editTextVotos[94] = view.findViewById(R.id.EditTextVotos95);
        editTextVotos[95] = view.findViewById(R.id.EditTextVotos96);
        editTextVotos[96] = view.findViewById(R.id.EditTextVotos97);
        editTextVotos[97] = view.findViewById(R.id.EditTextVotos98);
        editTextVotos[98] = view.findViewById(R.id.EditTextVotos99);
        editTextVotos[99] = view.findViewById(R.id.EditTextVotos100);
        editTextVotos[100] = view.findViewById(R.id.EditTextVotos101);
        editTextVotos[101] = view.findViewById(R.id.EditTextVotos102);
        editTextVotos[102] = view.findViewById(R.id.EditTextVotos103);
        editTextVotos[103] = view.findViewById(R.id.EditTextVotos104);
        editTextVotos[104] = view.findViewById(R.id.EditTextVotos105);
        editTextVotos[105] = view.findViewById(R.id.EditTextVotos106);
        editTextVotos[106] = view.findViewById(R.id.EditTextVotos107);
        editTextVotos[107] = view.findViewById(R.id.EditTextVotos108);
        editTextVotos[108] = view.findViewById(R.id.EditTextVotos109);
        editTextVotos[109] = view.findViewById(R.id.EditTextVotos110);
        editTextVotos[110] = view.findViewById(R.id.EditTextVotos111);
        editTextVotos[111] = view.findViewById(R.id.EditTextVotos112);
        editTextVotos[112] = view.findViewById(R.id.EditTextVotos113);
        editTextVotos[113] = view.findViewById(R.id.EditTextVotos114);
        editTextVotos[114] = view.findViewById(R.id.EditTextVotos115);
        editTextVotos[115] = view.findViewById(R.id.EditTextVotos116);
        editTextVotos[116] = view.findViewById(R.id.EditTextVotos117);
        editTextVotos[117] = view.findViewById(R.id.EditTextVotos118);
        editTextVotos[118] = view.findViewById(R.id.EditTextVotos119);
        editTextVotos[119] = view.findViewById(R.id.EditTextVotos120);


        for(int i=0; i<editTextVotos.length;i++)
            editTextVotos[i].setFocusable(false);


        partidos.add("pan");
        partidos.add("pri");
        partidos.add("prd");
        partidos.add("verde");
        partidos.add("pt");
        partidos.add("mc");
        partidos.add("na");
        partidos.add("morena");
        partidos.add("pes");
        partidos.add("pan_prd_mc");
        partidos.add("pan_prd");
        partidos.add("pan_mc");
        partidos.add("prd_mc");
        partidos.add("pri_verde_na");
        partidos.add("pri_verde");
        partidos.add("pri_na");
        partidos.add("verde_na");
        partidos.add("pt_morena_pes");
        partidos.add("pt_morena");
        partidos.add("pt_pes");
        partidos.add("morena_pes");
        partidos.add("independiente");
        partidos.add("independiente2");
        partidos.add("nulos");

        opciones.add("Presidente");
        opciones.add("Senador");
        opciones.add("Gobernador");
        opciones.add("Dip_fed");
        opciones.add("Dip_loc");

    }

    public void showToolbar(String title, boolean upButton, View view )
    {
        android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }


}
