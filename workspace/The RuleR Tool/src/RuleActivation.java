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
	
	public RuleActivation(Rule newRuleName, String parameters) {
		this.rule = newRuleName;
		initialiseVariableBinding();
		this.parameterBindings = getParameterBindingSet(newRuleName,parameters);
	}
	
	public RuleActivation(String ruleName, Map<Integer,ParameterBinding> parameters) {
		this.rule = Interface.ruleSystem.getRule(ruleName);
		initialiseVariableBinding();
		this.parameterBindings = getParameterBindingSet(this.rule,parameters);
	}
	
	private void initialiseVariableBinding() {
		variableBinding = new HashMap<Integer, VariableBinding>();	
	}
	
	public void addVariableBinding(VariableBinding newVariableBinding) {
		variableBinding.put(newVariableBinding.getKey(), newVariableBinding);
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
			int i=0;
			// For Each Index
			for(int index : parameterIndexes){
				// Get Parameter
				Parameter param = rule.getParameter(index);
				
				// Create and add new ParameterBinding to temp Set
				tempSet.put(index,new ParameterBinding(param, parametersArray[i], this));
				
				i++;				
			}			
		} else {
			return null;
		}
		
		return tempSet;	
	}

	private Map<Integer,ParameterBinding> getParameterBindingSet(Rule rule, 
																Map<Integer,ParameterBinding> parameters) {
		// Initialise temporary Set
		Map<Integer,ParameterBinding> tempSet = new HashMap<Integer,ParameterBinding>();
		
		// Get parameter Array of the rule
		int[] parameterIndexes = rule.getRuleParameterIndexes();
			
		// For Each Index
		for(int index : parameterIndexes){
			// Get Parameter
			Parameter param = rule.getParameter(index);
			
			// Create and add new ParameterBinding to temp Set
			tempSet.put(index,new ParameterBinding(param, parameters.get(index).getParameterValue(), this));				
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
		return this.rule.getRuleName() + " [" + parameterBindings.size() + "]";
	}

	public ParameterBinding getParameterBinding(int key) {
		return parameterBindings.get(key);
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
}
