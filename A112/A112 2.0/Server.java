import java.net.*;
import java.lang.Thread;
import java.util.*;

import javax.swing.JOptionPane;

import java.awt.print.Book;
import java.io.*;

@SuppressWarnings("unchecked")
public class Server {
	public static int port;
	public static int clientCount;
	public String[] entries = {};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		port = Integer.parseInt(args[0]);
	
		/*
		 * Server where listeners are and connections are accepted. Trying not to let over 200 connections atm
		 */
	try {
		ServerSocket listener = new ServerSocket(port);
		
		/* 
		 * client # (ex. first client is 0, second is 1)
		 */
		int clientNum = 0; 
		
		while(true) {
			/* 
			 * run using clientReq class and client number increases with every new connection
			 */
			new ClientReq(listener.accept(), clientNum++).start();
			if (clientNum > 200) {
				break;
			}
		} try {
			listener.close();
		} catch (Exception closeErr) {
			// needs to be in the response window ? or joptionpane
			System.out.println("Cannot close server socket.");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	


	/*
	 * where the runnable will be as well as data from client is received here
	 */
	private static class ClientReq extends Thread {
		
		private static Socket s;
		private int clientNum;
		private String reqType;
		private String allState;
		private String bibtexState;
		
		/* 
		 * Array list for all of the bib entries
		 */
		public static ArrayList <Books> allEntries = new ArrayList<Books>();

		/*
		 *  number of entries in arr list at start
		 */
		public static int entryCount = 0;
		public static PrintWriter output;
		
		public ClientReq (Socket s, int clientNum) {
			this.s = s;
			this.clientNum = clientNum;
			/*
			 *  new client == increase in count
			 */
			clientCount++;
		}

		
		/*
		 *  this class will be used to collect the info of user input
		 */
		public static class Books {
			private String reqType;
			private String isbn;
			private String title;
			private String author;
			private String year;
			private String publisher;
			
			/*
			 *  constructor
			 */
			public Books() {
				
			}
			public Books (String isbn, String title, String author, String year, String publisher) {
				this.isbn = isbn;
				this.title = title;
				this.author = author;
				this.year = year;
				this.publisher = publisher;

			}
			
			/*
			 * Getters 
			 */
			public String getISBN() {
				return this.isbn; 
			}
			
			public String getTitle() {
				return this.title;
			}
			
			public String getAuthor() {
				return this.author;
			}
			
			public String getPub() {
				return this.publisher;
			}
			
			public String getYear() {
				return this.year;
			}

					/*
			 * Setters 
			 */
			public void setISBN(String isbn) {
				this.isbn = isbn; 
			}
			
			public void setTitle(String title) {
				this.title = title;
			}
			
			public void setAuthor(String author) {
				this.author = author;
			}
			
			public void setPub(String pub) {
				this.publisher = pub;
			}
			
			public void setYear(String year) {
				this.year = year;
			}
			
			public String bibFormat(String entries) {
				String citekey = this.author + this.year;
				String bibtex = "@BOOK{" + citekey + ",\n@title = " + getTitle() + "\nauthor = " + getAuthor() + "\npublisher = " + getPub() + "\nyear = " + getYear(); 
				return bibtex;	
				}
			}
		
		public void createEntry (ArrayList <String> entry, PrintWriter output) {
			/* 
			 * cannot add entry if no isbn
			 */
			if (String.valueOf(entry.get(3)) == null) {
				JOptionPane.showMessageDialog(null, "ISBN empty - cannot submit", null, JOptionPane.ERROR_MESSAGE);
			}
			/* 
			 * else move and try to add it.
			 */
			else {
				try {
					Books book = new Books (String.valueOf(entry.get(3)), String.valueOf(entry.get(4)), String.valueOf(entry.get(5)), String.valueOf(entry.get(6)), String.valueOf(entry.get(7)));
					
					/*
					 *  check if isbn alr exists
					 */
					for (Books b : allEntries) {
						if (b.getISBN() != String.valueOf(entry.get(3))){
							allEntries.add(book);
							entryCount++;
							output.println("Entry added");
						} else {
							output.println("ISBN exists - cannot add this entry");
						}
					}
					/*
					 *  sending to client
					 */
					output.flush();
					// needs to be in the response window
	
				} catch (Exception e) {
					// perhaps this can be a joptionpane window. but idk would it be too flashy?
					output.println("Cannot add this entry");
				}	
			}
		}
		
		public void getAll (PrintWriter output, boolean bibtexState) {
			Books book = new Books();
			try {
				output = new PrintWriter(s.getOutputStream(), true);
				String entries = "Bibliography: ";
				for (Books b : allEntries) {
					entries += b;
				}
				if (bibtexState == true) {
					// this needs to be in the response window
					output.println(book.bibFormat(entries));
				} 
				else {
					output.println(entries);
				}
				output.flush();
			} catch (Exception printErr) {
				// perhaps this can be a joptionpane window.
				output.println("Cannot print entries");
			}
		}
		
		public void getEntry (ArrayList <String> entry, PrintWriter output) {
			String isbn;
			String title;
			String author;
			String year;
			String publisher;
			
			/* NEED THIS 
			 * basically whatever isnt null, compare with like allEntries.get(i).getTitle or something, flush whatever matches
			 * similar to the getAll
			 * ArrList:
			 * [req, all, bibtex, isbn, title, author, yr, publisher]
			 */ 

			try {
				output = new PrintWriter(s.getOutputStream(), true); 
				String entriesFound = "Bibliographies found: ";
				
				for (i=0; i < allEntries.size(); i++) {

					if allEntries[i] == entry{
						entriesFound = entriesFound + "\n" + entry;
					}
				}


			}catch (Exception printErr) {
				// perhaps this can be a joptionpane window.
				output.println("Cannot print entries");
			}
		}
		
		public void updateEntry(ArrayList <String> entry, PrintWriter output) {
			String isbn = entry.getISBN();
			String title = entry.getTitle();
			String author = entry.getAuthor();
			String year = entry.getYear(); 
			String publisher = entry.getPub();
			

			//NEED THIS
			// basically whatever isnt null, compare with like allEntries.get(i).getTitle or something, update whatever matches and flush
			try {
				output = new PrintWriter(s.getOutputStream(), true); 
				for (int i = 0; i < allEntries.size(); i++) {
					if (allentries.get(i).getISBN() == isbn) {
						if (title != null) {
							allEntries.get(i).setTitle(title); 
						}
						if (author != null) {
							allEntries.get(i).setAuthor(author); 
						}
						if (year != null) {
							allEntries.get(i).setYear(year); 
						}
						if (publisher != null) {
							allEntries.get(i).setPub(publisher); 
						}
					}
			}
				output.println("Updated succefully");
				output.flush();	
			} catch (Exception printErr) {
				// perhaps this can be a joptionpane window.
				output.println("Cannot print entries");
			}

			}
		}
		
		public void removeAll(PrintWriter output) {
			try {
				output = new PrintWriter(s.getOutputStream(), true);
				for (int i=0; i < allEntries.size(); i++) {
					allEntries.remove(i);
				}
				
				output = new PrintWriter(s.getOutputStream(), true);
				// this needs to be in the response window
				output.println("Removed all entries");
				output.flush();
			}  catch (Exception remove) {
				output.println("Could not clear all entries");
			}
		}

		
		@Override
		public void run() {
			/*
			 *  Client sending...
			 */
			try {
				ObjectInputStream input = new ObjectInputStream(s.getInputStream());
				Object inObj = input.readObject();
				PrintWriter output = new PrintWriter(s.getOutputStream(), true);
				
				/* 
				 * checking what they sent; array goes as follows:
				 * ArrList:
				 * [req, all, bibtex, isbn, title, author, yr, publisher]
				 */
				
				while(inObj != null) {
					ArrayList<String> clientReq = (ArrayList<String>)inObj;
					if (clientReq.contains("SUBMIT")) {
						createEntry (clientReq, output);
					} else if (clientReq.contains("GET") && clientReq.get(1).equalsIgnoreCase("true")) {
						if (clientReq.get(2).equalsIgnoreCase("true")) {
							getAll(output, true);
						}
						else {
							getAll(output, false);
						}
						
					} else if (clientReq.contains("GET")) {
						getEntry(clientReq, output);
					} else if (clientReq.contains("UPDATE")) {
						updateEntry(clientReq, output);
					} else if (clientReq.contains("REMOVE")) {
						removeAll(output);
					} else {
						JOptionPane.showMessageDialog(null, "Nothing recieved", null, JOptionPane.ERROR_MESSAGE);
					}
					
					input = new ObjectInputStream(s.getInputStream());
					output = new PrintWriter(s.getOutputStream(), true);
					inObj = input.readObject();
				} 
			} catch (Exception connection) {
				// perhaps this can be a joptionpane window. but idk 
				System.out.println("Closing connection: everything sent");
				try {
					s.close();
				} catch(Exception closing) {
					// perhaps this can be a joptionpane window. but idk 
					System.out.println("Failed connection.");
				}
			}
		}
	}
}
