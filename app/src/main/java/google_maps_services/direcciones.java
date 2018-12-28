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

package google_maps_services;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.alcursado.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import others_utils.permisos_utils;

/*
* calcular la polilinea desde algun punto de la ciudad hasta la facultad donde se cursa. Se usa cuando el usuario no esta
* dentro de la ciudad universitaria
* */
public class direcciones {
    private String encodedPolyline;
    private PolylineOptions poly;


    public direcciones() {
        poly = new PolylineOptions();
        encodedPolyline = "";
    }


    public PolylineOptions obtener_instrucciones(LatLng origen, LatLng destino, Context context) {

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + origen.latitude + "," + origen.longitude
                + "&destination=" + destino.latitude + "," + destino.longitude + "&key=/* aca va el codigo de google para utilizar sus servicios */";

        NetworkAsyncTask nAsync = new NetworkAsyncTask();

        //crear peticion al servidor y parsear objetos JSON. checkear si hay primero conexion a internet
        if (permisos_utils.isNetworkAvailable(context)) {
            try {
                encodedPolyline = nAsync.execute(url).get();
                SuccessResponse(encodedPolyline);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return poly;
    }


    /*
    * si los datos se obtuvieron correctamente desde el web service, entonces transformo esos datos en un objeto JSON
    * y obtengo a partir de el, los puntos que trazan la polilinea
    * */
    private void SuccessResponse(String s) {
        JSONObject json;
        try {
            json = new JSONObject(s);
            String status = json.getString("status");
            if (status.equalsIgnoreCase("OK")) {
                String polilinea_codif = json.getJSONArray("routes")
                        .getJSONObject(0)
                        .getJSONObject("overview_polyline")
                        .getString("points");

                List<LatLng> lista = PolyUtil.decode(polilinea_codif);
                for (int i = 0; i < lista.size(); i++) {
                    this.poly.add(lista.get(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /*
    * creo una clase privada que contiene una funcion que ejecuta un hilo de ejecucion aparte para
    * obtener los datos de un web service
    * */
    private static class NetworkAsyncTask extends AsyncTask<String, Void, String> {

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
