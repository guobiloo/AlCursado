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
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;

import google_maps_services.MapsActivity;


public class frag_lugares extends Fragment {
    ExpandingList expandingList;


    public frag_lugares() {
    }


    /*
     * constructor de fabrica (factory method) para crear una nueva instancia de este fragmento
     * usando unos parametros especificos, porque luego se crea la actividad que trabaja en paralelo sobre el fragmento
     * y dicha actividad a veces necesita datos para realizar sus tareas.
     * @return A new instance of fragment frag_perfil.
     */
    public static frag_lugares newInstance() {
        return new frag_lugares();
    }


    /*
     Este es el constructor para crear la actividad que trabaja sobre el fragmento. Notese que toma los parametros de construccion
     del frag.
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
        return inflater.inflate(R.layout.activity_lugares, container, false);
    }


    /*
    * siemrpe que tenga que referirme a objetos correspondiente al layout, hacerlo sobre esta funcion
    * */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //obtengo instancia del ExpandingView y lo relaciono con un layout
        expandingList = view.findViewById(R.id.expanding_list_fich);

        //Let's start inflating the ExpandingList
        ExpandingItem oficinas = expandingList.createNewItem(R.layout.expanding_layout);

        //ExpandingItem extends from View, so you can call findViewById to get any View inside the layout. We set here an header
        TextView ItemHeader = oficinas.findViewById(R.id.title);
        ItemHeader.setText(R.string.oficinas);

        //metiendo colores
        oficinas.setIndicatorColorRes(R.color.light_blue_2); //color de seleccion
        oficinas.setIndicatorIconRes(R.mipmap.silla_2_gigante); //poner un icono al header

        //this will create a cuple of subitems
        oficinas.createSubItems(3);

        //configurando subitems (oficinas) de fich
        View subItemZero = oficinas.getSubItemView(0);
        ((TextView) subItemZero.findViewById(R.id.txt_sub_title)).setText(R.string.alumnado);
        View subItemOne = oficinas.getSubItemView(1);
        ((TextView) subItemOne.findViewById(R.id.txt_sub_title)).setText(R.string.secretaria_decanato);
        View subItemtwo = oficinas.getSubItemView(2);
        ((TextView) subItemtwo.findViewById(R.id.txt_sub_title)).setText(R.string.CEICH_MesaEntradas);
        for (int i = 0; i < oficinas.getSubItemsCount(); i++) {
            View subItem = oficinas.getSubItemView(i);
            subItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView text = v.findViewById(R.id.txt_sub_title);
                    Intent intent = new Intent(getActivity().getApplicationContext(), MapsActivity.class);
                    intent.putExtra("edificio", "FICH");
                    intent.putExtra("objetivo", text.getText().toString());
                    intent.putExtra("navegacion", true);
                    intent.putExtra("latitud", -31.640398507526978);
                    intent.putExtra("longitud", -60.671797916293144);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }

        //agregando los baños
        ExpandingItem banios = expandingList.createNewItem(R.layout.expanding_layout);
        ItemHeader = banios.findViewById(R.id.title);
        ItemHeader.setText(R.string.banios);
        banios.setIndicatorColorRes(R.color.light_blue_2); //color de seleccion
        banios.setIndicatorIconRes(R.mipmap.banio_gigante); //poner un icono al header
        banios.createSubItems(1);
        subItemZero = banios.getSubItemView(0);
        ((TextView) subItemZero.findViewById(R.id.txt_sub_title)).setText(R.string.banio);
        for (int i = 0; i < banios.getSubItemsCount(); i++) {
            View subItem = banios.getSubItemView(i);
            subItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView text = v.findViewById(R.id.txt_sub_title);
                    Intent intent = new Intent(getActivity().getApplicationContext(), MapsActivity.class);
                    intent.putExtra("edificio", "FICH");
                    intent.putExtra("objetivo", text.getText().toString());
                    intent.putExtra("navegacion", true);
                    intent.putExtra("latitud", -31.640398507526978);
                    intent.putExtra("longitud", -60.671797916293144);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }

        //agregando los lugares personalizados
        ExpandingItem favs = expandingList.createNewItem(R.layout.expanding_layout);
        ItemHeader = favs.findViewById(R.id.title);
        ItemHeader.setText(R.string.favoritos);
        favs.setIndicatorColorRes(R.color.light_blue_2); //color de seleccion
        favs.setIndicatorIconRes(R.mipmap.estrella_favorito); //poner un icono al header
        favs.createSubItems(2);
        subItemZero = favs.getSubItemView(0);
        ((TextView) subItemZero.findViewById(R.id.txt_sub_title)).setText(R.string.cantina);
        subItemOne = favs.getSubItemView(1);
        ((TextView) subItemOne.findViewById(R.id.txt_sub_title)).setText(R.string.fotocopiadora);
        for (int i = 0; i < favs.getSubItemsCount(); i++) {
            View subItem = favs.getSubItemView(i);
            subItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView text = v.findViewById(R.id.txt_sub_title);
                    Intent intent = new Intent(getActivity().getApplicationContext(), MapsActivity.class);
                    intent.putExtra("edificio", "FICH");
                    intent.putExtra("objetivo", text.getText().toString());
                    intent.putExtra("navegacion", true);
                    intent.putExtra("latitud", -31.640398507526978);
                    intent.putExtra("longitud", -60.671797916293144);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }


        //configurando FADU
        expandingList = view.findViewById(R.id.expanding_list_fadu);
        banios = expandingList.createNewItem(R.layout.expanding_layout);
        ItemHeader = banios.findViewById(R.id.title);
        ItemHeader.setText(R.string.banios);
        banios.setIndicatorColorRes(R.color.color_fadu); //color de seleccion
        banios.setIndicatorIconRes(R.mipmap.banio_gigante); //poner un icono al header
        banios.createSubItems(1);
        subItemZero = banios.getSubItemView(0);
        ((TextView) subItemZero.findViewById(R.id.txt_sub_title)).setText(R.string.banio);

        favs = expandingList.createNewItem(R.layout.expanding_layout);
        ItemHeader = favs.findViewById(R.id.title);
        ItemHeader.setText(R.string.favoritos);
        favs.setIndicatorColorRes(R.color.color_fadu); //color de seleccion
        favs.setIndicatorIconRes(R.mipmap.estrella_favorito); //poner un icono al header
        favs.createSubItems(1);
        subItemZero = favs.getSubItemView(0);
        ((TextView) subItemZero.findViewById(R.id.txt_sub_title)).setText(R.string.cantina);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}
