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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.android.alcursado.R;


/*
* esta clase es un dialogo que muestra las opciones para agregar una posicion como favorito por el usuario.
* Se activa despues de presionar el boton flotante para agregar un posicion como favorito.
* */
public class dialogo_agregar_marcador extends DialogFragment {

    //    --------------------------------------------------------------DATOS-------------------------------------------------------------------
    private int piso = 0;
    private String facultad = "Ciudad Universitaria";
    EditText et;
    private onNewPlaceSaved interf;


    //    --------------------------------------------------------------FUNCIONES-------------------------------------------------------------------
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //instancio un contructor de dialogos y se lo asigno a la actividad que convoca a este dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //debo asignar el layout personalizado del dialogo a esta clase
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_dialog_add_place, null);
        builder.setView(view);

        interf = (onNewPlaceSaved) getActivity();

        //rellenar el spinner de pisos de la facultad
        final String[] datos = new String[]{"planta baja", "1er piso", "2do piso", "3er piso", "4to piso"};
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_item, datos);
        adaptador.setDropDownViewResource(R.layout.spinner_dropdown_item);
        Spinner cmbOpciones = view.findViewById(R.id.spinner_piso);
        cmbOpciones.setAdapter(adaptador);
        cmbOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                piso = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //llenar el spinner de facultad
        final String[] facus = new String[]{"Ciudad Universitaria", "FICH", "FADU / FHUC"};
        ArrayAdapter<String> adaptador2 = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_item, facus);
        adaptador2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        Spinner cmbOpciones2 = view.findViewById(R.id.spinner_facultad);
        cmbOpciones2.setAdapter(adaptador2);
        cmbOpciones2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                facultad = facus[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        // agregar 2 botones para cancelar o aceptar la operacion
        builder.setTitle("Agregar una ubicacion favorita");
        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et = view.findViewById(R.id.Edittxt_nombre_ubicacion);
                interf.SaveNewPlace(et.getText().toString(), facultad, piso);
                dialog.cancel();
            }
        });


        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        //configurando la tecla de volver hacia atras, para destruir el dialogo
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.cancel();

                    //esto es importante que lo ponga para liberar memoria en el heap
                    Runtime.getRuntime().gc();
                    return true;
                } else {
                    return false;
                }
            }
        });

        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interf = null;
        Runtime.getRuntime().gc();
    }

    //    --------------------------------------------------------------INTERFAZ-------------------------------------------------------------------
    /*
    * INTERFAZ de comunicacion con la actividad que lo convoca
    * */
    public interface onNewPlaceSaved {
        void SaveNewPlace(String nombre, String facultad, int piso);
    }
}
