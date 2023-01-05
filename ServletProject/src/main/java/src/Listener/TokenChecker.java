package src.Listener;

import src.Dao.TokenDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class TokenChecker extends TimerTask {
    private final TokenDao dao = new TokenDao();

    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd");

        Date currentDate = new Date();
        dao.deleteExpiredTokens(currentDate);
    }
}