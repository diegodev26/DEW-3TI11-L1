

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Logger0 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Logger0() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// OBTENEMOS Y GUARDAMOS LAS VARIABLES ESCRITAS Y ENVIADAS DESDE EL FORMULARIO
		String dni = request.getParameter("dni");
		String nombre = request.getParameter("nombre");
		String apellidos = request.getParameter("apellidos");
		
		// INICIALIZAMOS EL PRINT EN PANTALLA Y DEFINIMOS QUE EL CONTENIDO DE LA RESPUESTA SE TRATA DE UN HTML
		PrintWriter pw = response.getWriter();
		response.setContentType("text/html");
		
		// IMPRIMIMOS EL HTML
		pw.println("<!DOCTYPE html>\n<html>\n<head>\n" + "<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />");
		pw.println(LocalDateTime.now().toString() + " " + request.getQueryString() + " " + dni + ", " + nombre + ", " + apellidos + " " + request.getRemoteAddr() + " " + getServletName() + " " + request.getRequestURI() + " " + request.getMethod() +" \n");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
