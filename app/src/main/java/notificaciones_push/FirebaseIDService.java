

package notificaciones_push;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/*
* esta identificación de usuarios Firebase la resuelve mediante el uso de tokens de registro, o InstanceID Token.
*  un token de registro no es más que un dato que identifica de forma única a una aplicación determinada instalada en un dispositivo determinado.
*
*  El token de registro se asigna a nuestra aplicación en el momento de su primera conexión con los servicios de mensajería, y en condiciones normales
*  se mantiene invariable en el tiempo. Sin embargo, en determinadas circunstancias este dato puede cambiar durante la vida de la aplicación
*
*  Para conocer el token de registro que tenemos asignado podremos acceder en cualquier momento a la instancia de nuestra
*  aplicación mediante getInstance() y obtener el valor del token llamando a getToken().
*
*  para ser notificados cuando se produzca alguna actualización del token, tendremos que definir un servicio que extienda de la clase base FirebaseInstanceIdService.
*  En principio tan sólo tendremos que sobrescribir su método onTokenRefresh(), que se llamará cada vez que el token se actualice, incluida su primera asignación.
* */
public class FirebaseIDService extends FirebaseInstanceIdService {


    /*
    * La devolución de llamada onTokenRefresh se activa cada vez que se genera un nuevo token, por lo que invocar getToken en este contexto
    * garantiza que se accede a un token de registro vigente y disponible
    * */
    @Override
    public void onTokenRefresh() {
        //Se obtiene el token actualizado
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString("FCMToken", refreshedToken).apply();

        Log.d("fcm firebase token", "Token actualizado: " + refreshedToken);

        //par enviar mensajes a esta app y manejar suscripciones a determinados avisos, hay que registrar el token de
        //esta app en el servidor
        sendRegistrationToServer(refreshedToken);
    }

    // send network request
    private void sendRegistrationToServer(String token) {

        // if registration sent was successful, store a boolean that indicates whether the generated token has been sent to server
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putBoolean("sentTokenToServer", true).apply();
    }
}
