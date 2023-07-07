package exceptions;

public class MovementException extends GameActionException {

	public MovementException() {
		// TODO Auto-generated constructor stub
	}
	public MovementException(String message) {
		super(message);
	}
	
	public String getMessage() {
	    // TODO Auto-generated method stub
	    return "Invalid Move!";
	  }
}
