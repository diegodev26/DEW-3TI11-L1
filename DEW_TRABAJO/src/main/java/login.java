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
			String dni = request.getParameter("dni");
			String password = request.getParameter("password");
			JSONobject acceso = new JSONobject();
			acceso.put("dni",dni);
			acceso.put("password",password);
			String accesoBody = acceso.toString();
			
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
					String responseBody = response.body();
				}else {
					//Show error
				}
					
			} catch (IOException | InterruptedException e){
			}
			
		}
	}
		
		

}
