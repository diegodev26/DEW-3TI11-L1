import java.io.IOException;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;

public class Authorization implements Filter {
    public Authorization() {
        super();
    }

    public void init(FilterConfig fConfig) throws ServletException {

    }

    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // DEFINIMOS LOS SERVLETS GENERICOS CON EL PROTOCOLO HTTP
        HttpServletRequest http_req = (HttpServletRequest) request;
        HttpServletResponse http_resp = (HttpServletResponse) response;

        // COMPROBACION DE QUE FUNCIONA EL FORMULARIO
        String user = http_req.getParameter("user");
        String password = http_req.getParameter("password");

        // PrintWriter out = http_resp.getWriter();
        // out.println("usuario: " + user + " contraseña: " + password);

        // CAPTURAMOS LA SESION ACTUAL DEL CLIENTE
        HttpSession session = http_req.getSession();

        // USAMOS BASICCOOKIE PARA CREAR COOKIES
        BasicCookieStore cookies = new BasicCookieStore();

        
        // HAY UN PROBLEMA CON LAS SESIONES
        // SOLUCION->VERIFICAMOS SI EL USUARIO YA ESTA INICIADO PORQUE SI LO ESTA, Y VOLVEMO A AUTENCIAT
        // DA UN ERROR 500 YA QUE AL VOLVER A LA MENU ALMUNO NO PUEDE AUTENTICAR Y NO PUEDE ACCEDER A RECURSOS
        // AUTENTICAMOS SOLO CUANDO LA SESION SEA NUEVA
        
        if(session.isNew()) {
        // AÑADO LOS PARAMETROS A LA SESION
        session.setAttribute("user", user);
        session.setAttribute("password", password);

        // CREO EL JSON PARA HACER LA SOLICITUD Y AÑADO LA INFORMACION
        JSONObject json = new JSONObject();
        json.put("dni", user);
        json.put("password", password);

        // DEFINO LA URL DE LA SOLICITUD POST ->
        // http://dew-milogin-2223.dsicv.upv.es:9090/CentroEducativo/login
        String milogin = http_req.getLocalName();
        String dir = "http://" + milogin + ":9090/CentroEducativo/login";

        // CREACION DEL CLIENTE Y POST DESTINO HTTP APACHE
        final CloseableHttpClient cliente = HttpClients.custom().setDefaultCookieStore(cookies).build();

        // ------------------------------------------------------
        // METODO DE CLIENTE GUARDADO EN SESION
        // HttpClient cliente =
        // HttpClients.custom().setDefaultCookieStore(cookies).build();
        // session.setAttribute("cliente", cliente);
        // ------------------------------------------------------

        // CONSULTA POST
        HttpPost post = new HttpPost(dir);

        // ENCAPSULO EL MENSAJE DE FORMATIO JSON, AÑADO ENTITY A EL PAQUETE DE ENVIO
        String jsonString = json.toString();
        // JSON COMPROBACION
        // out.println(jsonString);
        StringEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
        post.setEntity(entity);
        // EJECUTO LA SOLICITUD
        CloseableHttpResponse execute = cliente.execute(post);
        // COMPROBACION CONEXION
        // out.println(execute.getCode());
        if (execute.getCode() != 200) {
            System.out.println("Parece que no ha sido posible establecer la conexion con el servidor");
            http_req.getSession().invalidate();
            http_resp.sendRedirect("/Notas_Online/");
        } else {
            System.out.println("Conexion realizada con exito");
            // CREAMOS LA KEY PARA LAS CONSULTAS
            String key;
            try {
                key = EntityUtils.toString(execute.getEntity());
                session.setAttribute("key", key);
            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }

            session.setAttribute("cookies", cookies);
            // CERRAMOS RESPUESTA Y CLIENTE
            execute.close();
            // cliente.close();
            chain.doFilter(request, response);
        } 
    } else {chain.doFilter(request, response);}
        
        // ON ESTE METODO DE LOGIN POR FORMULARIO HE VISTO QUE CUANDO UN USUARIO NO ESTA
        // AUTORIZADO, SE DEVULVE UN ERROR 406. AHORA SOLO TENGO QUE DEFINIR EN EL
        // WEB.XML
        // QUE CUANDO RECIVA UN ERROR 406, SE REDIRIJA AL CLIENTE A UNA PAGINA
        // ESPECIFICA
        // UNA VEZ COMPROBADO EL CODIGO, LA EJECUCION DE CHAIN PERMITE CONTINUAR A LA
        // SOLICITUD
    }
}
