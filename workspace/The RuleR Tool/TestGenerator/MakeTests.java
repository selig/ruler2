import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class MakeTests {

	  private static Random r = new Random();
	  private static String openEvent;
	  private static String closeEvent;
	  private static PrintWriter fileWriter = null;
	  private static int numberOfEvents;

	  private static ArrayList<String> existingCharacters = new ArrayList<String>();

	  public static void main(String[] args) {
	    
	    numberOfEvents = 50;
	    
	    System.out.println("Hello");
	    for(int i = 0;i<7;i++) {
	    	File output = new File("CloseOpen"+numberOfEvents+".txt");
			try {
				fileWriter = new PrintWriter(output);
	
			    int openEventCount = 0;
			    int closeEventCount = 0;
			    
			    openEvent = "open";
			    closeEvent = "close";
	
	
			    while(openEventCount < numberOfEvents) {
			      int rEvent = r.nextInt(3);
			      if(rEvent < 2) { // Open Event
			        String rChar = addChar();
	
			        if(rChar.equals("FALSE"))
			          continue;
			        
			        //System.out.println("got char : " + rChar);
	
			        fileWriter.println(openEvent + "," + rChar);
	
			        openEventCount++;
			      }
			      else { // Close Event
			        String rChar = removeRandomChar();
	
			        if(rChar.equals("FALSE"))
			          continue;
			       
			        //System.out.println("Removed char : " + rChar);
	
			        fileWriter.println(closeEvent + "," + rChar);
			        closeEventCount++;
			      }
			      
			    }
			    
			    while(closeEventCount < openEventCount) {
			    	String rChar = removeRandomChar();
	
			        if(rChar.equals("False"))
			          break;
	
			        fileWriter.println(closeEvent + "," + rChar);
			        closeEventCount++;
			    }
			    
			    
			} catch (IOException e) {
				e.printStackTrace();
			} finally {		    
				fileWriter.close();
			}
			System.out.println("File " + fileWriter.toString() + " is done");
			numberOfEvents = numberOfEvents * 10;
	    }
		System.out.println("done");
	}

	public static String addChar() {
	  int count=0;
	  boolean found = false;
	  String character = "";
	  while(!found && count < 26) {
	    character = getRandomChar();
	    if(!existingCharacters.contains(character)) {
	      existingCharacters.add(character);
	      found = true;
	    }
	    count++;
	  }
	  if(!found)
	    return "FALSE";
	  else
	    return character;
	}

	private static String getRandomChar() {
		int nextCharIndex = r.nextInt(26);
		return (char)(nextCharIndex + 'a') + "";	
	}

	public static String removeRandomChar() {
	  if(existingCharacters.size() == 0)
		 return "FALSE";
	  /*boolean found = false;
	  while(!found) {
	    int nextCharIndex = r.nextInt(26);
	    if(existingCharacters.contains(nextCharIndex)) {
	      existingCharacters.remove((Integer)nextCharIndex);
	      found = true;
	    }
	  }
	  return (char)(r.nextInt(26) + 'a') + ""; */
	  String charInd = existingCharacters.get(0);
	  existingCharacters.remove(0);
	  return charInd;
	}
}
