package CRUD;

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

public class DeletePost extends HttpServlet {
    private SqlSession createSession() throws IOException {
        Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession session = sqlSessionFactory.openSession();
        return session;
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SqlSession session = createSession();
        processRequest(request, response);

        PrintWriter out = response.getWriter();

        String userId = request.getParameter("id");
        int id = Integer.parseInt(userId);
        int status = session.delete("Post.deleteById", id);

        if (status > 0) {
            out.print(" <p>Record deleted successfully!</p> ");
        } else {
            out.println("Sorry! unable to save record");
        }
    }
}
