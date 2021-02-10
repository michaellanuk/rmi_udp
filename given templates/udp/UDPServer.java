/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		try {
			pacSize = 1024;

			while (true) {
				pacData = new byte[pacSize];
				pac = new DatagramPacket(pacData, pacSize);
				recvSoc.setSoTimeout(30000);
				recvSoc.receive(pac);
				processMessage(new String(pac.getData()));
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		// Use the data to construct a new MessageInfo object
		try {
			msg = new MessageInfo(data.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// On receipt of first message, initialise the receive buffer
		if (msg.messageNum == 0) {
			totalMessages = msg.totalMessages;
			receivedMessages = new int[totalMessages];
			System.out.println("Receive buffer initialised");
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;
		System.out.println("Message " + (msg.messageNum + 1) + " received");

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if (msg.messageNum == totalMessages - 1) {
			System.out.println("Last expected message received");
			int missingMessages = 0;
			for (int i = 0; i < totalMessages; i++) {
				if (receivedMessages[i] != 1) {
					System.out.println("Message " + (i + 1) + " was lost");
					missingMessages++;
				}
			}
			System.out.println(missingMessages + " messages were lost in total");
			System.out.println(msg.totalMessages + " were sent by the client");
		}
	}


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try {
			recvSoc = new DatagramSocket(rp);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer server = new UDPServer(recvPort);
		try {
			System.out.println("UDP server listening on: ");
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		server.run();
	}
}
