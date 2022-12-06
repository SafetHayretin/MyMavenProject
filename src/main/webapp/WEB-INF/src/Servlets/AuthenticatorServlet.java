package Servlets;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_1;

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
public class AuthenticatorServlet extends HttpServlet {
    UserDao dao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Part username = request.getPart("name");
        String usernameStr = new BufferedReader(new InputStreamReader(username.getInputStream())).readLine();

        Part password = request.getPart("password");
        String passwordStr = new BufferedReader(new InputStreamReader(password.getInputStream())).readLine();
        String encryptedPassword = DigestUtils.sha1Hex(passwordStr);

        User user = dao.getCredentials(usernameStr);
        PrintWriter out = response.getWriter();

        if (encryptedPassword.equals(user.getPassword())) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            out.println("Welcome " + usernameStr);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            out.println("forbidden");
        }
    }
}
