

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;

public class Autenticate implements Filter {
	
	// Creacion del HashMap
	Map<String, String[]> users = new HashMap<>();
    public Autenticate() {
        super();
        // TODO Auto-generated constructor stub
    }

	public void destroy() {
		// TODO Auto-generated method stub
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String [] temp = {"", ""};
		BasicCookieStore cookies = new BasicCookieStore();
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		String user = req.getRemoteUser();
		String pass = req.getHeader("Authorization").substring(6);
		byte[] decodedBytes = Base64.getDecoder().decode(pass);
		String passDecoded = new String(decodedBytes).substring(10);
		
		if(!users.containsKey(user)) {
			temp[0] = user;
			temp[1] = passDecoded;
			users.put(user, temp);
		}

		String user_value = users.get(user)[0];
		String pass_value = users.get(user)[1];
		
        if(session.isNew() || session.getAttribute("key") == null) {
            session.setAttribute("dni", user_value);
            session.setAttribute("pass", pass_value);
            JSONObject json = new JSONObject();
            json.put("dni", user_value);
            json.put("password", pass_value);
            StringEntity entity = new StringEntity(json.toString());
            String url = request.getLocalName();
            
            try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookies).build()) {
                HttpPost post = new HttpPost("http://" + url + ":9090/CentroEducativo/login");
                post.setEntity(entity);
                post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                CloseableHttpResponse resp = httpclient.execute(post);
                session.setAttribute("cookie", cookies.getCookies());
                String respuesta = EntityUtils.toString(resp.getEntity());                              
                httpclient.close();
                session.setAttribute("key", respuesta);
            }           
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
		chain.doFilter(request, response);
	}


	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
	
}
