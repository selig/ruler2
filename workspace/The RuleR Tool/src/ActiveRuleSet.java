import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ActiveRuleSet {
	//private final int id;
	//              RuleID, RuleActivation
	private HashMap<Integer,RuleActivation> ruleActivations;
	
	public ActiveRuleSet() {
		//this.id = GlobalVariables.getNextActiveRuleSetID();
		this.ruleActivations = new HashMap<Integer,RuleActivation>();
	}
	
	public void addNewActivation(Integer key,RuleActivation newRuleActivation){
		/*
		 * 
		 * What happens if the same rule activation is added to the hashmap?
		 * later, how to recognise if the activation is for the same..
		 * 
		 */
		this.ruleActivations.put(key, newRuleActivation);
	}
	
	public RuleActivation[] getArrayOfRuleActivations() {
		return  (RuleActivation[]) ruleActivations.values().toArray(new RuleActivation[0]);
	}
	
	public boolean activeRuleExist(String RuleName){
		return this.ruleActivations.get(GlobalFunctions.hashName(RuleName)) != null;
	}
	
	public String[] getActivations() {
		
		String[] allActivations = new String[ruleActivations.size()];
		
		// Get a set of the entries
	    Set<Entry<Integer, RuleActivation>> set = ruleActivations.entrySet();
	    
	    // Get an iterator
	    Iterator<Entry<Integer, RuleActivation>> i = set.iterator();
	    
	    int index = 0;
	    
	    // Display elements
	    while(i.hasNext()) {
	       @SuppressWarnings("rawtypes")
	       Map.Entry ruleElement = (Map.Entry)i.next();
	       allActivations[index] = "<"+ruleElement.getValue().toString()+">";
	       index++;
	    }
		
		return allActivations;
	}

	public int getNumberOfActivations() {
		return ruleActivations.size();
	}

	public void deleteActivation(RuleActivation activation) {
		ruleActivations.remove(activation.getRule().getRuleID());		
	}
}
