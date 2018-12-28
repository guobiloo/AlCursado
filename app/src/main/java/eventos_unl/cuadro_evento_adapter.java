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


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.alcursado.R;

import java.util.Calendar;
import java.util.List;

/*
* esta clase simplemente es para crear la vista personalizada de UN SOLO evento dentro de un GridView
* */
public class cuadro_evento_adapter extends ArrayAdapter<evento> {

    private TextView titulo;
    private ImageView portada;
    private TextView fecha;


    public cuadro_evento_adapter(@NonNull Context context, int resource, @NonNull List<evento> objects) {
        super(context, 0, objects);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        evento e = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.vista_evento, parent, false);
        }
        //layout resources are initialized getting them first from the view
        titulo = convertView.findViewById(R.id.titulo);
        portada = convertView.findViewById(R.id.imagen);
        fecha = convertView.findViewById(R.id.fecha);

        titulo.setText(e.getTexto());
        portada.setImageResource(e.getImagen());

        String cal = Integer.toString(e.getFecha().get(Calendar.DAY_OF_MONTH))
                + "/" +
                Integer.toString(e.getFecha().get(Calendar.MONTH))
                + "/" +
                Integer.toString(e.getFecha().get(Calendar.YEAR));
        fecha.setText(cal);

        //: La librería Glide. Esta herramienta gestiona de forma automatizada muchos de los aspectos anteriores en la carga de imágenes.
        Glide.with(portada.getContext())
                .load(e.getImagen())
                .into(portada);

        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public evento getItem(int position) {
        return super.getItem(position);
    }
}
