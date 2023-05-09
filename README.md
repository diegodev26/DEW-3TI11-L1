# Resumen de Actas | DEW-3TI11-L1

#Acta de Reunión nº3

###Preámbulo
------------
Esta reunión se realizo el día jueves 27/04/2023

El grupo está compuesto por:
- Pablo Cerdá Puche
- Diego Córdoba Serra
- Jorge Gandara Sanchis
- Francisco Lozano Mellado
- José Andrés Penadés Cervelló

El alumno firmante es:
- José Andrés Penadés Cervelló

###Resumen
-----------
Tras la explicación de la sesión el día 26/04/2023 por parte del profesor, se inició esta reunión con el objetivo de realizar las tareas correspondientes a la 
sesión número 1 de la segunda parte de las prácticas. En dicha reunión, se desarrollaron mediante Eclipse los primeros **servlets** del proyecto (prototipos 
de log) que se  pusieron a prueba mediante formularios. Además, se realizaromn las primeras interacciones con la aplicación CentroEducativo mediante el uso 
de **curl**. 

###Desarrollo
-------------
**Formularios**
Para el desarrollo de los diferentes tipos de servlets, se desarrollaron una serie de formularios relativos a cada servlet. Para facilitar poder acceder a cada 
formulario relacionado con un servlet determinado sin afectar a los demás, se definió un html llamado "index.html", el cual contiene enlaces a los tres formularios, 
cada uno relacionado con un servlet (log) concreto. 
*Código de index.html

Seguidamente, se exxplicará el funcionamiento de los formularios desarrollados
El formulario adjuntado a continuación hace referencia a la ejecución del servlet log0. Se han desarrollado tres formularios diferentes, cada uno relacionado con 
su servlet. Cada formulario se llama "logn.html", siendo n el número de log. Como se puede observar, distinguimos cuatro inputs. 
El primer input es de tipo text y hace referencia al DNI del usuario. 
El segundo input, también de tipo text, hace referencia a los apellidos del usuario.
El tercer input, último de tipo ext, hace referencia al nombre del usuario.
El cuarto y último input, de tipo submit, actua de botón para realizar el envío del formulario. 

Es también observable que el formato del formulario descrito se aplica tanto para el método GET como para el método POST. 
*Código de log0.html

**Especificaciones de servlets, logger1 y logger2**
Los nombres de los servlets siguen la misma forma que los nombres de los formularios, loggern, siendo n el numero de log al que hacen referencia. Los servlets 
relativos a log1 y log2 (logger1 y logger2 respectivamente), pese a tener una estructura similar, tienen cambios significativos respecto al log0. Estos cambios
se definirán a continuación.
Los servlets incorporan dos métodos, doGet y doPost. Es aquí donde entran las diferencias entre servlets.  

En primer lugar, nos fijremos en logger1, el servlet relativo a log2. Si nos fijamos en el método doGet, podemos ver que se especifica
(mediante el uso de la clase File) una ubicación, que será donde los logs se generen. 
*Código de logger1.java

Por otro lado, en el servlet relativo a log2, también se especifica una ubicación pero esta vez se define desde el ficherp web.xml (facilitado en las 
transparencias del tema 7 de Desarrollo Web). Esto provoca un cambio en el código respecto del anterior. 
*Código de logger2.java

**Salidas **
La salida debe proporcionar datos del formulario,información del cliente, fecha actual, URI, método. Las diferencias notables entre los tres loggers se hacen 
visibles a la hora de observar la salida de cada uno de ellos.
En cuanto al logger0, la salida de este se mostrará en la pantalla del navegador una vez enviado el respectivo formulario. 
*salida de logger0
Por su parte, la salida de logger1 y logger2 quedarán grabadas en el fichero relativo a la ubicación definida anteriormente. 
*salida de logger1
*salida de logger2


**Curl**






###En referencia al profesor
----------------------------
La reunión ha transcurrido sin incidentes que precisaran de la presencia o ayuda del profesor.

