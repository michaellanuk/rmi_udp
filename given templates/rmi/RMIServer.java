/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;
	private static int port = 1099;
	private static String hostname = "146.169.53.185";

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// On receipt of first message, initialise the receive buffer
		if (msg.messageNum == 0) {
			totalMessages = msg.totalMessages;
			receivedMessages = new int[totalMessages];
			System.out.println("Receive buffer initialised");
		}

		// Log receipt of the message
		receivedMessages[msg.messageNum] = 1;
		System.out.println("Message: " + (msg.messageNum + 1) + " received");
		// If this is the last expected message, then identify
		//        any missing messages
		int missingMessages = 0;
		if (msg.messageNum == totalMessages - 1) {
			for (int i = 0; i < totalMessages; i++) {
				if (receivedMessages[i] != 1) {
					missingMessages++;
					System.out.println("Message: " + (i + 1) + " was lost");
				}
			}
		}

		System.out.println(missingMessages + " messages were lost in total");
		System.out.println(msg.totalMessages + " messages were sent by the RMI Client");
	}


	public static void main(String[] args) {

		RMIServer rmis = null;

		// Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
			System.out.println("Security manager initialised (RMI Server)");
		} else {
			System.out.println("A security manager already exists (RMI Server)");
		}

		// Instantiate the server class
		try {
			rmis = new RMIServer();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		// Bind to RMI registry
		String urlServer = new String("rmi://146.169.53.185:1099/RMIServer");
		rebindServer(urlServer, rmis);
	}

	protected static void rebindServer(String serverURL, RMIServer server) {

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)

		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.

		boolean registryCreated = false;

		try {
		    LocateRegistry.createRegistry(port);
			System.out.println("Successfully created registry");
			registryCreated = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (registryCreated) {
				System.out.println("Finding registry");
				//LocateRegistry.getRegistry(port);
				Naming.rebind(serverURL, server);
				System.out.println("Successfully rebinded registry");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
