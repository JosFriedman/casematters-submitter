package gov.nyc.doitt.casematters.submitter.cmii;

public class CmiiSubmitterException extends RuntimeException {

	static final long serialVersionUID = -1L;

	public CmiiSubmitterException() {
		super();
	}

	public CmiiSubmitterException(String message) {
		super(message);
	}

	public CmiiSubmitterException(String message, Throwable cause) {
		super(message, cause);
	}

	public CmiiSubmitterException(Throwable cause) {
		super(cause);
	}
}