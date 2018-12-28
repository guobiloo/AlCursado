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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import clima.tiempo1Dia;
import notificaciones_push.SwipeDismissListViewTouchListener;
import notificaciones_push.notificacion;
import notificaciones_push.notificaciones_adapter;
import others_utils.permisos_utils;


public class frag_informacion extends Fragment {

    private TextView dia1, dia2, dia3;
    private TextView temp1, temp2, temp3;
    private TextView cond1, cond2, cond3;
    private ImageView im1, im2, im3;

    private ArrayList<notificacion> lista;
    private notificaciones_adapter adapter;


    public frag_informacion() {
    }


    /*
     * constructor de fabrica (factory method) para crear una nueva instancia de este fragmento
     * usando unos parametros especificos, porque luego se crea la actividad que trabaja en paralelo sobre el fragmento
     * y dicha actividad a veces necesita datos para realizar sus tareas.
     */
    public static frag_informacion newInstance() {
        return new frag_informacion();
    }


    /*
     * Este es el constructor para crear la actividad que trabaja sobre el fragmento. Notese que toma los parametros de construccion
     * del frag.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /*
     se asigna la vista de usuario (layout) para este fragmento
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_informacion, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dia1 = view.findViewById(R.id.txt_dia);
        dia2 = view.findViewById(R.id.txt_dia2);
        dia3 = view.findViewById(R.id.txt_dia3);
        temp1 = view.findViewById(R.id.txt_temperatura);
        temp2 = view.findViewById(R.id.txt_temperatura2);
        temp3 = view.findViewById(R.id.txt_temperatura3);
        cond1 = view.findViewById(R.id.txt_condicion);
        cond2 = view.findViewById(R.id.txt_condicion2);
        cond3 = view.findViewById(R.id.txt_condicion3);
        im1 = view.findViewById(R.id.img_icono_clima);
        im2 = view.findViewById(R.id.img_icono_clima2);
        im3 = view.findViewById(R.id.img_icono_clima3);

//crear peticion al servidor y parsear objetos JSON. checkear si hay primero conexion a internet
        fetchData_fromRequest nAsync = new fetchData_fromRequest();
        String url = "http://api.wunderground.com/api/e8cd515cb766a5bf/lang:SP/forecast/geolookup/q/-31.6106578,-60.697294.json";
        if (permisos_utils.isNetworkAvailable(getActivity().getApplicationContext())) {
            String resultado = null;
            try {
                resultado = nAsync.execute(url).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            parseJSON(resultado);

        } else {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "No se pudo obtener datos del clima. No hay conexion a Internet", Toast.LENGTH_LONG);
            toast.show();
        }

        //inicializo el layout y lista para administrar las notificaciones
        lista = new ArrayList<>();
        adapter = new notificaciones_adapter(getActivity().getApplicationContext(), 0, lista);
        ListView listview = view.findViewById(R.id.lista_notificaciones);
        listview.setAdapter(adapter);

        //eliminar notificaciones en lista deslizando hacia un costado
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listview,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    lista.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Log.d("cantidad lista:", Integer.toString(lista.size()));
                                }

                            }
                        });
        listview.setOnTouchListener(touchListener);

        mostrar_notificaciones();
    }

    /*
    * en base a los datos recabados del webservice, se crea un JSON con los pronosticos del dia de hoy y los siguientes
    * */
    private void parseJSON(String r) {
        try {
            JSONObject response = new JSONObject(r);
            JSONObject obj1 = response.getJSONObject("forecast"); //obtener este objeto en particular
            JSONObject obj2 = obj1.getJSONObject("simpleforecast"); //buscar dentro del objeto, este otro objeto
            JSONArray arr = obj2.getJSONArray("forecastday"); //buscar dentro del objeto, este array
            List<tiempo1Dia> lista = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                lista.add(new tiempo1Dia(
                        arr.getJSONObject(i).getJSONObject("date").getString("weekday"),
                        arr.getJSONObject(i).getJSONObject("high").getString("celsius"),
                        arr.getJSONObject(i).getJSONObject("low").getString("celsius"),
                        arr.getJSONObject(i).getString("conditions"),
                        arr.getJSONObject(i).getString("icon_url")
                ));
            }

            dia1.setText(lista.get(0).getDia());
            dia2.setText(lista.get(1).getDia());
            dia3.setText(lista.get(2).getDia());
            String t = "Min:" + lista.get(0).getCelcius_min() + "  Max:" + lista.get(0).getCelcius_max();
            temp1.setText(t);
            t = "Min:" + lista.get(1).getCelcius_min() + "  Max:" + lista.get(1).getCelcius_max();
            temp2.setText(t);
            t = "Min:" + lista.get(2).getCelcius_min() + "  Max:" + lista.get(2).getCelcius_max();
            temp3.setText(t);
            cond1.setText(lista.get(0).getCondicion());
            cond2.setText(lista.get(1).getCondicion());
            cond3.setText(lista.get(2).getCondicion());

            Picasso.with(getActivity().getApplicationContext())
                    .load(lista.get(0).getLogo_clima())
                    .into(im1);
            Picasso.with(getActivity().getApplicationContext())
                    .load(lista.get(1).getLogo_clima())
                    .into(im2);
            Picasso.with(getActivity().getApplicationContext())
                    .load(lista.get(2).getLogo_clima())
                    .into(im3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void recibeData_fromActivity(String title, String description) {
        lista.add(new notificacion(title, description));

        //alzar mensajes almacenados o los que se reciben
        mostrar_notificaciones();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void mostrar_notificaciones() {
        //alzar mensajes almacenados o los que se reciben
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*
    * creo una clase privada que contiene una funcion que ejecuta un hilo de ejecucion aparte para
    * obtener los datos de un web service
    * */
    private static class fetchData_fromRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result;
            try {
                URL url = new URL(strings[0]);

                //Create a connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //Set methods and timeouts
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);

                //Connect to our url
                urlConnection.connect();

                //Create a new InputStreamReader
                InputStreamReader streamReader = new InputStreamReader(urlConnection.getInputStream());

                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                //Check if the line we are reading is not null
                String aux;
                while ((aux = reader.readLine()) != null) {
                    stringBuilder.append(aux);
                }

                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();

                result = stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                result = null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


}
