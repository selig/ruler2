import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class RuleActivation {
	
	
	// The Rule
	private final Rule rule;
	// The Parameters
	private final Map<Integer,ParameterBinding> parameterBindings;
	// The Parameter Values
	private HashMap<Integer,VariableBinding> variableBinding;
	private final int parameterHashValue;
	private final int ruleActivationID;
	
	public RuleActivation(Rule newRuleName, String parameters) {
		this.rule = newRuleName;
		initialiseVariableBinding();
		this.parameterBindings = getParameterBindingSet(newRuleName,parameters);
		String parameterValues = getOnlyRuleParameters();
		this.parameterHashValue = GlobalFunctions.hash(parameterValues);
		this.ruleActivationID = GlobalFunctions.hash(this.rule.getRuleNameID() +parameterValues);
	}
	
	public RuleActivation(String ruleName, Map<Integer,ParameterBinding> parameters, int[] consequentIndexes) {
		//System.out.println("Look for the rule with - " + ruleName + " name");
		this.rule = Interface.ruleSystem.getRule(ruleName);
		initialiseVariableBinding();
		this.parameterBindings = getParameterBindingSet(this.rule,parameters,consequentIndexes);
		String parameterValues = getOnlyRuleParameters();
		this.parameterHashValue = GlobalFunctions.hash(parameterValues);
		this.ruleActivationID = GlobalFunctions.hash(this.rule.getRuleNameID() + parameterValues);
	}
	
	private void initialiseVariableBinding() {
		variableBinding = new HashMap<Integer, VariableBinding>();	
	}
	
	public void addVariableBinding(VariableBinding newVariableBinding) {
		variableBinding.put(newVariableBinding.getKey(), newVariableBinding);
	}
	
	public Object getVariableValue(Variable variable) {
		return variableBinding.get(variable.getKey()).getVariableValue();
	}
	
	public VariableBinding getVariableBinding(Variable variable) {
		return variableBinding.get(variable.getKey());
	}
	
	public VariableBinding getVariableBinding(Integer key) {
		return variableBinding.get(key);
	}

	private Map<Integer,ParameterBinding> getParameterBindingSet(Rule rule,String parameters) {
		
		// Initialise temporary Set
		Map<Integer,ParameterBinding> tempSet = new HashMap<Integer,ParameterBinding>();
		
		// Get parameter Array of the rule
		Integer[] parameterIndexes = rule.getRuleParameterIndexes();
		
		// Create Array from passed Parameters
		String[] parametersArray = parameters.split(",");
		
		// Compare Size of each array
		if(parameterIndexes.length == parametersArray.length) {
			//System.out.println("Not supposed to match");
			int i=0;
			// For Each Index
			for(int index : parameterIndexes){
				// Get Parameter
				Parameter param = rule.getParameter(index);
				
				// Create and add new ParameterBinding to temp Set
				tempSet.put(index,new ParameterBinding(param, parametersArray[i], this));
				
				i++;				
			}			
		}
		
		return tempSet;	
	}

	private Map<Integer,ParameterBinding> getParameterBindingSet(Rule rule, 
																Map<Integer,ParameterBinding> parameters,int[] consequentIndexes) {
		// Initialise temporary Set
		Map<Integer,ParameterBinding> tempSet = new HashMap<Integer,ParameterBinding>();
		
		// Get parameter Array of the rule
		Integer[] parameterIndexes = rule.getRuleParameterIndexes();
		
		int i = 0;
		// For Each Index
		for(int index : parameterIndexes){
			// Get Parameter
			Parameter param = rule.getParameter(index);
			
			// Create and add new ParameterBinding to temp Set
			tempSet.put(index,new ParameterBinding(param, parameters.get(consequentIndexes[i]).getParameterValue(), this));
			
			i++;
		}
		
		return tempSet;	
	}
	
	public Rule getRule() {
		return rule;
	}

	public Map<Integer,ParameterBinding> getParameterBindings() {
		return parameterBindings;
	}
	
	public Map<Integer,ParameterBinding> getRuleParameterBindings() {
		Integer[] ruleIndexes = this.rule.getRuleParameterIndexes();
		Map<Integer,ParameterBinding> tempMap = new HashMap<Integer,ParameterBinding>();
		for(int index : ruleIndexes) {
			tempMap.put(index, parameterBindings.get(index));
		}
		return tempMap;
	}
	
	@Override
	public String toString() {
		
		return this.rule.getRuleName() + " [" + getParameters() + "]  key: " + ruleActivationID  ;
	}

	public String getParameters() {
		String finalValue = "";
		for(ParameterBinding param : parameterBindings.values()) {
			finalValue += param.getParameterValue()+",";
		}
		
		return GlobalFunctions.subStringLast(finalValue, 1);
	}
	
	/* Modification:
	 * change from getting only rule parameters to 
	 * getting only rule parameters which match with the events for that rule 
	 * unless that rule does not contain any events
	 * */
	public String getOnlyRuleParameters() {
		
		String finalValue = "";
		int[] matchRuleIndex = rule.getRuleMatchingParameterIndexArray();
		if(matchRuleIndex != null) {
			for(int index : matchRuleIndex) {
				ParameterBinding paramBinding = parameterBindings.get(index);
				if(paramBinding != null) {
					finalValue += paramBinding.getParameterValue()+",";
				}
			}
		} else {
			Integer[] ruleIndexes = this.rule.getRuleParameterIndexes();
			for(int index : ruleIndexes) {
				ParameterBinding paramBinding = parameterBindings.get(index);
				if(paramBinding != null) {
					finalValue += paramBinding.getParameterValue()+",";
				}
			}
		}
		
		return GlobalFunctions.subStringLast(finalValue, 1);
	}

	public ParameterBinding getParameterBinding(int key) {
		return parameterBindings.get(key);
	}
	
	public String getParameterBindingValue(int key) {
		if(parameterBindings.get(key) != null)
			return parameterBindings.get(key).getParameterValue();
		else
			return null;
	}

	public String getVariableValue(Integer key) {
		VariableBinding var = variableBinding.get(key);
		
		if(var == null)
			return null;
		
		return (String) var.getVariableValue();
	}
	
	public Set<Entry<Integer,ParameterBinding>> getParameterEntries() {
		return parameterBindings.entrySet();
	}

	public String getParameterValue(int ruleParamIndex) {
		return parameterBindings.get(ruleParamIndex).getParameterValue();
	}

	public int getRuleActivationID() {
		return ruleActivationID;
	}
	
	public int getParameterHashValue() {
		return this.parameterHashValue;
	}
	
	public HashMap<Integer, VariableBinding> getVariableBindings() {
		return this.variableBinding;
	}
	
	public static int getRuleActivationKey(Rule rule, Map<Integer,ParameterBinding> parameters) {
		
		Integer[] ruleIndexes = rule.getRuleParameterIndexes();
		String finalValue = "";
		for(int index : ruleIndexes) {
			finalValue += parameters.get(index).getParameterValue()+",";
		}
		
		finalValue = GlobalFunctions.subStringLast(finalValue, 1);
		
		
		return GlobalFunctions.hash(rule.getRuleNameID() + finalValue);
	}

	public boolean match(String[] parameterValues, int eventID) {
		Integer[] matchingIndexes;
		
		if(eventID == 0)
			matchingIndexes = this.rule.getRuleParameterIndexes();
		else
			matchingIndexes = this.rule.getEventToRuleParameterMatching(eventID);
		
		for(int i=0;i<matchingIndexes.length;i++) {
			
			String ruleactivationValue = parameterBindings.get(matchingIndexes[i]).getParameterValue();
			
			if(!parameterValues[i].equals(ruleactivationValue))
				return false;
		}
		
		return true;
	}
}
