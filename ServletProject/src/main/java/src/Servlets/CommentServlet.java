package src.Servlets;

import src.Dao.CommentDao;
import src.Models.Comment;

import src.Tools.QuerySplitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import http.*;
//import jakarta.servlet.http.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jakarta.servlet.http.HttpServletResponse.*;

public class CommentServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(CommentServlet.class.getName());

    private static final Pattern COMMENT_PATTERN_WITH_POST_ID = Pattern.compile("/comments\\?postid=(\\d)+");

    private static final Pattern COMMENT_PATTERN_WITH_ID = Pattern.compile("/comments/(\\d)+");

    private static final Pattern ALL_COMMENT_PATTERN = Pattern.compile("/comments");

    protected CommentDao dao = new CommentDao();

    protected void processResponse(HttpServletResponse resp) {
        resp.setContentType("application/json; charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        processResponse(response);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();

        String uri = request.getRequestURI();
        String query = request.getQueryString();
        Matcher matcher = COMMENT_PATTERN_WITH_POST_ID.matcher(uri + "?" + query);
        // /comments?postid=1
        if (matcher.matches()) {
            Map<String, String> splitQuery = QuerySplitter.splitQuery(query);
            Integer id = Integer.valueOf(splitQuery.get("postid"));
            List<Comment> comment = dao.getByPostId(id);
            String json = gson.toJson(comment);
            out.println(json);
            return;
        }
        String[] slpit = uri.split("/");
        // /comments/1
        matcher = COMMENT_PATTERN_WITH_ID.matcher(uri);
        if (matcher.matches()) {
            Integer id = Integer.valueOf(slpit[2]);
            Comment comment = dao.getById(id);
            String jsonString = gson.toJson(comment);
            out.println(jsonString);

            return;
        }
        // /comments
        matcher = ALL_COMMENT_PATTERN.matcher(uri);
        if (matcher.matches()) {
            List<Comment> comments = dao.getAll();
            String jsonString = gson.toJson(comments);
            out.println(jsonString);

            return;
        }

        response.setStatus(SC_FORBIDDEN);
        LOGGER.error("Forbidden");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        processResponse(response);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Comment comment = gson.fromJson(request.getReader(), Comment.class);

        dao.insert(comment);
        int id = comment.getId();

        if (id == 1) {
            response.setStatus(SC_CREATED);
            String object = gson.toJson(comment);
            out.print(object);
        } else {
            response.setStatus(SC_FORBIDDEN);
            LOGGER.error("Forbidden");
        }

        out.close();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getRequestURI();
        Matcher matcher = COMMENT_PATTERN_WITH_ID.matcher(pathInfo); // comments/1

        PrintWriter out = response.getWriter();
        if (!matcher.matches()) {
            response.setStatus(SC_FORBIDDEN);
            out.println("forbidden");
            LOGGER.error("Forbidden");
            return;
        }

        processResponse(response);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Comment comment = gson.fromJson(request.getReader(), Comment.class);

        int id = Integer.parseInt(matcher.group(1));
        comment.setId(id);

        int status = dao.update(comment);

        if (status == 1) {
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
        String pathInfo = request.getRequestURI();
        Matcher matcher = COMMENT_PATTERN_WITH_ID.matcher(pathInfo);

        PrintWriter out = response.getWriter();
        if (!matcher.matches()) {
            response.setStatus(SC_FORBIDDEN);
            out.println("forbidden");
            return;
        }
        processResponse(response);

        Integer id = Integer.valueOf(matcher.group(1));

        int status = dao.delete(id);

        if (status == 1) {
            response.setStatus(SC_ACCEPTED);
            out.println("updated");
        } else {
            response.setStatus(SC_FORBIDDEN);
            out.println("forbidden");
            LOGGER.error("Forbidden");
        }
    }
}
