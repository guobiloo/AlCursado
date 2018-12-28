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


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BaseDatos extends SQLiteOpenHelper {
    private int index;


    public BaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        cargaDB(sqLiteDatabase);
        index = 0;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Punto");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Conexiones");

        //Se crea la nueva versión de las tablas
        cargaDB(sqLiteDatabase);
    }

    private void cargaDB(SQLiteDatabase sqLiteDatabase) {
        //Creo las tablas
        sqLiteDatabase.execSQL("CREATE TABLE Punto (id INTEGER, latitud TEXT, longitud TEXT, edificio TEXT, piso INTEGER, nombre TEXT, imagen INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE Conexiones (idDesde INTEGER, idHasta INTEGER)");

        //Cargo los puntos
        // -------------------------------- AFUERAS ----------------------------
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (0, '-31.641007348556727', '-60.67320674657822', 'Ciudad Universitaria', 0, 'Salida Autos', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (1, '-31.640892317012657', '-60.671854242682464', 'Ciudad Universitaria', 0, 'Entrada Autos', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (2, '-31.640241800962855', '-60.67319400608539', 'Ciudad Universitaria', 0, 'Fuente de agua', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (3, '-31.640148176514362', '-60.671735890209675', 'Ciudad Universitaria', 0, 'Banco Credicoop', -1 )");

        // --------------------------------------------------------- FICH -----------------------------------------------
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (4, '-31.639935237631125', '-60.67195482552052', 'FICH', 0, 'Entrada FICH', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (5, '-31.639951222336332', '-60.67317053675651', 'FICH', 0,         'Entrada FBCB', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (6, '-31.639937235719415', '-60.67215465009213', 'FICH', 0,         'escalera subida fich', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (7, '-31.639939804690023', '-60.67245539277792', 'FICH', 0,         'Cantina', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (8, '-31.639871298782772', '-60.67244801670313', 'FICH', 0,         'Patio', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (9, '-31.639772250569294', '-60.67244768142701', 'FICH', 0,         'Aula 3-4', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (10, '-31.63977082336085', '-60.67261766642332', 'FICH', 0,         'Aula 1-2', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (11, '-31.639767112618745', '-60.672372579574585', 'FICH', 0,         'Aula magna', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (12, '-31.639934666748733', '-60.672327652573586', 'FICH', 0,         'Fotocopiadora', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (13, '-31.639727721655102', '-60.67176640033722', 'FICH', 0,         'Entrada nave Hidraulica', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (14, '-31.639732288724204', '-60.67158434540033', 'FICH', 0 , 'Simulador Hidraulica', -1 )");

        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (15, '-31.639838758459153', '-60.672584138810635', 'FICH', 1, 'lab 3 y 4', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (16, '-31.639776817636218', '-60.672585479915135', 'FICH', 1, ' ', -1 )"); //
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (17, '-31.639769396152364', '-60.67232262343168', 'FICH', 1, 'lab 1 y 2', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (18, '-31.639762260109634', '-60.672165043652065', 'FICH', 1, 'baño', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (19, '-31.639799082084238', '-60.6721556559205', 'FICH', 1, 'Centro de estudiantes / Mesa de entradas', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (20, '-31.63983476227827', '-60.672159343957894', 'FICH', 1, 'Alumnado', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (21, '-31.63983476227827', '-60.672159343957894', 'FICH', 1, 'Secretaria Academica / Decanato', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (22, '-31.6399358085135', '-60.672257915139205', 'FICH', 1,         'escalera bajada fich', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (23, '-31.639937235719415', '-60.67215465009213', 'FICH', 1,         'escalera subida fich', -1 )");

        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (24, '-31.639926959836256', '-60.67211039364337', 'FICH', 2, 'baño', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (25, '-31.6399358085135', '-60.672257915139205', 'FICH', 2,         'escalera bajada fich', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (26, '-31.639937235719415', '-60.67215465009213', 'FICH', 2,         'escalera subida fich', -1 )");

        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (27, '-31.63994494263104', '-60.672154314816', 'FICH', 3, 'Aula 9', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (28, '-31.63991325865696', '-60.672442317008965', 'FICH', 3, 'Biblioteca', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (29, '-31.6399358085135', '-60.672257915139205', 'FICH', 3,         'escalera bajada fich', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (30, '-31.639937235719415', '-60.67215465009213', 'FICH', 3,         'escalera subida fich', -1 )");

        // --------------------------------------------------------- FADU -----------------------------------------------
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (31, '-31.640125055828115', '-60.67337404936552', 'FADU', 0, 'entrada FADU', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (32, '-31.64013504624881', '-60.673483684659004', 'FADU', 0, 'octogono', -1 )"); //
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (33, '-31.640027435089483', '-60.673580579459674', 'FADU', 0, 'Cantina', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (34, '-31.640118776134564', '-60.67363858222962', 'FADU', 0, 'escalera', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (35, '-31.640139898738465', '-60.67363355308772', 'FADU', 0, 'baño hombre', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (36, '-31.640188994501965', '-60.673635229468346', 'FADU', 0, 'baño mujer', -1 )");

        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (37, '-31.640118776134564', '-60.67363858222962', 'FADU', 2, 'escalera', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (38, '-31.640066825926183', '-60.67361008375883', 'FADU', 2, ' ', -1 )");
        sqLiteDatabase.execSQL("INSERT INTO Punto (id, latitud, longitud, edificio, piso, nombre, imagen) VALUES (39, '-31.640061402551105', '-60.6733750551939', 'FADU', 2,         'biblioteca', -1 )");



        //Agrego las conexiones

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (0 , 2 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (1 , 3 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (2 , 0 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (2 , 3 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (2 , 5 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (2 , 31 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (3 , 1 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (3 , 2 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (3 , 13 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (3 , 4 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (4 , 3 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (4 , 13 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (4 , 6 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (5 , 7 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (5 , 2 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (5 , 31 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (6 , 4 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (6 , 12 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (6 , 22 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (7 , 5 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (7 , 12 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (7 , 8 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (8 , 7 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (8 , 9 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (8 , 12 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (9 , 8 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (9 , 10 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (9 , 11 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (10 , 9 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (11 , 9 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (12 , 6 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (12 , 7 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (12 , 8 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (13 , 4 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (13 , 3 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (13 , 14 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (14 , 13 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (15 , 16 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (16 , 15 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (16 , 17 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (17 , 16 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (17 , 18 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (18 , 17 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (18 , 19 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (19 , 18 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (19 , 20 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (19 , 21 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (20 , 19 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (20 , 21 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (21 , 20 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (21 , 23 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (21 , 19 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (22 , 6 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (22 , 23 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (23 , 21 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (23 , 22 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (23 , 25 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (24 , 26 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (25 , 23 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (25 , 26 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (26 , 25 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (26 , 29 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (27 , 30 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (27 , 28 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (28 , 27 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (28 , 29 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (29 , 26 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (29 , 28 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (29 , 30 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (30 , 27 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (30 , 29 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (31 , 2 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (31 , 5 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (31 , 32 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (32 , 31 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (32 , 34 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (33 , 34 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (34 , 33 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (34 , 32 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (34 , 35 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (34 , 36 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (34 , 37 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (35 , 34 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (36 , 34 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (37 , 34 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (37 , 38 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (38 , 37 )");
        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (38 , 39 )");

        sqLiteDatabase.execSQL("INSERT INTO Conexiones (idDesde, idHasta) VALUES (39 , 38 )");

        index = 40;
    }

    public void insert(Punto p, Punto vecino){
        ContentValues v = new ContentValues();
        v.put("id",p.getId());
        v.put("latitud",p.getLatitud());
        v.put("longitud", p.getLongitud());
        v.put("edificio", p.getEdificio());
        v.put("piso", p.getPiso());
        v.put("nombre", p.getNombre());
        getWritableDatabase().insert("Punto", null, v);

        index = index +1;

        v.clear();
        v.put("idDesde",p.getId());
        v.put("idHasta",vecino.getId());
        getWritableDatabase().insert("Conexiones",null,v);

        v.clear();
        v.put("idDesde",vecino.getId());
        v.put("idHasta",p.getId());
        getWritableDatabase().insert("Conexiones",null,v);
    }

    public int getIndex(){
        return index;
    }

}

