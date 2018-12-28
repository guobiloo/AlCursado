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

package eventos_unl;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.alcursado.R;

import java.util.Calendar;

/*
* esta clase es para crear un dialogo personalizado de tal manera de mostrar con mas detalles un evento de la unl, con la posibilidad
* de poder guardarlo como recordatorio en google Calendar. Notese que extiende de la clase DialogFragment. primero se asigna
* su layout, luego para pasarle argumentos desde la actividad que lo invoca, se sobreescribe la funcion heredada show() para obtener
* un bundle de datos y llenar la informacion.
* */
public class detalle_evento_dialog extends DialogFragment {

    Bundle bundle;
    private TextView titulo, descripcion, fecha;
    private ImageView portada;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //instancio un contructor de dialogos y se lo asigno a la actividad que convoca a este dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //debo asignar el layout personalizado del dialogo a esta clase
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_detalle_evento, null);
        builder.setView(view);

        //inicializo elementos
        titulo = view.findViewById(R.id.txt_titulo);
        descripcion = view.findViewById(R.id.txt_descripcion);
        fecha = view.findViewById(R.id.txt_fecha);
        portada = view.findViewById(R.id.img_portada_detalle);

        titulo.setText(bundle.getString("titulando"));
        String aux = Integer.toString(bundle.getInt("dia")) +
                "/" + Integer.toString(bundle.getInt("mes")) +
                "/" + Integer.toString(bundle.getInt("anio"));
        fecha.setText(aux);
        portada.setImageResource(bundle.getInt("poniendo portada"));

        ImageButton boton = view.findViewById(R.id.btn_agregar_calendario);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_agendar_evento();
            }
        });

        //configurando la tecla de volver hacia atras, para destruir el dialogo
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });

        return builder.create();
    }


    /*
    * este es el metodo de la clase DialogFragment
    * */
    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }


    /*
    * este es un metodo polimorfico que viene de la clase DialogFragment, construido para recibir un Bundle donde se pasan los datos del evento al dialogo
    * */
    public void show(FragmentManager manager, String tag, Bundle b) {
        super.show(manager, tag);
        this.bundle = b;
    }


    /*
     * Se agrega un evento al google Calendar o la app determinada mediante Intent
     * */
    public void click_agendar_evento() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, titulo.getText().toString());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, descripcion.getText().toString());

        Calendar calDate = Calendar.getInstance();
        calDate.set(bundle.getInt("anio"), bundle.getInt("mes"), bundle.getInt("dia"));
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calDate.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calDate.getTimeInMillis());

        startActivity(intent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.dismiss();
    }
}
