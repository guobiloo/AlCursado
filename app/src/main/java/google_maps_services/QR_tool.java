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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.example.android.alcursado.R;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class QR_tool extends DialogFragment implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    private onScannResult interf;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //instancio un contructor de dialogos y se lo asigno a la actividad que convoca a este dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //debo asignar el layout personalizado del dialogo a esta clase
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_qr_tool, null);
        builder.setView(view);

        interf = (onScannResult) getActivity();
        mScannerView = view.findViewById(R.id.scanner);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.

        //configurando la tecla de volver hacia atras, para destruir el dialogo
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.cancel();

                    //esto es importante que lo ponga para liberar memoria en el heap
                    Runtime.getRuntime().gc();
                    return true;
                } else {
                    return false;
                }
            }
        });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        interf = (onScannResult) getActivity();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }


    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
        interf =null;
    }


    @Override
    public void handleResult(Result result) {
        String[] datos = result.getContents().split(":"); //separo el string codificado en QR en un arreglo.
        interf.sendResults(datos[0], datos[1]);
        mScannerView.stopCamera();
        getDialog().cancel();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        interf = null;
        Runtime.getRuntime().gc();
    }


    public interface onScannResult {
        void sendResults(String nombre_nodo, String edificio);
    }

}
