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

import java.util.Calendar;

/*
* clase que simplemente representa elementos de un evento en el adaptador de eventos de UNL
* */
public class evento {

    private String texto;
    private int imagen;
    private Calendar Fecha;


    public evento(String texto, int imagen, Calendar fecha) {
        this.texto = texto;
        this.imagen = imagen;
        this.Fecha = Calendar.getInstance();
        this.Fecha.set(fecha.get(Calendar.YEAR), fecha.get(Calendar.MONTH), fecha.get(Calendar.DAY_OF_MONTH));
    }

    public evento(evento e){
        this.texto = e.getTexto();
        this.Fecha = Calendar.getInstance();
        Fecha.setTime(e.getFecha().getTime());
        this.imagen = e.getImagen();
    }


    // ------------GETTERS-------------------
    public Calendar getFecha() {
        return Fecha;
    }

    public String getTexto() {
        return texto;
    }

    public int getImagen() {
        return imagen;
    }


}

