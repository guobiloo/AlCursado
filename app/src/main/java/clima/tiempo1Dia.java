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

package clima;



public class tiempo1Dia {
    private String dia;
    private String celcius_max;
    private String celcius_min;
    private String condicion;
    private String url_logo_clima;


    public tiempo1Dia(String dia, String celcius_max, String celcius_min, String condicion, String url_logo_clima) {
        this.dia = dia;
        this.celcius_max = celcius_max;
        this.celcius_min = celcius_min;
        this.condicion = condicion;
        this.url_logo_clima = url_logo_clima;
    }



    //------------------ METODOS PERSONALIZADOS -------------------------------



    // --------------- GETTERS -------------------------
    public String getDia() {
        return dia;
    }

    public String getCelcius_max() {
        return celcius_max;
    }

    public String getCondicion() {
        return condicion;
    }

    public String getLogo_clima() {
        return url_logo_clima;
    }

    public String getCelcius_min() {
        return celcius_min;
    }





    // --------------- SETTERS -------------------------
    public void setDia(String dia) {
        this.dia = dia;
    }


    public void setCelcius_max(String celcius_max) {
        this.celcius_max = celcius_max;
    }


    public void setCelcius_min(String celcius_min) {
        this.celcius_min = celcius_min;
    }


    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }


    public void setLogo_clima(String logo_clima) {
        this.url_logo_clima = url_logo_clima;
    }
}
