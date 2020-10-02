package cp372;

import javax.swing.JOptionPane;

public class Request {

		String type, isbn,title,author,publisher,year, request;
		
		public Request(String type, String isbn,String title,String author,String publisher,String year) {
			this.isbn = isbn; 
			this.title = title; 
			this.author = author; 
			this.publisher = publisher; 
			this.year = year;
			this.type = type; 
		}
		
		//Setters 
		public void setType(String type) {
			type = this.type; 
		}
		
		public void setISBN(String isbn) {
			isbn = this.isbn; 
		}
		
		public void setTitle(String title) {
			title = this.title; 
		}
		
		public void setAuthor(String author) {
			author = this.author; 
		}
		
		public void setPub(String pub) {
			pub = this.publisher; 
		}
		
		public void setYear(String year) {
			year = this.year; 
		}
		
		//Getters 
		public String getType() {
			return this.type; 
		}
		
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
		
		
		//Request Validation
		public boolean validateRequest() {
			boolean isvalid = false; 
			
			if (this.type.equals("UPDATE") && !this.isbn.equals("")) {
				JOptionPane.showMessageDialog(null, "ISBN is a key field and cannot be updated", null, JOptionPane.ERROR_MESSAGE);
				isvalid = false;
			}
			
			else if (this.type.equals("REMOVE")) {
				if (!(isbn.isEmpty() && title.isEmpty() && author.isEmpty() && publisher.isEmpty() && year.isEmpty())) {
					JOptionPane.showConfirmDialog(null, "Are you sure you want to remove entry?", null, JOptionPane.YES_NO_OPTION);
				}
				else {
					JOptionPane.showMessageDialog(null, "You must enter at least one field", null, JOptionPane.ERROR_MESSAGE);
					isvalid = false;
				}
				
				//if yes, delete entry from data structure 
				//if no, do nothing 
			}
			else if (this.type.equals("GET") || this.type.equals("SEND")) {
				if (isbn.isEmpty() && title.isEmpty() && author.isEmpty() && publisher.isEmpty() && year.isEmpty()) {
					JOptionPane.showMessageDialog(null, "You must enter at least one field", null, JOptionPane.ERROR_MESSAGE);
					isvalid = false;
				}
			}
			else if (!isbn.isEmpty()){
				if (validateIsbn(this.isbn)) {
					isvalid = true;
				}
				
			}
				
			return isvalid; 
		}
		
		public String toString() {
			this.request = " ";
			if(this.isbn != "") {
				this.request = this.request + "ISBN" + " "+ this.isbn;
			}
			if(this.title != "") {
				this.request = this.request + "TITLE" + " "+ this.title;
			}
			if(this.author != "") {
				this.request = this.request + "AUTHOR" + " "+ this.author;
			}
			if(this.publisher != "") {
				this.request = this.request + "PUBLISHER" + " "+ this.publisher;
			}
			if(this.year != "") {
				this.request = this.request + "YEAR" + " "+ this.year;
			}
			System.out.println(this.request);
			return this.request;
		}
		
	    //ISBN-13 validation 
		public static boolean validateIsbn( String isbn )
	    {
			boolean isValid; 
			
	        if ( isbn == null )
	        {
	        	isValid =  false;
	        }
	        //remove any hyphens
	        isbn = isbn.replaceAll( "-", "" );
	        int len = isbn.length(); 
	        //must be a 13 digit ISBN
	        if (len != 13)
	        {
	        	isValid = false;
	        }

	        try
	        {
	            int sum = 0;
	            for ( int i = 0; i < 12; i++ )
	            {
	                int digit = Integer.parseInt( isbn.substring( i, i + 1 ) );
	                if (i % 2 == 0) {
	                	sum+= (digit*1); 
	                }
	                else {
	                	sum+= (digit*3); 
	                }

	            }

	            //checksum must be 0-9. If calculated as 10 then = 0
	            int checksum = 10 - (sum % 10);
	            if ( checksum == 10 )
	            {
	                checksum = 0;
	            }

	            if ((checksum == Integer.parseInt(isbn.substring(12))) == true ) {
	            	isValid = true; 
	            }
	            else {
	            	isValid = false; 
	            }
	        }
	        catch ( NumberFormatException nfe )
	        {
	            //to catch invalid ISBNs that have non-numeric characters in them
	        	isValid = false;
	        }
	       return isValid;
	    }
		
		//year validation
		public static boolean year_check(String year) {
			boolean isValid; 
			try {
				Integer.parseInt(year);
				isValid = true;
			}catch(Exception e) {
				isValid = false;
			}
			return isValid;
		}
		
		
}
