package Listener;

import Dao.TokenDao;
import Models.Token;

import java.sql.Date;

import java.util.List;

public class TokenChecker implements Runnable{
    private final TokenDao dao = new TokenDao();

    private List<Token> tokens;

    @Override
    public void run() {
        tokens = dao.getAll();
        Date currentDate = new java.sql.Date(System.currentTimeMillis());
        for (Token token : tokens) {
            if (currentDate.compareTo(token.getExpirationDate()) > 0)
                dao.delete(token.getId());
        }
    }
}
