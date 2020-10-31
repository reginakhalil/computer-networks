import java.io.*;
import java.net.*;


public class Sender {

	public static String IPaddr; // host addr of receiver
	public static int recPort; // port number used by receiver to receive sender data
	public static int sendPort; // port number used by sender to receive ACKs from receiver
	public static String file; // name of file to be transferred
	public static int maxData; //max data inside datagram in bytes
	public static int timeout; // timeout in ms
	public static String dataDecoding = null;
	
	public static void main(String[] args) throws Exception {
		/*
		 * initializing vars and setting up sockets for file transfer.
		 * senderSocket:  socket for the sender to receive ACKs from + send the file from
		 * receiverSocket: socket for the receiver to receive the file from
		 * fileBuffer set to max size 
		 */
		IPaddr = args[0];
		recPort = Integer.parseInt(args[1]);
		sendPort = Integer.parseInt(args[2]);
		file = args[3];
		maxData = Integer.parseInt(args[4]);
		timeout = Integer.parseInt(args[5]);
		initSend(recPort, IPaddr, file, maxData, timeout, sendPort);
		
	}
	
	/*
	 * function initializes the sending of the file given the 2 ports and 
	 * creats a datagrapm packet to send and receive. 
	 * The sending and receiving of the acks will be in the Receiver class
	 */
	public static void initSend (int rport, String ipAddr, String file, int mds, int timeout, int sport) throws IOException {
		DatagramSocket ssocket = new DatagramSocket(sport);
		InetAddress ip = InetAddress.getByName(ipAddr);
		String newFile = "received.txt";
		byte[] newFileData = newFile.getBytes();
		DatagramPacket finalEOT = new DatagramPacket(newFileData, newFileData.length, ip, rport);
		ssocket.send(finalEOT);
		File filename = new File(file);
		byte[] fileArr = new byte [(int) filename.length()];
		
		/* 
		 * here is the sending of the file 
		 * and sending the EOT datagram
		 */
		sendData(ssocket, fileArr, ip, mds, rport, timeout);
		sendEOT(ssocket, ip, rport);
		ssocket.close();
		
	}

	private static void sendData(DatagramSocket socket, byte[] fileArr, InetAddress ip, int mds, int rport, int timeout) throws IOException {
		int seqNum = 0;
		boolean flag;
		int ackSeq = 0;
		// 2 bytes for msgs; since les than 1024 (1023) it will be 1021 + 2 so the first 2 bytes are for the msg seqNum
		for (int i = 0; i < fileArr.length; i+= 1021) {
			seqNum += 1;
			/*
			 * creating msg with the given MDS
			 * seqNum >> is for a right shift of 2 of the 8 bits from the msg being sent (2 seq# for each msg)
			 * the if statement is if the size is greater or equal to file len, meaning it has sent, so seq# of 1
			 * else it's not all sent, so seq# of 0
			 */
			byte[] msg = new byte[mds];
			msg[0] = (byte) (seqNum >> 8); 
			msg[1] = (byte) (seqNum); 
			
			if ((i + 1021) >= fileArr.length) {
				flag = true;
				msg[2] = (byte) (1);
			} else {
				flag = false;
				msg[2] = (byte) (0);
			}
			
			/*
			 *  is it the last msg or not? if not, do the first
			 *  else do the second one
			 *  dest pos of 3 cuz for msg2
			 */
			
			if (!flag) {
				System.arraycopy(fileArr, i, msg, 3, 1021);
			} else {
				System.arraycopy(fileArr, i, msg, 3, fileArr.length-i);
			}
			
			//pckt sending
			DatagramPacket sendPacket = new DatagramPacket(msg, msg.length, ip, rport);
			
			if (i != 0) {
				socket.send(sendPacket);
				System.out.println("Sent: Sequence number = " + seqNum);
			}
			
			// ack? yes or no
			boolean ackYN;
			
			while (true) {
				// byte arr of len 3 for the ack
				byte[] ack = new byte[2];
				DatagramPacket ackPckt = new DatagramPacket(ack, ack.length);
				
				/*
				 * this is where we attempt to ack the packet 
				 * we also use the timeout val given to us here
				 */
				try {
					socket.setSoTimeout(timeout);
					socket.receive(ackPckt);
					// 0xFF  is used to make an operation between byte and int work
					// kinda like typecasting
					ackSeq = ((ack[0] & 0xff) << 8) + (ack[1] & 0xff);
					ackYN = true;
				} catch (SocketTimeoutException e) {
					System.out.println("Socket timed out waiting for packet: " + seqNum);
					ackYN = false;		
				}
				
				/*
				 * pckt has been successfully send
				 * break is to move onto the next pckt
				 * else retransmit (this is for
				 */
				if ((ackSeq == seqNum) && (ackYN)) {
					System.out.println("ACK received of Sequence #: " + ackSeq);
					break;
				}
				else {
					socket.send(sendPacket);
					System.out.println("Resending Sequence # " + seqNum);
					break;
				}
				
			}
			
			int total = totalPackets(sendPacket);
			int time = timer(sendPacket);
			System.out.println("Total Packets Sent: " + total + "\nTime (ms): " + time);		
 		}
	}
	
	
	private static void sendEOT (DatagramSocket socket,InetAddress ip, int rport) {
		String eot = "END OF TRANSFER. File is saved as 'received.txt'";
		byte[] data;
		data = eot.getBytes();
		DatagramPacket eotPckt = new DatagramPacket(data, data.length, ip, rport);
		try {
			socket.send(eotPckt);
		} catch (IOException EOTransfer) {
			System.out.println("Unable to trasnfer EOT");
		}
	}
	
	 private static int totalPackets (DatagramPacket packet) {
		 int total = 0;
	     total = packet.getLength() + total;
	     total = Math.round(total);

	     return total;
	    }
	 
	 
	 private static int timer (DatagramPacket packet) {
		 int count = 0;
	     for (;;) {
	    	 try {
	    		 Thread.sleep(1);
	    		 count++;
	    	 } catch (InterruptedException e) {
	    		 e.printStackTrace();
	    	 }	    	 
	    return count;
	     }
	  }
}
