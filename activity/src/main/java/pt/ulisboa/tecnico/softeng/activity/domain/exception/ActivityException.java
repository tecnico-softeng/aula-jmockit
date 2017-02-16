package pt.ulisboa.tecnico.softeng.activity.domain.exception;

public class ActivityException extends RuntimeException {
	public ActivityException() {
		super();
	}

	public ActivityException(String message) {
		super(message);
	}
}
