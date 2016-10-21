import java.util.ArrayList;
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
	
public void addPredifinedRules() {
		
		String RuleString ="<None Step Open(file) { [write(file)<> ¬> Write(file)][write(file)<> ¬> Write(file)][write(file)<> ¬> Write(file) ] }>";
		
		String[] RuleAndEvent = RuleString.split("{");
		
		String[] RuleInfo = RuleAndEvent[0].replaceAll("<", "").split(" ");
		
		String extraModifier = RuleInfo[0];
		String modifier = RuleInfo[1];
		String ruleName = RuleInfo[2].split("(")[0];
		String ruleParameters = RuleInfo[2].split("(")[1].replaceAll(")", "");
		
		
		String[] Events = RuleAndEvent[1].replace("\\s+", "").split("][");
		
		ArrayList<RuleBinding> ruleBindings = new ArrayList<RuleBinding>();
		
		for(String event : Events) {
			String[] eventSplit = event.split("¬>");
			
			String eventName = eventSplit[0].split("(")[0];
			
			String[] eventParameters = eventSplit[0].split("(")[1].split("<")[0].replaceAll(")","").split(",");
			
			String[] eventConditions = eventSplit[0].split("(")[1].split("<")[1].replaceAll(">","").split(",");
			
			String[] consRules = eventSplit[1].split(",");
			

			ArrayList<ConsequentRule> consequentRules = new ArrayList<ConsequentRule>();
			
			for(String cons : consRules) {
				
				String consequentName = eventSplit[1].split("(")[0];
				
				String[] consequentParameters = eventSplit[1].split("(")[1].replaceAll(")", "").split(",");
				
				consequentRules.add(new ConsequentRule(consequentName, consequentParameters));
			}

			ruleBindings.add(new RuleBinding(eventName, eventParameters, eventConditions, consequentRules));
		}
		
		
		this.addNewRule(new Rule(ruleName, modifier, extraModifier, ruleParameters, ruleBindings));
			
		
	}


}
