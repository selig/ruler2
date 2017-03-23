import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ActiveRuleSet {
	private HashMap<Integer,RuleActivation> ruleActivations;
	private HashMap<Integer,HashMap<String, RuleActivation>> ruleIDtoRuleActivationMapping;
	private int activeRuleCount;
	
	public ActiveRuleSet() {
		//this.id = GlobalVariables.getNextActiveRuleSetID();
		this.ruleActivations = new HashMap<Integer,RuleActivation>();
		ruleIDtoRuleActivationMapping = new HashMap<Integer,HashMap<String, RuleActivation>>();
		activeRuleCount = 0;
	}
	
	public boolean addNewActivation(RuleActivation newRuleActivation){
		int key = newRuleActivation.getRuleActivationID();
		if(ruleActivations.get(key) == null) {
			this.ruleActivations.put(key, newRuleActivation);
		}
			
		int ruleId = newRuleActivation.getRule().getRuleNameID();
		
		
		boolean result = addRuleIdToRuleIdRuleActivationMapping(ruleId,newRuleActivation);
		
		if(result) {
			Interface.log("\n" + "Rule Activated: " + newRuleActivation.getRuleActivationID() + " " + newRuleActivation.toString() + "\n");
			activeRuleCount++;
		}
		
		return result;
		//}
	}

	public RuleActivation[] getArrayOfRuleActivations() {
		return  (RuleActivation[]) ruleActivations.values().toArray(new RuleActivation[0]);
	}
	
	public boolean activeRuleExist(String RuleName){
		int key = GlobalFunctions.hash(RuleName);
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
	
	public String[] getActivations2() {
		
		String[] allActivations = new String[ruleActivations.size()];
		
		int count = 0;
		for(Integer key1 : ruleIDtoRuleActivationMapping.keySet()){
			HashMap<String,RuleActivation> map = ruleIDtoRuleActivationMapping.get(key1);
			if(map != null) {
				for(String key2 : map.keySet()) {
					 RuleActivation actRule = map.get(key2);
					allActivations[count] = "< " + actRule.toString() + " >";
					count++;
				}
			}
		}
		
		return allActivations;
	}

	public int getNumberOfActivations() {
		return activeRuleCount;
		//return ruleActivations.size();
	}

	public boolean deleteActivation(RuleActivation activation) {
		
		String parameterValueKey = activation.getParameterValueKey();
		
		int ruleID = activation.getRule().getRuleNameID();
		
		boolean result = deleteRuleActivationFromMapping(ruleID,parameterValueKey);
		
		if(result)
			activeRuleCount--;
		
		return result;
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
	
	public ArrayList<RuleActivation> findMatchingRules(Rule rule,
			int[] parameterIndexes, Map<Integer, ParameterBinding> parameterValues) {
		
		ArrayList<RuleActivation> TempArray = new ArrayList<RuleActivation>();
		
		activations : for(RuleActivation ruleActivation : ruleActivations.values() ){
			
			Rule ruleActivationRule = ruleActivation.getRule();
			int ruleActivationRuleId = ruleActivationRule.getRuleNameID();
			int conditionRuleId = rule.getRuleNameID();
			
			
			if(ruleActivationRuleId == conditionRuleId){
				//parameterIndexes = rule.getRuleParameterIndexes();
				
				for(int index : parameterIndexes) {
					String param1 = ruleActivation.getParameterBindingValue(index);
					ParameterBinding param2Binding = parameterValues.get(index);
					if(param2Binding != null) {
						String param2 = param2Binding.getParameterValue();
						if(!param1.equals(param2)){
							continue activations;
						}
					}
				}
				
				TempArray.add(ruleActivation);
			}
		}

		return TempArray;
	}

	public RuleActivation getRuleActivation(int ruleActivationKey) {
		return ruleActivations.get(ruleActivationKey);
	}
	
	private boolean addRuleIdToRuleIdRuleActivationMapping(int ruleId,
			RuleActivation newRuleActivation) {
		
		HashMap<String, RuleActivation> parameterToActivationMapping;
		
		if((parameterToActivationMapping = getRuleIdToRuleActivationMappingMap(ruleId)) != null) {
			
			//Integer parameterHashValue = newRuleActivation.getParameterHashValue();
			String parameterValueKey = newRuleActivation.getParameterValueKey();
			
			parameterToActivationMapping.put(parameterValueKey, newRuleActivation);
			
			
		} else {
			parameterToActivationMapping = new HashMap<String, RuleActivation>();
			
			parameterToActivationMapping.put(newRuleActivation.getParameterValueKey(), newRuleActivation);
			
			ruleIDtoRuleActivationMapping.put(ruleId, parameterToActivationMapping);
		}
		
		return true;
	}

	/*private boolean isSame(RuleActivation ruleExist,
			RuleActivation newRuleActivation) {
		
		if(ruleExist == null || newRuleActivation == null)
			return false;
		
		Map<Integer, ParameterBinding> parameters = ruleExist.getParameterBindings();
		Map<Integer, ParameterBinding> parameters2 = newRuleActivation.getParameterBindings();
		
		boolean parameterMatch;
		
		for(Integer key : parameters.keySet()){
			parameterMatch = false;
			for(Integer key2 : parameters2.keySet()) {
				String paramValue1 = parameters.get(key).getParameterValue();
				String paramValue2 = parameters2.get(key2).getParameterValue();
				
				if(paramValue1.equals(paramValue2)) {
					parameterMatch = true;
					break;
				}
			}
			if(!parameterMatch) {
				return false;
			}
		}
		return true;
	}*/

	private HashMap<String, RuleActivation> getRuleIdToRuleActivationMappingMap(
			int ruleId) {
		return ruleIDtoRuleActivationMapping.get(ruleId);
	}

	public RuleActivation getRuleActivationFromMapping(int ruleId, String parameterValueKey) {
		
		HashMap<String, RuleActivation> map = ruleIDtoRuleActivationMapping.get(ruleId);
		
		if(map != null) 
			return map.get(parameterValueKey);
		else 
			return null;
	}
	
	public boolean deleteRuleActivationFromMapping(int ruleId, String parameterValueKey) {
		
		HashMap<String, RuleActivation> map = ruleIDtoRuleActivationMapping.get(ruleId);
		
		if(map == null)
			return false;
				
		map.remove(parameterValueKey);
		
		if(map.size() == 0)
			ruleIDtoRuleActivationMapping.remove(ruleId);
		
		return true;		
	}
}
