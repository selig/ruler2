
public class ParameterBinding {
	
	private final Parameter parameter;
	private final String parameterValue;
	private final RuleActivation ruleActivation;

	public ParameterBinding(Parameter newParameter, String newParameterValue, RuleActivation newRuleActivation) {
		this.ruleActivation = newRuleActivation;
		this.parameter = newParameter;
		this.parameterValue = newParameterValue;
		assignVariableValue(parameterValue);
	}

	public Parameter getParameter() {
		return parameter;
	}

	public String getParameterValue() {
		return parameterValue;
	}
	
	private void assignVariableValue(String parameterValue) {
		// Get Variable indexes
		Integer[] indexes = this.parameter.getParameterVariables();
		
		Variable[] variables = new Variable[indexes.length];
		int index = 0;
		// Get Variables
		for(int i : indexes){
			variables[index] = this.parameter.getRule().getVariable(i);
		}
		
		if(indexes.length == 1) {
			this.ruleActivation.addVariableBinding(new VariableBinding(variables[0], this.parameterValue));
		} else {
			
			int parameterValueInt = Integer.parseInt(this.parameterValue);
			
			int variableIndex = 0;
			int numberIndex = -1;
			int notNumberIndex = -1;
			// Check if any of variables is number
			for(Variable var : variables){
				if(var.isNumber()) {
					numberIndex=variableIndex;
				} else notNumberIndex = variableIndex;
				variableIndex++;
			}
			// If any of these are Number
			if(numberIndex > -1) {
				// Not Number Variable Value
				int notNumberValue = this.parameter.getVariableValue(parameterValueInt, variables[numberIndex].getValue());
				
				this.ruleActivation.addVariableBinding(new VariableBinding(variables[notNumberIndex], notNumberValue));
			} else { // If no numbers but two variables
				// Can't assign variable value
				System.out.println("Can't assign variable value");
				return;
			}
		}
	}
}
