

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
 * Servlet implementation class Certificado2
 */
public class Certificado2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Certificado2() {
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

        // ------------------------------------------------------
        // METODO DE CLIENTE GUARDADO EN SESION
        // CloseableHttpClient cliente = (CloseableHttpClient)
        // session.getAttribute("cliente");
        // ------------------------------------------------------

        // TIPO DE CONTENIDO
        response.setContentType("text/html");
        // OBJETO PRINTLN
        PrintWriter out = response.getWriter();

        // SOLICITUD
        // URL
        String milogin = request.getLocalName();
        String dir_info = "http://" + milogin + ":9090/CentroEducativo/alumnos/" + user + "?key=" + key;

        // GET
        HttpGet get1 = new HttpGet(dir_info);

        // EJECUTO LA SOLICITUD
        final CloseableHttpResponse execute1 = cliente.execute(get1);

        // OBTENEMOS INFO
        try {
        	String info = EntityUtils.toString(execute1.getEntity());
            // OBTENCION DE VALORES DE ALUMNO
            JSONObject infoJSON = new JSONObject(info);
            String dni = infoJSON.getString("dni");
            String nombre = infoJSON.getString("nombre");
            String apellidos = infoJSON.getString("apellidos");
            session.setAttribute("nombre", nombre);
            session.setAttribute("apellidos", apellidos);
            
         // HTML PARA ENCABEZADO
    		out.println("<!DOCTYPE html>");
    		out.println("<html lang='es-es'>");
    		out.println("");
    		out.println("<head>");
    		out.println("    <meta charset='UTF-8'>");
    		out.println("    <title>Certificado</title>");
    		out.println("    <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css\">");
    		out.println("</head>");
    		out.println("");
    		out.println("<body style='background-color: whitesmoke;'>");
    		out.println("    <div class=\"container mt-3 py-3\">");
    		out.println("        <div class=\"row mb-10 row no-gutters border rounded overflow-hidden flex-md-row mb-4 shadow-sm h-md-250 w-md-100 position-relative\" style=\"background-color: darkslategrey;\">");
    		out.println("            <div>");
    		out.println("                <h1 class=\"display-4 font-italic\" style=\"color: white\">DEW - Centro Educativo</h1>");
    		out.println("            </div>");
    		out.println("        </div>");
    		out.println("    </div>");
    		
    		
    		out.println("    <div class=\"container border rounded mt-3 py-3\" style=\"background-color: rgb(232, 230, 229);\">");
    		out.println("        <div class=\"row mb-10 \">");
    		out.println("            <div>");
    		out.println("                <div class=\"col md-6\">");
    		out.println("                    <p class=\"display-6 font-italic\">DEW - Centro Educativo certifica que D/Dª" + nombre + " " + apellidos + ", con DNI " + dni +", matriculado/a en el curso 2022/23, ha obtenido las calificaciones que se muestran en la siguiente tabla</p>");
    		out.println("                </div>");
    		out.println("                <div class=\"col md-6\">");
    		out.println("                    <img src='' alt=\"Imagen del7la alumno/a\" />");
    		out.println("                </div>");
    		out.println("            </div>");
    		out.println("        </div>");
    		out.println("    </div>");
    		
    		
    		// OBTENEMOS ASIGNATURAS DE ALUMNO
            // SOLICITUD
            // URL
            String dir_asignaturas = "http://" + milogin + ":9090/CentroEducativo/alumnos/" + user + "/asignaturas" + "?key=" + key;
            
            // GET
            HttpGet get2 = new HttpGet(dir_asignaturas);

            // EJECUTO LA SOLICITUD
            final CloseableHttpResponse execute2 = cliente.execute(get2);

            // USAMOS JSONARRAY PARA PODER DESPLEGAR LA LISTA DE APLICACIONES
            	// String asignaturas = EntityUtils.toString(execute2.getEntity()).replaceAll("[\\[\\]]", "");
            	String asignaturas = EntityUtils.toString(execute2.getEntity());
            	// out.println(asignaturas);
            	// CONVERSION DE OBJECT A ARRAY
            	JSONArray array = new JSONArray(asignaturas);
            	// out.println(array);
            	
            
            	// DESPLUEGUE DE ASIGNATURAS EN TABLA
            	out.println("<table class='table'>");
            	out.println("    <thead class='thead-dark'>");
            	out.println("      <tr>");
            	out.println("        <th scope='col'>#</th>");
            	out.println("        <th scope='col'>Acronimo</th>");
            	out.println("        <th scope='col'>Nota</th>");
            	out.println("      </tr>");
            	out.println("    </thead>");
            	
            	out.println("    <tbody>");
            	for(int i = 0; i < array.length(); i++) {
                		JSONObject asig_index = (JSONObject) array.get(i);
                		// String acronimo = (asig_index.getString("acronimo")).toString();
                		int num = i;
                		String asig = (asig_index.getString("asignatura")).toString();
                		String nota = (asig_index.getString("nota")).toString();
                		out.println("      <tr>");
                		out.println("        <th scope='row'>" + num + "</th>");
                		// out.println("        <td>" + acronimo + "</td>");
                		out.println("        <td>" + asig + "</td>");
                		out.println("        <td>" + nota + "</td>");
                		out.println("      </tr>");
            	}
            	out.println("    </tbody>");
            	out.println("  </table>");
            	
        		out.println("</body>");
                out.println("");
                out.println("</html>");

                // CERRAMOS RESPUESTA Y CLIENTE
                execute2.close();
            
        } catch (ParseException | IOException e) {
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
