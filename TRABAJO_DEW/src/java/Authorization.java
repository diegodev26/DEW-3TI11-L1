import org.apache.hc.client5.http.cookie.BasicCookieStore;

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
        HttpSession session = http_req.getSerssion();

        // USAMOS BASICCOOKIE PARA CREAR COOKIES
        BasicCookieStore cookies = new BasicCookieStore();

        // SOLICITAMOS LOS DATOS DE AUTENTICACION DEL CLIENTE
        String login = http_req.getRemoteUser();
        

        //


        // UNA VEZ COMPROBADO EL CODIGO, LA EJECUCION DE CHAIN PERMITE CONTINUAR A LA SOLICITUD
        chain.doFilter(request, response);
    }
}
