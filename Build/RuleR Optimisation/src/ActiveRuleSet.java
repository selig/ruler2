import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ActiveRuleSet {
	//private final int id;
	//              RuleID, RuleActivation
	private HashMap<Integer,RuleActivation> ruleActivations;
	private HashMap<Integer,HashMap<Integer, RuleActivation>> ruleIDtoRuleActivationMapping;
	
	public ActiveRuleSet() {
		//this.id = GlobalVariables.getNextActiveRuleSetID();
		this.ruleActivations = new HashMap<Integer,RuleActivation>();
		ruleIDtoRuleActivationMapping = new HashMap<Integer,HashMap<Integer, RuleActivation>>();
	}
	
	public void addNewActivation(RuleActivation newRuleActivation){
		int key = newRuleActivation.getRuleActivationID();
		
		if(ruleActivations.get(key) == null) {
			this.ruleActivations.put(key, newRuleActivation);
			
			int ruleId = newRuleActivation.getRule().getRuleNameID();
			
			addRuleIdToRuleIdRuleActivationMapping(ruleId,newRuleActivation);
			
			Interface.log("Rule Activated: " + key + " " + newRuleActivation.toString() + "\n");
		}
	}

	public RuleActivation[] getArrayOfRuleActivations() {
		return  (RuleActivation[]) ruleActivations.values().toArray(new RuleActivation[0]);
	}
	
	public boolean activeRuleExist(String RuleName){
		int key = GlobalFunctions.hashName(RuleName);
		//System.out.println("-------   Find active rule " + RuleName + " " + key);
		return this.ruleActivations.get(key) != null;
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

	public boolean deleteActivation(RuleActivation activation) {
		int ruleActivationKey = activation.getRuleActivationID();
		//System.out.println("Rule Activation key: " + ruleActivationKey); 
		if(null == ruleActivations.remove(ruleActivationKey)){
			return false;
		} else {
			return true;
		}
	}

	public ArrayList<RuleActivation> findMatchingRule(int[] sharedParamIndex, int ruleNameID, Map<Integer, ParameterBinding> parameterValues) {

		ArrayList<RuleActivation> TempArray = new ArrayList<RuleActivation>();
		activations : for(RuleActivation ruleActivation : ruleActivations.values() ){
			if(ruleActivation.getRule().getRuleNameID() == ruleNameID){
				for(int index: sharedParamIndex) {
					String param1 = ruleActivation.getParameterBindingValue(index);
					String param2 = parameterValues.get(index).getParameterValue();
					if(!param1.equals(param2)){
						continue activations;
					}
				}
				TempArray.add(ruleActivation);
			}
		}
		return TempArray;
		
	}
	
	//public 

	public RuleActivation getRuleActivation(int ruleActivationKey) {
		return ruleActivations.get(ruleActivationKey);
	}
	
	private void addRuleIdToRuleIdRuleActivationMapping(int ruleId,
			RuleActivation newRuleActivation) {
		
		HashMap<Integer, RuleActivation> parameterToActivationMapping;
		
		if((parameterToActivationMapping = getRuleIdToRuleActivationMappingMap(ruleId)) != null) {
			
			parameterToActivationMapping.put(newRuleActivation.getParameterHashValue(), newRuleActivation);
			
		} else {
			parameterToActivationMapping = new HashMap<Integer, RuleActivation>();
			
			parameterToActivationMapping.put(newRuleActivation.getParameterHashValue(), newRuleActivation);
			
			ruleIDtoRuleActivationMapping.put(ruleId, parameterToActivationMapping);
		}
	}

	private HashMap<Integer, RuleActivation> getRuleIdToRuleActivationMappingMap(
			int ruleId) {
		return ruleIDtoRuleActivationMapping.get(ruleId);
	}

	public RuleActivation getRuleActivationFromMapping(int ruleId, int parameterHash) {
		HashMap<Integer, RuleActivation> map = ruleIDtoRuleActivationMapping.get(ruleId);
		if(map != null)
			return map.get(parameterHash);
		else 
			return null;
	}
}
