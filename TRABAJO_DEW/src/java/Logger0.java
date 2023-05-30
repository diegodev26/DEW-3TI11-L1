

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
		// CREACIÓN DE VARIABLES OBTENIDAS DEL FORM
		PrintWriter pw = response.getWriter();
		String dni = request.getParameter("dni");
		String nombre = request.getParameter("nombre");
		String apellidos = request.getParameter("apellidos");
		// HTML DINAMICO
		String html5 = "<!DOCTYPE html>\n<html>\n<head>\n" + "<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />";
		response.setContentType("text/html");
		pw.println(html5);
		pw.println(LocalDateTime.now().toString() + " " + request.getQueryString() + " " + dni + ", " + nombre + ", " + apellidos + " " + request.getRemoteAddr() + " " + getServletName() + " " + request.getRequestURI() + " " + request.getMethod() +" \n");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
