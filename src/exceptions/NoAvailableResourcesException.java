package exceptions;

public class NoAvailableResourcesException extends GameActionException {

	public NoAvailableResourcesException() {
		// TODO Auto-generated constructor stub
	}
	
	public NoAvailableResourcesException(String message) {
		super(message);
	}
	
	public String getMessage() {
	    // TODO Auto-generated method stub
	    return "Inventory is Empty!";
	  }

}
