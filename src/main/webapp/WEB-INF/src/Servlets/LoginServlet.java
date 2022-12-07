package Servlets;

import Dao.UserDao;
import Models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

@MultipartConfig
public class LoginServlet extends HttpServlet {
    UserDao dao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Part username = request.getPart("name");
        String usernameStr = new BufferedReader(new InputStreamReader(username.getInputStream())).readLine();

        Part password = request.getPart("password");
        String passwordStr = new BufferedReader(new InputStreamReader(password.getInputStream())).readLine();

        User user = dao.getCredentials(usernameStr);

        int salt = user.getSalt();
        String encryptedPassword = DigestUtils.sha1Hex(passwordStr + salt);

        PrintWriter out = response.getWriter();
        if (encryptedPassword.equals(user.getPassword())) {
            HttpSession session = request.getSession();
            session.setAttribute("user", usernameStr);
            out.println("Welcome " + usernameStr);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            out.println("forbidden");
        }
    }
}
