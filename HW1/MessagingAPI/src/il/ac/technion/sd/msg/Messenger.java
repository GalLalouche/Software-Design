package il.ac.technion.sd.msg;

import java.util.Optional;

/**
 * Allows passing messages between two different, possibly distributed, java applications.
 * Each messenger has an address (checked using {@link Messenger#getAddress()}),
 * that can be used to send it messages using {@link Messenger#send(String, byte[])}.
 * <br/>
 * Messages can be retrieved using {@link Messenger#listen()} 
 * (busy wait) or {@link Messenger#tryListen()}, which returns immediately.
 * <br/>
 * Messengers that are no longer needed should be discarded with {@link Messenger#kill()}.
 */
public interface Messenger {

	/**
	 * Stops the messenger; 
	 * Killed messengers cannot accept new messages,
	 * but it is possible to create a new messenger with the same address
	 */
	void kill() throws MessageException;

	/**
	 * @return the address of messenger
	 */
	String getAddress();

	/**
	 * sends a message to another messenger
	 * 
	 * @param to the address of the recipient of the message
	 * @param payload the message's content
	 * @see Messenger#getAddress()
	 */
	void send(String to, byte[] payload) throws MessageException;

	/**
	 * retrieves and removes a sent message; messages are retrieved in a FIFO
	 * fashion. This is a <b>blocking</b> call, i.e., it will not return until a
	 * message has been intercepted.
	 * 
	 * @return the earliest unretrieved message
	 */
	byte[] listen() throws MessageException;

	/**
	 * retrieves a and removes a sent message; messages are retrieved in a FIFO
	 * fashion.
	 * 
	 * @return an {@link Optional} of the earliest unretrieved message
	 */
	Optional<byte[]> tryListen() throws MessageException;
}
