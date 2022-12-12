package Dao;

import Models.Token;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class TokenDao {
    SqlSession session;

    public TokenDao() {
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

    public List<Token> getAll() {
        List<Token> tokens = session.selectList("Models.Token.getAll");
        session.commit();

        return tokens;
    }

    public int insert(Token token) {
        int id = session.insert("Models.Token.insert", token);
        session.commit();

        return id;
    }
    public Integer delete(Integer id) {
        int status = session.delete("Models.Token.deleteById", id);
        session.commit();

        return status;
    }
}
