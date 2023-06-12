

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class detailAlu
 */
public class detailAlu extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public detailAlu() {
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

        // VARIABLES
        String milogin = request.getLocalName();
        String dni = (String)session.getAttribute("user");
        String nombre = (String)session.getAttribute("nombre");
        String apellidos = (String)session.getAttribute("apellidos");
        String asignatura = request.getParameter("asig");
        String nota = request.getParameter("nota");
        
        if(nota == "") {nota = "Sin calificar";}
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang='es-es'>");
        out.println("");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>Notas OnLine</title>");
        out.println("    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'");
        out.println("        integrity='sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM' crossorigin='anonymous'>");
        out.println("    <script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'");
        out.println("        integrity='sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz'");
        out.println("        crossorigin='anonymous'></script>");
        out.println("</head>");
        out.println("");
        out.println("<body style='background-color: whitesmoke;'>");
        out.println("    <div class='container'>");
        out.println("        <div class='row mb-10 row no-gutters overflow-hidden flex-md-row mb-4 h-md-250 w-md-100 position-relative'");
        out.println("            style='background: linear-gradient(to right, black, white)'>");
        out.println("            <h1 class='display-4' style='color: white'>Informaci&oacute;n Detallada</h1>");
        out.println("            <h2 class='lead my-3' style='color: white'>Esta p&aacute;gina muestra la información detallada de");
        out.println("                la asignatura " + asignatura + " y su calificacion.</h2>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("    <div class='container'>");
        out.println("        <div class='row no-gutters overflow-hidden flex-md-row mb-4 shadow-sm h-md-250 w-md-100 position-relative'");
        out.println("            style='background-color: white;'>");
        out.println("            <p></p>");
        out.println("            <div>");
        out.println("                <p>" + nombre + " " + apellidos + ", " + dni + "</p>");
        out.println("            </div>");
        out.println("            <div>");
        out.println("                <p>Matriculad@ en la asignatura " + asignatura + "</p>");
        out.println("            </div>");
        out.println("            <div>");
        out.println("                INFORMACI&Oacute;N SOBRE LA ASIGNATURA:");
        out.println("                <p>Lorem ipsum dolor sit amet consectetur, adipisicing elit. In voluptatem est incidunt dolore, ipsum");
        out.println("                    quaerat obcaecati neque itaque quibusdam doloremque, impedit iure temporibus culpa iusto eaque");
        out.println("                    voluptatibus ducimus delectus cumque. Lorem ipsum dolor sit, amet consectetur adipisicing elit.");
        out.println("                    Similique quaerat eius eos nemo totam nam delectus harum! Perspiciatis natus velit, omnis");
        out.println("                    dignissimos neque doloribus asperiores illum, voluptate facere eveniet qui. Lorem ipsum dolor sit");
        out.println("                    amet consectetur, adipisicing elit. Consequatur optio alias sint omnis numquam voluptates dolor");
        out.println("                    rerum aspernatur, laudantium nobis placeat velit enim exercitationem est tempore animi iure beatae");
        out.println("                    inventore? Lorem ipsum dolor sit amet, consectetur adipisicing elit. Cumque sed necessitatibus");
        out.println("                    tenetur, debitis voluptas mollitia at quos eveniet odio unde perspiciatis earum, velit veniam");
        out.println("                    assumenda vel voluptates optio dignissimos! Iusto? Lorem ipsum dolor sit amet consectetur");
        out.println("                    adipisicing elit. Amet voluptas est distinctio tempore non unde debitis dolor odio eveniet, enim");
        out.println("                    eius temporibus, explicabo quidem ut incidunt a, ratione veritatis aspernatur.</p>");
        out.println("            </div>");
        out.println("            <div>");
        out.println("                <p id='calificacion'>Calificación: " + nota + "</p>");
        out.println("            </div>");
        out.println("        </div>");
        out.println("        <form action='MenuAlumno'>");
        out.println("            <button type='submit' class='btn btn-dark' id='loginAlumno'>Atras</button>");
        out.println("        </form>");
        out.println("        <div class='dropdown-divider mt-4'></div>");
        out.println("    </div>");
        out.println("    <div class='container-fluid text-center footer'>");
        out.println("        <p>Trabajo en grupo realizado para la asignatura Desarrollo Web | Curso 2022-2023</p>");
        out.println("    </div>");
        out.println("</body>");
        out.println("");
        out.println("</html>");
        

    }
            
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
