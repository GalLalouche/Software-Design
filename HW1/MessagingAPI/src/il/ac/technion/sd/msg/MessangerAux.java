package il.ac.technion.sd.msg;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface MessangerAux extends Remote {
	void auxSend(byte[] payload) throws RemoteException;
}
