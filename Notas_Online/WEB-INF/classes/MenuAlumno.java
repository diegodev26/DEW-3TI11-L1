
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        String user = (String) session.getAttribute("usr");
        String pass = (String) session.getAttribute("pass");

        out.println("<!DOCTYPE html>" +
                "<html lang='es-es'>" +

                "<head>" +
                "<meta charset='UTF-8'>" +
                "<title>MenuAlumno.java</title>" +
                "<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css'"
                +
                "integrity='sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T' crossorigin='anonymous'>"
                +
                "<script src='https://code.jquery.com/jquery-3.3.1.slim.min.js'" +
                "integrity='sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo'" +
                "crossorigin='anonymous'></script>" +
                "<script src='https://cdn.jsdelivr.net/npm/popper.js@1.14.7/dist/umd/popper.min.js'" +
                "integrity='sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1'" +
                "crossorigin='anonymous'></script>" +
                "<script src='https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js'" +
                "integrity='sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM'" +
                "crossorigin='anonymous'></script>" +
                "</head>" +
                "<body>" +
                "<div class='container-fluid text-center py-5' id='welcome'>" +
                "<h1>MenuAlumno.java</h1>" +
                "</div>" +
                "</body>");

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
