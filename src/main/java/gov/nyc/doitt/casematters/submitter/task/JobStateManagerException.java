package gov.nyc.doitt.casematters.submitter.task;

public class JobStateManagerException extends RuntimeException {

	static final long serialVersionUID = -1L;

	public JobStateManagerException() {
		super();
	}

	public JobStateManagerException(String message) {
		super(message);
	}

	public JobStateManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public JobStateManagerException(Throwable cause) {
		super(cause);
	}
}