package Exceptions;

public class MissingIndexFile extends RuntimeException{
    public MissingIndexFile(String msg) {
        super(msg);
    }
}
