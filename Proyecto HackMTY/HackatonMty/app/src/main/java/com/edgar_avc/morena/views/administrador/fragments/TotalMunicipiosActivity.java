package com.edgar_avc.morena.views.administrador.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.edgar_avc.morena.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TotalMunicipiosActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spCasillasUsuario;
    ArrayAdapter comboAdapter;

    List<String> listaMunicipios;

    List<String> listaCasillaClave;

    List<String> listaCasillasAmatlan;
    List<String> listaCasillasCordoba;
    List<String> listaCasillasYanga;
    List<String> listaCasillasTodas;

    List<String> partidos;
    List<String> opciones;

    DatabaseReference dbRefCas;

    String CasillaActual;

    String vUsuario,vContraseña;

    EditText editTextVotos[];
    int votos[];
    EditText grupos[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_municipios);

        showToolbar("Totales",true);

        //OBTENER INFORMACION DEL USUARIO
        ObtenerUsuario();


        editTextVotos =  new EditText[120];
        votos =  new int[120];
        grupos = new EditText[15];

        partidos = new ArrayList<>();
        opciones = new ArrayList<>();


        IniciarEditText();

        //ASIGNAR SPINNER

        spCasillasUsuario = (Spinner) findViewById(R.id.sp_casillasUsuario);
        spCasillasUsuario.setOnItemSelectedListener(this);

        listaMunicipios = new ArrayList<>();
        listaMunicipios.add("Municipio 1");
        listaMunicipios.add("Municipio 2");
        listaMunicipios.add("Municipio 3");
        listaMunicipios.add("Todos");

        comboAdapter = new ArrayAdapter<String>(getApplication(),android.R.layout.simple_spinner_item, listaMunicipios);
        spCasillasUsuario.setAdapter(comboAdapter);



        listaCasillaClave = new ArrayList<>();



        listaCasillasAmatlan = new ArrayList<>();
        listaCasillasCordoba = new ArrayList<>();
        listaCasillasYanga = new ArrayList<>();
        listaCasillasTodas = new ArrayList<>();


        //CONSULTA OBTENER CASILLAS DEL USUARIO
        /*
        dbRefCas = FirebaseDatabase.getInstance().getReference("casilla");
        Query Cref = dbRefCas.orderByKey();
        Cref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshotX) {

                for (DataSnapshot dataSnapshotX2: dataSnapshotX.getChildren()){

                    String casilla= dataSnapshotX2.getKey();
                    listaCasillaClave.add(casilla);
                }

                if(listaCasillaClave.isEmpty())
                    Toast.makeText(getApplication(), "No tienes casillas asignadas", Toast.LENGTH_SHORT).show();

                comboAdapter = new ArrayAdapter<String>(getApplication(),android.R.layout.simple_spinner_item, listaCasillaClave);
                spCasillasUsuario.setAdapter(comboAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */



        //OBTENER CASILLAS DE CADA MUNICIPIO

        dbRefCas = FirebaseDatabase.getInstance().getReference("casilla");
        Query query = dbRefCas.orderByChild("seccion");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshotX) {

                for (DataSnapshot dataSnapshotX2: dataSnapshotX.getChildren()){

                    String casilla= dataSnapshotX2.getKey();
                    Integer seccion=  Integer.parseInt( dataSnapshotX2.child("seccion").getValue().toString() ) ;

                    if(seccion>=294&&seccion<=319) {
                        listaCasillasAmatlan.add(casilla);
                        listaCasillasTodas.add(casilla);
                    }
                    else
                        if(seccion>=980&&seccion<=1093) {
                            listaCasillasCordoba.add(casilla);
                            listaCasillasTodas.add(casilla);
                        }
                        else
                            if(seccion>=4527&&seccion<=4540) {
                                listaCasillasYanga.add(casilla);
                                listaCasillasTodas.add(casilla);
                            }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
        
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.sp_casillasUsuario:

                String id = listaMunicipios.get(i);

                Log.d("salida",id);

                Consultar(id);

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void Consultar(String id)
    {
        List<String> listaActual = new ArrayList<>(listaCasillasAmatlan);

        if(id.equals("Municipio 1"))
            listaActual = new ArrayList<>(listaCasillasAmatlan);
        if(id.equals("Municipio 2"))
            listaActual = new ArrayList<>(listaCasillasCordoba);
        if(id.equals("Municipio 3"))
            listaActual = new ArrayList<>(listaCasillasYanga);
        if(id.equals("Todos"))
            listaActual = new ArrayList<>(listaCasillasTodas);

        for(int i=0; i<editTextVotos.length;i++)
            editTextVotos[i].setText("");

        final int puntosPre[] = new int[partidos.size()];
        final int puntosSen[] = new int[partidos.size()];
        final int puntosGob[] = new int[partidos.size()];
        final int puntosDipFed[] = new int[partidos.size()];
        final int puntosDipLoc[] = new int[partidos.size()];

        for(int i=0; i<listaActual.size(); i++) {

            dbRefCas = FirebaseDatabase.getInstance().getReference("CasillasContadas").child(listaActual.get(i));
            Query q = dbRefCas;
            q.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            String tipo = dataSnapshot1.getKey().toString();

                            for (int i = 0; i < partidos.size(); i++) {
                                String puntos = dataSnapshot1.child(partidos.get(i)).getValue().toString();

                                if (tipo.equals("Presidente"))
                                    puntosPre[i] += Integer.parseInt(puntos);
                                if (tipo.equals("Senador"))
                                    puntosSen[i] += Integer.parseInt(puntos);
                                if (tipo.equals("Gobernador"))
                                    puntosGob[i] += Integer.parseInt(puntos);
                                if (tipo.equals("Dip_fed"))
                                    puntosDipFed[i] += Integer.parseInt(puntos);
                                if (tipo.equals("Dip_loc"))
                                    puntosDipLoc[i] += Integer.parseInt(puntos);
                            }
                        }

                        //AQUI SE OBTIENE LA INFORMACION YA SUMADA Y SE MUESTRA EN LOS EDITTEXT
                        for (int i = 0, j = 0; i < partidos.size(); i++, j += 5) {
                            editTextVotos[j].setText(String.valueOf(puntosPre[i]));
                            editTextVotos[j + 1].setText(String.valueOf(puntosSen[i]));
                            editTextVotos[j + 2].setText(String.valueOf(puntosGob[i]));
                            editTextVotos[j + 3].setText(String.valueOf(puntosDipFed[i]));
                            editTextVotos[j + 4].setText(String.valueOf(puntosDipLoc[i]));
                        }
                        AnalisisGrupos();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // ...
                }
            });
        }

    }

    public void AnalisisGrupos(){
        //Obtener sumas por grupos
        //Grupo A
        //grupos[0].setText(String.valueOf(Integer.parseInt(editTextVotos[0].getText().toString())+Integer.parseInt(editTextVotos[1].getText().toString())));
        grupos[0].setText(String.valueOf(Integer.parseInt(editTextVotos[0].getText().toString())+Integer.parseInt(editTextVotos[10].getText().toString())+
                Integer.parseInt(editTextVotos[25].getText().toString())+Integer.parseInt(editTextVotos[45].getText().toString())+Integer.parseInt(editTextVotos[50].getText().toString())+
                Integer.parseInt(editTextVotos[55].getText().toString())+Integer.parseInt(editTextVotos[60].getText().toString())));

        grupos[1].setText(String.valueOf(Integer.parseInt(editTextVotos[1].getText().toString())+Integer.parseInt(editTextVotos[11].getText().toString())+
                Integer.parseInt(editTextVotos[26].getText().toString())+Integer.parseInt(editTextVotos[46].getText().toString())+Integer.parseInt(editTextVotos[51].getText().toString())+
                Integer.parseInt(editTextVotos[56].getText().toString())+Integer.parseInt(editTextVotos[61].getText().toString())));

        grupos[2].setText(String.valueOf(Integer.parseInt(editTextVotos[2].getText().toString())+Integer.parseInt(editTextVotos[12].getText().toString())+
                Integer.parseInt(editTextVotos[27].getText().toString())+Integer.parseInt(editTextVotos[47].getText().toString())+Integer.parseInt(editTextVotos[52].getText().toString())+
                Integer.parseInt(editTextVotos[57].getText().toString())+Integer.parseInt(editTextVotos[62].getText().toString())));

        grupos[3].setText(String.valueOf(Integer.parseInt(editTextVotos[3].getText().toString())+Integer.parseInt(editTextVotos[13].getText().toString())+
                Integer.parseInt(editTextVotos[28].getText().toString())+Integer.parseInt(editTextVotos[48].getText().toString())+Integer.parseInt(editTextVotos[53].getText().toString())+
                Integer.parseInt(editTextVotos[58].getText().toString())+Integer.parseInt(editTextVotos[63].getText().toString())));

        grupos[4].setText(String.valueOf(Integer.parseInt(editTextVotos[4].getText().toString())+Integer.parseInt(editTextVotos[14].getText().toString())+
                Integer.parseInt(editTextVotos[29].getText().toString())+Integer.parseInt(editTextVotos[49].getText().toString())+Integer.parseInt(editTextVotos[54].getText().toString())+
                Integer.parseInt(editTextVotos[59].getText().toString())+Integer.parseInt(editTextVotos[64].getText().toString())));

        //Grupo B
        grupos[5].setText(String.valueOf(Integer.parseInt(editTextVotos[5].getText().toString())+Integer.parseInt(editTextVotos[15].getText().toString())+
                Integer.parseInt(editTextVotos[30].getText().toString())+Integer.parseInt(editTextVotos[65].getText().toString())+Integer.parseInt(editTextVotos[70].getText().toString())+
                Integer.parseInt(editTextVotos[75].getText().toString())+Integer.parseInt(editTextVotos[80].getText().toString())));

        grupos[6].setText(String.valueOf(Integer.parseInt(editTextVotos[6].getText().toString())+Integer.parseInt(editTextVotos[16].getText().toString())+
                Integer.parseInt(editTextVotos[31].getText().toString())+Integer.parseInt(editTextVotos[66].getText().toString())+Integer.parseInt(editTextVotos[71].getText().toString())+
                Integer.parseInt(editTextVotos[76].getText().toString())+Integer.parseInt(editTextVotos[81].getText().toString())));

        grupos[7].setText(String.valueOf(Integer.parseInt(editTextVotos[7].getText().toString())+Integer.parseInt(editTextVotos[17].getText().toString())+
                Integer.parseInt(editTextVotos[32].getText().toString())+Integer.parseInt(editTextVotos[67].getText().toString())+Integer.parseInt(editTextVotos[72].getText().toString())+
                Integer.parseInt(editTextVotos[77].getText().toString())+Integer.parseInt(editTextVotos[82].getText().toString())));

        grupos[8].setText(String.valueOf(Integer.parseInt(editTextVotos[8].getText().toString())+Integer.parseInt(editTextVotos[18].getText().toString())+
                Integer.parseInt(editTextVotos[33].getText().toString())+Integer.parseInt(editTextVotos[68].getText().toString())+Integer.parseInt(editTextVotos[73].getText().toString())+
                Integer.parseInt(editTextVotos[78].getText().toString())+Integer.parseInt(editTextVotos[83].getText().toString())));

        grupos[9].setText(String.valueOf(Integer.parseInt(editTextVotos[9].getText().toString())+Integer.parseInt(editTextVotos[19].getText().toString())+
                Integer.parseInt(editTextVotos[34].getText().toString())+Integer.parseInt(editTextVotos[69].getText().toString())+Integer.parseInt(editTextVotos[74].getText().toString())+
                Integer.parseInt(editTextVotos[79].getText().toString())+Integer.parseInt(editTextVotos[84].getText().toString())));

        //Grupo C
        grupos[10].setText(String.valueOf(Integer.parseInt(editTextVotos[20].getText().toString())+Integer.parseInt(editTextVotos[35].getText().toString())+
                Integer.parseInt(editTextVotos[40].getText().toString())+Integer.parseInt(editTextVotos[85].getText().toString())+Integer.parseInt(editTextVotos[90].getText().toString())+
                Integer.parseInt(editTextVotos[95].getText().toString())+Integer.parseInt(editTextVotos[100].getText().toString())));

        grupos[11].setText(String.valueOf(Integer.parseInt(editTextVotos[21].getText().toString())+Integer.parseInt(editTextVotos[36].getText().toString())+
                Integer.parseInt(editTextVotos[41].getText().toString())+Integer.parseInt(editTextVotos[86].getText().toString())+Integer.parseInt(editTextVotos[91].getText().toString())+
                Integer.parseInt(editTextVotos[96].getText().toString())+Integer.parseInt(editTextVotos[101].getText().toString())));

        grupos[12].setText(String.valueOf(Integer.parseInt(editTextVotos[22].getText().toString())+Integer.parseInt(editTextVotos[37].getText().toString())+
                Integer.parseInt(editTextVotos[42].getText().toString())+Integer.parseInt(editTextVotos[87].getText().toString())+Integer.parseInt(editTextVotos[92].getText().toString())+
                Integer.parseInt(editTextVotos[97].getText().toString())+Integer.parseInt(editTextVotos[102].getText().toString())));

        grupos[13].setText(String.valueOf(Integer.parseInt(editTextVotos[23].getText().toString())+Integer.parseInt(editTextVotos[38].getText().toString())+
                Integer.parseInt(editTextVotos[43].getText().toString())+Integer.parseInt(editTextVotos[88].getText().toString())+Integer.parseInt(editTextVotos[93].getText().toString())+
                Integer.parseInt(editTextVotos[98].getText().toString())+Integer.parseInt(editTextVotos[103].getText().toString())));

        grupos[14].setText(String.valueOf(Integer.parseInt(editTextVotos[24].getText().toString())+Integer.parseInt(editTextVotos[39].getText().toString())+
                Integer.parseInt(editTextVotos[44].getText().toString())+Integer.parseInt(editTextVotos[89].getText().toString())+Integer.parseInt(editTextVotos[94].getText().toString())+
                Integer.parseInt(editTextVotos[99].getText().toString())+Integer.parseInt(editTextVotos[104].getText().toString())));
    }


    public void ObtenerUsuario()
    {
        //OBTENER USUARIO ACTUAL
        SharedPreferences sharedPref = getApplication().getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
        vUsuario = sharedPref.getString("spUser","null");
        vContraseña = sharedPref.getString("spPassword","null");
    }


    public void IniciarEditText()
    {

        editTextVotos[0] = findViewById(R.id.EditTextVotos1);
        editTextVotos[1] = findViewById(R.id.EditTextVotos2);
        editTextVotos[2] = findViewById(R.id.EditTextVotos3);
        editTextVotos[3] = findViewById(R.id.EditTextVotos4);
        editTextVotos[4] = findViewById(R.id.EditTextVotos5);
        editTextVotos[5] = findViewById(R.id.EditTextVotos6);
        editTextVotos[6] = findViewById(R.id.EditTextVotos7);
        editTextVotos[7] = findViewById(R.id.EditTextVotos8);
        editTextVotos[8] = findViewById(R.id.EditTextVotos9);
        editTextVotos[9] = findViewById(R.id.EditTextVotos10);
        editTextVotos[10] = findViewById(R.id.EditTextVotos11);
        editTextVotos[11] = findViewById(R.id.EditTextVotos12);
        editTextVotos[12] = findViewById(R.id.EditTextVotos13);
        editTextVotos[13] = findViewById(R.id.EditTextVotos14);
        editTextVotos[14] = findViewById(R.id.EditTextVotos15);
        editTextVotos[15] = findViewById(R.id.EditTextVotos16);
        editTextVotos[16] = findViewById(R.id.EditTextVotos17);
        editTextVotos[17] = findViewById(R.id.EditTextVotos18);
        editTextVotos[18] = findViewById(R.id.EditTextVotos19);
        editTextVotos[19] = findViewById(R.id.EditTextVotos20);
        editTextVotos[20] = findViewById(R.id.EditTextVotos21);
        editTextVotos[21] = findViewById(R.id.EditTextVotos22);
        editTextVotos[22] = findViewById(R.id.EditTextVotos23);
        editTextVotos[23] = findViewById(R.id.EditTextVotos24);
        editTextVotos[24] = findViewById(R.id.EditTextVotos25);
        editTextVotos[25] = findViewById(R.id.EditTextVotos26);
        editTextVotos[26] = findViewById(R.id.EditTextVotos27);
        editTextVotos[27] = findViewById(R.id.EditTextVotos28);
        editTextVotos[28] = findViewById(R.id.EditTextVotos29);
        editTextVotos[29] = findViewById(R.id.EditTextVotos30);
        editTextVotos[30] = findViewById(R.id.EditTextVotos31);
        editTextVotos[31] = findViewById(R.id.EditTextVotos32);
        editTextVotos[32] = findViewById(R.id.EditTextVotos33);
        editTextVotos[33] = findViewById(R.id.EditTextVotos34);
        editTextVotos[34] = findViewById(R.id.EditTextVotos35);
        editTextVotos[35] = findViewById(R.id.EditTextVotos36);
        editTextVotos[36] = findViewById(R.id.EditTextVotos37);
        editTextVotos[37] = findViewById(R.id.EditTextVotos38);
        editTextVotos[38] = findViewById(R.id.EditTextVotos39);
        editTextVotos[39] = findViewById(R.id.EditTextVotos40);
        editTextVotos[40] = findViewById(R.id.EditTextVotos41);
        editTextVotos[41] = findViewById(R.id.EditTextVotos42);
        editTextVotos[42] = findViewById(R.id.EditTextVotos43);
        editTextVotos[43] = findViewById(R.id.EditTextVotos44);
        editTextVotos[44] = findViewById(R.id.EditTextVotos45);
        editTextVotos[45] = findViewById(R.id.EditTextVotos46);
        editTextVotos[46] = findViewById(R.id.EditTextVotos47);
        editTextVotos[47] = findViewById(R.id.EditTextVotos48);
        editTextVotos[48] = findViewById(R.id.EditTextVotos49);
        editTextVotos[49] = findViewById(R.id.EditTextVotos50);
        editTextVotos[50] = findViewById(R.id.EditTextVotos51);
        editTextVotos[51] = findViewById(R.id.EditTextVotos52);
        editTextVotos[52] = findViewById(R.id.EditTextVotos53);
        editTextVotos[53] = findViewById(R.id.EditTextVotos54);
        editTextVotos[54] = findViewById(R.id.EditTextVotos55);
        editTextVotos[55] = findViewById(R.id.EditTextVotos56);
        editTextVotos[56] = findViewById(R.id.EditTextVotos57);
        editTextVotos[57] = findViewById(R.id.EditTextVotos58);
        editTextVotos[58] = findViewById(R.id.EditTextVotos59);
        editTextVotos[59] = findViewById(R.id.EditTextVotos60);
        editTextVotos[60] = findViewById(R.id.EditTextVotos61);
        editTextVotos[61] = findViewById(R.id.EditTextVotos62);
        editTextVotos[62] = findViewById(R.id.EditTextVotos63);
        editTextVotos[63] = findViewById(R.id.EditTextVotos64);
        editTextVotos[64] = findViewById(R.id.EditTextVotos65);
        editTextVotos[65] = findViewById(R.id.EditTextVotos66);
        editTextVotos[66] = findViewById(R.id.EditTextVotos67);
        editTextVotos[67] = findViewById(R.id.EditTextVotos68);
        editTextVotos[68] = findViewById(R.id.EditTextVotos69);
        editTextVotos[69] = findViewById(R.id.EditTextVotos70);
        editTextVotos[70] = findViewById(R.id.EditTextVotos71);
        editTextVotos[71] = findViewById(R.id.EditTextVotos72);
        editTextVotos[72] = findViewById(R.id.EditTextVotos73);
        editTextVotos[73] = findViewById(R.id.EditTextVotos74);
        editTextVotos[74] = findViewById(R.id.EditTextVotos75);
        editTextVotos[75] = findViewById(R.id.EditTextVotos76);
        editTextVotos[76] = findViewById(R.id.EditTextVotos77);
        editTextVotos[77] = findViewById(R.id.EditTextVotos78);
        editTextVotos[78] = findViewById(R.id.EditTextVotos79);
        editTextVotos[79] = findViewById(R.id.EditTextVotos80);
        editTextVotos[80] = findViewById(R.id.EditTextVotos81);
        editTextVotos[81] = findViewById(R.id.EditTextVotos82);
        editTextVotos[82] = findViewById(R.id.EditTextVotos83);
        editTextVotos[83] = findViewById(R.id.EditTextVotos84);
        editTextVotos[84] = findViewById(R.id.EditTextVotos85);
        editTextVotos[85] = findViewById(R.id.EditTextVotos86);
        editTextVotos[86] = findViewById(R.id.EditTextVotos87);
        editTextVotos[87] = findViewById(R.id.EditTextVotos88);
        editTextVotos[88] = findViewById(R.id.EditTextVotos89);
        editTextVotos[89] = findViewById(R.id.EditTextVotos90);
        editTextVotos[90] = findViewById(R.id.EditTextVotos91);
        editTextVotos[91] = findViewById(R.id.EditTextVotos92);
        editTextVotos[92] = findViewById(R.id.EditTextVotos93);
        editTextVotos[93] = findViewById(R.id.EditTextVotos94);
        editTextVotos[94] = findViewById(R.id.EditTextVotos95);
        editTextVotos[95] = findViewById(R.id.EditTextVotos96);
        editTextVotos[96] = findViewById(R.id.EditTextVotos97);
        editTextVotos[97] = findViewById(R.id.EditTextVotos98);
        editTextVotos[98] = findViewById(R.id.EditTextVotos99);
        editTextVotos[99] = findViewById(R.id.EditTextVotos100);
        editTextVotos[100] = findViewById(R.id.EditTextVotos101);
        editTextVotos[101] = findViewById(R.id.EditTextVotos102);
        editTextVotos[102] = findViewById(R.id.EditTextVotos103);
        editTextVotos[103] = findViewById(R.id.EditTextVotos104);
        editTextVotos[104] = findViewById(R.id.EditTextVotos105);
        editTextVotos[105] = findViewById(R.id.EditTextVotos106);
        editTextVotos[106] = findViewById(R.id.EditTextVotos107);
        editTextVotos[107] = findViewById(R.id.EditTextVotos108);
        editTextVotos[108] = findViewById(R.id.EditTextVotos109);
        editTextVotos[109] = findViewById(R.id.EditTextVotos110);
        editTextVotos[110] = findViewById(R.id.EditTextVotos111);
        editTextVotos[111] = findViewById(R.id.EditTextVotos112);
        editTextVotos[112] = findViewById(R.id.EditTextVotos113);
        editTextVotos[113] = findViewById(R.id.EditTextVotos114);
        editTextVotos[114] = findViewById(R.id.EditTextVotos115);
        editTextVotos[115] = findViewById(R.id.EditTextVotos116);
        editTextVotos[116] = findViewById(R.id.EditTextVotos117);
        editTextVotos[117] = findViewById(R.id.EditTextVotos118);
        editTextVotos[118] = findViewById(R.id.EditTextVotos119);
        editTextVotos[119] = findViewById(R.id.EditTextVotos120);

        //Grupos
        grupos[0] = findViewById(R.id.EditTextgroup1);
        grupos[1] = findViewById(R.id.EditTextgroup2);
        grupos[2] = findViewById(R.id.EditTextgroup3);
        grupos[3] = findViewById(R.id.EditTextgroup4);
        grupos[4] = findViewById(R.id.EditTextgroup5);
        grupos[5] = findViewById(R.id.EditTextgroup6);
        grupos[6] = findViewById(R.id.EditTextgroup7);
        grupos[7] = findViewById(R.id.EditTextgroup8);
        grupos[8] = findViewById(R.id.EditTextgroup9);
        grupos[9] = findViewById(R.id.EditTextgroup10);
        grupos[10] = findViewById(R.id.EditTextgroup11);
        grupos[11] = findViewById(R.id.EditTextgroup12);
        grupos[12] = findViewById(R.id.EditTextgroup13);
        grupos[13] = findViewById(R.id.EditTextgroup14);
        grupos[14] = findViewById(R.id.EditTextgroup15);

        for(int i=0; i<grupos.length;i++)
            grupos[i].setFocusable(false);


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

    public void showToolbar(String title, boolean upButton)
    {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }


}
