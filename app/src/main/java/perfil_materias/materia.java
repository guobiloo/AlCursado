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



public class materia {
    private String nombre;
    private String edificio;
private String aula;



    public materia(String nombre, String edificio, String aula){
        this.nombre = nombre;
        this.edificio = edificio;
        this.aula = aula;
    }

    public materia(materia m){
        this.nombre = m.getNombre();
        this.edificio = m.getEdificio();
    }

    public String getNombre() {
        return nombre;
    }

    public String getEdificio() {
        return edificio;
    }

    public String getAula(){return aula;}
}
