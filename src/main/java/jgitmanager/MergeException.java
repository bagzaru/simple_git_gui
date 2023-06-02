package jgitmanager;

// merge중 충돌이 났을 때의 exception
public class MergeException extends Exception {
    public MergeException() {   }
    public MergeException(String message) {
        super(message);
    }
}
