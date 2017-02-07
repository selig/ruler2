import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MakeTests {

	  private static Random r = new Random();
	  private static PrintWriter fileWriter = null;
	  private static int numberOfEvents;
	  private static int eventCount;
	  private static boolean worstCase;

	private static ArrayList<String> existingCharacters = new ArrayList<String>();
	
	public static void main(String[] args) {
		  
		numberOfEvents = 100;  
		worstCase = true;
		System.out.println("Start");
		for(int i = 0;i<8;i++) {
			eventCount = 0;	  
			
			File output = new File((i+1)+"_" + "UnsafeMapIterator" +(numberOfEvents)+ ".txt");
		try {
			fileWriter = new PrintWriter(output);
			
		   // Test Method	
			UnsafeMapIterator();
		    
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
	
	private static void RespectPriorities() {
		/*	HashMap<String, String> ActiveMaps = new HashMap<String,String>();
			
			while(eventCount < numberOfEvents) {
		    	int RandomEvent = r.nextInt(2);
		    	switch(RandomEvent) {
		    	case 1: // create
	for(int i1 = 0 ;i1 < 5; i1++) {
		String rCollection = getRandomChar();
		String rMap = getRandomChar();
		if(!ActiveMaps.containsKey(rCollection)) {
			ActiveMaps.put(rCollection, rMap);
			String text = "create,"+rMap+","+rCollection;
				PrintToFile(fileWriter, text);
				break;
			}
		}
	break;
	case 0: // iterator(collection, iterrator)
	for(int i1 = 0 ;i1 < 10; i1++) {
		String rItem = getRandomChar();
		if(ActiveMaps.containsKey(rItem)) {
			String rIterator = getRandomChar();
			String text = "iterator" + "," + rItem + "," + rIterator;
				PrintToFile(fileWriter, text);	    			
				break;
			}
		}
	break;
	default:
		System.out.println("??");
		    	}
		    }//*/
		} 
	  
	private static void UnsafeMapIterator() {
			HashMap<String, MapCollection> Created = new HashMap<String,MapCollection>(); //<Map,Collection> for iterator
			HashMap<String, MapIterator> Live = new HashMap<String,MapIterator>(); //<Map,iterator> for update
			int usedMapIterators=0;
			
			while(eventCount < numberOfEvents) {
								
				int RandomEvent= -1;
				if(worstCase) {
					if(numberOfEvents/3 > eventCount)
						RandomEvent = 0;
					else
						RandomEvent = r.nextInt(2) + 1;
				} else {
					RandomEvent = r.nextInt(3);
				}
		    	switch(RandomEvent) {
		    	case 0: // create(map, collection)
		    		while(true) {
		    			String rMap = getRandomChar(Created.size());
			    		String rCollection = getRandomChar(Created.size());
			    		String key = rMap + rCollection;
			    		if(!Created.containsKey(key)) {
			    			Created.put(key, new MapCollection(rMap, rCollection));
			    			String text = "create,"+rMap + "," +rCollection;
			    			PrintToFile(fileWriter, text);
			    			break;
			    		}
		    		}
				break;
				case 1: // iterator(collection, iterator)
					if(Created.size() > 0) {
			    		while(true) {
			    			List<String> keys      = new ArrayList<String>(Created.keySet());
			    			String       rMapCollection = keys.get( r.nextInt(keys.size()) );
				    		if(Created.containsKey(rMapCollection)) {
				    			MapCollection object = Created.get(rMapCollection);
				    			String key = "";
				    			String iterator = "";
				    			do {
				    				iterator = getRandomChar(Created.size());
				    				//check if iterator does not repeat with map
				    				key = object.getMap() + iterator;
				    			}
				    			while(Live.containsKey(key));
				    			
				    			String text = "iterator," + object.getCollection() + "," + iterator;
				    			Live.put(key, new MapIterator(object.getMap(), iterator));
				    			PrintToFile(fileWriter, text);
				    			break;
				    		}
			    		}
		    		}
				break;
				case 2: // update(map)
					if(Live.size() > 0 && usedMapIterators < Live.size()) {
			    		while(true) {
			    			List<String> keys      = new ArrayList<String>(Live.keySet());
			    			String       rMapIterator = keys.get( r.nextInt(keys.size()) );
				    		if(Live.containsKey(rMapIterator)) {
				    			MapIterator object = Live.get(rMapIterator);
				    			if(!object.isUsed()) {
				    				String text = "update," + object.getMap();
				    				PrintToFile(fileWriter, text);
				    				object.setUsed(true);
				    				usedMapIterators++;
				    				break;
				    			}
				    		}
			    		}
		    		}
				break;
				default:

		    		Error(RandomEvent);
		    	}
			}//*/
		}  
	  
	private static void ExactlyOneSuccess() {
		/*	  HashMap<String, String> sucAccounts = new HashMap<String,String>();
			HashMap<String, String> doneAccounts = new HashMap<String,String>();
			int RandomEvent;
			while(eventCount < numberOfEvents || sucAccounts.size() != 0 || doneAccounts.size() != 0) {
				if(eventCount + (sucAccounts.size()*2) + doneAccounts.size() < numberOfEvents) {
					RandomEvent = r.nextInt(3);
				}
				else {
				   if(sucAccounts.size() > 0) {
					   RandomEvent = 2;
				   } else if (doneAccounts.size() > 0){
					   RandomEvent = 0;
				   } else {
					   RandomEvent = 5;
				   }
				}
				
		    	switch(RandomEvent) {
		    	case 1: // com(n)
		    		for(int i1 = 0 ;i1 < 10; i1++) {
			    		String rItem = getRandomChar();
			    		if(!sucAccounts.containsKey(rItem)) {
			    			sucAccounts.put(rItem, "suc");
			    			String text = "com,"+rItem;
			    			PrintToFile(fileWriter, text);
			    			break;
			    		}
		    		}
		    	break;
		    	case 2: // suc(n)
		    		for(int i1 = 0 ;i1 < 10; i1++) {
			    		String rItem = getRandomChar();
			    		if(sucAccounts.containsKey(rItem)) {
			    			String text = "suc" + "," + rItem;
			    			PrintToFile(fileWriter, text);
			    			sucAccounts.remove(rItem);
			    			doneAccounts.put(rItem,"done");		    			
			    			break;
			    		}
		    		}
		    	break;
		    	case 0: // done(n)
		    		for(int i1 = 0 ;i1 < 10; i1++) {
		    			String rItem = getRandomChar();
			    		if(doneAccounts.containsKey(rItem)) {
			    			int m = r.nextInt(20);
			    			String text = "done" + "," + rItem;
			    			PrintToFile(fileWriter, text);
			    			doneAccounts.remove(rItem);	    			
			    			break;
			    		}
		    		}
		    	break;
		    	default:
		    		System.out.println("??");
		    	}
		    } //*/
		}   
	 
	private static void HasNext() {
		/*	ArrayList<String> SafeIter = new ArrayList<String>();
			
			while(eventCount < numberOfEvents) {
		    	int RandomEvent = r.nextInt(2);
		    	switch(RandomEvent) {
		    	case 1: // hasNext(windName)
		    		for(int i1 = 0 ;i1 < 10; i1++)	 {
			    		String rItem = getRandomChar();
			    		if(!SafeIter.contains(rItem)) {
			    			SafeIter.add(rItem);
			    			String text = "hasNext,"+rItem + ", true";
			    			PrintToFile(fileWriter, text);
			    			break;
			    		}
		    		}
		    	break;
		    	case 0: // next(iter)
		    		for(int i1 = 0 ;i1 < 10; i1++) {
		    			String rItem = getRandomChar();
			    		if(SafeIter.contains(rItem)) {
			    			String text = "next" + "," + rItem;
			    			PrintToFile(fileWriter, text);
			    			SafeIter.remove(rItem);	    			
			    			break;
			    		}
		    		}
		    	break;
		    	default:
		    		System.out.println("??");
		    	}
		    } //*/
		}    
	 
	private static void StateMachine() {
		  String prevEvent = "b";
			
			while(eventCount < numberOfEvents) {
		    	if(prevEvent.equals("b")) {
		    		prevEvent = "a";
	    			PrintToFile(fileWriter, prevEvent);
		    	}
		    	else {
		    		prevEvent = "b";
	    			PrintToFile(fileWriter, prevEvent);
		    	}
		    }
		}  
	
	private static void Interface() {
		/*HashMap<String, String> ActiveAccounts = new HashMap<String,String>();
		
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
	    } // */ 
	}  
	  
	private static void LotteryTest()  {
			HashMap<String, Integer> ActiveAccounts = new HashMap<String,Integer>();
			HashMap<String, Lottery> PayOrTake = new HashMap<String,Lottery>();
			
			while(eventCount < numberOfEvents) {
		    	int RandomEvent= -1;
				if(worstCase) {
					if(numberOfEvents/3 > eventCount)
						RandomEvent = 0;
					else
						if(ActiveAccounts.size() > 0)
							RandomEvent = 1;
						else if(PayOrTake.size() > 0)
							RandomEvent = 2;
						else
							RandomEvent = 0;
				} else {
					RandomEvent = r.nextInt(3);
				}
		    	switch(RandomEvent) {
		    	case 0: // GetNumber(Account,n)
		    		while(true) {
			    		String rAccount = getRandomChar(ActiveAccounts.size());
			    		if(!ActiveAccounts.containsKey(rAccount)) {
			    			int rNumber = r.nextInt(20);
			    			ActiveAccounts.put(rAccount, rNumber);
			    			String text = "getNumber,"+rAccount + "," +rNumber;
			    			PrintToFile(fileWriter, text);
			    			break;
			    		}
		    		}
		    	break;
		    	case 1: // compare(account, m)
		    		if(ActiveAccounts.size() > 0) {
			    		while(true) {
			    			List<String> keys      = new ArrayList<String>(ActiveAccounts.keySet());
			    			String       rAccount = keys.get( r.nextInt(keys.size()) );
				    		if(ActiveAccounts.containsKey(rAccount)) {
				    			Integer n = ActiveAccounts.get(rAccount);
				    			int m = r.nextInt(20);
				    			String text = "compare," + rAccount + "," + m;
				    			PrintToFile(fileWriter, text);
				    			ActiveAccounts.remove(rAccount);
				    			if(m > n) {
				    				PayOrTake.put(rAccount, new Lottery("pay",m+1));
				    			} else {
				    				PayOrTake.put(rAccount, new Lottery("take",n+1));
				    			}
				    			break;
				    		}
			    		}
		    		}
		    	break;
		    	case 2: // pay or take
		    		if(PayOrTake.size() > 0) {
			    		while(true) {
			    			List<String> keys      = new ArrayList<String>(PayOrTake.keySet());
			    			String       rAccount = keys.get( r.nextInt(keys.size()) );
				    		if(PayOrTake.containsKey(rAccount)) {
				    			Lottery lottery = PayOrTake.get(rAccount);
				    			String text = lottery.getEvent()+"," + rAccount + "," + lottery.getAmount();
				    			PrintToFile(fileWriter, text);
				    			PayOrTake.remove(rAccount);
				    			break;
				    		}
			    		}
		    		}
		    	break;
		    	default:
		    		Error(RandomEvent);
		    	}
		    } //*/
		}    
	  
	private static void ThreeEventTest()  {
		HashMap<String, MaxMin> ActiveItems = new HashMap<String,MaxMin>();
		
		while(eventCount < numberOfEvents) {
	    	int RandomEvent = r.nextInt(3);
	    	switch(RandomEvent) {
	    	case 1: // create Item
	    		for(int i1 = 0 ;i1 < 10; i1++) {
		    		String rItem = getRandomChar(ActiveItems.size());
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
		    		String rItem = getRandomChar(ActiveItems.size());
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
		    		String rItem = getRandomChar(ActiveItems.size());
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
	  
	private static void OpenCloseFile() {
	    HashSet<String> openedFiles = new HashSet<String>();
	    
	    while(eventCount < numberOfEvents) {
			int RandomEvent;
			if(worstCase) {
				if(numberOfEvents/2 > eventCount)
					RandomEvent = 1;
				else 
					RandomEvent = 0;
			} else {
				RandomEvent = r.nextInt(2);
			}
	    	switch(RandomEvent) {
	    	case 1: // Open file
		    	boolean opened = false;
				while(!opened) {
					String fileName = getRandomChar(openedFiles.size());
					if(!openedFiles.contains(fileName)) {
						openedFiles.add(fileName);
						String text = "open,"+fileName;
						PrintToFile(fileWriter, text);
						opened = true;
					}
				}
			break;
			case 0: // close
				Iterator<String> iterator = openedFiles.iterator(); 
				      
				String filename;
				if(iterator.hasNext()) {
					filename = iterator.next();					   
					openedFiles.remove(filename);
					String text = "close,"+filename;
					PrintToFile(fileWriter, text);
				}
			break;
			default:
				System.out.println("??");
	    	} // switch
		} // while
	}
	
	/*public static String addChar() {
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
	}*/
	
	private static String getRandomChar(int size) {
		int nextCharIndex = 0;
		String finalchar = "";
		for(int i=0;i<(size/26)+1;i++) {
			nextCharIndex = r.nextInt(26);
			finalchar += (char)(nextCharIndex + 'a');
		}
		return finalchar;	
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
	
	private static void Error(int RandomEvent) {
		System.out.println("Something Went Wrong. Event - "+ RandomEvent);
		System.exit(0);
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
		
		public Lottery(String newEvent, int newAmount) {
			event = newEvent;
			amount = newAmount;
		}

		public String getEvent() {
			return event;
		}

		public void setEvent(String event) {
			this.event = event;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}
	}
	
	class MapCollection {
		private String map;
		private String collection;
		
		public MapCollection(String newMap, String newCollection) {
			this.map = newMap;
			this.collection = newCollection;
		}

		public String getMap() {
			return map;
		}

		public void setMap(String map) {
			this.map = map;
		}

		public String getCollection() {
			return collection;
		}

		public void setCollection(String collection) {
			this.collection = collection;
		}
	}
	
	class MapIterator {
		private String map;
		private String iterator;
		private boolean used;
		
		public MapIterator(String newMap, String newIterator) {
			this.map = newMap;
			this.iterator = newIterator;
			this.used = false;
		}
		
		public boolean isUsed() {
			return used;
		}
		
		public void setUsed(boolean newUsed) {
			this.used = newUsed;
		}
		
		public String getMap() {
			return map;
		}

		public void setMap(String map) {
			this.map = map;
		}

		public String getIterator() {
			return iterator;
		}

		public void setIterator(String iterator) {
			this.iterator = iterator;
		}
	}
