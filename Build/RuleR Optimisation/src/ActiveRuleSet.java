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
	private int activeRuleCount;
	
	public ActiveRuleSet() {
		//this.id = GlobalVariables.getNextActiveRuleSetID();
		this.ruleActivations = new HashMap<Integer,RuleActivation>();
		ruleIDtoRuleActivationMapping = new HashMap<Integer,HashMap<Integer, RuleActivation>>();
		activeRuleCount = 0;
	}
	
	public boolean addNewActivation(RuleActivation newRuleActivation){
		int key = newRuleActivation.getRuleActivationID();
		if(ruleActivations.get(key) == null) {
			this.ruleActivations.put(key, newRuleActivation);
		}
			
		int ruleId = newRuleActivation.getRule().getRuleNameID();
		
		Interface.log("Rule Activated: " + newRuleActivation.getRuleActivationID() + " " + newRuleActivation.toString() + "\n");
		
		boolean result = addRuleIdToRuleIdRuleActivationMapping(ruleId,newRuleActivation);
		
		if(result)
			activeRuleCount++;
		
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
			HashMap<Integer,RuleActivation> map = ruleIDtoRuleActivationMapping.get(key1);
			if(map != null) {
				for(Integer key2 : map.keySet()) {
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
		
		int ruleActivationKey = activation.getRuleActivationID();
		
		int parameterHashValue = activation.getParameterHashValue();
		
		int ruleID = activation.getRule().getRuleNameID();
		
		boolean result = deleteRuleActivationFromMapping(ruleID,parameterHashValue,ruleActivationKey);
		
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
		
		HashMap<Integer, RuleActivation> parameterToActivationMapping;
		
		if((parameterToActivationMapping = getRuleIdToRuleActivationMappingMap(ruleId)) != null) {
			
			RuleActivation ruleExist;
			/*if((ruleExist= parameterToActivationMapping.get(newRuleActivation.getParameterHashValue())) != null) {
				System.out.println("Rule Already exists.. \n" + ruleExist);
				return false;
			}*/
			parameterToActivationMapping.put(newRuleActivation.getParameterHashValue(), newRuleActivation);
			
		} else {
			parameterToActivationMapping = new HashMap<Integer, RuleActivation>();
			
			parameterToActivationMapping.put(newRuleActivation.getParameterHashValue(), newRuleActivation);
			
			ruleIDtoRuleActivationMapping.put(ruleId, parameterToActivationMapping);
		}
		
		return true;
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
	
	public boolean deleteRuleActivationFromMapping(int ruleId, int parameterHash, int ruleActivationID) {
		HashMap<Integer, RuleActivation> map = ruleIDtoRuleActivationMapping.get(ruleId);
		
		if(map == null)
			return false;
		
		RuleActivation activation = map.get(parameterHash);
		
		if(activation == null)
			return false;
		
		if(activation.getRuleActivationID() != ruleActivationID)
			return false;
		
		map.remove(parameterHash);
		
		if(map.size() == 0)
			ruleIDtoRuleActivationMapping.remove(ruleId);
		
		return true;		
	}
}
