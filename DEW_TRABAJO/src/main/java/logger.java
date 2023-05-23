import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.net.URI;
import java.net.http.*;
import java.net.http.HttpClient;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Servlet implementation class login
 */
public class logger extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public logger() {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			//El form
			String dni = request.getParameter("dni");
			String password = request.getParameter("password");
			//creamos el obj json de la peticion http post login
			JSONObject acceso = new JSONObject();
			acceso.put("dni",dni);
			acceso.put("password",password);
			String accesoBody = acceso.toString();
			
			//creamos la peticion http post
			HttpRequest requestPOST = null;
			try {
				requestPOST = HttpRequest.newBuilder()
					.uri(new URI("http://DEW-flozmel-2223.dsicv.upv.es:9090/CentroEducativo/login"))
					.header("Content-Type","application/json")
					.POST(HttpRequest.BodyPublishers.ofString(accesoBody))
					.build();
			} catch (Exception e) {
			}
			
			HttpClient httpClient = HttpClient.newHttpClient();
			String key = "";
			try {
				HttpResponse<String> responsePOST = httpClient.send(requestPOST, HttpResponse.BodyHandlers.ofString());
				int responseCode = responsePOST.statusCode();
				if (responseCode == HttpServletResponse.SC_OK) {
					key = responsePOST.body();
				}else {
					//Show error
					key = "";
				}
					
			} catch (IOException | InterruptedException e){
			}
		
			HttpRequest request2 = null;
			try {
				request2 = HttpRequest.newBuilder()
					.uri(new URI("http://DEW-flozmel-2223.dsicv.upv.es:9090/CentroEducativo/alumnos/"+dni+"/asignaturas?key="+ key))
					.header("Content-Type","application/json")
					.GET()
					.build();
			} catch (Exception e) {
			}
			HttpClient httpClient2 = HttpClient.newHttpClient();
			JSONArray asigs = new JSONArray();
			try {
				HttpResponse<String> response2 = httpClient2.send(request2, HttpResponse.BodyHandlers.ofString());
				int responseCode2 = response2.statusCode();
				if (responseCode2 == HttpServletResponse.SC_OK) {
					asigs = new JSONArray(response2.body());
				}else {
					//Show error
				}
					
			} catch (IOException | InterruptedException e){
			}
			
			String preTituloHTML5 = "<!DOCTYPE html>\n<html>\n<head>\n"
			 + "<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />";
			 response.setContentType("text/html");
			 PrintWriter out = response.getWriter();
			 out.println(preTituloHTML5);
			out.println("<title>Menu Alumno</title>");
			out.println("<link rel="+"stylesheet"+"href=https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css>");
			out.println("</head>");
			
			out.println("<body>");
			out.println("<div class="+"container" + ">");
			out.println("<div class="+"jumbotron p-4 p-md-5 text-white rounded bg-dark>");
            out.println("<div class="+"col-md-12 px-0>");
            out.println("<h1 class=" + "display-4 font-italic>Notas OnLine</h1>");
            out.println("<h3 class=" + "display-4 font-italic>" + "Notas del alumn@:"+dni+" </h3>");
            out.println("<p> Esta p&aacute;gina muestra las asignaturas en las que el/la alumn@ est&aacute; matriculad@. </p>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class="+ "row mb-2>");
            out.println("<div class="+"col-md-6>");
            out.println("<div class=" + "row no-gutters border rounded overflow-hidden flex-md-row mb-4 shadow-sm h-md-250 position-relative>");
            out.println("<div class=" + "col p-4 d-flex flex-column position-static>");
            out.println("<strong class=" + "d-inline-block mb-2>Asignaturas</strong>");      
                  
            out.println("<table class = table table-striped style = border-color:black>");
            out.println("<thead> ");      
            out.println("</thead>");            
            out.println("<tbody>");       
                    
			for (int i = 0; i < asigs.length(); i++) {
				JSONObject json = asigs.getJSONObject(i);
				out.println("<tr><a href=" + "info_asignatura1.html" + ">" + json.get("asignatura").toString() +"</a></tr>");
			}
			out.println("</tbody>");	  
            out.println("</table>");            
            out.println("</div>");    
            out.println("</div>");    
            out.println("</div>");    
              
            out.println("<div class=" + "col-md-6>");
            out.println("<div class=" + "row no-gutters border rounded overflow-hidden flex-md-row mb-4 shadow-sm h-md-250 position-relative>");
            out.println("<div class=" + "col p-4 d-flex flex-column position-static>");  
            out.println("<strong class=" + "d-inline-block mb-2>Grupo</strong>");    
                 
            out.println("</div>");  
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
			out.println("</body>");
			out.println("</html>");
	}
		
			
}