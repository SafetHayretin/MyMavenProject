package CRUD;

import Model.Post;
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

public class UpdatePost extends HttpServlet {

    private SqlSession createSession() throws IOException {
        Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession session = sqlSessionFactory.openSession();
        return session;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        SqlSession session = createSession();

        PrintWriter out = resp.getWriter();

        String id = req.getParameter("id");
        String body = req.getParameter("body");
        String title = req.getParameter("title");
        String userid = req.getParameter("userId");

        Post post = new Post(id, body, title, userid);
        int status = session.update("Post.update", post);

        if (status > 0) {
            out.print(" <p>Record saved successfully!</p> ");
        } else {
            out.println("Unable to save record");
        }

        out.close();
    }

}
