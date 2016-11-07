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
		//System.out.println("Add new Rule \"" + myRule.getRuleName() + "\" With hashCode " + myRule.getRuleNameID());
		Integer key = myRule.getRuleNameID();
		if(!list.containsKey(key)) {
			list.put(key, myRule);
			return true;
		}
		else
		   return false;
	}
	
	public String[] getRules() {
		
		String[] allRules = new String[list.size()];
		
		// Get a set of the entries
	    Set<Entry<Integer, Rule>> set = list.entrySet();
	    
	    // Get an iterator
	    Iterator<Entry<Integer, Rule>> i = set.iterator();
	    
	    int index = 0;
	    
	    // Display elements
	    while(i.hasNext()) {
	       @SuppressWarnings("rawtypes")
	       Map.Entry ruleElement = (Map.Entry)i.next();
	       allRules[index] = "<"+ruleElement.getValue().toString()+">";
	       index++;
	    }
		
		return allRules;
	}

	public String getOneRule(Integer key) {
		return "<"+list.get(key).toString()+">";
	}
	
	public static int getRuleID(String name) {
		Rule rule = list.get(GlobalFunctions.hashName(name));
		if(rule == null) return -1;
		else return rule.getRuleNameID();
	}
	
	public Rule getRule(String name) {
		int key = GlobalFunctions.hashName(name);
		//System.out.println("Get Rule \"" + name +"\" with hashCode " + key);
		return list.get(key);
	}
	
	public static int getRuleID(Integer key) {
		Rule rule = list.get(key);
		if(rule == null) return -1;
		else return rule.getRuleNameID();
	}

	
	public static String getRuleName(int ruleID) {
		Rule rule = list.get(ruleID);
		if(rule == null) return null;
		else return rule.getRuleName();
	}

	public int getNumberOfRules() {
		return list.size();
	}
	
	public void activateRules(ActiveRuleSet activeRuleSet) {
			for(Rule rule : list.values()){
				if(rule.getRuleModifier() == Rule.Modifier.Always || rule.getExtraModifier()== Rule.ExtraModifier.Start){
					activeRuleSet.addNewActivation(rule.getRuleNameID(), new RuleActivation(rule, ""));
				}
			}	
	}
	
	public void addPredifinedRules(String RuleString) {
		
		//String RuleString ="<None Step Open(file) { [write(file)<> ¬> Write(file)][write(file)<> ¬> Write(file)][write(file)<> ¬> Write(file) ] }>";
		
		String[] RuleAndEvent = RuleString.split("\\{");
		
		String[] RuleInfo = RuleAndEvent[0].replaceAll("<", "").split(" ");
		
		String extraModifier = RuleInfo[0];
		String modifier = RuleInfo[1];
		String ruleName = RuleInfo[2].split("\\(")[0];
		String ruleParameters = RuleInfo[2].split("\\(")[1].replaceAll("\\)", "");
		
		
		String[] Events = RuleAndEvent[1].replace("\\s+", "").split("\\]\\[");
		
		ArrayList<RuleBinding> ruleBindings = new ArrayList<RuleBinding>();
		
		for(String event : Events) {
			String[] eventSplit = event.split("¬>");
			
			String eventName = GlobalFunctions.removeWhiteSpaces(eventSplit[0]).split("\\(")[0].replaceAll("\\[", "");
			
			String[] eventParameters = eventSplit[0].split("\\(")[1].split("<")[0].replaceAll("\\)","").split(",");
			
			String[] eventConditions = eventSplit[0].split("<")[1].replaceAll(">","").split(",");
			
			String[] consRules = eventSplit[1].split(",");
			
			////System.out.println("--> " + consRules);

			ArrayList<ConsequentRule> consequentRules = new ArrayList<ConsequentRule>();
			
			for(@SuppressWarnings("unused") String cons : consRules) {
				
				String consequentName = eventSplit[1].split("\\(")[0];
				
				try {
				String[] consequentParameters = eventSplit[1].split("\\(")[1].replaceAll("\\)", "").split(",");
				
				consequentRules.add(new ConsequentRule(consequentName, consequentParameters));
				} catch(Exception e) {
					
					String[] consequentParameters = new String[0];
					
					consequentName = eventSplit[1].split("\\]")[0];
					////System.out.println("-- -- --> " + consequentName);
					
					consequentRules.add(new ConsequentRule(consequentName, consequentParameters));
				}
			}

			ruleBindings.add(new RuleBinding(eventName, eventParameters, eventConditions, consequentRules));
		}
		
		
		this.addNewRule(new Rule(ruleName, modifier, extraModifier, ruleParameters, ruleBindings));
			
		
	}
}
