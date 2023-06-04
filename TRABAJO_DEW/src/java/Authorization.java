import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
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
        StringEntity entity = new StringEntity(json);
        post.setEntity(entity);
        post.setHeader("Content-Type", "application/json");

        // CONSULTA POST
        CloseableHttpResponse execute = cliente.execute(dir);

        // CERRAMOS RESPUESTA Y CLIENTE
        execute.close();
        cliente.close();

        // UNA VEZ COMPROBADO EL CODIGO, LA EJECUCION DE CHAIN PERMITE CONTINUAR A LA SOLICITUD
        chain.doFilter(request, response);
    }
}
