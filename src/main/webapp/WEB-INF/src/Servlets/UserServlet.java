package Servlets;

import static jakarta.servlet.http.HttpServletResponse.*;

import Dao.UserDao;
import Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UserServlet extends HttpServlet {
    UserDao dao = new UserDao();

    protected void processResponse(HttpServletResponse resp) {
        resp.setContentType("application/xml;charset=UTF-8");
    }

    protected void checkAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            PrintWriter out = response.getWriter();
            response.setStatus(SC_UNAUTHORIZED);
            out.println("forbidden");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        checkAuthentication(request, response);

        processResponse(response);

        List<User> users = dao.getAll();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        PrintWriter out = response.getWriter();

        for (User user : users) {
            String jsonString = gson.toJson(user);
            out.println(jsonString);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        checkAuthentication(request, response);

        processResponse(response);

        PrintWriter out = response.getWriter();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        User user = gson.fromJson(request.getReader(), User.class);
        encryptPassword(user);

        dao.insert(user);
        int id = user.getId();

        if (id > 0) {
            response.setStatus(SC_CREATED);
            String object = gson.toJson(user);
            out.print(object);
        } else {
            response.setStatus(SC_FORBIDDEN);
        }

        out.close();
    }

    private void encryptPassword(User user) {
        String password = user.getPassword();
        if (password != null) {
            String encryptedPassword = DigestUtils.sha1Hex(password);
            user.setPassword(encryptedPassword);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        checkAuthentication(request, response);

        processResponse(response);

        PrintWriter out = response.getWriter();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        User user = gson.fromJson(request.getReader(), User.class);
        encryptPassword(user);

        int status = dao.update(user);

        if (status > 0) {
            response.setStatus(SC_OK);
            String object = gson.toJson(user);
            out.print(object);
        } else {
            response.setStatus(SC_FORBIDDEN);
        }

        out.close();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        checkAuthentication(request, response);

        processResponse(response);

        PrintWriter out = response.getWriter();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        User user = gson.fromJson(request.getReader(), User.class);
        int postId = user.getId();

        int status = dao.delete(postId);


        if (status > 0) {
            response.setStatus(SC_ACCEPTED);
            String object = gson.toJson(user);
            out.println(object);
        } else {
            response.setStatus(SC_FORBIDDEN);
        }
    }
}
