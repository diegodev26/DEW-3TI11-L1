

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;

/**
 * Servlet implementation class setNota
 */
public class setNota extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public setNota() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// CAPTURAMOS LA SESION ACTUAL DEL CLIENTE
        HttpSession session = request.getSession();

        // CREAMOS NUEVO ALMACEN DE COOKIES
        BasicCookieStore cookies = new BasicCookieStore();
        // OBTENEMOS LISTA DE COOKIES ANTERIORES
        BasicCookieStore cookies_lts = (BasicCookieStore) session.getAttribute("cookies");
        List<Cookie> cookielist = cookies_lts.getCookies();

        // CARGAMOS VARIABLES DE SESION, TODAS LAS COOKIES ANTERIORES EN LA NUEVA
        // VARIABLE
        // REVISAR METODO COOKIES, PUEDE NO TENER SETNIDO ESTE PROCESO
        String key = (String) session.getAttribute("key");
        String user = (String) session.getAttribute("user");
        for (Cookie cookie : cookielist) {
            cookies.addCookie(cookie);
        }

        // OBJETOS
        final CloseableHttpClient cliente = HttpClients.custom().setDefaultCookieStore(cookies).build();
        
        // TIPO DE CONTENIDO
        response.setContentType("text/html");
        // OBJETO PRINTLN
        PrintWriter out = response.getWriter();
        

        String dni = request.getParameter("dniProf");
        String nombre = request.getParameter("nombreProf");
        String apellidos = request.getParameter("apellidosProf");
        String acronimo = request.getParameter("acronimo");
        String dniAlu = request.getParameter("opcionAlu");
        String nota = request.getParameter("nota");
        
        //out.println(nota);
        String milogin = request.getLocalName();
        String dir = "http://" + milogin + ":9090/CentroEducativo/alumnos/" + dniAlu + "/asignaturas/" + acronimo + "?key=" + key;
        HttpPut put = new HttpPut(dir);
        
        JSONObject json = new JSONObject();
        json.put("nota", nota);
        
        // ENCAPSULO EL MENSAJE DE FORMATO JSON, AÑADO ENTITY A EL PAQUETE DE ENVIO
        String jsonString = json.toString();
        // JSON COMPROBACION
        // out.println(jsonString);
        StringEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
        put.setEntity(entity);
        // EJECUTO LA SOLICITUD
        CloseableHttpResponse execute = cliente.execute(put);
        
        //response.sendRedirect("/Notas_Online/MenuProfe/");
        
        try {
			String result = EntityUtils.toString(execute.getEntity());
			out.println(result);
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
