
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Logger1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Logger1() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// CREACION DEL ARCHIVO DE TEXTO Y CONTROL DE FALLO DE CREACION(ESTANDAR)
		File f = new File("/home/user/Escritorio/outputLog.log");
		try {
			f.createNewFile();
		}catch(Exception e) {
			System.out.println("Error de creacion de archivo");
		}
		
		// OBTENEMOS Y GUARDAMOS LAS VARIABLES ESCRITAS Y ENVIADAS DESDE EL FORMULARIO
		String dni = request.getParameter("dni");
		String nombre = request.getParameter("nombre");
		String apellidos = request.getParameter("apellidos");
		
		// INICIALIZAMOS EL PRINT EN PANTALLA
		PrintWriter pw = new PrintWriter(new FileOutputStream(new File("/home/user/Escritorio/outputLog.log"),true));
		
		// ESCRIBIMOS LA INFORMACION EN EL ARCHIVO DE TEXTO
		pw.println(LocalDateTime.now().toString() + " " + request.getQueryString() + " " + dni + ", " + nombre + ", " + apellidos + " " + request.getRemoteAddr() + " " + getServletName() + " " + request.getRequestURI() + " " + request.getMethod() +" \n");
		pw.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
