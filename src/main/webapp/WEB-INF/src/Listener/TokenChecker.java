package Listener;

import Dao.TokenDao;
import Models.Token;

import java.sql.Date;

import java.util.List;
import java.util.TimerTask;

public class TokenChecker extends TimerTask {
    private final TokenDao dao = new TokenDao();

    private List<Token> tokens;

    @Override
    public void run() {
        Date currentDate = new java.sql.Date(System.currentTimeMillis());
        tokens = dao.getExpiredTokens(currentDate);

        for (Token token : tokens) {
            dao.delete(token.getToken());
        }
    }
}
