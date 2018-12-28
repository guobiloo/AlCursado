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

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.Vector;


public class Punto implements Comparable<Punto> {

    private double Latitud;
    private double Longitud;
    private String Edificio;
    private Vector<Punto> Vecinos;
    private int Piso;
    private String Nombre;
    private Integer id;
    private Punto Padre;
    public double costo;



    //    ---------------------------------------------CONSTRUCTOR------------------------------------------------
    public Punto(Integer id, double Lat, double Lon, String oEdificio, int piso, String oNombre){
        this.Latitud = Lat;
        this.Longitud = Lon;
        this.Edificio = oEdificio;
        this.Piso = piso;
        this.Nombre = oNombre;
        this.Vecinos = new Vector<>();
        this.costo = 0;
        this.Padre = null;
        this.id = id;
    }



//    ---------------------------------------------GETTERS------------------------------------------------
    public double getLatitud() {
        return Latitud;
    }


    public double getLongitud() {
        return Longitud;
    }


    public String getEdificio() {
        return Edificio;
    }


    public Punto getVecino(int i) {
        return Vecinos.elementAt(i);
    }


    public int cant_vecinos(){
        return Vecinos.size();
    }


    public int getPiso() {
        return Piso;
    }


    public String getNombre() {
        return Nombre;
    }


    public Integer getId() {
        return id;
    }


    public Punto getPadre() {
        return Padre;
    }


    public LatLng getLatLngFromPunto(){
        return new LatLng(this.Latitud,this.Longitud);
    }

    public double getCosto(){
        return this.costo;
    }


    //    ---------------------------------------------SETTERS------------------------------------------------
    public void setPadre(Punto padre) {
        Padre = padre;
    }

    public void addVecino(Punto P){
        Vecinos.add(P);
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    //    ---------------------------------------------INTERFACE Comparable<Punto>------------------------------------------------
    @Override
    public int compareTo(@NonNull Punto o) {
        int c = 0;
        if(this.equals(o)){
        }
        else{
            if (this.costo > o.costo){
                c = 1;
            }
            else{
                c = -1;
            }
        }
        return c;
    }


}
