
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class MenuAlumno extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public MenuAlumno() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // CAPTURAMOS LA SESION ACTUAL DEL CLIENTE
        HttpSession session = request.getSession();

        // CREAMOS NUEVO ALMACEN DE COOKIES
        BasicCookieStore cookies = new BasicCookieStore();
        // OBTENEMOS LISTA DE COOKIES ANTERIORES
        BasicCookieStore cookies_lts = (BasicCookieStore) session.getAttribute("cookies");
        List<Cookie> cookielist = cookies_lts.getCookies();

        // CARGAMOS VARIABLES DE SESION, TODAS LAS COOKIES ANTERIORES EN LA NUEVA
        // VARIABLE
        // REVISAR METODO COOKIES, PUEDE NO TENER SETNIDO ESTE PROCESO
        String key = (String) session.getAttribute("key");
        String user = (String) session.getAttribute("usr");
        for (Cookie cookie : cookielist) {
            cookies.addCookie(cookie);
        }

        // OBJETOS
        final CloseableHttpClient cliente = HttpClients.custom().setDefaultCookieStore(cookies).build();

        // ------------------------------------------------------
        // METODO DE CLIENTE GUARDADO EN SESION
        // CloseableHttpClient cliente = (CloseableHttpClient)
        // session.getAttribute("cliente");
        // ------------------------------------------------------

        // TIPO DE CONTENIDO
        response.setContentType("text/html");
        // OBJETO PRINTLN
        PrintWriter out = response.getWriter();

        // SOLICITUD
        // URL
        String milogin = request.getLocalName();
        String dir = "http://" + milogin + ":9090/CentroEducativo/alumnos/" + user + "?key=" + key;
        out.println(key);
        out.println(dir);

        // GET
        HttpGet get = new HttpGet(dir);

        // EJECUTO LA SOLICITUD
        final CloseableHttpResponse execute = cliente.execute(get);

        // OBTENEMOS INFO
        String info;
        try {
            info = EntityUtils.toString(execute.getEntity());
            // COMPROBACION
            out.println(info);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        // HTML
        out.println("<!DOCTYPE html>");
        out.println("<html lang='es-es'>");
        out.println("");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>Notas OnLine</title>");
        out.println(
                "    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'");
        out.println(
                "        integrity='sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM' crossorigin='anonymous'>");
        out.println("    <script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'");
        out.println("        integrity='sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz'");
        out.println("        crossorigin='anonymous'></script>");
        out.println("</head>");
        out.println("");
        out.println("<body style='background-color: whitesmoke;'>");
        out.println("    <div class='container'>");
        out.println(
                "        <div class='row mb-10 row no-gutters overflow-hidden flex-md-row mb-4 h-md-250 w-md-100 position-relative '");
        out.println("            style='background: linear-gradient(to right, black, white)'>");
        out.println("            <h1 class='display-4' style='color: white'>Men&uacute; de Alumno</h1>");
        out.println(
                "            <h2 class='lead my-3' style='color: white'>Esta p&aacute;gina muestra las asignaturas en las que");
        out.println("                nombre_alumno apellido_alumno est&aacute; matriculad@.</h2>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("    <div class='container'>");
        out.println("        <div class='row mb-2'>");
        out.println("            <div class='col-md-12'>");
        out.println(
                "                <div class='row no-gutters overflow-hidden flex-md-row mb-4 shadow-sm  h-md-250 w-md-100 position-relative'");
        out.println("                    style='background-color: white;'>");
        out.println("                    <div class='col p-4 d-flex flex-column position-static'>");
        out.println("");
        out.println("                        <form action='detail' method='GET'>");
        out.println("                            <input type='hidden' name='asig' value=acronimo>");
        out.println("                            <input type='hidden' name='dni' value=dni>");
        out.println("                            <input type='hidden' name='nombre' value=ombre_alumno>");
        out.println("                            <input type='hidden' name='apellido' value=apellido_alumno>");
        out.println("                            <button type='submit' class='btn btn-link'>Asignaturas</button>");
        out.println("                        </form>");
        out.println("                    </div>");
        out.println("                </div>");
        out.println("            </div>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("    <div class='container'>");
        out.println("        <div>");
        out.println(
                "            <button type='button' class='btn btn-dark ' data-bs-toggle='modal' data-bs-target='#cerrarsesion'>");
        out.println("                Cerrar Sesi&oacute;n");
        out.println("            </button>");
        out.println("        </div>");
        out.println("        <div class='dropdown-divider mt-4'></div>");
        out.println("        <div class='container-fluid text-center footer'>");
        out.println(
                "            <p>Trabajo en grupo realizado para la asignatura Desarrollo Web | Curso 2022-2023</p>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("");
        out.println("    <!-- Modal -->");
        out.println(
                "    <div class='modal fade' id='cerrarsesion' data-bs-backdrop='static' data-bs-keyboard='false' tabindex='-1'");
        out.println("        aria-labelledby='staticBackdropLabel' aria-hidden='true'>");
        out.println("        <div class='modal-dialog modal-dialog-centered'>");
        out.println("            <div class='modal-content'>");
        out.println("                <div class='modal-header'>");
        out.println(
                "                    <h1 class='modal-title fs-5' id='staticBackdropLabel'>Cerrar Sesi&oacute;n</h1>");
        out.println(
                "                    <button type='button' class='btn-close' data-bs-dismiss='modal' aria-label='Close'></button>");
        out.println("                </div>");
        out.println("                <div class='modal-body'>");
        out.println("                    Estas seguro de que quieres salir de tu cuenta.");
        out.println("                </div>");
        out.println("                <div class='modal-footer'>");
        out.println(
                "                    <button type='button' class='btn btn-secondary' data-bs-dismiss='modal'>Cancelar</button>");
        out.println("                    <form action='logout'>");
        out.println(
                "                        <button type='submit' class='btn btn-danger' id='logout'>Cerrar Sesi&oacute;n</button>");
        out.println("                    </form>");
        out.println("                </div>");
        out.println("            </div>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("");
        out.println("</body>");
        out.println("");
        out.println("</html>");
        // HTML

        // CERRAMOS RESPUESTA Y CLIENTE
        execute.close();
        cliente.close();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
