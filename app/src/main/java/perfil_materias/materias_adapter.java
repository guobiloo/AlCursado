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

package perfil_materias;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.alcursado.R;

import java.util.List;



public class materias_adapter extends ArrayAdapter<materia>{


    public materias_adapter(@NonNull Context context, int resource, @NonNull List<materia> objects) {
        super(context, 0, objects);
    }



    @NonNull
    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Nullable
    @Override
    public materia getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable materia item) {
        return super.getPosition(item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        materia n = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.vista_materia, parent, false);
        }

        TextView nombre_materia = convertView.findViewById(R.id.txt_nombreMateria);
        TextView nombre_facultad = convertView.findViewById(R.id.txt_nombreFacultad);
        TextView aula = convertView.findViewById(R.id.txt_aula);
        ImageView img = convertView.findViewById(R.id.img_facu);

        nombre_materia.setText(n.getNombre());
        nombre_facultad.setText(n.getEdificio());
        aula.setText(n.getAula());
        switch (n.getEdificio()){
            case "FADU / FHUC":
                img.setImageResource(R.mipmap.logo_fadu);
                break;
            case "FICH":
                img.setImageResource(R.mipmap.logo_fich);
                break;
        }

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
