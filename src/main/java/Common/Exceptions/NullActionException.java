package Common.Exceptions;

public class NullActionException extends RuntimeException {
    public NullActionException(){
        super("The given action is null!");
    }

    public NullActionException(String message){
        super(message);
    }
}
