package Servlets;

import static jakarta.servlet.http.HttpServletResponse.*;

import Dao.PostDao;
import Models.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;



public class PostServlet extends HttpServlet {
    PostDao dao = new PostDao();

    protected void processResponse(HttpServletResponse resp) {
        resp.setContentType("application/xml; charset=UTF-8");
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

        List<Post> posts = dao.getAll();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        PrintWriter out = response.getWriter();

        for (Post post : posts) {
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
        Post post = gson.fromJson(request.getReader(), Post.class);
        dao.insert(post);
        int id = post.getId();

        if (id > 0) {
            response.setStatus(SC_CREATED);
            String object = gson.toJson(post);
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
        Post post = gson.fromJson(request.getReader(), Post.class);

        int status = dao.update(post);

        if (status > 0) {
            response.setStatus(SC_OK);
            String object = gson.toJson(post);
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
        Post post = gson.fromJson(request.getReader(), Post.class);
        int postId = post.getId();

        int status = dao.delete(postId);

        if (status > 0) {
            response.setStatus(SC_ACCEPTED);
            String object = gson.toJson(post);
            out.print(object);
        } else {
            response.setStatus(SC_FORBIDDEN);
        }
    }
}
