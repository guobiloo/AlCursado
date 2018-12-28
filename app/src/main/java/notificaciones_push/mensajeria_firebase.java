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

package notificaciones_push;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


/*
* las notificaciones que hemos enviado hasta ahora tan sólo aparecen en el dispositivo cuando nuestra aplicación se encuentra en segundo plano.
* Por lo tanto ahora se quiere resolver eso.
* cuando se recibía un mensaje sobre una aplicación que se encontraba en segundo plano el sistema generaba y mostraba automáticamente
* la notificación en el dispositivo sin necesidad de hacer nada por nuestra parte, incluso asignaba una acción por defecto a la notificación de
* forma que al pulsarla se iniciaba nuestra actividad principal. Sin embargo, cuando el mensaje va dirigido a la aplicación
* que está en primer plano deberá ser la propia aplicación la que se encargue de realizar la gestión completa de dicho mensaje.
*
* En esta clase tan sólo tendremos que sobrescribir un método llamado onMessageReceived(), que se llamará automáticamente
* cada vez que la aplicación reciba un mensaje de Firebase Cloud Messaging. El método onMessageReceived() recibe como parámetro
* un objeto RemoteMessage que encapsula la información del mensaje recibido. Así, para acceder por ejemplo al título o al texto de
* la notificación podremos llamar a los métodos correspondientes, getTitle() y getBody() en este caso, sobre dicho objeto.
* Adicionalmente, si quisiéramos mostrar la notificación recibida en la barra de estado y la bandeja del sistema tendríamos que hacerlo
* por nosotros mismos, haciendo uso de NotificationCompat.
* */
public class mensajeria_firebase extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {

            String titulo = remoteMessage.getNotification().getTitle();
            String texto = remoteMessage.getNotification().getBody();

            Log.d("fcm firebase mensaje", "NOTIFICACION RECIBIDA");
            Log.d("fcm firebase mensaje", "Título: " + titulo);
            Log.d("fcm firebase mensaje", "Texto: " + texto);

            //mostramos la notificación en la barra de estado
            showNotification(titulo, texto);
        }
    }


/*
* esta funcion muestra la notificacion recibida en la barra de estado.
* Para almacenar los datos recibidos desde firebase en la activity principal, se debe implementar un servicio de
* BroadcastReceiver mediante Intents
* */
    private void showNotification(String titulo, String texto) {
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); //simplemente para hacer un ringtone de aviso

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this) //creando una notificacion en barra de estado
                        .setSmallIcon(android.R.drawable.stat_sys_warning)
                        .setContentTitle(titulo)
                        .setSound(defaultSoundUri)
                        .setContentText(texto);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


        // enviar datos a la activity mediante broadcastReceiver
        Intent intent = new Intent();
        intent.setAction("push_notif_firebase"); //"push_notif_firebase" seria como la frecuencia de sintonizacion para recibir este intent
        if(titulo==null){
            intent.putExtra("title","");
        }else{
            intent.putExtra("title",titulo);
        }
        intent.putExtra("description",texto);
        sendBroadcast(intent);
    }

}
