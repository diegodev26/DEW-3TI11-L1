import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Servlet implementation class logger
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
		
		String dni = request.getParameter("dni");
        	String password = request.getParameter("password");
		
		// Construir el objeto JSON con los datos
		JSONObject jsonData = new JSONObject();
		jsonData.put("dni", dni);
		jsonData.put("password", password);

		// Crear un objeto HttpClient
		HttpClient httpClient = HttpClient.newHttpClient();

		// Crear una solicitud POST
		HttpRequest httpRequest = HttpRequest.newBuilder()
			.uri(URI.create("http://DEW-flozmel-2223.dsicv.upv.es:9090/CentroEducativo/login"))
			.header("Content-Type", "application/json")
			.POST(HttpRequest.BodyPublishers.ofString(jsonData.toString()))
			.build();

		try {
		    // Enviar la solicitud y obtener la respuesta
		    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

		    // Obtener el código de estado y el cuerpo de la respuesta
		    int statusCode = httpResponse.statusCode();
		    String responseBody = httpResponse.body();

		    if (statusCode == 200) {
			// Procesar la respuesta y obtener la clave de sesión
			JSONObject responseJson = new JSONObject(responseBody);
			String sessionKey = responseJson.getString("key");
			
		//Obtener las asignaturas y datos del estudiante con otra peticion HTTP GET
			JSONObject jsonDataGET = new JSONObject();
			jsonDataGET.put("dni", dni);
			HttpRequest httpRequest2 = HttpRequest.newBuilder()
			.uri(URI.create("http://DEW-flozmel-2223.dsicv.upv.es:9090/CentroEducativo/alumnos/"+dni+"/asignaturas?key="+sessionKey))
			.header("Content-Type", "application/json")
			.GET(HttpRequest.BodyPublishers.ofString(jsonData.toString()))
			.build();
			    
			HttpResponse<String> httpResponseGET = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());
			int statusCodeGET = httpResponseGET.statusCode();
		    	String responseBodyGET = httpResponseGET.body();
			    
			if (statusCode == 200) {
			// Procesar la respuesta y obtener la clave de sesión
			JSONObject responseJsonGET = new JSONObject(responseBodyGET);
				
			}   

			// Construir la página HTML de respuesta
			    String preTituloHTML5 = "<!DOCTYPE html>\n<html>\n<head>\n"
				 + "<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />";
				 response.setContentType("text/html");
				 PrintWriter out = response.getWriter();
			   	 out.println(preTituloHTML5);
			    
			

			// Enviar la respuesta al cliente
			PrintWriter writer = response.getWriter();
			writer.write(htmlResponse);
		    } else {
			// Enviar una respuesta de error en caso de que el código de estado no sea 200 OK
			response.sendError(statusCode);
		    }
		} catch (IOException | InterruptedException e) {
		    e.printStackTrace();
		}

}
