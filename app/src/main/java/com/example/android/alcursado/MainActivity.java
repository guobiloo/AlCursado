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

package com.example.android.alcursado;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.florent37.bubbletab.BubbleTab;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Locale;

import notificaciones_push.LogIn_Dialog;
import notificaciones_push.notificacion;
import others_utils.permisos_utils;


public class MainActivity extends Activity implements
        LogIn_Dialog.completar_formulario {

    public BroadcastReceiver UIReceiver;
    private FirebaseAuth mAuth;
    private ArrayList<notificacion> lista_notifs = new ArrayList<>();

    //    ------------------------------------------METODOS-----------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check que los servicios de google play estan todos actualizados
        permisos_utils.isPlayServicesUpdated(this);

        // Create the adapter that will return a fragment for each of the three primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //se configura la navegacion por pestañas
        BubbleTab bubbleTab = findViewById(R.id.bubbleTab);
        bubbleTab.setupWithViewPager(mViewPager);

        //obtener instancia del gestor de autenticacion de Firebase
        mAuth = FirebaseAuth.getInstance();

        //pedir permisos para utilizar servicios y recursos
        permisos_utils.request_all_permissions(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth != null) {
            // verifica si el usuario ya ha accedido.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                //mostrar el dialogo
                new LogIn_check().execute();
            }
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction("push_notif_firebase"); //sintonizar datos recibidos solo de esta categoria: "push_notif_firebase"
        UIReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String titulo = intent.getStringExtra("title");
                String descripcion = intent.getStringExtra("description");
                lista_notifs.add(new notificacion(titulo, descripcion));

                String tag = "android:switcher:" + R.id.container + ":" + 1;
                frag_informacion f = (frag_informacion) getFragmentManager().findFragmentByTag(tag);
                f.recibeData_fromActivity(titulo, descripcion);
            }
        };
        registerReceiver(UIReceiver, filter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_menu_config:
                show_Language_Dialog();
                break;
            case R.id.btn_menu_about:
                show_About_Dialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /*
       * crear un dialogo con fines de configuracion para cambiar el idioma de la app
       */
    private void show_Language_Dialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this); //llama al constructor Builder para alzar un nuevo Dialog
        b.setTitle(R.string.idiomas); //poner un titulo al dialogo
        final String[] items = {"Español", "English", "Deutsch", "Français"};

        //setear contenido y su evento asociado a los clicks
        b.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Locale localizacion;
                Configuration config = new Configuration();
                dialog.dismiss();
                switch (which) {
                    case 0:
                        localizacion = new Locale("es", "");
                        config.locale = localizacion;
                        break;
                    case 1:
                        localizacion = new Locale(Locale.ENGLISH.getLanguage());
                        config.locale = localizacion;
                        break;
                    case 2:
                        localizacion = new Locale(Locale.GERMAN.getLanguage());
                        config.locale = localizacion;
                        break;
                    case 3:
                        localizacion = new Locale(Locale.FRENCH.getLanguage());
                        Locale.setDefault(localizacion);
                        config.locale = localizacion;
                        break;
                    default:
                        localizacion = new Locale("es", "");
                        Locale.setDefault(localizacion);
                        config.locale = localizacion;
                }
                getResources().updateConfiguration(config, null);
                Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refresh);
                finish();
            }

        });

        b.show();
    }

    /*
       * crear un dialogo con fines de mostrar informacion del autor
       */
    private void show_About_Dialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this); //llama al constructor Builder para alzar un nuevo Dialog
        b.setTitle("About..."); //poner un titulo al dialogo


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            b.setView(R.layout.layout_about_dialog);
        }else {
            b.setMessage("Autor: Joaquin Gonzalez Budiño \n \nMail:joa_gzb@hotmail.com \nProducto licenciado. Consulte terminos y condiciones de " +
                    "licenciamiento con su autor \n \n \n Copyright (c) 2018 - Todos los derechos reservados" );
        }

        b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        b.show();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(UIReceiver);
        super.onStop();
    }


    //    -------------------------------------------METODOS DE ESCUCHA DE INTERFACES ------------------------------


    @Override
    public void validar_login(String user, String pass) {
        new IslogInValid().execute(user, pass);
    }


    private class LogIn_check extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            new LogIn_Dialog().show(getFragmentManager(), "LogIn");
            return null;
        }
    }

    private class IslogInValid extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            mAuth.signInWithEmailAndPassword(strings[0], strings[1]).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(getApplicationContext(), "Authentication Success!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        new LogIn_Dialog().show(getFragmentManager(), "LogIn");
                    }
                }
            });
            return null;
        }
    }


    //    ------------------------------MANEJADOR DE PESTANAS (TABS)----------------------------------------------
    /*
     * A {FragmentPagerAdapter} retorna un fragmento correspondiente a una pestaña en particular
     * Aqui se va a crear el Adaptador que va a manejar nuestras pestañas,
     * que a su vez va a ser el que haga llamada a los distintos Fragments de cada pestaña.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        // getItem crea una instancia de un fragmento en particular segun la pestaña clickeada.
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = frag_perfil.newInstance("hola");
                    break;
                case 1:
                    fragment = frag_informacion.newInstance();
                    break;
                case 2:
                    fragment = frag_eventos.newInstance();
                    break;
                case 3:
                    fragment = frag_lugares.newInstance();
                    break;
            }
            return fragment;
        }


        @Override
        public int getCount() {
            return 4; //cantidad de pestanas
        }

    }
}
