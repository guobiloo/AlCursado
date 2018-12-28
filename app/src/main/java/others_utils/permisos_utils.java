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

package others_utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


// esta clase brinda informacion acerca de si algunos componentes del telefono estan activados o no, como BT, GPS, etc.
public class permisos_utils {

    /*
    *determinar si esta activado el gps
     */
    public static boolean isGPSProviderAvailable(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /*
    *determinar si esta activada la geolocalizacion por la red (personal, claro, etc)
     */
    public static boolean isNetowrkProviderAvailable(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    /*
    *determinar si esta activada la conexion a internet
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting());
    }


    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
    public static void getLocationPermission(Activity a) {

        if (ContextCompat.checkSelfPermission(a.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(a, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            ActivityCompat.requestPermissions(a,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }

    }

    /*
    * indica si esta activados los permisos de geolocalizacion
    * */
    public static boolean isLocationPermissionGranted(Context a) {
        return ContextCompat.checkSelfPermission(a,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    /*
    * pedir permisos al dispositivo para usar la camara
    * */
    public static void getCameraPermission(Activity a) {
        if (ContextCompat.checkSelfPermission(a.getApplicationContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(a,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }
    }

    public static boolean isCamenraPermissionGranted(Context a){
        return ContextCompat.checkSelfPermission(a,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    /*
* pedir permisos al dispositivo para almacenar datos en la memoria de telefono y recuperarlos
* */
    public static void getStoragePermission(Activity a) {
        if (ContextCompat.checkSelfPermission(a.getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(a,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

        if (ContextCompat.checkSelfPermission(a.getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(a,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
    }


    //verify play services is active and up to date
public static void isPlayServicesUpdated(final Activity a){
    int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(a.getApplicationContext());
    switch (resultCode) {
        case ConnectionResult.SUCCESS:
            break;
        default:
            GoogleApiAvailability.getInstance().showErrorDialogFragment(a, resultCode, 10,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            a.finish();
                        }
                    });
    }
}

public static void request_all_permissions(Activity a){
    String[] PERMISSIONS = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE};

    ActivityCompat.requestPermissions(a, PERMISSIONS, 1);
}
}
