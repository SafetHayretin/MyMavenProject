package Servlets;

import Models.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
        Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession session = sqlSessionFactory.openSession();

        return session;
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        List<Post> posts;
        try (SqlSession session = createSession()) {
            posts = session.selectList("Comments.getAll");
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        PrintWriter out = resp.getWriter();
        for (Post post : posts) {
            String jsonString = gson.toJson(post);
            out.println(jsonString);
        }
    }
}
