# MEMORIA ENTREGA FINAL

## Inicio, index.html
La página de entrada o índice es la primera pantalla que los clientes verán al acceder al sitio web, **index.html**. Esta contiene dos botones que envían al usuario a su menú principal correspondiente con el filtro de autenticación antes de acceder.
Dependiendo si el usuario es un/a alumno/a o un/a profesor/a tendrá el rol **rolalu** o **rolpro** respectivamente. Estos roles los debemos definir en el fichero tomcat/conf/tomcat-users.xml

Para las consultas http entre el cliente y el código servidor hemos utilizado los javax.servlet y para las consultas a centro educativo hemos usado Apache y Cliente.http v5.
Hemos añadido el link de Bootstrap 4.3.1 para estilos, y el script JQuery y dos scripts de JS para las ventanas modales, botones, etc.

En index.html se abre un formulario y se introducen los datos de usuario para iniciar sesión:
```html
<form action="MenuProfe">
    <input type='hidden' name='rol' value='profesor'>
    <div class="form-group">
        <label for="recipient-name" class="col-form-label">Nombre de usuario</label>
        <input type="text" class="form-control" name="user">
    </div>
    <div class="form-group">
        <label for="recipient-name" class="col-form-label">Contrase&ntilde;a</label>
        <input type="password" class="form-control" name="password">
    </div>
    <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>
        <input type="submit" class="btn btn-dark" value="Ingresar">
    </div>
</form>

```

## Filtro de autenticación
El filtro de autorización Authorization.java se ejecuta en el instante en el que el usuario cliente accede a un servlet al cual se le ha mapeado dicho filtro:
```xml
 <filter>
    <filter-name>Authorization</filter-name>
    <filter-class>Authorization</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>Authorization</filter-name>
    <servlet-name>MenuAlumno</servlet-name>
    <servlet-name>MenuProfe</servlet-name>
  </filter-mapping>
```

La interfaz del filtro implementa tres métodos, el init() para establecer valores inicializados por defecto, destroy() para liberar recursos asociados al filtro y por último el campo más importante doFilter() que se encarga del proceso de filtrado de datos.

En nuestro método doFilter(), en primera instancia nos encargamos de definir el protocolo http que empleamos en los servlets y capturado las credenciales del usuario. Iniciamos la sesión y un almacén de cookies.

Durante la fase de comprobación y debugging, detectamos que al disponer de un botón de "Atrás" en el menú de inicio de los usuarios(alumno y profesor), el filtro aplicado a este, se ejecutaba cada vez que se accedía al servlet. Esto iniciaba una nueva ejecución del protocolo de autenticación, obtención de key, etc., lo cual suponía un problema para la persistencia de la sesión y datos. Por ello, decidimos definir una condición de autenticación solo si la sesión era nueva:
```java
if(session.isNew()) {
    ...
    chain.doFilter(request, response);
}
```
Esto originaba un nuevo problema, ya que como bien sabemos el chain.doFilter() es el callback encargado de proseguir la ejecución del servlet llamado inicialmente, el problema es que si el usuario ya dispone de una sesion, esta debia pasar por alto todo el código hasta ejecutar el chain y, por otro lado, si la sesión era nueva, esta debía comprobar las credenciales y decidir si continúa con chain o se suspende el filtro. Por esto, primero establecemos el else por si el cliente ya tiene una sesión y si es nueva, comprobamos a través del código que retorna execute, si es 200 guardas datos en la sesión y cookies y continua con chain, de lo contrario, si no es 200 se invalida la sesión y se redirige al usuario a la pantalla de inicio.
```java
if(session.isNew()) {
    ...
    if (execute.getCode() != 200) {
        http_req.getSession().invalidate();
        http_resp.sendRedirect("/Notas_Online/");
    } else {
        ...
        chain.doFilter(request, response);
    }
} else {chain.doFilter(request, response);}
```

Otro detalle a destacar para que la sesión del cliente esté controlada correctamente es almacenar las cookies del cliente en cada solicitud correctamente mediante:
```java
final CloseableHttpClient cliente = HttpClients.custom().setDefaultCookieStore(cookies).build();
```
Aparte, almacenamos la clave(key) recibida en el post de login, en la sesión del cliente y también almacenamos las cookies capturadas en las solicitudes. Este proceso nos ha sido crucial para poder realizar solicitudes posteriores. En otro servlet, para crear un cliente, el cual tenga asignado la misma key que el cliente de la autenticación, añadimos las cookies almacenadas en la sesión al cliente nuevo, de no ser así, el servidor no detectaba al usuario con autenticación asociada a esa clave:
```java
// Servlet 2
BasicCookieStore cookies_lts = (BasicCookieStore) session.getAttribute("cookies");
List<Cookie> cookielist = cookies_lts.getCookies();
String key = (String) session.getAttribute("key");
String user = (String) session.getAttribute("user");
for (Cookie cookie : cookielist) {
    cookies.addCookie(cookie);
}
```

## Explicacion paso a paso autenticacion
En el método de autenticar obtenemos las credenciales del formulario e inicializamos la sesión, utilizamos el objeto el Basic cookie store para almacenar cookies en cada consulta.
```java
String user = http_req.getParameter("user");
String password = http_req.getParameter("password");
// CAPTURAMOS LA SESION ACTUAL DEL CLIENTE
HttpSession session = http_req.getSession();
// USAMOS BASICCOOKIE PARA CREAR COOKIES
BasicCookieStore cookies = new BasicCookieStore();
```

Para comprobar las credenciales del usuario hemos añadido una condición para comprobar si la sesión es nueva (session.isNew()) para que la autenticación de usuario se haga solo una vez.
```java
if (session.isNew());
```
Así hemos hecho las consultas:
```java
JSONObject json = new JSONObject();
json.put("x", x);
// DEFINIMOS LA URL DE LA SOLICITUD POST -> http://dew-milogin-2223.dsicv.upv.es:9090/CentroEducativo/login
String milogin = http_req.getLocalName();
String dir = "http://" + milogin + ":9090/CentroEducativo/login";
// CREACION DEL CLIENTE Y POST DESTINO HTTP APACHE
final CloseableHttpClient cliente = HttpClients.custom().setDefaultCookieStore(cookies).build();
// CONSULTA POST
HttpPost post = new HttpPost(dir);
// ENCAPSULAMOS EL MENSAJE DE FORMATO JSON, AÑADO ENTITY AL PAQUETE DE ENVIO
String jsonString = json.toString();
// JSON COMPROBACION
StringEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
post.setEntity(entity);
// EJECUTAMOS LA SOLICITUD
CloseableHttpResponse execute = cliente.execute(post);
```

Tras hacer la consulta en el Autenticar obtenemos la clave del usuario y la almacenamos en una variable de sesión Key, para posteriormente al crear otros clientes para hacer consultas la clave corresponda al primer cliente, almacenamos las cookies en la sesión y posteriormente se las asignamos a posteriores clientes.
```java
// CREAMOS LA KEY PARA LAS CONSULTAS
String key;
try {
    key = EntityUtils.toString(execute.getEntity());
    session.setAttribute("key", key);
} catch (ParseException | IOException e) {
    e.printStackTrace();
}
session.setAttribute("cookies", cookies);
// CERRAMOS RESPUEST
execute.close();
```

## Menu de inicio, MenúAlumno.java 
Una vez se comprueba que las credenciales son correctas, el usuario pasa a la ventana del menú principal de su rol.
En este menú hacemos el mismo uso de la clave de usuario para almacenar las cookies.
Se construye la página html usando los "out.println()".
Hemos añadido el botón de cerrar sesión que ejecuta una acción, el cual es un servlet que invalida la sesión y redirige al index.html

En Menú Alumno, el alumno ve las asignaturas en las que está matriculado, y clicando en cada una puede ver la información y su nota de la asignatura, redirigiendo a la página detailAlu. También encuentra un enlace para generar su Certificado Académico, el cual redirige a la página Certificado.
Los datos que se necesitan para construir la página del MenuAlumno, como lo son asignaturas, dni, nombre y apellidos del usuario(datos personales) se obtienen mediante un objeto JSON que crearemos a partir del parámetro info. A partir de este objeto, podemos extraer los datos anteriormente nombrados de la siguiente manera:
```java
JSONObject infoJSON = new JSONObject(info);
String dni = infoJSON.getString("dni");
String nombre = infoJSON.getString("nombre");
String apellidos = infoJSON.getString("apellidos");
```

Una vez tenemos los datos, los añadimos a la sesión como atributos como aquí abajo se indica:
```java
session.setAttribute("nombre", nombre);
session.setAttribute("apellidos", apellidos);
```

Con estos datos obtenidos, la creación de la página se efectúa con total normalidad, haciendo uso de los anteriormente citados, hasta que llegamos a la lista de notas y asignaturas.
Éstas requerirán de otra solicitud para la cual obtendremos primero la URL de CentroEducativo donde se encuentran y haremos un Get sobre esta y ejecutaremos la solicitud obtenida.
```java
String dir_asignaturas = "http://" + milogin + ":9090/CentroEducativo/alumnos/" + user + "/asignaturas" + "?key=" + key;
HttpGet get2 = new HttpGet(dir_asignaturas);
final CloseableHttpResponse execute2 = cliente.execute(get2);
```

Usaremos una variación de JSON, JSONArray, para poder desplegar la lista de aplicaciones:
```java
String asignaturas = EntityUtils.toString(execute2.getEntity());
```

Convertimos de object a array:
```java
JSONArray array = new JSONArray(asignaturas);
```

Finalmente usamos de un bucle for con el que recorreremos la lista de asignaturas,  y conseguiremos el nombre de la asignatura con su respectiva nota:
```java
for(int i = 0; i < array.length(); i++) {
    JSONObject asig_index = (JSONObject) array.get(i);
    String asig = (asig_index.getString("asignatura")).toString();
    String nota = (asig_index.getString("nota")).toString();
    ...
}
```

Con esto daríamos por concluida la solicitud de datos para la creación del MenuAlumno.

## Menu de inicio, MenúProfesor.java
En Menú Profe, el profesor puede acceder a las asignaturas que él mismo imparte, redirigiéndole al servlet detailProfe.java

Los datos que se necesitan para construir la página del MenuProfe, como lo son dni, nombre y apellidos del usuario(datos personales) se obtienen mediante un objeto JSON (al igual que para el alumno) que crearemos a partir del parámetro info. A partir de este objeto, podemos extraer los datos anteriormente nombrados de la siguiente manera:
```java
JSONObject infoJSON = new JSONObject(info);
String dni = infoJSON.getString("dni");
String nombre = infoJSON.getString("nombre");
String apellidos = infoJSON.getString("apellidos");
```
La página también contiene el enlace para acceder a las asignaturas:
```java
out.println("    <form action='detailProfe' method='GET'>");
out.println("        <input type='hidden' name='dni' value=" + dni + ">");
out.println("        <input type='hidden' name='nombre' value=" + nombre + ">");
out.println("        <input type='hidden' name='apellidos' value=" + apellidos + ">");
out.println("        <button type='submit' class='btn btn-link'>Asignaturas</button>");
out.println("    </form>");
```

## Pagina detalle, detailAlu.java
En esta página se muestra la información del alumno, la información de la asignatura seleccionada en la página anterior y la nota del alumno en la misma (si el profesor no ha puesto las notas, aparece “Sin calificar”).

Para facilitar la creación de los "out.println()" a la hora de construir los HTML dinámicamente, hemos creado un htmlToPrintln.java que convierte el documento html a txt.

Este servlet es llamado desde el servlet *MenuAlumno.java* y su propósito es mostrar la calificación del alumno en la asignatura seleccionada en la “pantalla” anterior.

Del servlet invocante recibirá los parámetros necesarios para mostrar la información. Este Servlet no realiza ninguna petición http.

```java
// VARIABLES
String milogin = request.getLocalName();
String dni = (String)session.getAttribute("user");
String nombre = (String)session.getAttribute("nombre");
String apellidos = (String)session.getAttribute("apellidos");
String asignatura = request.getParameter("asig");
String nota = request.getParameter("nota");
...
if(nota == "") {nota = "Sin calificar";}
...
```
En este fragmento de código vemos cómo obtiene los datos del servlet petición y los usará en la construcción de la página respuesta. 
Es interesante cómo se asigna la cadena: “Sin calificar” cuando se detecta que el parámetro nota no tiene valor numérico, no está asignada una nota para ese alumno en esa asignatura.

## Pagina detalle, detailProfe.java
Este servlet es llamado desde el servlet *MenuProfe.java* y su propósito es mostrar las asignaturas impartidas por el profesor, también brinda al profesor la posibilidad de modificar una nota de un alumno matriculado en una asignatura impartida por él.

Del servlet que lo invoca recibimos los parámetros dni, nombre y apellidos del profesor. El dni nos servirá para la petición que deberá hacer el servlet para obtener las asignaturas de este profesor.

```java
String dir = "http://" + milogin + ":9090/CentroEducativo/profesores/" + dni + "/asignaturas" + "?key=" + key;
//Traza de la key -> out.println(key);
//Traza de la dir -out.println(dir);
// GET
HttpGet get = new HttpGet(dir);
// EJECUTO LA SOLICITUD
final CloseableHttpResponse execute = cliente.execute(get);
// OBTENEMOS INFO
String info;
try {
    info = EntityUtils.toString(execute.getEntity());
    JSONArray infoJSON = new JSONArray(info);
    ...
}
...

```
En esta sección del código del servlet vemos cómo construye la petición HTTP GET para obtener un Array de Objetos JSON, cada objeto JSON es una asignatura.

Para incluir las asignaturas en la página que se devuelve en la respuesta deberemos recorrer el array de objetos JSON Asignaturas extrayendo los atributos que nos interesa mostrar:
```java
out.println("Asignaturas que imparte:");
out.println("<br>");

for (int i = 0; i < infoJSON.length(); i++) {
    JSONObject jsonObject = infoJSON.getJSONObject(i);
    // Imprimir por pantalla cada objeto JSON
    out.println("- " + jsonObject.getString("acronimo") + " | " + jsonObject.getString("nombre")); 
    out.println("<br>");
    }

```
Después insertamos en la página un formulario que invocará al servlet para comenzar con la modificación de una nota. En ese *form* se pasan atributos al servlet **seleccionAsig.java** como el dni del profesor y un string con las asignaturas.

```java
out.println("    <form action='seleccionAsig' method='GET'>");
out.println("        <input type='hidden' name='dni' value=" + dni + ">");
out.println("        <input type='hidden' name='nombre' value=" + nombre + ">");
out.println("        <input type='hidden' name='apellidos' value=" + apellidos + ">");
out.println("        <input type='hidden' name='asignaturas' value=" + info + ">");  
out.println("        <button type='submit' class='btn btn-link'>Modificar nota Asignatura-Alumno</button>");
out.println("    </form>");

```

## Menu de seleccion, seleccionAsig.java
En este servlet el profesor indicará qué asignatura es la que tiene una nota que quiere modificar. Es invocado desde *detailProfe.java* y llamará al servlet *alumnosDeAsig.java*.
De su invocador, este servlet hace especial uso del atributo asignaturas en el que le pasan un String con las asignaturas obtenidas en el servlet anterior.

Con ese String de Asignaturas construye un **JSONArray** que usará para recorrerlo y construir un formulario en el que enviarle al servlet siguiente la asignatura escogida por el profesor:

```java
out.println("    <form action='alumnosDeAsig' method='GET'>");
out.println("        <input type='hidden' name='dni' value=" + dni + ">");
out.println("        <input type='hidden' name='nombre' value=" + nombre + ">");
out.println("        <input type='hidden' name='apellidos' value=" + apellidos + ">");
for (int i = 0; i < asignaturasJSON.length(); i++) {
    JSONObject jsonObject = asignaturasJSON.getJSONObject(i);
    // Imprimir por pantalla cada objeto JSON
    out.println("<input type='radio' name='opcionAsig' value=" + jsonObject.getString("acronimo") + " required>" + jsonObject.getString("acronimo"));
    out.println("<br>");
    ...
}
out.println("        <p><button type='submit' class='btn btn-link'>Seleccionar Asignatura</button></p>");
out.println("    </form>");
```
La implementación de esta funcionalidad como vemos en el código ha sido mediante un formulario y radioButtons, de tal manera que el profesor escoja una y solo una de las asignaturas. El atributo *required* impide continuar sin seleccionar asignaturas.

## Menu de seleccion, alumnosDeAsig.java
Este servlet se encarga de la selección del alumno a quien el profesor quiere modificar la nota en la asignatura seleccionada en el servlet anterior, el que lo invoca a este.

En este servlet se hace uso del atributo que le pasa el servlet invocante: **acrónimo**, este atributo lo usa para construir la petición GET destinada a obtener los alumnos matriculados en una asignatura concreta = la que se seleccionó justo antes.

```java
String acronimo = request.getParameter("opcionAsig");
String dir = "http://" + milogin + ":9090/CentroEducativo/asignaturas/" + acronimo + "/alumnos" + "?key=" + key;
HttpGet get = new HttpGet(dir);
// EJECUTO LA SOLICITUD
final CloseableHttpResponse execute = cliente.execute(get);
String alumnos;
try {
    alumnos = EntityUtils.toString(execute.getEntity());
    // COMPROBACION
    //PRUEBA Mostrar Datos del Profesor -> out.println(info);
    JSONArray alumnosJSON = new JSONArray(alumnos);
    ...
}
```
Aquí se ve la construcción y envío de la petición y como guarda la respuesta en un JSONArray con los objetos JSON Alumnos.
Luego usará ese JSONArray para recorrerlo y hacer exactamente lo mismo que antes: Insertar en un form los radioButtons correspondientes al dni de los alumnos para pasarle esta información al servlet siguiente encargado de poner la nota: *indicarNota.java*
```java
out.println("    <p>Alumnos de " + acronimo + ":</p>");
out.println("<br>");
out.println("    <form action='indicarNota' method='GET'>");
out.println("        <input type='hidden' name='dniProf' value=" + dni + ">");
out.println("        <input type='hidden' name='nombreProf' value=" + nombre + ">");
out.println("        <input type='hidden' name='apellidosProf' value=" + apellidos + ">");
out.println("        <input type='hidden' name='acronimo' value=" + acronimo + ">");
for (int i = 0; i < alumnosJSON.length(); i++) {
    JSONObject jsonObject = alumnosJSON.getJSONObject(i);
    // Imprimir por pantalla cada objeto JSON
    out.println("<input type='radio' name='opcionAlu' value=" + jsonObject.getString("alumno") + " required>" + jsonObject.getString("alumno"));
    out.println("<br>");
}
out.println("        <p><button type='submit' class='btn btn-link'>Siguiente</button></p>");
out.println("    </form>");
```

## Cambio de nota, indicarNota.java
En este servlet, el profesor indicará la nota que quiere poner al alumno y pasará toda la información recopilada durante todos los servlets anteriores para establecer la nota al alumno en la asignatura

La obtención de la nota se hace con un input type number y se pasa al servlet definitivo en un formulario.

## Accion de cambio de nota, setNota.java
Este servlet definitivo será quien construya la petición PUT encargada de acceder al nivel de Datos para modificar la nota al alumno.

***

# DOCUMENTACION

- Documentacion1 Apache: https://hc.apache.org/httpcomponents-client-5.1.x/index.html
- Documentacion2 Apache: https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html

- Documentacion XML: https://docs.oracle.com/cd/E13222_01/wls/docs81/webapp/web_xml.html

- Ejemplos Apache de interes:

  https://github.com/apache/httpcomponents-client/blob/5.1.x/httpclient5/src/test/java/org/apache/hc/client5/http/examples/ClientFormLogin.java
  
  https://mkyong.com/java/apache-httpclient-examples/
  
  https://www.baeldung.com/httpclient-guide

- Documentacion Servlets: https://docs.oracle.com/javaee/7/api/javax/servlet/Servlet.html

- Documentacion Boostrap: https://getbootstrap.com/docs/4.3/getting-started/introduction/

- Funcionamiento y ejemplos de Servlets:

  https://docs.oracle.com/javaee/7/tutorial/servlets.htm

  https://www.tutorialspoint.com/servlets/servlets-first-example.htm

- Libreria JSON-JAVA: https://github.com/stleary/JSON-java

- Videos:

  https://www.youtube.com/watch?v=aJc166sLjuQ

# A TENER EN CUENTA

- Export en formato .war, apache v.9, librerias en lib referenciadas y jdk v.11.

- Habilitar usuarios de Centro Educativo en tomcat-users.xds.

- Usar **apache-tomcat-9.0.72.tar** como target runtime.

- Ejecutar en terminal para iniciar Centro Educativo:
```sh
/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -jar es.upv.etsinf.ti.centroeducativo-0.2.0.jar
```

