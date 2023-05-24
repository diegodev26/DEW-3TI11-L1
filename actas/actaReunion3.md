## Acta de Reunión nº3

### Preámbulo
----
Esta reunión se realizo online a través de Discord el día jueves **04/05/2023** en referencia a la sesión 2 de laboratorio.

En la reunión han participado:
- Pablo Cerdá Puche
- Diego Córdoba Serra
- Jorge Gandara Sanchis
- Francisco Lozano Mellado
- José Andrés Penadés Cervelló
El alumno firmante es:
- José Andrés Penadés Cervelló

### Resumen
---
Tras la explicación de la sesión el día 26/04/2023 por parte del profesor, se inició esta reunión con el objetivo de realizar las tareas correspondientes a la sesión número 1 de la segunda parte de las prácticas. En dicha reunión, se desarrollaron mediante Eclipse los primeros **servlets** del proyecto (prototipos de log) que se  pusieron a prueba mediante formularios. Además, se realizaromn las primeras interacciones con la aplicación CentroEducativo mediante el uso de **curl**.

### Desarrollo
---
**- Formularios**

Para el desarrollo de los diferentes tipos de servlets, se desarrollaron una serie de formularios relativos a cada servlet. Para facilitar poder acceder a cada formulario relacionado con un servlet determinado sin afectar a los demás, se definió un html llamado "index.html", el cual contiene enlaces a los tres formularios, cada uno relacionado con un servlet (log) concreto.
```html
...
<body>
	<h1>Indice de Loggers</h1>
	<a href="log0.html">Enlace al Logger version 0</a>
	<a href="log1.html">Enlace al Logger version 1</a>
	<a href="log2.html">Enlace al Logger version 2</a>
</body>
...
```

Seguidamente, se explicará el funcionamiento de los formularios desarrollados:
1. El formulario adjuntado a continuación hace referencia a la ejecución del servlet log0. Se han desarrollado tres formularios diferentes, cada uno relacionado con su servlet. Cada formulario se llama "logn.html", siendo n el número de log. Como se puede observar, distinguimos cuatro inputs. 
2. El primer input es de tipo text y hace referencia al DNI del usuario. 
3. El segundo input, también de tipo text, hace referencia a los apellidos del usuario.
4. El tercer input, último de tipo ext, hace referencia al nombre del usuario.
5. El cuarto y último input, de tipo submit, actua de botón para realizar el envío del formulario. 

Es también observable que el formato del formulario descrito se aplica tanto para el método GET como para el método POST:
```java
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter pw = response.getWriter();
		String dni = request.getParameter("dni");
		String nombre = request.getParameter("nombre");
		String apellidos = request.getParameter("apellidos");

		String preTituloHTML5 = "<!DOCTYPE html>\n<html>\n<head>\n"
		 + "<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />";
		 response.setContentType("text/html");
		pw.println(preTituloHTML5);
				pw.println(LocalDateTime.now().toString() + " " + request.getQueryString() + " " + dni + ", " + nombre + ", " + apellidos + " " + request.getRemoteAddr() + " " + getServletName() + " " + request.getRequestURI() + " " + request.getMethod() +" \n");
	}
 ```
 ```java
 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
 ```

**- Especificaciones de servlets, logger1 y logger2**

Los nombres de los servlets siguen la misma forma que los nombres de los formularios, loggern, siendo n el numero de log al que hacen referencia. Los servlets 
relativos a log1 y log2 (logger1 y logger2 respectivamente), pese a tener una estructura similar, tienen cambios significativos respecto al log0. Estos cambios
se definirán a continuación.
Los servlets incorporan dos métodos, doGet y doPost. Es aquí donde entran las diferencias entre servlets.  

En primer lugar, nos fijremos en logger1, el servlet relativo a log2. Si nos fijamos en el método doGet, podemos ver que se especifica
(mediante el uso de la clase File) una ubicación, que será donde los logs se generen. 
```java
PrintWriter pw2 = new PrintWriter(new FileOutputStream(new File("/home/user/Escritorio/salidasLog.log"),true));
```
Por otro lado, en el servlet relativo a log2, también se especifica una ubicación pero esta vez se define desde el ficherp web.xml (facilitado en las 
transparencias del tema 7 de Desarrollo Web). Esto provoca un cambio en el código respecto del anterior. 
```xml
  ...
  <context-param>
        <param-name>logPath</param-name>
        <param-value>/home/user/Escritorio/salidasLog.log</param-value>
  </context-param>
  ...
  ```
  
**- Salidas**

La salida debe proporcionar datos del formulario,información del cliente, fecha actual, URI, método. Las diferencias notables entre los tres loggers se hacen 
visibles a la hora de observar la salida de cada uno de ellos.
En cuanto al logger0, la salida de este se mostrará en la pantalla del navegador una vez enviado el respectivo formulario. 
```txt
2023-05-09T19:16:17.566295 dni=12345678A&nombre=User&apellidos=Resu+Resu 12345678A, User, Resu Resu 172.23.2.245 logger0 /DEW_TRABAJO/logger0 GET
2023-05-09T19:16:54.688158 null 12345678A, User, Resu Resu 172.23.2.245 logger0 /DEW_TRABAJO/logger0 POST
```
Por su parte, la salida de logger1 y logger2 quedarán grabadas en el fichero relativo a la ubicación definida anteriormente. 
```txt
2023-05-09T19:17:26.359610 dni=12345678A&nombre=User&apellidos=Resu+Resu 12345678A, User, Resu Resu 172.23.2.245 logger1 /DEW_TRABAJO/logger1 GET
2023-05-09T19:17:37.260188 null 12345678A, User, Resu Resu 172.23.2.245 logger1 /DEW_TRABAJO/logger1 POST
```
```txt
2023-05-09T19:18:37.075084 dni=12345678A&nombre=User&apellidos=Resu+Resu 12345678A, User, Resu Resu 172.23.2.245 logger2 /DEW_TRABAJO/logger2 GET
2023-05-09T19:18:00.259516 null 12345678A, User, Resu Resu 172.23.2.245 logger2 /DEW_TRABAJO/logger2 POST
```

**- Curl**

Para la ejecución de las órdenes curl, se ha creado un script llamado OrdenesCurl.sh. Este script contiene órdenes de consulta, adición y eliminación. 


```sh
key=$(curl -s --data '{"dni":"111111111","password":"654321"}' -X POST -H "content-type: application/json" http://democe.dsic.upv.es:9090/CentroEducativo/login -c galleta -b galleta)

curl -s -X GET 'http://democe.dsic.upv.es:9090/CentroEducativo/alumnos?key='$key -H "accept: application/json" -c galleta -b galleta

curl -s --data '{"apellidos": "lopez", "dni": "111111112", "nombre": "pepe", "password": "654321"}' -X POST -H "content-type: application/json" 'http://democe.dsic.upv.es:9090/CentroEducativo/alumnos?key='$key  -c galleta -b galleta

curl -s --data '{"dni":"111111112"}' -X DELETE -H "content-type: application/json" 'http://democe.dsic.upv.es:9090/CentroEducativo/alumnos/111111112?key='$key  -c galleta -b galleta
```

Para el desarrollo de las órdenes se ha creado una variable de entorno llamada **key**, la cual sirve para guardar la clave de sesión. Esta variable se utiliza para completar la URL de las instrucciones. 
Para procesar en el cliente en formato json, se añade accept:application/json. además, se añade "-b galleta -c galleta" para guardar el fichero de cookies. 

La **primera orden** curl del script, de consulta, muestra por pantalla una lista con todos los objetos json alumnos, con sus propiedades(apellidos,dni,nombre,password),pertenecientes a CentroEducativo.
Salida:
```text
[{"dni":"12345678W","nombre":"Pepe","apellidos":"Garcia Sanchez"},{"dni":"23456387R","nombre":"Maria","apellidos":"Fernandez Gómez"},{"dni":"34567891F","nombre":"Miguel","apellidos":"Hernandez Llopis"},{"dni":"93847525G","nombre":"Laura","apellidos":"Benitez Torres"},{"dni":"37264096W","nombre":"Minerva","apellidos":"Alonso Pérez"}]
```

La **segunda orden** curl del script añade un nuevo alumno de dni "111111112", nombre "pepe", apellido "lopez" y contraseña "654321".Nos devuelve la ejecución de esta orden si todo ha ido bien un **OK**.Si tras la ejecución de esta orden, ejecutamos la primera orden GET para consultar los alumnos de CentroEducativo, podremos observar que el alumno ha sido añadido. 
Salida:
```text
[{"dni":"12345678W","nombre":"Pepe","apellidos":"Garcia Sanchez"},{"dni":"23456387R","nombre":"Maria","apellidos":"Fernandez Gómez"},{"dni":"34567891F","nombre":"Miguel","apellidos":"Hernandez Llopis"},{"dni":"93847525G","nombre":"Laura","apellidos":"Benitez Torres"},{"dni":"37264096W","nombre":"Minerva","apellidos":"Alonso Pérez"},{"dni":"111111112","nombre":"pepe","apellidos":"lopez"}]
```

La **tercera orden** curl del script elimina un alumno de CentroEducativo a partir de su dni. Nos devuelve la ejecución de esta orden si todo ha ido bien un **OK**.Si tras la ejecución de esta orden, ejecutamos la primera para consultar los alumnos de CentroEducativo, podremos observar que el alumno ha sido eliminado. En este ejemplo, se ha eliminado el alumno añadido en la orden anterior. 
Salida:
```text
[{"dni":"12345678W","nombre":"Pepe","apellidos":"Garcia Sanchez"},{"dni":"23456387R","nombre":"Maria","apellidos":"Fernandez Gómez"},{"dni":"34567891F","nombre":"Miguel","apellidos":"Hernandez Llopis"},{"dni":"93847525G","nombre":"Laura","apellidos":"Benitez Torres"},{"dni":"37264096W","nombre":"Minerva","apellidos":"Alonso Pérez"}]
```

### En referencia al profesor
---
La reunión ha transcurrido sin incidentes que precisaran de la presencia o ayuda del profesor.