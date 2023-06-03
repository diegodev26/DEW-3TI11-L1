import org.apache.hc.client5.http.cookie.BasicCookieStore;

public class Authorization implements Filter{
    public Authorization(){
        super();
    }

    public void init(FilterConfig fConfig) throws ServletException {

	}

    public void destroy() {

	}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // ESTABLECEMOS QUE LOS OBJETOS GENERICOS DE LA CLASE SERVLET USARAN HTTP
        HttpServletRequest http_req =  (HttpServletRequest) request;
        HttpServletResponse http_resp = (HttpServletResponse) response;

        // CAPTURAMOS LA SESION ACTUAL DEL CLIENTE
        HttpSession session = http_req.getSerssion();

        // CON APACHE, USAMOS BASICCOOKIE PARA CREAR COOKIES
        BasicCookieStore cookies = new BasicCookieStore();

        // SOLICITAMOS LOS DATOS DEL CLIENTE A ATRAVES DE:
        String login = http_req.getRemoteUser();

        // UNA VEZ COMPROBADA LA AUTENTICACION, EL CHAIN PERMITE CONTINUAR A LA SOLICITUD
        chain.doFilter(request, response);
    }
}
