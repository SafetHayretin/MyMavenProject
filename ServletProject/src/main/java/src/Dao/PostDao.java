package src.Dao;

import src.Models.Post;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class PostDao {
    SqlSession session;

    public PostDao() {
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

    public List<Post> getAll() {
        List<Post> posts = session.selectList("Models.Post.getAll");
        session.commit();

        return posts;
    }

    public Post get(Integer id) {
        Post post = session.selectOne("Models.Post.selectById", id);
        session.commit();

        return post;
    }

    public int insert(Post post) {
        int id = session.insert("Models.Post.insert", post);
        session.commit();

        return id;
    }

    public int update(Post post) {
        int id = session.update("Models.Post.update", post);
        session.commit();

        return id;
    }

    public Integer delete(Integer id) {
        int status = session.delete("Models.Post.deleteById", id);
        session.commit();

        return status;
    }
}
