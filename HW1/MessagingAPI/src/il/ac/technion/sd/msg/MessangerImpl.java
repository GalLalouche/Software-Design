package il.ac.technion.sd.msg;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class MessangerImpl implements MessangerAux, Messenger {
	private final String address;

	public MessangerImpl(String address) {
		if (address.matches("\\s*"))
			throw new IllegalArgumentException("Address cannot be whitespace");
		this.address = address;
	}

	private final BlockingQueue<byte[]> q = new LinkedBlockingQueue<>();

	private String getRmiAddress() {
		return getRmiAddresOf(this.address + "");
	}

	private static String getRmiAddresOf(String s) {
		return "localhost/" + s;
	}

	static {
		try {
			LocateRegistry.createRegistry(1099);
		} catch (RemoteException e) {
			throw new AssertionError(e);
		}
	}

	public static MessangerImpl createInstance(String address) throws MessageException {
		try {
			MessangerImpl $ = new MessangerImpl(address);
			Remote stub = UnicastRemoteObject.exportObject($, 1099);
			LocateRegistry.getRegistry().bind($.getRmiAddress(), stub);
			return $;
		} catch (RuntimeException e) {
			throw e;
		} catch(Exception e) {
			throw new MessageException(e);
		}
	}

	@Override
	public void kill() throws MessageException {
		try {
			LocateRegistry.getRegistry().unbind(getRmiAddress());
		} catch (Exception e) {
			throw new MessageException(e);
		}
	}

	@Override
	public void send(String address, byte[] payload) throws MessageException {
		try {
			((MessangerAux) (LocateRegistry.getRegistry()
					.lookup(getRmiAddresOf(address)))).auxSend(payload);
		} catch (Exception e) {
			throw new MessageException(e);
		}
	}

	@Override
	public void auxSend(byte[] payload) {
		q.add(payload);
	}

	@Override
	public byte[] listen() throws MessageException {
		try {
			return q.take();
		} catch (InterruptedException e) {
			throw new MessageException(e);
		}
	}

	@Override
	public Optional<byte[]> tryListen() throws MessageException {
		return Optional.ofNullable(q.poll());
	}

	@Override
	public String getAddress() {
		return address + "";
	}
}
