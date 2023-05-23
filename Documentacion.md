# Documentación

## Página de Entrada + Enlace a la operación
La página de entrada es la primera pantalla que los clientes verán al acceder al sitio web, **index.html**. En esta página se encuentran dos botones para acceder al sistema y realizar la operación POST sobre "/login" de la API REST. 

![captura](http://personales.alumno.upv.es/flozmel/dew5/index.png)


Dependiendo si el usuario es un/a alumno/a o un/a profesor/a tendrá el rol **rolalu** o **rolpro** respectivamente. Estos roles los debemos definir en el fichero tomcat/conf/tomcat-users.xml

```code
...
<role rolename="rolpro"/>
<role rolename="rolalu"/>

<user username="clientepro" password="miKlavepro" roles="rolpro"/>
<user username="clientealu" password="miKlavealu" roles="rolalu"/>
<user username="12345678W" password="123456" roles="rolalu"/>
<user username="234456733H" password="123456" roles="rolpro"/>
</tomcat-users>

```

Cada botón redirige al cliente a su respectivo servlet para iniciar sesión con sus credenciales (DNI y Contraseña), aunque antes se ejecuta un servlet filtro de autorizacion para comprobar roles, permitir el acceso a esos servlets y que nadie con un rol pueda acceder al login del otro rol. A parte en esta página están los nombres de los participantes de nuestro grupo. Esto se puede ven en el fichero web.xml:

```code
 <filter>
    <display-name>Autentificador</display-name>
    <filter-name>Autentificador</filter-name>
    <filter-class>Autentificador</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>Autentificador</filter-name>
    <servlet-name>LoginAlumno</servlet-name>
    <servlet-name>LoginProf</servlet-name>
  </filter-mapping>
...

<security-constraint>
    <web-resource-collection>
      <web-resource-name>DEWAlu</web-resource-name>
      <url-pattern>/LoginAlumno</url-pattern>
      <http-method>GET</http-method>
      <http-method>POST</http-method>
    </web-resource-collection>
    <auth-constraint>
      <role-name>rolalu</role-name>
    </auth-constraint>
  </security-constraint>
  <security-constraint>
    <web-resource-collection>
      <web-resource-name>DEWProf</web-resource-name>
      <url-pattern>/LoginProf</url-pattern>
      <http-method>GET</http-method>
      <http-method>POST</http-method>
    </web-resource-collection>
    <auth-constraint>
      <role-name>rolpro</role-name>
    </auth-constraint>
  </security-constraint>

```

## Autenticación Web
Como hemos mencionado en el punto anterior, se ejecuta un filtro de Autenticación basado en roles que permite o no acceder al login del rol para más tarde poder enviar las credenciales por **POST**

![captura](http://personales.alumno.upv.es/flozmel/dew5/weblogin.png)

---
## Login con CentroEducativo + Mantener Sesión

El siguiente código muestra como hacemos login y mantenemos la sesión a traves del uso de cookies que se van almcenando. Para hacer Login con CentroEducativo construimos un ObjetoJSON que contiene los datos necesarios (dni,password) para introducir en la peticion por POST. Tras hacer la petición, si todo ha ido bien obtenemos la key de sesión que la asignamos a las sesión y será necesaria para realizar las operaciones en CentroEducativo.

```code

public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		String [] temp = {"", ""};
		BasicCookieStore cookies = new BasicCookieStore();
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();

		String user = req.getRemoteUser();
		String pass = req.getHeader("Authorization").substring(6);	
		
		byte[] decodedBytes = Base64.getDecoder().decode(pass);
		String passDecoded = new String(decodedBytes).substring(10);
		
		if(!users.containsKey(user)) {
			temp[0] = user;
			temp[1] = passDecoded;
			users.put(user, temp);
		}

		String user_value = users.get(user)[0];
		String pass_value = users.get(user)[1];
		
        if(session.isNew() || session.getAttribute("key") == null) {
            session.setAttribute("dni", user_value);
            session.setAttribute("pass", pass_value);
            JSONObject json = new JSONObject();
            json.put("dni", user_value);
            json.put("password", pass_value);
            StringEntity entity = new StringEntity(json.toString());
            String url = request.getLocalName();
            
            try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookies).build()) {
                HttpPost post = new HttpPost("http://" + url + ":9090/CentroEducativo/login");
                post.setEntity(entity);
                post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                CloseableHttpResponse resp = httpclient.execute(post);
                session.setAttribute("cookie", cookies.getCookies());
                String respuesta = EntityUtils.toString(resp.getEntity());                              
                httpclient.close();
                session.setAttribute("key", respuesta);
            }           
            catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

```

---
## Construir y Enviar peticiones a CentroEducativo
Las peticiones las construimos como hemos visto en el ejmplo de código anterior. Si hace falta enviar datos en la petición debemos codificarlos en JSON y añadirlo a la petición.

```code

Executor executor = Executor.newInstance();
        String t = executor.use(cookies)
                .execute(Request.get("http://" + url + ":9090/CentroEducativo/alumnos/" + dni + "?key=" + key))
                .returnContent().toString();

```
En este código vamos a invocar al método getAlumnoByDNI de la APIREST del modelo de Datos por lo que construimos una petición HTTP GET y el objeto resultado será un alumno codificado como string por el metodo toString()

```code

 String asignaturas = executor.use(cookies)
                .execute(Request.get("http://" + url + ":9090/CentroEducativo/alumnos/" + dni + "/asignaturas/?key=" + key))
                .returnContent().toString();

```
En este otro ejemplo de código construimos otra petición HTTP GET para invocar a otro método distinto usando las cookies de la sesión

---
## Interpretar respuestas de CentroEducativo
La petición GET anterior nos devuelve un string por la conversión pero nos interesará reconvertirlo a un array de ObjetosJSON pues así podremos extraer los atributos de las asignatiuras, que esos son los objetos JSON del array respuesta

```code

 JSONArray array = new JSONArray(asignaturas);
        for (int i = 0; i < array.length(); i++) {
            JSONObject asignatura_json = array.getJSONObject(i);
            String acronimo = asignatura_json.getString(parametro_asignatura);
   
  String detallesAsignaturas = executor.use(cookies)
                    .execute(Request.get("http://" + url + ":9090/CentroEducativo/asignaturas/" + acronimo + "/?key=" + key))
                    .returnContent().toString();
            JSONObject detalles = new JSONObject(detallesAsignaturas);
            String nombreD = detalles.getString("nombre");
```
Estos fragmentos de código nos permiten entender como se tratan las respuestas a las peticiones HTTP, haciendo las conversiones a JSON para poder acceder facilmente a sus atributos.
---
## Construcción y Retorno de las páginas HTML de respuesta
El servlet de Login para alumno debe construir dinámicamente la página HTML a devolver al cliente con la información de este.

```code

out.println("<form action='Detalles' method='GET'>" + 
                    "<input type='hidden' name='asig' value='" + acronimo + "'/>" +
                    "<input type='hidden' name='dni' value='" + dni + "'/>" +
                    "<input type='hidden' name='nombre' value='" + nombre_alumno + "'/>" +
                    "<input type='hidden' name='apellido' value='" + apellido_alumno + "'/>" +
                    "<button type='submit' class='btn btn-link'>" + nombreD + "</button> " + 
                    "</form>");
        }

````
Con este código del servlet para el login de Alumno sirve de ejemplo para ilustrar la creación dinámica y construcción de la respuesta al cliente.
Antes de todo esto debemos escribir todo lo que una correcta página HTML tiene:

```code

 String titulo = "<!DOCTYPE html>" + 
                "<html lang=\"es-es\">" + 
                "<head>" + 
                "<meta charset=\"UTF-8\">" + 
                "<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p\" crossorigin=\"anonymous\"></script>" +
                "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3\" crossorigin=\"anonymous\">" +
                "<title>NOL</title>" +
                "<script src=\"https://code.jquery.com/jquery-3.6.0.min.js\"></script>" +
                "<link rel='stylesheet' type='text/css' href='General.css'> " + 
                "</head>";
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(titulo);
```
usaremos el "out.println("")" para ir añadiendo cada linea del documento HTML en construcción.
