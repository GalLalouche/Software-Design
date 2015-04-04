package il.ac.technion.sd.msg;

/**
 * A wrapper of exceptions thrown by the underlying messaging mechanism
 */
public class MessageException extends Exception {
	public MessageException(Throwable cause) {
		super(cause);
	}
}
