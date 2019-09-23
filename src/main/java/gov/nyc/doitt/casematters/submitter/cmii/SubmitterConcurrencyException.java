package gov.nyc.doitt.casematters.submitter.cmii;

public class SubmitterConcurrencyException extends RuntimeException {

	static final long serialVersionUID = -1L;

	public SubmitterConcurrencyException() {
		super();
	}

	public SubmitterConcurrencyException(String message) {
		super(message);
	}

	public SubmitterConcurrencyException(String message, Throwable cause) {
		super(message, cause);
	}

	public SubmitterConcurrencyException(Throwable cause) {
		super(cause);
	}
}