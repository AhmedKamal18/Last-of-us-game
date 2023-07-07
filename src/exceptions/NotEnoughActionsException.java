package exceptions;

public class NotEnoughActionsException extends GameActionException {

	public NotEnoughActionsException() {
		// TODO Auto-generated constructor stub
	}
	
	public NotEnoughActionsException(String message) {
		super(message);
	}
	
	public String getMessage() {
	    // TODO Auto-generated method stub
	    return "No Enough Actions!";
	  }

}
