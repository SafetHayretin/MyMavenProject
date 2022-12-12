package Servlets;

import Dao.TokenDao;
import Dao.UserDao;
import Models.Token;
import Models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@MultipartConfig
public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(LoginServlet.class.getName());
    UserDao userDao = new UserDao();

    TokenDao tokenDao = new TokenDao();

    List<Token> tokens;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {


        Part username = request.getPart("name");
        Part password = request.getPart("password");

        String usernameStr = new BufferedReader(new InputStreamReader(username.getInputStream())).readLine();
        String passwordStr = new BufferedReader(new InputStreamReader(password.getInputStream())).readLine();

        User user = userDao.getCredentials(usernameStr);
        PrintWriter out = response.getWriter();
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            out.println("forbidden");
            LOGGER.error("Unable to login");
            return;
        }


        int salt = user.getSalt();
        String encryptedPassword = DigestUtils.sha1Hex(passwordStr + salt);

        if (encryptedPassword.equals(user.getPassword())) {
            tokens = tokenDao.getAll();
            Token token = getToken(tokens, user.getId());

            if (token == null) {
                token = createToken(user);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.println("Authorization: " + token);
            out.println("Welcome " + usernameStr);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            out.println("forbidden");
            LOGGER.error("Unable to login");
        }
    }

    private Token createToken(User user) {
        int userid = user.getId();
        Token token = new Token();

        token.setUserId(userid);
        Date date = new Date(System.currentTimeMillis());
        LocalDate monthLate = date.toLocalDate().plusMonths(1);
        Date expireDate = Date.valueOf(monthLate);

        token.setCreatedDate(new Date(System.currentTimeMillis()));
        token.setExpirationDate(expireDate);

        tokenDao.insert(token);
        return token;
    }

    private Token getToken(List<Token> tokens, int userId) {
        for (Token token : tokens) {
            if (token.getUserId() == userId)
                return token;
        }
        return null;
    }
}
