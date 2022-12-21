package Servlets;

import Dao.CommentDao;
import Dao.PostDao;
import Models.Comment;
import Models.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jakarta.servlet.http.HttpServletResponse.*;


public class PostServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(PostServlet.class.getName());

    private static final Pattern POST_PATTERN_WITH_ID = Pattern.compile("/posts/(\\d)+");

    private static final Pattern COMMENTS_PATTERN_WITH_POST_ID = Pattern.compile("/posts/(\\d)+/comments");

    private static final Pattern ALL_POST_PATTERN = Pattern.compile("/posts");

    PostDao dao = new PostDao();

    protected void processResponse(HttpServletResponse resp) {
        resp.setContentType("application/json; charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        processResponse(response);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();

        String pathInfo = request.getRequestURI();
        Matcher matcher = POST_PATTERN_WITH_ID.matcher(pathInfo);
        String[] slpit = pathInfo.split("/");
        // posts/1
        if (matcher.matches()) {
            Integer id = Integer.valueOf(slpit[2]);
            Post post = dao.get(id);
            String json = gson.toJson(post);
            out.println(json);
            return;
        }
        // /posts
        matcher = ALL_POST_PATTERN.matcher(pathInfo);
        if (matcher.matches()) {
            List<Post> posts = dao.getAll();
            for (Post post : posts) {
                String json = gson.toJson(post);
                out.println(json);
            }
            return;
        }

        //posts/1/comments
        matcher = COMMENTS_PATTERN_WITH_POST_ID.matcher(pathInfo);
        if (matcher.matches()) {
            CommentDao dao = new CommentDao();
            Integer id = Integer.valueOf(slpit[2]);
            List<Comment> comment = dao.getByPostId(id);
            String json = gson.toJson(comment);
            out.println(json);

            return;
        }

        response.setStatus(SC_FORBIDDEN);
        out.println("forbidden");
        LOGGER.error("Unable to get post");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        processResponse(response);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Post post = gson.fromJson(request.getReader(), Post.class);

        dao.insert(post);
        int id = post.getId();

        if (id == 1) {
            response.setStatus(SC_CREATED);
            String object = gson.toJson(post);
            out.print(object);
        } else {
            response.setStatus(SC_FORBIDDEN);
            LOGGER.error("Unable to add post");
        }

        out.close();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getRequestURI();
        Matcher matcher = POST_PATTERN_WITH_ID.matcher(pathInfo); // posts/1

        PrintWriter out = response.getWriter();
        if (!matcher.matches()) {
            response.setStatus(SC_FORBIDDEN);
            out.println("forbidden");
            return;
        }

        processResponse(response);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Post post = gson.fromJson(request.getReader(), Post.class);

        int id = Integer.parseInt(matcher.group(1));
        post.setId(id);

        int status = dao.update(post);

        if (status == 1) {
            response.setStatus(SC_OK);
            String object = gson.toJson(post);
            out.print(object);
        } else {
            response.setStatus(SC_FORBIDDEN);
            LOGGER.error("Unable to update post");
        }

        out.close();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getRequestURI();
        Matcher matcher = POST_PATTERN_WITH_ID.matcher(pathInfo); // posts/1

        PrintWriter out = response.getWriter();
        if (!matcher.matches()) {
            response.setStatus(SC_FORBIDDEN);
            out.println("forbidden");
            LOGGER.error("Incorrect URL");
            return;
        }
        processResponse(response);

        Integer postId = Integer.valueOf(matcher.group(1));

        int status = dao.delete(postId);

        if (status == 1) {
            response.setStatus(SC_ACCEPTED);
            out.println("deleted");
        } else {
            response.setStatus(SC_FORBIDDEN);
            out.println("forbidden");
            LOGGER.error("Unable to delete post");
        }
    }
}
