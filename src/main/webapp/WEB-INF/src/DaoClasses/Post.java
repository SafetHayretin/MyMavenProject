package DaoClasses;

public class Post {
    int id;

    String body;

    String title;

    int userId;

    public Post(int id, String body, String title, int userId) {
        this.id = id;
        this.body = body;
        this.title = title;
        this.userId = userId;
    }
}
