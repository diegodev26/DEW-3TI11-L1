

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
 * Servlet implementation class alumnosDeAsig
 */
public class alumnosDeAsig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public alumnosDeAsig() {
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
        
        String milogin = request.getLocalName();
        String dni = request.getParameter("dni");
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String acronimo = request.getParameter("opcionAsig");
        
        String dir = "http://" + milogin + ":9090/CentroEducativo/asignaturas/" + acronimo + "/alumnos" + "?key=" + key;
        HttpGet get = new HttpGet(dir);
        
        // EJECUTO LA SOLICITUD
        final CloseableHttpResponse execute = cliente.execute(get);
        String alumnos;
        try {
            alumnos = EntityUtils.toString(execute.getEntity());
  
            // COMPROBACION
            //PRUEBA Mostrar Datos del Profesor -> out.println(info);
            JSONArray alumnosJSON = new JSONArray(alumnos);
            
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es-es'>");
            out.println("");
            out.println("<head>");
            out.println("    <meta charset='UTF-8'>");
            out.println("    <title>Notas OnLine</title>");
            out.println(
                    "    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'");
            out.println(
                    "        integrity='sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM' crossorigin='anonymous'>");
            out.println("    <script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'");
            out.println("        integrity='sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz'");
            out.println("        crossorigin='anonymous'></script>");
            out.println("</head>");
            out.println("");
            out.println("<body style=\"background-color: whitesmoke;\">");
            out.println("   <div class=\"container\">");
            out.println("        <div class=\"row mb-10 row no-gutters overflow-hidden flex-md-row mb-4 h-md-250 w-md-100 position-relative\"");
            out.println("            style=\"background: linear-gradient(to right, black, white)\">");
            out.println("            <h1 class=\"display-4\" style=\"color: white\">Seleccionar Alumno</h1>");
            out.println("            <h2 class=\"lead my-3\" style=\"color: white\">En esta p&aacute;gina debes seleccionar el alumno a quien quieres modificar la nota.");
            out.println("                </h2>");
            out.println("        </div>");
            out.println("    </div>");
            out.println("    <div class=\"container\">");
            out.println("        <div class=\"row no-gutters overflow-hidden flex-md-row mb-4 shadow-sm h-md-250 w-md-100 position-relative\"");
            out.println("            style=\"background-color: white;\">");
            out.println("            <p></p>");
            out.println("            <div>");
            out.println("                <p>Profesor: " + nombre + " " + apellidos + ", " +  dni + "</p>");
            out.println("            </div>");
            out.println("            <div>");
            out.println("                Alumnos de " + acronimo);
            out.println("<br>");
            

            out.println("                        <form action='indicarNota' method='GET'>");
            out.println("                            <input type='hidden' name='dniProf' value=" + dni + ">");
            out.println("                            <input type='hidden' name='nombreProf' value=" + nombre + ">");
            out.println("                            <input type='hidden' name='apellidosProf' value=" + apellidos + ">");
            out.println("                            <input type='hidden' name='acronimo' value=" + acronimo + ">");
            
            
						            for (int i = 0; i < alumnosJSON.length(); i++) {
						                JSONObject jsonObject = alumnosJSON.getJSONObject(i);
						                // Imprimir por pantalla cada objeto JSON
						                out.println("<input type='radio' name='opcionAlu' value=" + jsonObject.getString("alumno") + " required>" + jsonObject.getString("alumno"));
						                out.println("<br>");
						            }

            out.println("                            <p><button type='submit' class='btn btn-link'>Siguiente</button></p>");
            out.println("                        </form>");
            out.println("            </div>");
            out.println("    </div>");
            out.println("        <div class='dropdown-divider mt-4'></div>");
            out.println("        <div class='container-fluid text-center footer'>");
            out.println("            <p>Trabajo en grupo realizado para la asignatura Desarrollo Web | Curso 2022-2023</p>");
            out.println("        </div>");
            out.println("</body>");
            out.println("</html>");
        
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        // CERRAMOS RESPUESTA Y CLIENTE
        execute.close();
        cliente.close();
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
