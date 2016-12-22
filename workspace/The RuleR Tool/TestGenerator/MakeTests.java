import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MakeTests {

	  private static Random r = new Random();
	  private static String openEvent;
	  private static String closeEvent;
	  private static PrintWriter fileWriter = null;
	  private static int numberOfEvents;
	  private static int eventCount;

	  private static ArrayList<String> existingCharacters = new ArrayList<String>();

	  public static void main(String[] args) {
		  
		numberOfEvents = 100;  
		System.out.println("Start");
		for(int i = 0;i<12;i++) {
	    	eventCount = 0;	  
	    	
	    	File output = new File("Interface"+(numberOfEvents)+".txt");
			try {
				fileWriter = new PrintWriter(output);
				
			   // Test Method	
			   Interface();
			    
			} catch (IOException e) {
				e.printStackTrace();
			} finally {		    
				fileWriter.close();
			}
			System.out.println("File " + numberOfEvents + " is done");
			numberOfEvents = numberOfEvents * 2;
	    
	  }
	    System.out.println("done");
	}
	
	private static void Interface() {
		HashMap<String, String> ActiveAccounts = new HashMap<String,String>();
		
		while(eventCount < numberOfEvents) {
	    	int RandomEvent = r.nextInt(3);
	    	switch(RandomEvent) {
	    	case 1: // openwindow(windName)
	    		for(int i1 = 0 ;i1 < 10; i1++) {
		    		String rItem = getRandomChar();
		    		if(!ActiveAccounts.containsKey(rItem)) {
		    			ActiveAccounts.put(rItem, "loaddata");
		    			String text = "openwindow,"+rItem;
		    			PrintToFile(fileWriter, text);
		    			break;
		    		}
	    		}
	    	break;
	    	case 2: // loaddata(windName, data)
	    		for(int i1 = 0 ;i1 < 10; i1++) {
		    		String rItem = getRandomChar();
		    		if(ActiveAccounts.containsKey(rItem)) {
		    			String event = ActiveAccounts.get(rItem);
		    			if(event.equals("loaddata")) {
			    			String data = "anyData";
			    			String text = event + "," + rItem + "," + data;
			    			PrintToFile(fileWriter, text);
			    			ActiveAccounts.remove(rItem);
			    			ActiveAccounts.put(rItem,"close");		    			
			    			break;
		    			}
		    		}
	    		}
	    	break;
	    	case 0: // close(windname)
	    		for(int i1 = 0 ;i1 < 10; i1++) {
	    			String rItem = getRandomChar();
		    		if(ActiveAccounts.containsKey(rItem)) {
		    			String event = ActiveAccounts.get(rItem);
		    			if(event.equals("close")) {
			    			int m = r.nextInt(20);
			    			String text = event + "," + rItem;
			    			PrintToFile(fileWriter, text);
			    			ActiveAccounts.remove(rItem);	    			
			    			break;
		    			}
		    		}
	    		}
	    	break;
	    	default:
	    		System.out.println("??");
	    	}
	    }
	}  
	  
	private static void LotteryTest()  {
			HashMap<String, Integer> ActiveAccounts = new HashMap<String,Integer>();
			HashMap<String, Lottery> PayOrTake = new HashMap<String,Lottery>();
			
			while(eventCount < numberOfEvents) {
		    	int RandomEvent = r.nextInt(3);
		    	switch(RandomEvent) {
		    	case 1: // GetNumber(Account,n)
		    		for(int i1 = 0 ;i1 < 10; i1++) {
			    		String rItem = getRandomChar();
			    		if(!ActiveAccounts.containsKey(rItem)) {
			    			int rMin = r.nextInt(20);
			    			ActiveAccounts.put(rItem, rMin);
			    			String text = "getNumber,"+rItem + "," +rMin;
			    			PrintToFile(fileWriter, text);
			    			break;
			    		}
		    		}
		    	break;
		    	case 2: // compare(account, m)
		    		for(int i1 = 0 ;i1 < 10; i1++) {
			    		String rItem = getRandomChar();
			    		if(ActiveAccounts.containsKey(rItem)) {
			    			Integer n = ActiveAccounts.get(rItem);
			    			int m = r.nextInt(20);
			    			String text = "compare," + rItem + "," + m;
			    			PrintToFile(fileWriter, text);
			    			ActiveAccounts.remove(rItem);
			    			if(m > n) {
			    				PayOrTake.put(rItem, new Lottery("pay",m+1));
			    			} else {
			    				PayOrTake.put(rItem, new Lottery("take",n+1));
			    			}
			    			break;
			    		}
		    		}
		    	break;
		    	case 0: // pay or take
		    		for(int i1 = 0 ;i1 < 10; i1++) {
			    		String rItem = getRandomChar();
			    		if(PayOrTake.containsKey(rItem)) {
			    			Lottery events = PayOrTake.get(rItem);
			    			String text = events.getAccount()+"," + rItem + "," + events.getAmount();
			    			PrintToFile(fileWriter, text);
			    			PayOrTake.remove(rItem);
			    			break;
			    		}
		    		}
		    	break;
		    	default:
		    		System.out.println("??");
		    	}
		    }
		}    
	  
	private static void ThreeEventTest()  {
		HashMap<String, MaxMin> ActiveItems = new HashMap<String,MaxMin>();
		
		while(eventCount < numberOfEvents) {
	    	int RandomEvent = r.nextInt(3);
	    	switch(RandomEvent) {
	    	case 1: // create Item
	    		for(int i1 = 0 ;i1 < 10; i1++) {
		    		String rItem = getRandomChar();
		    		if(!ActiveItems.containsKey(rItem)) {
		    			int rMin = r.nextInt(9);
		    			ActiveItems.put(rItem, new MaxMin(rMin,0));
		    			String text = "create,"+rItem + "," +rMin;
		    			PrintToFile(fileWriter, text);
		    			break;
		    		}
	    		}
	    	break;
	    	case 2: // bid
	    		for(int i1 = 0 ;i1 < 10; i1++) {
		    		String rItem = getRandomChar();
		    		if(ActiveItems.containsKey(rItem)) {
		    			MaxMin rules = ActiveItems.get(rItem);
		    			int rAmount = rules.getMax() + r.nextInt(10);
		    			rules.setMax(rAmount);
		    			String text = "bid," + rItem + "," + rAmount;
		    			PrintToFile(fileWriter, text);
		    			break;
		    		}
	    		}
	    	break;
	    	case 0: // sell
	    		for(int i1 = 0 ;i1 < 10; i1++) {
		    		String rItem = getRandomChar();
		    		if(ActiveItems.containsKey(rItem)) {
		    			MaxMin rules = ActiveItems.get(rItem);
		    			if(rules.getMax() > rules.getMin()) {
			    			String text = "sell," + rItem;
			    			PrintToFile(fileWriter, text);
			    			ActiveItems.remove(rItem);
			    			break;
		    			}
		    		}
	    		}
	    	break;
	    	default:
	    		System.out.println("??");
	    	}
	    }
	}  
	  
	  
	private static void PrintToFile(PrintWriter fileWriter, String text) {
		fileWriter.println(text);
		eventCount++;
	}
	  
	private static void TwoEventTest() {
	    openEvent = "list";
	    closeEvent = "sell";
	    
	    System.out.println("Hello");
	    for(int i = 0;i<1;i++) {
	    	File output = new File("BidSellDelete"+(numberOfEvents*2)+".txt");
			try {
				fileWriter = new PrintWriter(output);
	
			    int openEventCount = 0;
			    int closeEventCount = 0;	
	
			    while(openEventCount < numberOfEvents) {
			      int rEvent = r.nextInt(3);
			      if(rEvent < 2) { // Open Event
			        String rChar = addChar();
			        int rAmount = r.nextInt(10);
	
			        if(rChar.equals("FALSE"))
			          continue;
			        
			        //System.out.println("got char : " + rChar);
	
			        fileWriter.println(openEvent + "," + rChar + "," + rAmount);
	
			        openEventCount++;
			      }
			      else { // Close Event
			        String rChar = removeRandomChar();
			        int rAmount = r.nextInt(10);
	
			        if(rChar.equals("FALSE"))
			          continue;
			       
			        //System.out.println("Removed char : " + rChar);
	
			        fileWriter.println(closeEvent + "," + rChar + "," + rAmount);
			        closeEventCount++;
			      }
			      
			    }
			    
			    while(closeEventCount < openEventCount) {
			    	String rChar = removeRandomChar();
			    	int rAmount = r.nextInt(10);
	
			        if(rChar.equals("False"))
			          break;
	
			        fileWriter.println(closeEvent + "," + rChar + "," + rAmount);
			        closeEventCount++;
			    }
			    
			    
			} catch (IOException e) {
				e.printStackTrace();
			} finally {		    
				fileWriter.close();
			}
			System.out.println("File Auction" + numberOfEvents + ".txtis done");
			numberOfEvents = numberOfEvents * 2;
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

	class MaxMin {
		private int max;
		private int min;
		
		public MaxMin(int newMin, int newMax) {
			max = newMax;
			min = newMin;
		}
	
		public int getMax() {
			return max;
		}
	
		public void setMax(int max) {
			this.max = max;
		}
	
		public int getMin() {
			return min;
		}
	
		public void setMin(int min) {
			this.min = min;
		}
	}
	
	class Lottery {
		private String event;
		private int amount;
		
		public Lottery(String Acc, int newAmount) {
			event = Acc;
			amount = newAmount;
		}

		public String getAccount() {
			return event;
		}

		public void setAccount(String event) {
			this.event = event;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}
	}
