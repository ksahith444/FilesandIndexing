import java.io.IOException;
import java.io.RandomAccessFile;




	public class RandomAccessFileDemo {

		  /* public static void main(String[] args) {
		      try {
		         String s = "Hello World";

		         // create a new RandomAccessFile with filename test
		         RandomAccessFile raf = new RandomAccessFile("C:/Users/Saquib/Desktop/test.txt", "rw");

		         // write a char in the file
		        // for (int i = 0; i < s.length(); i++) {
			             raf.writeBytes(s);
			         
		        
		         System.out.println(raf.getFilePointer());
		         // set the file pointer at 0 position
		         raf.seek(0);
		         char[] bytes = new char[s.length()];
		         // read chars
		         for (int i = 0; i < s.length(); i++) {
		        	 
		        	 bytes[i]=  (char) raf.readByte();
		        	// System.out.println("" +  bytes[i]);
		            
		         }
		         System.out.println("" + new String(bytes));
		         System.out.println(raf.getFilePointer());
		         // set the file pointer at 0 position
		         raf.seek(0);

		         // change the line for better view
		         System.out.println();

		         // write a char at the start
		         raf.writeChars("This is an example");

		         // set the file pointer at 0 position
		         raf.seek(0);

		         // read chars
		         for (int i = 0; i < 18; i++) {
		            System.out.print("" + raf.readChar());
		         }
		      } catch (IOException ex) {
		         ex.printStackTrace();
		      }

		   }*/
		}


