
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.fluent.Executor;
import org.apache.hc.client5.http.fluent.Request;
import org.json.JSONArray;
import org.json.JSONObject;

public class DetailPage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DetailPage() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String titulo = "<!DOCTYPE html>" +
                "<html lang=\"es-es\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p\" crossorigin=\"anonymous\"></script>"
                +
                "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3\" crossorigin=\"anonymous\">"
                +
                "<title>P&aacute;gina Detalle</title>" +
                "<script src=\"https://code.jquery.com/jquery-3.6.0.min.js\"></script>" +
                "</head>";
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(titulo);
        String acronimo = request.getParameter("asig");
        String dni = request.getParameter("dni");
        String nombre_alumno = request.getParameter("nombre");
        String apellido_alumno = request.getParameter("apellido");
        HttpSession session = request.getSession();
        List<Cookie> cookie_list = (List<Cookie>) session.getAttribute("cookie");
        Executor executor = Executor.newInstance();
        BasicCookieStore cookies = new BasicCookieStore();
        cookies.addCookie(cookie_list.get(0));
        String url = request.getLocalName();
        String key = session.getAttribute("key").toString();

        String detalles_asignatura = executor.use(cookies)
                .execute(Request.get("http://" + url + ":9090/CentroEducativo/asignaturas/" + acronimo
                        + "/?key=" + key))
                .returnContent().toString();
        JSONObject detalles_json = new JSONObject(detalles_asignatura);

        out.println("<body>\r\n"
                + "    <div class='card text-center' id='titulo'>\r\n");
        out.println("        <div class='col-10'>\r\n"
                + "            <h1>" + apellido_alumno + ", " + nombre_alumno + " (" + dni
                + ")</h1>\r\n"
                + "        </div>\r\n"
                + "    </div>\r\n"
                + "\r\n"
                + "    <hr>\r\n"
                + "    <div class='card text-center'>\r\n"
                + "        <div class='col-8'><h5>[Matriculad@ en: " + detalles_json.getString("nombre")
                + " (" + acronimo + ")]</h5></div>\r\n"
                + "    </div>");

        String notas = executor.use(cookies)
                .execute(Request.get("http://" + url + ":9090/CentroEducativo/alumnos/" + dni
                        + "/asignaturas/?key=" + key))
                .returnContent().toString();

        JSONArray notas_json = new JSONArray(notas);

        String nota_asignatura = "0";
        for (int i = 0; i < notas_json.length(); i++) {
            JSONObject notas_obj = notas_json.getJSONObject(i);
            String nota = notas_obj.get("nota").toString();
            if (nota.length() == 0) {
                nota = "sin calificacion";
            }
            if (notas_obj.get("asignatura").toString().equals(acronimo)) {
                nota_asignatura = nota;
            }
        }

        out.println("<div class='card text-center'>\r\n"
                + "    <div class='card-body'>\r\n"
                + "        <p><b>Nombre: </b>" + apellido_alumno + ", " + nombre_alumno + "</p>\r\n"
                + "        <p><b>DNI: </b>" + dni + "</p>\r\n"
                + "        <p id='calificacion'><b>" + nota_asignatura + "</b></p>\r\n"
                + "    </div>\r\n"
                + "</div>"
                + "<form action='alumno'>" +
                "<button type=\"submit\" class=\"btn btn-outline-primary\" id=\"loginAlumno\">Atras</button>"
                +
                "</form>");

        out.println("</body></html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
