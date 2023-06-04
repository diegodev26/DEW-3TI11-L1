import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
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

        // CAPTURAMOS LA SESION ACTUAL DEL CLIENTE
        HttpSession session = http_req.getSession();

        // USAMOS BASICCOOKIE PARA CREAR COOKIES
        BasicCookieStore cookies = new BasicCookieStore();

        // SOLICITAMOS LOS DATOS DE AUTENTICACION DEL CLIENTE
        String user = http_req.getParameter("user");
        String password = http_req.getParameter("password");

        // AÑADO LOS PARAMETROS A LA SESION
        session.setAttribute("usr", user);
        session.setAttribute("pass", password);
        
        // CREO EL JSON PARA HACER LA SOLICITUD Y AÑADO LA INFORMACION
        JSONObject json = new JSONObject();
        json.put("dni", user);
        json.put("pass", password);

        // DEFINO LA URL DE LA SOLICITUD POST -> http://dew-milogin-2223.dsicv.upv.es:9090/CentroEducativo/login 
        String milogin = http_req.getLocalName();
        String dir = "http://" + milogin + ":9090/CentroEducativo/login";

        // CREACION DEL CLIENTE Y POST DESTINO HTTP APACHE
        CloseableHttpClient cliente = HttpClients.createDefault();
        HttpPost post = new HttpPost(dir);

        // ENCAPSULO EL JSON, AÑADO ENTITY Y LA CABECERA
        String jsonString = json.toString();
        StringEntity entity = new StringEntity(jsonString);
        post.setEntity(entity);
        post.setHeader("Content-Type", "application/json");

        // CONSULTA POST
        CloseableHttpResponse execute = cliente.execute(post);

        // CERRAMOS RESPUESTA Y CLIENTE
        execute.close();
        cliente.close();

        // UNA VEZ COMPROBADO EL CODIGO, LA EJECUCION DE CHAIN PERMITE CONTINUAR A LA SOLICITUD
        chain.doFilter(request, response);
    }
}
