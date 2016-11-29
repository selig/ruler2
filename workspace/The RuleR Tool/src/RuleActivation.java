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
	private Map<Integer,VariableBinding> variableBinding;
	private final int ruleActivationID;
	
	public RuleActivation(Rule newRuleName, String parameters) {
		this.rule = newRuleName;
		initialiseVariableBinding();
		this.parameterBindings = getParameterBindingSet(newRuleName,parameters);
		this.ruleActivationID = GlobalFunctions.hashName(this.rule.getRuleNameID() + getOnlyRuleParameters());
	}
	
	public RuleActivation(String ruleName, Map<Integer,ParameterBinding> parameters, int[] consequentIndexes) {
		System.out.println("Look for the rule with - " + ruleName + " name");
		this.rule = Interface.ruleSystem.getRule(ruleName);
		initialiseVariableBinding();
		this.parameterBindings = getParameterBindingSet(this.rule,parameters,consequentIndexes);
		this.ruleActivationID = GlobalFunctions.hashName(this.rule.getRuleNameID() + getOnlyRuleParameters());
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

	private Map<Integer,ParameterBinding> getParameterBindingSet(Rule rule,String parameters) {
		
		// Initialise temporary Set
		Map<Integer,ParameterBinding> tempSet = new HashMap<Integer,ParameterBinding>();
		
		// Get parameter Array of the rule
		int[] parameterIndexes = rule.getRuleParameterIndexes();
		
		// Create Array from passed Parameters
		String[] parametersArray = parameters.split(",");
		
		// Compare Size of each array
		if(parameterIndexes.length == parametersArray.length) {
			System.out.println("Not supposed to match");
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
		int[] parameterIndexes = rule.getRuleParameterIndexes();
		
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
	
	public String getOnlyRuleParameters() {
		int[] ruleIndexes = this.rule.getRuleParameterIndexes();
		String finalValue = "";
		for(int index : ruleIndexes) {
			finalValue += parameterBindings.get(index).getParameterValue()+",";
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

	public int getVariableValue(Integer key) {
		return variableBinding.get(key).getVariableValueInt();
	}
	
	public Set<Entry<Integer,ParameterBinding>> getParameterEntries() {
		return parameterBindings.entrySet();
	}

	public String getParameterValue(int ruleParamIndex) {
		return parameterBindings.get(ruleParamIndex).getParameterValue();
	}

	public Integer getParameterHashValue() {
		return GlobalFunctions.hashName(getParameters());
	}

	public int getRuleActivationID() {
		return ruleActivationID;
	}
}
