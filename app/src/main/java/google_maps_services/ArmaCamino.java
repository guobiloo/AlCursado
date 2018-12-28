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


import android.location.Location;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

import java.util.Vector;

public class ArmaCamino {

    /*
    Nodos -> Vector de Puntos donde están los nodos y conexiones
    puntoMasCercano -> un objeto de la clase punto que representa el nodo mas cercano a mi posicion
     */
    private Vector<Punto> Nodos;
    private Punto puntoMasCercano = null;

    //    ---------------------------------------------CONSTRUCTOR------------------------------------------------
    public ArmaCamino() {
        Nodos = new Vector<>();
    }


    //    ---------------------------------------------METODOS------------------------------------------------

    /*
    Funcion para agregar nodos al vector
    */
    public void addNodo(Punto P) {
        Nodos.add(P);
    }


    public Vector<Punto> camino_Dijkstra(String Edificio, String nombre_nodo) {
        Vector<Punto> path = new Vector<>(); //este sera el resultado de la funcion
        Vector<Punto> visitado = new Vector<>();
        Vector<Punto> pendientes = new Vector<>(); //vector de nodos que voy almacenando para luego continuar por uno de estos nodos que tenga el costo minimo frente al resto

        // recordar que un punto tiene (costo, padre) y existen funciones para asignar estos valores
        puntoMasCercano.setCosto(0);
        Punto aux = puntoMasCercano; //agrego el punto de partida como un nodo definitivo del camino y para evaluar sus vecinos
        visitado.add(puntoMasCercano);

        Punto vecino;
        while (!(aux.getNombre().equalsIgnoreCase(nombre_nodo) && aux.getEdificio().equalsIgnoreCase(Edificio))) {
            for (int i = 0; i < aux.cant_vecinos(); i++) {
                vecino = aux.getVecino(i);
                if (!visitado.contains(vecino)) {
                    vecino.setCosto(aux.getCosto() + distancia2Puntos(aux.getLatLngFromPunto(), vecino.getLatLngFromPunto()));
                    vecino.setPadre(aux);
                    visitado.add(vecino);
                    pendientes.add(vecino);
                } else {
                    if (pendientes.contains(vecino)) {
                        double nuevo_costo = aux.getCosto() + distancia2Puntos(aux.getLatLngFromPunto(), vecino.getLatLngFromPunto());
                        if (nuevo_costo <= vecino.getCosto()) {
                            vecino.setCosto(nuevo_costo);
                            vecino.setPadre(aux);
                        }
                    }
                }
            }
            double costo_referencia = 9999;
            int index = 0;
            for (int i = 0; i < pendientes.size(); i++) {
                if (pendientes.get(i).getCosto() < costo_referencia) {
                    costo_referencia = pendientes.get(i).getCosto();
                    index = i;
                }
            }
            aux = pendientes.remove(index);
        }

        //ya encontre una ruta minima(pueden existir varias). tomo una de ellas
        while (aux.getEdificio() != puntoMasCercano.getEdificio() && aux.getNombre() != puntoMasCercano.getNombre()) {
            path.add(aux);
            aux = aux.getPadre();
        }
        return path;
    }

    public void borrar_nodos() {
        Nodos.clear();
    }

    /*
    * Setear el punto mas cercano. Empiezo tomando al primer elemento del vector
    * */
    public void setPuntoMasCercano(LatLng posicion, int pisoActual, Circle circle) {
        puntoMasCercano = getPuntoMasCercano(posicion, pisoActual, circle);
    }

    public Punto getPuntoMasCercano(LatLng posicion, int pisoActual, Circle circle) {
        Punto p = null;

        //Si estoy dentro de la CU, busco dentro
        if (dentro_ciudad_universitaria(circle, posicion)) {
            double dist = 999999;
            for (int i = 0; i < Nodos.size(); i++) {
                if (Nodos.elementAt(i).getPiso() == pisoActual) { //solamente considero nodos cercanos pertenecientes a un piso determinado
                    double dist2 = distancia2Puntos(Nodos.elementAt(i).getLatLngFromPunto(), posicion);
                    if (dist2 < dist) {
                        dist = dist2;
                        p = Nodos.elementAt(i);
                    }
                }
            }
        }
        return p;
    }


    public double distancia2Puntos(LatLng pos1, LatLng pos2) {
        float[] result = new float[2];
        Location.distanceBetween(pos1.latitude, pos1.longitude, pos2.latitude, pos2.longitude, result);
        return Math.sqrt(Math.pow(result[0], 2) + Math.pow(result[1], 2));
    }


    /*
     * se limita a calcular si una posicion determinada esta dentro de un poligono(circulo) que delimita la ciudad universitaria
     */
    public boolean dentro_ciudad_universitaria(Circle c, LatLng p) {
        boolean respuesta = false;
        float[] disResultado = new float[2];
        Location.distanceBetween(
                c.getCenter().latitude,
                c.getCenter().longitude,
                p.latitude,
                p.longitude,
                disResultado);
        if (disResultado[0] < c.getRadius()) {
            respuesta = true;
        }
        return respuesta;
    }


    /*
    * devuelve un nodo especifico dado el nombre de algun lugar y el edificio donde se encuentra
    * */
    public Punto getNodo(String nombre, String edificio) {
        boolean bandera = true;
        int i = 0;
        while (bandera && i < Nodos.size()) {
            if (Nodos.elementAt(i).getNombre().equalsIgnoreCase(nombre) && Nodos.elementAt(i).getEdificio().equalsIgnoreCase(edificio)) {
                bandera = false;
            } else {
                i = i + 1;
            }
        }
        return Nodos.elementAt(i);
    }

}
