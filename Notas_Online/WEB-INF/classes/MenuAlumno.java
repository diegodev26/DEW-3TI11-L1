

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class MenuAlumno extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MenuAlumno() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// CAPTURAMOS LA SESION ACTUAL DEL CLIENTE
        HttpSession session = request.getSession();

        // CREAMOS NUEVO ALMACEN DE COOKIES
        BasicCookieStore cookies = new BasicCookieStore();
        // OBTENEMOS LISTA DE COOKIES ANTERIORES
        BasicCookieStore cookies_lts = (BasicCookieStore) session.getAttribute("cookies");
        List<Cookie> cookielist = cookies_lts.getCookies();

        // CARGAMOS VARIABLES DE SESION, TODAS LAS COOKIES ANTERIORES EN LA NUEVA VARIABLE
        // REVISAR METODO COOKIES, PUEDE NO TENER SETNIDO ESTE PROCESO
        String key = (String) session.getAttribute("key");
        String user = (String) session.getAttribute("usr");
        for (Cookie cookie : cookielist) {
        	cookies.addCookie(cookie);
        }

        // OBJETOS
        final CloseableHttpClient cliente = HttpClients.custom().setDefaultCookieStore(cookies).build();
        
        //------------------------------------------------------
        // METODO DE CLIENTE GUARDADO EN SESION
        // CloseableHttpClient cliente = (CloseableHttpClient) session.getAttribute("cliente");
        //------------------------------------------------------

        // TIPO DE CONTENIDO
        response.setContentType("text/html");
        // OBJETO PRINTLN
        PrintWriter out = response.getWriter();

        // SOLICITUD
        // URL
        String milogin = request.getLocalName();
        String dir = "http://" + milogin + ":9090/CentroEducativo/alumnos/" + user + "?key=" + key;
        out.println(key);
        out.println(dir);

        // GET
        HttpGet get = new HttpGet(dir);

        // EJECUTO LA SOLICITUD
        final CloseableHttpResponse execute = cliente.execute(get);
        
        // OBTENEMOS INFO
        String info;
		try {
			info = EntityUtils.toString(execute.getEntity());
			// COMPROBACION
	        out.println(info);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
        

        // CERRAMOS RESPUESTA Y CLIENTE
        execute.close();
        cliente.close();
        
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
