

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
        
        // AÑADO LOS PARAMETROS A LA SESION
        // String user = session.getAttribute("usr").toString();
        // String password = session.getAttribute("pass").toString();
        
        // TIPO DE CONTENIDO 
        response.setContentType("text/html");
        
        PrintWriter out = response.getWriter();
        
        // COMPROBACIONES DE CREDENCIALES Y KEY
        String user = (String)session.getAttribute("usr");
        String pass = (String)session.getAttribute("pass");
        String key = (String)session.getAttribute("key");
        out.println("user: " + user);
        out.println("pass: " + pass);
        out.println("key: " + key);
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang='es-es'>");
        out.println("");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>Notas OnLine</title>");
        out.println("    <link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css'>");
        out.println("</head>");
        out.println("");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <div class='row mb-10 row no-gutters border rounded overflow-hidden flex-md-row mb-4 shadow-sm h-md-250 w-md-100 position-relative ' style='background-color: #444444;'>");
        out.println("                <h1 class='display-4 font-italic' style='color: white'>Inicio</h1>");
        out.println("                <h2 class='lead my-3' style='color: white'>Esta p&aacute;gina muestra las asignaturas en las que nombre_alumno apellido_alumno est&aacute; matriculad@.</h2>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("    <div class='container rounded' >");
        out.println("        <div class='row mb-2'>");
        out.println("            <div class='col-md-6'>");
        out.println("              <div class='row no-gutters border rounded overflow-hidden flex-md-row mb-4 shadow-sm h-md-250 w-md-100 position-relative'>");
        out.println("                <div class='col p-4 d-flex flex-column position-static'>");
        out.println("                    ");
        out.println("                        <form action='detail' method='GET'>");
        out.println("                            <input type='hidden' name='asig' value=acronimo>");
        out.println("                            <input type='hidden' name='dni' value=dni>");
        out.println("                            <input type='hidden' name='nombre' value=ombre_alumno>");
        out.println("                            <input type='hidden' name='apellido' value=apellido_alumno>");
        out.println("                            <button type='submit' class='btn btn-link'> Asignaturas </button>");
        out.println("                        </form>");
        out.println("                    ");
        out.println("                </div>");
        out.println("              </div>");
        out.println("            ");
        out.println("                <div class='col p-4 d-flex flex-column position-static' >");
        out.println("                    <form action='logout'>");
        out.println("                        <button type='submit' class='btn btn-outline-dark' id='logout'>Cerrar Sesi&oacute;n</button>");
        out.println("                    </form>");
        out.println("                </div>");
        out.println("              ");
        out.println("            </div>");
        out.println("        ");
        out.println("            <div class='col-md-6'>");
        out.println("                <div class='row no-gutters border rounded overflow-hidden flex-md-row mb-4 shadow-sm h-md-250 position-relative'>");
        out.println("                  <div class='col p-4 d-flex flex-column position-static'>");
        out.println("                    <div class='square'>");
        out.println("                        <h5>Gupo DEW-3TI11-L1</h5>");
        out.println("                        <ol>");
        out.println("                            <li>Pablo Cerd&aacute; Puche</li>");
        out.println("                            <li>Diego C&oacute;rdoba Serra</li>");
        out.println("                            <li>Jorge G&aacute;ndara Sanchis</li>");
        out.println("                            <li>Francisco Lozano Mellado</li>");
        out.println("                            <li>Jos&eacute; Andr&eacute;s Penad&eacute;s Cervell&oacute;</li>");
        out.println("                        </ol>");
        out.println("                </div>");
        out.println("                  </div>");
        out.println("                </div>");
        out.println("              </div>");
        out.println("          </div>");
        out.println("    </div>");
        out.println("</body>");
        out.println("");
        out.println("</html>");
        out.println("");
        out.println("");
        out.println("");
        out.println("");
        out.println("");
        out.println("");
        out.println("");

        
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
