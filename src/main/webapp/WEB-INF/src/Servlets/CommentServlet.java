package Servlets;

import static jakarta.servlet.http.HttpServletResponse.*;

import Dao.CommentDao;
import Models.Comment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CommentServlet extends HttpServlet {
    protected CommentDao dao = new CommentDao();

    protected void processResponse(HttpServletResponse resp) {
        resp.setContentType("application/xml;charset=UTF-8");
    }

    protected void checkAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            PrintWriter out = response.getWriter();
            response.setStatus(SC_FORBIDDEN);
            out.println("forbidden");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        checkAuthentication(request, response);

        processResponse(response);

        List<Comment> comments = dao.getAll();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        PrintWriter out = response.getWriter();

        for (Comment post : comments) {
            String jsonString = gson.toJson(post);
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
        Comment comment = gson.fromJson(request.getReader(), Comment.class);

        dao.insert(comment);
        int id = comment.getId();

        if (id > 0) {
            response.setStatus(SC_CREATED);
            String object = gson.toJson(comment);
            out.print(object);
        } else {
            response.setStatus(SC_FORBIDDEN);
        }

        out.close();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        checkAuthentication(request, response);

        processResponse(response);

        PrintWriter out = response.getWriter();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Comment comment = gson.fromJson(request.getReader(), Comment.class);

        int status = dao.update(comment);

        if (status > 0) {
            response.setStatus(SC_OK);
            String object = gson.toJson(comment);
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
        Comment comment = gson.fromJson(request.getReader(), Comment.class);

        int id = comment.getId();
        int status = dao.delete(id);

        if (status > 0) {
            response.setStatus(SC_ACCEPTED);
            String object = gson.toJson(comment);
            out.println(object);
        } else {
            response.setStatus(SC_FORBIDDEN);
            out.println("Unable to save record");
        }
    }
}
