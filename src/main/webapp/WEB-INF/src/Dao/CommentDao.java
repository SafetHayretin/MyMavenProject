package Dao;

import Models.Comment;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class CommentDao {
    SqlSession session;

    public CommentDao() {
        this.session = createSession();
    }

    private SqlSession createSession() {
        SqlSession session = null;
        try {
            Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            session = sqlSessionFactory.openSession();
        } catch (IOException e) {
            System.out.println("Unable to create session");
        }

        return session;
    }

    public List<Comment> getAll() {
        List<Comment> comments = session.selectList("Models.Comment.getAll");
        session.commit();

        return comments;
    }

    public List<Comment> getByPostId(Integer id) {
        List<Comment> comment = session.selectList("Models.Comment.selectByPostId", id);
        session.commit();

        return comment;
    }

    public Comment getById(Integer id) {
        Comment comment = session.selectOne("Models.Comment.selectById", id);
        session.commit();

        return comment;
    }

    public int insert(Comment comment) {
        int id = session.insert("Models.Comment.insert", comment);
        session.commit();

        return id;
    }

    public int update(Comment comment) {
        int id = session.update("Models.Comment.update", comment);
        session.commit();

        return id;
    }

    public Integer delete(Integer id) {
        int status = session.delete("Models.Comment.deleteById", id);
        session.commit();

        return status;
    }
}
