import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class RuleSystem {
	
	private static HashMap<Integer,Rule> list;
	
	
	public RuleSystem() {
		list = new HashMap<Integer,Rule>();
	}

	public boolean addNewRule(Rule myRule) {
		Integer key = myRule.getRuleNameID();
		if(!list.containsKey(key)) {
			list.put(key, myRule);
			return true;
		}
		else
		   return false;
	}
	
	public String getRules() {
		
		String allRules = "";
		
		// Get a set of the entries
	    Set<Entry<Integer, Rule>> set = list.entrySet();
	    
	    // Get an iterator
	    Iterator<Entry<Integer, Rule>> i = set.iterator();
	    
	    // Display elements
	    while(i.hasNext()) {
	       @SuppressWarnings("rawtypes")
	       Map.Entry ruleElement = (Map.Entry)i.next();
	       allRules += "<"+ruleElement.getValue().toString()+"> ; ";
	    }
		
		return allRules;
	}

	public String getOneRule(Integer key) {
		return "<"+list.get(key).toString()+">";
	}
	
	public static int getRuleID(String name) {
		Rule rule = list.get(GlobalFunctions.hashName(name));
		if(rule == null) return -1;
		else return rule.getRuleID();
	}
	
	public static int getRuleID(Integer key) {
		Rule rule = list.get(key);
		if(rule == null) return -1;
		else return rule.getRuleID();
	}

	
	public static String getRuleName(int ruleID) {
		Rule rule = list.get(ruleID);
		if(rule == null) return null;
		else return rule.getRuleName();
	}

	public int getNumberOfRules() {
		return list.size();
	}

}
