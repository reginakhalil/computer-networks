package cp372;

public class Validation {

	    //ISBN-13 validatior 
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
