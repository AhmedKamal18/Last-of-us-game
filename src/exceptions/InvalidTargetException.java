package exceptions;

public class InvalidTargetException extends GameActionException {

	public InvalidTargetException() {
		// TODO Auto-generated constructor stub
	}
	public InvalidTargetException(String message) {
		super(message);
	}
	public String getMessage() {
	    // TODO Auto-generated method stub
	    return "Invalid Target!";
	  }
}
