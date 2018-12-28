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


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.alcursado.R;


/*
* Esto es un Dialogo, que no es mas que un fragmento que levanta un layout y se ejecuta sobre una actividad principal. Simplemente
* se extiende de la clase DialogFragment y se sobreescribe el metodo onCreateDialog() para que retorne en la instancia de alguna subclase de Dialog.
*  Aunque Dialog es la clase que genera la interfaz, DialogFragment es quien permite controlar los eventos de forma fluida.
* */
public class LogIn_Dialog extends DialogFragment{
    /*
    * sobrescribe el método onCreateDialog() para que retorne en la instancia de alguna subclase de Dialog.
    * */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //generamos un constructor de dialogos (builder)

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_login, null); //asociamos el layout a este fragmento

        builder.setView(view); //utilizaremos el método setView() del builder para asociarle nuestro layout personalizado

        final completar_formulario interf = (completar_formulario) getActivity(); //asociar la interfaz a este fragmento

        // boton para continuar la operacion
        builder.setTitle("LogIn");
        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText usuario = view.findViewById(R.id.edittxt_mail);
                EditText pass = view.findViewById(R.id.edittxt_pass);

                //validar que los campos no esten vacios
                if(usuario.getText().toString().isEmpty()  ||  pass.getText().toString().isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(), "debe rellenar ambos campos", Toast.LENGTH_LONG).show();
                }else{
                    interf.validar_login(
                            usuario.getText().toString(),
                            pass.getText().toString());
                }
                dialog.cancel();
            }
        });

        return builder.create();
    }




    /*
    * Interface con la que este fragment (Dialog) envia informacion a la actividad que lo contiene
    * */
    public interface completar_formulario{

        void validar_login(String user, String pass); //verificar en la actividad contenedora, si existen el usuario y contraseña introducido
    }


}
