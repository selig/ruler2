import java.util.HashMap;
import java.util.Map;

public class RuleActivation {
	
	private final Rule rule;
	private final Map<Integer,ParameterBinding> parameterBindings;
	
	public RuleActivation(Rule newRuleName, String parameters) {
		this.rule = newRuleName;
		this.parameterBindings = getParameterBindingSet(newRuleName,parameters);		
	}

	public RuleActivation(String ruleName, Map<Integer,ParameterBinding> parameters) {
		this.rule = Interface.ruleSystem.getRule(ruleName);
		this.parameterBindings = getParameterBindingSet(this.rule,parameters);
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
				tempSet.put(index,new ParameterBinding(param, parametersArray[i]));
				
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
			
		int i=0;
		// For Each Index
		for(int index : parameterIndexes){
			// Get Parameter
			Parameter param = rule.getParameter(index);
			
			// Create and add new ParameterBinding to temp Set
			tempSet.put(index,new ParameterBinding(param, parameters.get(index).getParameterValue()));
			
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
		return this.rule.getRuleName() + " [" + parameterBindings.size() + "]";
	}
}
