

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.hc.client5.http.cookie.BasicCookieStore;

public class MenuAlumno extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MenuAlumno() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// CAPTURAMOS LA SESION ACTUAL DEL CLIENTE
        HttpSession session = request.getSession();

        // USAMOS BASICCOOKIE PARA CREAR COOKIES
        BasicCookieStore cookies = new BasicCookieStore();

        // CARGAMOS VARIABLES DE SESION
        String key = (String)session.getAttribute("key");
        List<Cookie> cookielist = cookies.getCookies();
        cookies.addCookie(cookielist);

        // OBJETOS
        final CloseableHttpClient cliente = HttpClients.custom().setDefaultCookieStore(cookies).build();

        // TIPO DE CONTENIDO 
        response.setContentType("text/html");
        // OBJETO PRINTLN
        PrintWriter out = response.getWriter();

        // SOLICITUD
        // URL
        String milogin = http_req.getLocalName();
        String dir = "http://" + milogin + ":9090/CentroEducativo/login";

        // POST
        HttpPost post = new HttpPost(dir);

        // EJECUTO LA SOLICITUD
        final CloseableHttpResponse execute = cliente.execute(post);
        
        
        // CERRAMOS RESPUESTA Y CLIENTE
        execute.close();
        cliente.close();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
