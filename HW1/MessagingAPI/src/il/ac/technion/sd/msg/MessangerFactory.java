package il.ac.technion.sd.msg;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows creating new messengers
 */
public class MessangerFactory {
	List<Messenger> ms = new ArrayList<>();

	/**
	 * Creates a new messenger and starts it
	 * @return A new messenger
	 */
	public Messenger start(String address) throws MessageException {
		return MessangerImpl.createInstance(address);
	}

	Messenger startAndAddToList() throws Exception {
		MessangerImpl $ = MessangerImpl.createInstance(ms.size() + "");
		ms.add($);
		return $;
	}
}
