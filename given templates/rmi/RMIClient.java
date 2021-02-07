/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		// Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
			System.out.println("Security manager initialised (RMI Client)");
		} else {
			System.out.println("A security manager already exists (RMI Client)");
		}
		// Bind to RMIServer
		boolean isServerActive = false;

		try {
			iRMIServer = (RMIServerI) Naming.lookup(urlServer);
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		// Attempt to send messages the specified number of times
		if (isServerActive) {
			System.out.println("Attempting to send " + numMessages + " messages");
			try {
				for (int i = 0; i < numMessages; i++) {
					MessageInfo message = new MessageInfo(numMessages, i);
					iRMIServer.receiveMessage(message);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			} finally {
				System.exit(0);
			}
		}
	}
}
