/*
    AlCursado: El guia para llegar a cualquier oficina dentro de la Universidad. No te pierdas dentro ni pierdas mas tiempo

    Copyright (C) 2018  Gonzalez Budinho Joaquin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License (GPL-3.0+) as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program as a file called LICENCE.txt and LICENCE.
    If not, see <https://www.gnu.org/licenses/>.

    for more information o discussions, please contact with the author: joa_gzb@hotmail.com
*/

package com.example.android.alcursado;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

import google_maps_services.MapsActivity;
import perfil_materias.materia;
import perfil_materias.materias_adapter;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * { frag_perfil.ir_a_cursar} interface to handle interaction events.
 * Use the {@link frag_perfil#newInstance} factory method to create an instance of this fragment.
 */
public class frag_perfil extends Fragment {
    // parametros iniciales para construir un fragmento.
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    private ArrayList<materia> lista = new ArrayList<>();
    private materias_adapter adapter;

    public frag_perfil() {
        // este constructor debe estar vacio
    }

    /*
     * constructor de fabrica (factory method) para crear una nueva instancia de este fragmento
     * usando unos parametros especificos, porque luego se crea la actividad que trabaja en paralelo sobre el fragmento
     * y dicha actividad a veces necesita datos para realizar sus tareas.

     */
    public static frag_perfil newInstance(String param1) {
        frag_perfil fragment = new frag_perfil();
        Bundle args = fragment.getArguments();
        if (args == null)
            args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    /*
     * Este es el constructor para crear la actividad que trabaja sobre el fragmento. Notese que toma los parametros de construccion
     * del frag.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }


    /*
     se asigna la vista de usuario (layout) para este . El sistema lo llama cuando
     el fragmento debe diseñar su interfaz de usuario por primera vez.
     Aqui pueden ir las funciones de botones, metodos ante eventos de click en la interfaz, etc.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_perfil, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new materias_adapter(getActivity().getApplicationContext(), 0, lista);
        ListView l = view.findViewById(R.id.listView_pag_perfil);
        l.setAdapter(adapter);
        mostrar_materias();

        //configurar boton para agregar materias que se cursan
        FloatingActionButton fab_add_materia = view.findViewById(R.id.btnFlotante_agregarMateria);
        fab_add_materia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(getActivity()); //llama al constructor Builder para alzar un nuevo Dialog
                final String[] items = {"Matematica Basica", "Quimica"};

                //setear contenido y su evento asociado a los clicks
                b.setItems(items, new DialogInterface.OnClickListener() {
                    materia m;

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        switch (which) {
                            case 0:
                                m = new materia("Matematica Basica", "FICH", "Aula 1-2");
                                lista.add(m);
                                break;
                            case 1:
                                m = new materia("Quimica", "FICH", "Aula 9");
                                lista.add(m);
                                break;
                        }
                        mostrar_materias();
                    }

                });
                b.show();
            }
        });


        //agrego evento de click sobre las materias de cursado, para que me indique como llegar hasta el aula donde se cursa
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               materia m = lista.get(position);
//               mListener.make_path(m.getAula(),m.getEdificio());
                Intent intent = new Intent(getActivity().getApplicationContext(), MapsActivity.class);
                intent.putExtra("edificio",m.getEdificio());
                intent.putExtra("objetivo",m.getAula());
                intent.putExtra("navegacion",true);
                intent.putExtra("latitud",-31.640398507526978);
                intent.putExtra("longitud",-60.671797916293144);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    /*
        * puedo elegir usar este metodo derivado para terminar de inicializar cosas, rellenar informacion faltante en la actividad
        * o recuperar datos de una instancia previa
        * */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        mostrar_materias();
    }

    public void mostrar_materias() {
        //alzar mensajes almacenados o los que se reciben
        adapter.notifyDataSetChanged();
    }



}
