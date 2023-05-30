

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Logger2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Logger2() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// CREACION DEL ARCHIVO DE TEXTO USANDO EL PARAMETRO DEL SERVLET LOGPATH Y CONTROL DE FALLO DE CREACION(ESTANDAR)
		File f = new File(getServletContext().getInitParameter("logPath"));
		try {
			f.createNewFile();
		}catch(Exception e) {
			System.out.println("Error de creacion de archivo");
		}
		
		// OBTENEMOS Y GUARDAMOS LAS VARIABLES ESCRITAS Y ENVIADAS DESDE EL FORMULARIO
		String dni = request.getParameter("dni");
		String nombre = request.getParameter("nombre");
		String apellidos = request.getParameter("apellidos");
		
		// INICIALIZAMOS EL PRINT EN PANTALLA CON UBICACIÓN DE ESCRITURA EN EL PARAMETRO LOGPATH
		PrintWriter pw = new PrintWriter(new FileOutputStream(new File(getServletContext().getInitParameter("logPath")),true));
		
		// ESCRIBIMOS LA INFORMACION EN EL ARCHIVO DE TEXTO
		pw.println(LocalDateTime.now().toString() + " " + request.getQueryString() + " " + dni + ", " + nombre + ", " + apellidos + " " + request.getRemoteAddr() + " " + getServletName() + " " + request.getRequestURI() + " " + request.getMethod() +" \n");
		pw.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
