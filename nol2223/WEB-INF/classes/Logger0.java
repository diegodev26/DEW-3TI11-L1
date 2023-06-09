
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Logger0 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Logger0() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String dni = request.getParameter("dni");
		String nombre = request.getParameter("nombre");
		String apellidos = request.getParameter("apellidos");

		PrintWriter pw = response.getWriter();
		response.setContentType("text/html");

		pw.println("<!DOCTYPE html>\n<html>\n<head>\n"
				+ "<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />");
		pw.println(LocalDateTime.now().toString() + " " + request.getQueryString() + " " + dni + ", " + nombre + ", "
				+ apellidos + " " + request.getRemoteAddr() + " " + getServletName() + " " + request.getRequestURI()
				+ " " + request.getMethod() + " \n");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
