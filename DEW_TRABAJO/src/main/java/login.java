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
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Servlet implementation class login
 */
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public login() {
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
			JSONobject acceso = new JSONobject();
			acceso.put("dni",dni);
			acceso.put("password",password);
			String accesoBody = acceso.toString();
			
			//creamos la peticion http post
			HttpRequest request = null;
			try {
				request = HttpRequest.newBuilder()
					.uri(new URI("http://DEW-flozmel-2223.dsicv.upv.es:9090/CentroEducativo/login"))
					.header("Content-Type","application/json")
					.POST(HttpRequest.BodyPublishers.ofString(jsonBody))
					.build();
			} catch (URISyntaxException e) {
			}
			
			HttpClient httpClient = HttpClient.newHttpClient();
			try {
				HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
				int responseCode = response.statusCode();
				if (responseCode == HttpServletResponse.SC_OK) {
					String key = response.body();
				}else {
					//Show error
				}
					
			} catch (IOException | InterruptedException e){
			}
		
			HttpRequest request2 = null;
			try {
				request2 = HttpRequest.newBuilder()
					.uri(new URI("http://DEW-flozmel-2223.dsicv.upv.es:9090/CentroEducativo/alumnos/"+dni+"/asignaturas?key="key))
					.header("Content-Type","application/json")
					.GET()
					.build();
			} catch (URISyntaxException e) {
			}
			HttpClient httpClient2 = HttpClient.newHttpClient();
			try {
				HttpResponse<String> response2 = httpClient2.send(request2, HttpResponse.BodyHandlers.ofString());
				int responseCode2 = response2.statusCode();
				if (responseCode2 == HttpServletResponse.SC_OK) {
					String asigs = reponse2.body().tostring();
				}else {
					//Show error
				}
					
			} catch (IOException | InterruptedException e){
			}
			
			JSONarray asignaturas = new JSONArray(asigs);
			String preTituloHTML5 = "<!DOCTYPE html>\n<html>\n<head>\n"
			 + "<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />";
			 response.setContentType("text/html");
			 PrintWriter out = response.getWriter();
			 out.println(preTituloHTML5);
			out.println("<title>Menu Alumno</title>");
			out.println("<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css">");
			out.println("</head>");
			
			out.println("<body>");
			out.print(" <div class="container" >
        <div class="jumbotron p-4 p-md-5 text-white rounded bg-dark">
            <div class="col-md-12 px-0">
              <h1 class="display-4 font-italic">Notas OnLine</h1>
              <h3 class="display-4 font-italic">Notas del alumn@:"+dni+" </h3>
              <p class="lead my-3">Esta p&aacute;gina muestra las asignaturas en las que el/la alumn@ est&aacute; matriculad@. </p>
            </div>
          </div>
        <div class="row mb-2">
            <div class="col-md-6">
              <div class="row no-gutters border rounded overflow-hidden flex-md-row mb-4 shadow-sm h-md-250 position-relative">
                <div class="col p-4 d-flex flex-column position-static">
                  <strong class="d-inline-block mb-2 ">Asignaturas</strong>
                  
                  <table class="table table-striped " style="border-color:black">
                    <thead> 
                        
                    </thead>
                    <tbody>");
			for (int i = 0; i < asignaturas.length(); i++) {
				JSONObject json = asignaturas(i);
				out.println("<tr><a href="info_asignatura1.html">" + json.get("asignatura").toString() +"</a></tr>");
			}
			out.println("	  
                        </tbody>
                </table>
                </div>
                
              </div>
            </div>
            <div class="col-md-6">
              <div class="row no-gutters border rounded overflow-hidden flex-md-row mb-4 shadow-sm h-md-250 position-relative">
                <div class="col p-4 d-flex flex-column position-static">
                  <strong class="d-inline-block mb-2 ">Grupo</strong>
                  <table class="table table-striped " style="border-color:black">
                    <thead> 
                    </thead>
                    <tbody>
                        <tr><td>Josete</td></tr>
                        <tr><td>Fran</td></tr>
                        <tr><td>Pablo</td></tr>
                        <tr><td>Jorge</td></tr>
                        <tr><td>Diego</td></tr>
                        </tbody>
                </table>
                </div>
              </div>
            </div>
          </div>
    </div>");
			out.println("</body>");
			out.println("</html>")
			
			
			
		
		
	}
		
			
}
