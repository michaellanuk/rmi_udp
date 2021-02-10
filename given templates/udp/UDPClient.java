/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import common.MessageInfo;

public class UDPClient {

	private DatagramSocket sendSoc;

	public static void main(String[] args) {
		InetAddress	serverAddr = null;
		int			recvPort;
		int 		countTo;
		String 		message;

		// Get the parameters
		if (args.length < 3) {
			System.err.println("Arguments required: server name/IP, recv port, message count");
			System.exit(-1);
		}

		try {
			serverAddr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[1]);
		countTo = Integer.parseInt(args[2]);


		// Construct UDP client class and try to send messages
		UDPClient client = new UDPClient();
		client.testLoop(serverAddr, recvPort, countTo);
	}

	public UDPClient() {
		// Initialise the UDP socket for sending data
		try {
			sendSoc = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	private void testLoop(InetAddress serverAddr, int recvPort, int countTo) {
		int				tries = 0;

		// Send the messages to the server
		while (tries < countTo) {
			MessageInfo message = new MessageInfo(countTo, tries);
			send(message.toString(), serverAddr, recvPort);
			tries++;
		}
	}

	private void send(String payload, InetAddress destAddr, int destPort) {
		int				payloadSize;
		byte[]				pktData;
		DatagramPacket		pkt;

		// build the datagram packet and send it to the server
		pktData = payload.getBytes();
		payloadSize = pktData.length;
		pkt = new DatagramPacket(pktData, payloadSize, destAddr, destPort);

		try {
			sendSoc.send(pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
