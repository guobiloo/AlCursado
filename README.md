# AlCursado

Aplicación móvil en Android de tipo informativa que permite a los estudiantes realizar consultas de su interés, tales como el estado de las materias que realiza, próximos eventos institucionales, aviso de profesores, realización de trámites. Asimismo, permite publicar información relacionada a formación de grupos de estudio o falencias en los distintos espacios de la facultad.
Orientada principalmente a estudiantes ingresantes o extranjeros con soporte en variedad de idiomas.


## video de promoción
[![AlCursado Android app video](http://img.youtube.com/vi/JCkww69nbj4/0.jpg)](http://www.youtube.com/watch?v=JCkww69nbj4 "AlCursado Android app")

## Tecnologías empleadas
* Android como SO de soporte para la aplicacion.
* Java
* SQLite como base de datos relacional local
* Google Maps para mostrar un mapa
* Firebase - Messaging para el manejo de notificaciones push
* Firebase - Realtime database como base de datos en tiempo real NoSQL
* Google Auth para la autenticación del usuario

## Arquitectura de software
Descripcion de modulos:

- Login: Contiene toda la lógica asociada al manejo de sesión del usuario: registrarse como nuevo, recuperar contraseña e iniciar sesión. 
El inicio de sesión se hace a través de google, facebook o bien ingresando un mail y contraseña.

- PageViewer: crea y administra las distintas subpantallas (fragments) que se muestran, las cuales representan los demás módulos. Consiste en utilizar un navegador por pestañas (tabs) para el control de subpantallas. Además, este módulo se encarga de administrar los mensajes o parámetros que los módulos se envían entre sí para comunicarse.

- User Home Screen: muestra rápidamente el perfil del usuario. permite suscribirse a las distintas materias que se dictan en el cuatrimestre para recibir notificaciones acerca de la misma. Además, gestiona otros contactos que se agregan como amigos.

- Push notification: Muestra un listado, ordenado por fecha, de todas las notificaciones recibidas a partir de la suscripción al feed de una materia que el alumno haya configurado.
- posts wall: Al igual que twitter, permite editar y enviar un breve mensaje público para que cualquiera lo lea. Por ej: formar un grupo de estudio.
- University map guidance: muestra un mapa del campus y el interior del edificio, al mismo tiempo que indica el recorrido a realizar para alcanzar un punto en particular. Este recorrido se establece a partir de la ubicación del usuario que se obtiene 
  - online:gps o bien 
  - offline: mediante códigos QR en las entradas a las oficinas o aulas 
y un destino seleccionado en el módulo de “common places”.
Las aulas y oficinas que poseen un código QR en su entrada, permite consultar información acerca de los trámites que se pueden ejecutar en tal lugar, asimismo como una breve descripción de la labor que allí se realiza y el cuerpo de directivos a cargo.

- common places: establece un listado clasificado de todos los lugares importantes de conocer de la universidad, como lo son oficinas administrativas, baños, cantina. Aquí también se almacenan los lugares como favoritos marcados por el usuario.

- events & activities: Muestra un listado con los próximos eventos a realizarse en la universidad, como lo son conciertos, talleres, charlas, etc. Permite guardar los mismos eventos al calendario del teléfono a modo de recordatorio.


## contacto
Email: <joa_gzb@hotmail.com> // <joa.gzb@gmail.com>
[linkedin](https://www.linkedin.com/in/joaquin-gonzalez-budino/)

## licencia
Copyright (C) 2018 Gonzalez budinho Joaquin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
