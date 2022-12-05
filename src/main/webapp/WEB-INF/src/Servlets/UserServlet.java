package Servlets;

import Models.Post;
import Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.List;

public class UserServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("text/html;charset=UTF-8");
    }

    private SqlSession createSession() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession session = sqlSessionFactory.openSession();

        return session;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        List<User> users;
        try (SqlSession session = createSession()) {
            users = session.selectList("Models.User.getAll");
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        PrintWriter out = resp.getWriter();

        for (User user : users) {
            String jsonString = gson.toJson(user);
            System.out.println(jsonString);
            out.println(jsonString);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        SqlSession session = createSession();
        processRequest(req, resp);

        PrintWriter out = resp.getWriter();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        User user = gson.fromJson(req.getReader(), User.class);

        int id = session.insert("Models.User.insert", user);

        if (id > 0) {
            out.print("Record saved successfully! id = " + id);
            req.getRequestDispatcher("index.html").include(req, resp);
        } else {
            out.println("Sorry! unable to save record");
        }

        out.close();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        SqlSession session = createSession();

        PrintWriter out = resp.getWriter();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        User user = gson.fromJson(req.getReader(), User.class);

        int status = session.update("Models.Post.update", user);

        if (status > 0) {
            out.print(" <p>Record updated successfully!</p> ");
        } else {
            out.println("Unable to save record");
        }

        out.close();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out;
        int status;
        try (SqlSession session = createSession()) {
            processRequest(request, response);

            out = response.getWriter();

            String userId = request.getParameter("id");
            int id = Integer.parseInt(userId);
            status = session.delete("Models.User.deleteById", id);
        }

        if (status > 0) {
            out.print(" <p>Record deleted successfully!</p> ");
        } else {
            out.println("Sorry! unable to save record");
        }
    }
}
