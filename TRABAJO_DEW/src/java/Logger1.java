
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Logger1 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Logger1() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		File f = new File("/home/user/Escritorio/outputLog.log");
		try {
			f.createNewFile();
		} catch (Exception e) {
			System.out.println("Error de creacion de archivo");
		}

		String dni = request.getParameter("dni");
		String nombre = request.getParameter("nombre");
		String apellidos = request.getParameter("apellidos");

		PrintWriter pw = new PrintWriter(new FileOutputStream(new File("/home/user/Escritorio/outputLog.log"), true));

		pw.println(LocalDateTime.now().toString() + " " + request.getQueryString() + " " + dni + ", " + nombre + ", "
				+ apellidos + " " + request.getRemoteAddr() + " " + getServletName() + " " + request.getRequestURI()
				+ " " + request.getMethod() + " \n");
		pw.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
