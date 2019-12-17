package gov.nyc.doitt.casematters.submitter;

public class JobFlowManagerException extends RuntimeException {

	static final long serialVersionUID = -1L;

	public JobFlowManagerException() {
		super();
	}

	public JobFlowManagerException(String message) {
		super(message);
	}

	public JobFlowManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public JobFlowManagerException(Throwable cause) {
		super(cause);
	}
}