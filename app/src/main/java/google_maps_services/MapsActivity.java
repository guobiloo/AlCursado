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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.example.android.alcursado.MainActivity;
import com.example.android.alcursado.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import others_utils.permisos_utils;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        QR_tool.onScannResult,
        dialogo_agregar_marcador.onNewPlaceSaved
        /*GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener*/ {

    private static final int DEFAULT_ZOOM = 19;
    private static final int elevacion_PlantaBaja = 20; //es la altura sobre el nivel del mar de la planta baja de la fich
    public double lat; //localizacion geografica actual del usuario
    public double lon;
    public double elevacion; //altura sobre el nivel del mar del dispositivo
    GoogleMap mMap;
    private BaseDatos CUdb;
    private FloatingActionMenu cmbOpciones;
    private Circle circle;
    private Marker marcadorPosActual;
    private Vector<Marker> marcadoresActivos = new Vector<>();
    private GroundOverlay[] imagen_pisoActivo = new GroundOverlay[2];//se usa para manejar que mapa de la facu se dibuja segun el piso donde me encuentre
    private List<GroundOverlayOptions> pisos_fich = new ArrayList<>();
    private List<GroundOverlayOptions> pisos_fadu = new ArrayList<>();
    private Vector<PolylineOptions> misPolilineas = new Vector<>(); //Vector de polilineas, cada elemento será una polilinea para un piso
    private Vector<MarkerOptions> misMarcadores = new Vector<>(); //vector de marcadores. Se indica todos los puntos de interes en un piso determinado
    private Polyline polilinea_actual;
    private Polyline polilinea_usuarioLejos;
    private ArmaCamino oArmaCamino; //este elemento me permitira calcular las rutas
    private short pisoActual; // ¿en que piso se encuentra el alumno?
    private String edificio_destino; //en que edificio_destino esta lo que busco
    private String nodo_destino; //lugar que el usuario esta buscando (Aula, baño, cantina)
    private boolean modoNavegacion; //bandera que indica si solo queremos ver el mapa, o queremos que nos lleve hacia algun punto en especial
    private boolean usuarioCerca;
    private FusedLocationProviderClient mFusedLocationClient;
//    private GoogleApiClient gac;
//    private LocationRequest locationRequest;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Instancio la base de datos
        CUdb = new BaseDatos(getApplicationContext(), "DBCUI", null, 1);

        //cargo todos los nodos del mapa
        oArmaCamino = new ArmaCamino();
        cargaNodos(); // cargar los elementos desde la base de datos

        //recibir los datos del nodo_destino al cual quiero ir
        Bundle bundle = getIntent().getExtras();
        edificio_destino = bundle.getString("edificio");
        nodo_destino = bundle.getString("objetivo");
        modoNavegacion = bundle.getBoolean("navegacion");
        lat = bundle.getDouble("latitud");
        lon = bundle.getDouble("longitud");


        if (!(permisos_utils.isGPSProviderAvailable(getApplicationContext()) || permisos_utils.isNetowrkProviderAvailable(getApplicationContext()))) {
            showGPSAlert();
        }
//        locationRequest = new LocationRequest();
//        locationRequest.setInterval(2000); //5 segundos
//        locationRequest.setFastestInterval(1000); //2 segundos
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        gac = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();

        Runtime.getRuntime().gc();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (permisos_utils.isLocationPermissionGranted(this) && (mMap != null)) {

            //crea los mapas interiores de la fich y los guarda en el vector 'pisos_fich'
            new mapas_interiores().execute();

            // dibuja un circulo sobre el mapa en la zona de la facultad
            dibujar_circulo();

            //activar listener de evento para cambios de posiciones de gps
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            //me aseguro de tomar la posicion del usuario antes de trabajar
            if (permisos_utils.isGPSProviderAvailable(getApplicationContext()) || permisos_utils.isNetowrkProviderAvailable(getApplicationContext())) {
                new get_gps_loc().execute();
            } else {
                mover_camara(-31.641007348556727, -60.67320674657822);
            }


            //configurar las rutas del mapa hasta el nodo_destino. En el caso de que el usuario este lejos de la ciudad universitaria,
            // lo conduzco hasta alli primero
            if (modoNavegacion && permisos_utils.isGPSProviderAvailable(getApplicationContext())) {
                //tomar posicion del usuario cada 5 segundos
                navegar();
            }
        }

        //inicializar los botones (nuevo marcados, QR, seleccion pisos)
        set_buttons();

        //activar opcion para hacer clicks en el mapa y poner un marcador
        obtenerPosicion_click();

        //aplico configs iniciales
        set_API_maps_configs();

        //cuando se detiene la camara en alguna posicion, determinar si se esta dentro de los limites del circulo.
        // para activar la opcion de cambiar la vista de los pisos
        mostrar_nodos(pisoActual);

        imagen_pisoActivo[0] = mMap.addGroundOverlay(pisos_fich.get(pisoActual));
        imagen_pisoActivo[1] = mMap.addGroundOverlay(pisos_fadu.get(pisoActual));
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng pos_camera = new LatLng(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);
                if (oArmaCamino.dentro_ciudad_universitaria(circle, pos_camera) && mMap.getCameraPosition().zoom >= DEFAULT_ZOOM) {
                    cmbOpciones.setVisibility(View.VISIBLE);
                } else {
                    cmbOpciones.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
//        gac.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
//        gac.disconnect();
        super.onStop();
    }

    private void set_buttons() {
        //inicializo el boton flotante para la vista de pisos de la facultad, junto a sus eventos de click
        cmbOpciones = findViewById(R.id.fab_menu);
        FloatingActionButton fab_piso0 = findViewById(R.id.menu_item_piso_0);
        fab_piso0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrar_piso_facultad(0);
            }
        });
        FloatingActionButton fab_piso1 = findViewById(R.id.menu_item_piso_1);
        fab_piso1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrar_piso_facultad(1);
            }
        });
        FloatingActionButton fab_piso2 = findViewById(R.id.menu_item_piso_2);
        fab_piso2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrar_piso_facultad(2);
            }
        });
        FloatingActionButton fab_piso3 = findViewById(R.id.menu_item_piso_3);
        fab_piso3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrar_piso_facultad(3);
            }
        });
        FloatingActionButton fab_piso4 = findViewById(R.id.menu_item_piso_4);
        fab_piso4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrar_piso_facultad(4);
            }
        });


        //creo e inicializo el boton flotante para escanear un codigo QR. Al presionarlo, mostrar ventana para escanear un QR
        FloatingActionButton fab_QR = findViewById(R.id.fab);
        fab_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new QR_tool().show(getFragmentManager(), "QR Scanner");
            }
        });
        if (modoNavegacion) {
            if (permisos_utils.isNetworkAvailable(getApplicationContext())) {
                if (permisos_utils.isGPSProviderAvailable(getApplicationContext()) || permisos_utils.isNetowrkProviderAvailable(getApplicationContext())) {
                    fab_QR.setVisibility(View.INVISIBLE);
                } else {
                    fab_QR.setVisibility(View.VISIBLE);
                }
            }
        } else {
            fab_QR.setVisibility(View.INVISIBLE);
        }


        //inicializando el boton para agregar un nuevo lugar como favorito
        FloatingActionButton fab_addNewPlace = findViewById(R.id.fab2);
        fab_addNewPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mover_camara(marcadorPosActual.getPosition().latitude, marcadorPosActual.getPosition().longitude);
                new dialogo_agregar_marcador().show(getFragmentManager(), "nuevo marcador favorito");
            }
        });
    }

    /*
    * setea configuraciones iniciales del mapa
    * */
    @SuppressLint("MissingPermission")
    public void set_API_maps_configs() {
        //modificar tipo vista de mapa.
        //GoogleMap.MAP_TYPE_NORMAL para mapa de carreteras
        //GoogleMap.MAP_TYPE_SATELLITE. Imágenes de satélite.
        //GoogleMap.MAP_TYPE_TERRAIN. Mapa topográfico.
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //poner botones de zoom sobre la pantalla
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //para cambiar sólo el nivel de zoom
//        CameraUpdateFactory.zoomIn(); //Aumenta en 1 el nivel de zoom.
//        CameraUpdateFactory.zoomOut(); //Disminuye en 1 el nivel de zoom.
        CameraUpdateFactory.zoomTo(DEFAULT_ZOOM); //Establece el nivel de zoom.

        //al hacer click sobre algun marcador sobre el mapa,  en la esquina inferior derecha aparecer
        // dos botones para mostrar dicha ubicación en la aplicación de Google Maps y para calcular la
        // ruta al lugar marcado. Si no queremos que aparezcan estos botones, debemos llamar
        mMap.getUiSettings().setMapToolbarEnabled(false);

        //mostrar la brujula en el mapa
        mMap.getUiSettings().setCompassEnabled(true);

        //crear boton de ubicacion gps sobre mapa y enfocar donde esta el alumno cuando lo presiona.
        // Tambien redibuja las polilineas si presiona el boton
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true); //activar el boton para saber la ubicacion actual
        mMap.setOnMyLocationButtonClickListener(this);

        mMap.setMinZoomPreference(15.0f);
    }


    /*
     * si el modo de navegacion esta activo, toma la posicion del usuario cada 5 segundos
     */
    public void navegar() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            new get_gps_loc().execute();
                            usuarioCerca = oArmaCamino.dentro_ciudad_universitaria(circle, getLatLng());
                            if (usuarioCerca) {

                                if(polilinea_usuarioLejos!=null){
                                    polilinea_usuarioLejos.remove();
                                    polilinea_usuarioLejos.setVisible(false);
                                }

                                if ((elevacion > 2) && (elevacion <= elevacion_PlantaBaja + 3)) {
                                    pisoActual = 0;
                                    if (misPolilineas.isEmpty()) {
                                        setear_Busqueda(edificio_destino, nodo_destino);
                                    }
                                    cambiar_piso_facultad(pisoActual);
                                } else if ((elevacion > elevacion_PlantaBaja + 3 && elevacion <= elevacion_PlantaBaja + 6) && (pisoActual != 1)) {
                                    pisoActual = 1;
                                    if (misPolilineas.isEmpty()) {
                                        setear_Busqueda(edificio_destino, nodo_destino);
                                    }
                                    cambiar_piso_facultad(pisoActual);
                                } else if ((elevacion > elevacion_PlantaBaja + 6 && elevacion <= elevacion_PlantaBaja + 9) && (pisoActual != 2)) {
                                    pisoActual = 2;
                                    if (misPolilineas.isEmpty()) {
                                        setear_Busqueda(edificio_destino, nodo_destino);
                                    }
                                    cambiar_piso_facultad(pisoActual);
                                } else if ((elevacion > elevacion_PlantaBaja + 9 && elevacion <= elevacion_PlantaBaja + 12) && (pisoActual != 3)) {
                                    pisoActual = 3;
                                    if (misPolilineas.isEmpty()) {
                                        setear_Busqueda(edificio_destino, nodo_destino);
                                    }
                                    cambiar_piso_facultad(pisoActual);
                                } else if ((elevacion > elevacion_PlantaBaja + 12) && (pisoActual != 4)) {
                                    pisoActual = 4;
                                    if (misPolilineas.isEmpty()) {
                                        setear_Busqueda(edificio_destino, nodo_destino);
                                    }
                                    cambiar_piso_facultad(pisoActual);
                                }
                            } else {
                                //cargo el servicio de google Directions
                                if(polilinea_usuarioLejos == null || (!polilinea_usuarioLejos.isVisible())){
                                    direcciones ruta_Hacia_la_facu = new direcciones();
                                    polilinea_usuarioLejos = mMap.addPolyline(
                                            ruta_Hacia_la_facu.obtener_instrucciones(getLatLng(), new LatLng(-31.641007348556727, -60.67320674657822), getApplicationContext())
                                    );
                                    polilinea_usuarioLejos.setVisible(true);
                                }
                                if(!misPolilineas.isEmpty()){
                                    misPolilineas.clear();
                                    polilinea_actual.remove();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Runtime.getRuntime().gc();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 0, 3000);  //ejecutar en intervalo de 4 segundos.
    }


    private LatLng getLatLng() {
        return new LatLng(this.lat, this.lon);
    }

    /*
     * funcion que simplemente mueve la camara para enfocar una posicion en el mapa y pone un marcador
     */
    private void mover_camara(double latitud, double longitud) {
        LatLng coordinate = new LatLng(latitud, longitud);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, DEFAULT_ZOOM));

        mover_marcador(coordinate);
    }

    /*
     * mueve el marcador desde donde estaba hasta la nueva posicion @pos
     */
    private void mover_marcador(LatLng pos) {
        if (marcadorPosActual != null) {
            marcadorPosActual.remove();
        }
        marcadorPosActual = mMap.addMarker(new MarkerOptions()
                .position(pos));
    }


    /*
     * actualiza la posicion geografica del usuario (latitud,longitud,altitud)
     */
    @SuppressLint("MissingPermission")
    public void get_gps_location() {
//        mFusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback(), null);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            actualizar_latlong(location.getLatitude(), location.getLongitude(), location.getAltitude());
                        }
                    }
                });
    }


    public void actualizar_latlong(double latitud, double longitud, double elev) {
        this.lat = latitud;
        this.lon = longitud;
        this.elevacion = elev;
    }

    /*
     * redibuja el plano de la facu segun en piso que el usuario quiera ver. No cambia la variable pisoActual donde se encuetra el
     * usuario realmente
     */
    public void mostrar_piso_facultad(int piso) {
        imagen_pisoActivo[0].remove();
        imagen_pisoActivo[1].remove();
        imagen_pisoActivo[0] = mMap.addGroundOverlay(pisos_fich.get(piso));
        imagen_pisoActivo[1] = mMap.addGroundOverlay(pisos_fadu.get(piso));
        mostrar_nodos(piso);
        cambiarPolilinea(piso);
    }


    /*
     * cambia el mapa un piso de la facultad por otro. Tambien cambia la variable pisoActual que indica donde esta el usuario
     * en un momento determinado
     */
    public void cambiar_piso_facultad(int piso) {
        pisoActual = (short) piso;
        mostrar_piso_facultad(pisoActual);
    }


    /*
     * llama a las funciones que:
     * -configura(no muestra) una polilinea desde el punto mas cercano hasta el objetivo. Esta config la hace la func dibujaCamino()
     * -setPuntoMasCercano setea en oArmaCamino el nodo mas cercano a la posición donde estoy parado
     */
    public void setear_Busqueda(String Edificio, String Nombre) {
        oArmaCamino.setPuntoMasCercano(new LatLng(this.lat, this.lon), pisoActual, circle);
        dibujaCamino(oArmaCamino.camino_Dijkstra(Edificio, Nombre));
    }


    /*
     * al hacer click sobre el mapa, pone un marcdor. Esta funcion sirve para guardar algun lugar personalizado del usuario
     */
    private void obtenerPosicion_click() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                Projection proj = mMap.getProjection();
                Point coord = proj.toScreenLocation(point);

                mover_marcador(point);
            }
        });
    }


    /*
     * esta funcion SOLO guarda los mapas interiors configurados en el arreglo "pisos_fich", pero no dibuja sobre el mapa
     */
    private void cargar_mapas_interiores() {
        pisos_fich.add(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.fich_pb))
                .positionFromBounds(new LatLngBounds(new LatLng(-31.6401455, -60.6732800), new LatLng(-31.6395500, -60.6709800))));

        pisos_fich.add(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.fich_1er_piso))
                .positionFromBounds(new LatLngBounds(new LatLng(-31.64008, -60.67314), new LatLng(-31.63963, -60.67187))));

        pisos_fich.add(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.fich_2do_piso))
                .positionFromBounds(new LatLngBounds(new LatLng(-31.640060, -60.673134), new LatLng(-31.639657, -60.671870))));

        pisos_fich.add(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.fich_3er_piso))
                .positionFromBounds(new LatLngBounds(new LatLng(-31.640055, -60.673134), new LatLng(-31.639650, -60.671972))));

        pisos_fich.add(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.fich_4to_piso))
                .positionFromBounds(new LatLngBounds(new LatLng(-31.640052, -60.673130), new LatLng(-31.639672, -60.671975))));

        pisos_fadu.add(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.fadu_pb))
                .positionFromBounds(new LatLngBounds(new LatLng(-31.64038, -60.67396), new LatLng(-31.63991, -60.67335))));

        pisos_fadu.add(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.fadu_1piso))
                .positionFromBounds(new LatLngBounds(new LatLng(-31.64033, -60.67396), new LatLng(-31.63993, -60.67309))));

        pisos_fadu.add(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.fadu_2piso))
                .positionFromBounds(new LatLngBounds(new LatLng(-31.64032, -60.67395), new LatLng(-31.63993, -60.67312))));

        pisos_fadu.add(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.fadu_3piso))
                .positionFromBounds(new LatLngBounds(new LatLng(-31.64033, -60.67395), new LatLng(-31.63993, -60.67313))));

        pisos_fadu.add(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.fadu_4piso))
                .positionFromBounds(new LatLngBounds(new LatLng(-31.64034, -60.67397), new LatLng(-31.63992, -60.67309))));
    }


    /*
     * Funcion para crear nodos del mapa y sus conexiones. Se carga toda la info en una instancia de ArmaCamino
     */
    private void cargaNodos() {
        if (!misMarcadores.isEmpty()) {
            misMarcadores.clear();
        }
        Vector<Punto> puntos = new Vector<>();
        SQLiteDatabase db1 = CUdb.getReadableDatabase();
        Cursor c = db1.rawQuery("SELECT *  FROM Punto", null);
        c.moveToFirst();

        //Creo y agrego los nodos
        if (c.getCount() > 0) {
            do {
                Punto oPunto = new Punto(c.getInt(0), Double.parseDouble(c.getString(1)), Double.parseDouble(c.getString(2)), c.getString(3), c.getInt(4), c.getString(5));
                misMarcadores.add(new MarkerOptions()
                        .title(Integer.toString(oPunto.getPiso()) + ":" + oPunto.getNombre())
                        .position(oPunto.getLatLngFromPunto())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin2_24px)));
                puntos.add(oPunto);
            } while (c.moveToNext());
        }
        c.close(); //cierro el cursor

        //Genero las conexiones
        oArmaCamino.borrar_nodos();
        for (int i = 0; i < puntos.size(); i++) {
            Cursor d = db1.rawQuery("SELECT idHasta FROM Conexiones WHERE idDesde = " + puntos.elementAt(i).getId(), null);
            d.moveToFirst();
            if (d.getCount() > 0) {
                do {
                    boolean bandera = true;
                    int j = 0;
                    while (bandera && j < puntos.size()) {
                        if (puntos.elementAt(j).getId() == d.getInt(0)) {
                            bandera = false;
                        } else {
                            j = j + 1;
                        }
                    }
                    puntos.elementAt(i).addVecino(puntos.get(j));
                } while (d.moveToNext());
            }
            d.close();
            oArmaCamino.addNodo(puntos.elementAt(i));
        }

        //Cierro DB
        puntos.clear();
        db1.close();
    }


    /*
     * Recibo un vector de puntos y creo(no muestro) un polilinea con ellos. el resultado se guarda en el vector misPolilineas<>
     */
    private void dibujaCamino(Vector<Punto> path) {
        misPolilineas.clear();
        int cantPisos = 0;

        //Veo cuantos pisos hay
        for (int i = 0; i < path.size(); i++) {
            if (path.elementAt(i).getPiso() > cantPisos) {
                cantPisos = path.elementAt(i).getPiso();
            }
        }

        //Creo las polilineas en base a la cantidad de pisos de una facultad
        for (int i = 0; i < cantPisos + 1; i++) {
            PolylineOptions p = new PolylineOptions().color(Color.MAGENTA);
            misPolilineas.add(p);
        }

        //Agrego puntos a las polilineas segun piso
        for (int i = 0; i < path.size(); i++) {
            misPolilineas.elementAt(path.elementAt(i).getPiso()).add(path.elementAt(i).getLatLngFromPunto());
        }
        misPolilineas.elementAt(pisoActual).add(new LatLng(this.lat, this.lon));
    }


    /*
     * borra las polilineas que habia en el mapa y vuelve a dibujarlas, segun el piso donde el alumno se encuentre.
     */
    public void cambiarPolilinea(int piso) {
        if (polilinea_actual != null) {
            polilinea_actual.remove();
        }
        if (!misPolilineas.isEmpty() && (piso + 1 <= misPolilineas.size())) {
            polilinea_actual = mMap.addPolyline(misPolilineas.elementAt(piso)
                    .color(Color.MAGENTA));
        }
    }


    private void dibujar_circulo() {
        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(-31.639919, -60.672622))
                .radius(150); // In meters
        circle = mMap.addCircle(circleOptions);

        //hace el circulo invisible
        circle.setVisible(false);

    }


    /*
     * Funcion para actualizar los nodos según el piso que se quiera ver
     */
    public void mostrar_nodos(int piso) {
        if (!marcadoresActivos.isEmpty()) {
            for (int i = 0; i < marcadoresActivos.size(); i++) {
                marcadoresActivos.get(i).remove();
            }
            marcadoresActivos.clear();
        }
        for (int i = 0; i < misMarcadores.size(); i++) {
            if (misMarcadores.elementAt(i).getTitle().substring(0, 1).contains(Integer.toString(piso))) {
                Marker m = mMap.addMarker(misMarcadores.get(i));
                m.setTitle(misMarcadores.elementAt(i).getTitle().substring(2)); //remueve del titulo del marcador, el prefijo [piso:]
                marcadoresActivos.add(m);
            }
        }

    }

    private void showGPSAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use the map location tracking correctly, otherwise must use the QR scanner tool to get tracked")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }


    //    --------------------------------------METODOS IMPLEMENTADOS DE INTERFACES--------------------------------------

    /*
     * Si existe permiso de ubicacion, llama a la funcion para ubicar en el mapa.
     * if location permission isn't set, this function handles it by calling getLocationPermission()
     */
    @Override
    public boolean onMyLocationButtonClick() {
        if (permisos_utils.isLocationPermissionGranted(this)) {
            new get_gps_loc().execute();
            mover_camara(this.lat, this.lon);
            cambiar_piso_facultad(pisoActual);
        } else {
            permisos_utils.getLocationPermission(this);
        }
        return false; // return false; mueve la camara donde el gps da la ubicacion. Si no se quiere esto, se pone true
    }

    @Override
    public void sendResults(String nombre_nodo, String facu) {

        Punto p = oArmaCamino.getNodo(nombre_nodo, facu);
        lat = p.getLatitud();
        lon = p.getLongitud();
        pisoActual = (short) p.getPiso();
        this.edificio_destino = p.getEdificio();

        //vuelvo a calcular el camino hacia el nodo_destino y redibujo la polilinea
        setear_Busqueda(this.edificio_destino, this.nodo_destino);
        cambiar_piso_facultad(pisoActual);

        //muevo la camara para enfocar donde se encuentra el usuario
        mover_camara(this.lat, this.lon);
    }


    @Override
    public void SaveNewPlace(String nombre, String facultad, int piso) {
        //agregar nodo al mapa
        Punto p = new Punto(CUdb.getIndex(), marcadorPosActual.getPosition().latitude, marcadorPosActual.getPosition().longitude, facultad, piso, nombre);
        Punto masCercano = oArmaCamino.getPuntoMasCercano(p.getLatLngFromPunto(), piso, circle);

        if (masCercano != null) {
            CUdb.insert(p, masCercano);

            //actualizo los nodos para mostrar el que recien agregue
            cargaNodos();
            mostrar_nodos(pisoActual);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }




//    --------------------------------------HILOS DE EJECUCION (ASYNC TASKS)--------------------------------------
/*
    * hilo de ejecucion secundario que obtiene la ubicacion del dispositivo por gps, consultando los servicios de geolocalizacion
    * */
private class get_gps_loc extends AsyncTask<Void,Void,Void>{
    @Override
    protected Void doInBackground(Void... voids) {
        get_gps_location();
        return null;
    }
}

    /*
   * hilo de ejecucion secundario que se encarga de cargar los mapas internos de las facultades para insertalas luego en el mapa
   * */
    private class mapas_interiores extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            cargar_mapas_interiores();
            return null;
        }
    }


}
