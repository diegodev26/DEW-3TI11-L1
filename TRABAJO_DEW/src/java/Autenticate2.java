
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.Cookie;
import org.json.JSONObject;

public class Autenticate2 implements Filter{
    
    /*
     * PASOS PARA AUTENTICACION DE USUARIO
     * 1-CAPTURAMOS LA SESION DEL CLIENTE QUE ACCEDE A LA WEB
     * 2-DEFINIMOS EL METODO QUE USAREMOS PARA ALMECENAR LAS COOKIES
     * 3-
     * ...-EJECUTAR FILTRO EN CHAIN AL FINAL PARA POR SI EXISTEN MAS FILTROS
     */
    public Autenticate2() {
        super();
    }

    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {

        HttpSession session = ((HttpServletRequest) request).getSession();
        Cookie c = new Cookie();

        chain.doFilter(request, response);
        
    }

    public void init(FilterConfig fConfig) throws ServletException {

    }

}
