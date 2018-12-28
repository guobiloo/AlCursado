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

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Calendar;

import eventos_unl.cuadro_evento_adapter;
import eventos_unl.detalle_evento_dialog;
import eventos_unl.evento;


public class frag_eventos extends Fragment {
    private ArrayList<evento> lista;



    public frag_eventos() {
    }


    public static frag_eventos newInstance() {
        return new frag_eventos();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_eventos, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //creamos una lista para poner algunos eventos y probar. Esto en realidad se debe hacer recibiendo datos JSON desde un webService
        lista = new ArrayList<>();

        Calendar fecha = Calendar.getInstance();
        fecha.set(2018, Calendar.FEBRUARY, 18);
        lista.add(new evento("Cursos de verano para emprendedores", R.mipmap.innovacion, fecha));
        lista.add(new evento("Formación Extracurricular en Investigación para Alumnos de la Lic. en Biotecnología", R.mipmap.trigo, fecha));
        lista.add(new evento("Cursos de verano para emprendedores", R.mipmap.innovacion, fecha));
        lista.add(new evento("Cursos de verano para emprendedores", R.mipmap.innovacion, fecha));

        //la lista de eventos lo asigno a mi adaptador que se encarga de rellenar la GridView
        cuadro_evento_adapter adaptador = new cuadro_evento_adapter(getActivity().getApplicationContext(), 0, lista);

        GridView grilla = view.findViewById(R.id.grid_eventos);
        grilla.setAdapter(adaptador);

        //asigno la funcion de click sobre los elementos de la GridView para mostrarlos en detalle con un Dialog
        grilla.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                evento e = new evento(lista.get(position));

                Bundle bundle = new Bundle();
                bundle.putString("titulando", e.getTexto());
                bundle.putString("poniendo fecha", e.getFecha().toString());
                bundle.putInt("anio", e.getFecha().get(Calendar.YEAR));
                bundle.putInt("mes", e.getFecha().get(Calendar.MONTH));
                bundle.putInt("dia", e.getFecha().get(Calendar.DAY_OF_MONTH));
                bundle.putInt("poniendo portada", e.getImagen());

                new detalle_evento_dialog().show(getFragmentManager(), "Detalles", bundle);

            }
        });
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }




}
