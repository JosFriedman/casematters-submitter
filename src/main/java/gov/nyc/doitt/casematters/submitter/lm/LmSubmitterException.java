package gov.nyc.doitt.casematters.submitter.lm;

public class LmSubmitterException extends RuntimeException {

	static final long serialVersionUID = -1L;

	public LmSubmitterException() {
		super();
	}

	public LmSubmitterException(String message) {
		super(message);
	}

	public LmSubmitterException(String message, Throwable cause) {
		super(message, cause);
	}

	public LmSubmitterException(Throwable cause) {
		super(cause);
	}
}