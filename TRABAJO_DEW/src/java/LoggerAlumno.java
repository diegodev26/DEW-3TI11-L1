
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

public class LoggerAlumno extends HttpServlet {

        private static final long serialVersionUID = 1L;

        public LoggerAlumno() {
                super();
                // TODO Auto-generated constructor stub
        }

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException {

                // GUARDAMOS EL <HEAD> DEL HTML DINAMICO POR SI LO USAMOS EN MAS DE UNA OCASIÓN
                // PARA SALTOS DE LINEA CONCATENAMOS CADA LINEA Y PARA LAS COMILLAS DOBLES
                // \"ejemplo\"
                String html = "<!DOCTYPE html>" +
                                "<html lang=\"es-es\">" +
                                "<head>" +
                                "<meta charset=\"UTF-8\">" +
                                "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3\" crossorigin=\"anonymous\">"
                                +
                                "<title>Notas Oline Alumno</title>" +
                                "</head>";

                // ESTABLECEMOS QUE LOS RESPONSE IMPRESOS SERAN DE TIPO HTML
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println(titulo);
                HttpSession session = request.getSession();
                String dni = session.getAttribute("dni").toString();
                String key = session.getAttribute("key").toString();

                List<Cookie> cookie_list = (List<Cookie>) session.getAttribute("cookie");
                String url = request.getLocalName();
                BasicCookieStore cookies = new BasicCookieStore();
                cookies.addCookie(cookie_list.get(0));
                Executor executor = Executor.newInstance();
                String t = executor.use(cookies)
                                .execute(Request.get("http://" + url + ":9090/CentroEducativo/alumnos/" + dni + "?key="
                                                + key))
                                .returnContent().toString();

                JSONObject alumno = new JSONObject(t);
                String nombre_alumno = alumno.get("nombre").toString();
                String apellido_alumno = alumno.get("apellidos").toString();
                out.println("<body>" +
                                "<div class=\"jumbotron p-4 p-md-5 text-white rounded bg-dark\">" +
                                "   <div class='row' id='titulo'>" +
                                "       <h1 class=\"display-1 font-italic\"><b>Notas OnLine Alumno</b>" + "<br>" +
                                "<b class=\"h2 font-italic\">Esta p&aacute;gina muestra las asignaturas en las que "
                                + nombre_alumno + " " + apellido_alumno + " est&aacute; matriculad@.</b></h1> </div>" +
                                "       <div id='descripcion'>" +
                                "<br>" +
                                "       </div><br>");

                out.println("<div class='row'>" +
                                "   <div class='col-8' id='inicios'>" +
                                "       <div class='row' id='asignaturas'>" +
                                "           <p><b>ASIGNATURAS:</b></p>" +
                                "       </div>");

                session.setAttribute("rol", "rolalu");
                String parametro_asignatura = "asignatura";
                String asignaturas = executor.use(cookies)
                                .execute(Request.get("http://" + url + ":9090/CentroEducativo/alumnos/" + dni
                                                + "/asignaturas/?key=" + key))
                                .returnContent().toString();

                JSONArray array = new JSONArray(asignaturas);
                for (int i = 0; i < array.length(); i++) {
                        JSONObject asignatura_json = array.getJSONObject(i);
                        String acronimo = asignatura_json.getString(parametro_asignatura);

                        String detallesAsignaturas = executor.use(cookies)
                                        .execute(Request.get("http://" + url + ":9090/CentroEducativo/asignaturas/"
                                                        + acronimo + "/?key=" + key))
                                        .returnContent().toString();
                        JSONObject detalles = new JSONObject(detallesAsignaturas);
                        String nombreD = detalles.getString("nombre");

                        out.println("<form action='DetailPage' method='GET'>" +
                                        "<input type='hidden' name='asig' value='" + acronimo + "'/>" +
                                        "<input type='hidden' name='dni' value='" + dni + "'/>" +
                                        "<input type='hidden' name='nombre' value='" + nombre_alumno + "'/>" +
                                        "<input type='hidden' name='apellido' value='" + apellido_alumno + "'/>" +
                                        "<button type='submit' class='btn btn-link'>" + nombreD + "</button> " +
                                        "</form>");
                }
                out.println("<br>" +
                                "<br>" +
                                "<br>");
                out.println("<form action='Logout'>" +
                                "<button type=\"submit\" class=\"btn btn-outline-danger\" id=\"logout\">Cerrar Sesi&oacute;n</button>"
                                +
                                "</form>");

                out.println("</div>");
                out.println("<div class='card col-3 text-dark' style='width: 18rem;' id='nombres'>" +
                                "   <div class='card-body'>" +
                                "       <h5 class='card-title'>Grupo 3TI11_L1</h5>" +
                                "       <ol>" +
                                "           <li>Pablo Cerdá Puche</li>" +
                                "           <li>Diego Córdoba Serra</li>" +
                                "           <li>Jorge Gandara Sanchis/li>" +
                                "           <li>Francisco Lozano Mellado</li>" +
                                "           <li>José Andrés Penadés Cervelló</li>" +
                                "       </ol>" +
                                "   </div>" +
                                "</div>" +
                                "</div>");

                out.println("<br>" +
                                "<br>" +
                                "<br>" +
                                "<br>");
                out.println("<div class=\"container-fluid text-center py-3 footer\">"
                                + "<p>Trabajo en grupo realizado por el grupo 3TI11_L1 en el curso DEW 2022/2023</p>"
                                + "</div>");
                out.println("</body></html>");
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException {
                // TODO Auto-generated method stub
                doGet(request, response);
        }
}
