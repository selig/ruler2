/**
 * Parameter Binding is a class which represents full value of the parameter.
 * 
 * It assigns parameter value from passed value, but checks if the parameter has math by creating this result(value)
 * 
 * If the parameter value was i.e. x+1 then assignVariableValue function will find x by x = value - 1 and will put x variable value into a Map inside Rule Activation
 * 
 * @author mbax2md4
 *
 */
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
		} else {// check if parameter (i.e. x+1) has only one variable. which means that it can't be x+y.
			
			
			try{
				// Assume all values has to be Integers
				int parameterValueInt = Integer.parseInt(this.parameterValue);
			
				int variableIndex = 0;
				int numberIndex = -1;
				int notNumberIndex = -1;
				// Find Numeric and Not Numeric Value indexes in Parameter Equation
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
					
					// check if we have any of the variable value stored already
					VariableBinding variable1value = this.ruleActivation.getVariableBinding(variables[0]);
					VariableBinding variable2value = this.ruleActivation.getVariableBinding(variables[1]);
					
					// only one variable can have value one has to be null
					if((variable1value != null && variable2value == null) || (variable1value == null && variable2value != null)) {
						if(variable1value != null) {
							if(variable1value.isInteger()) {
								int value = this.parameter.getVariableValue(parameterValueInt, variable1value.getVariableValueInt());
								this.ruleActivation.addVariableBinding(new VariableBinding(variables[1], value));
							} else {
								System.out.println("Can't assign variable value because variable 1 is String");
							}
						} else {
							if(variable2value.isInteger()) {
								int value = this.parameter.getVariableValue(parameterValueInt, variable2value.getVariableValueInt());
								this.ruleActivation.addVariableBinding(new VariableBinding(variables[0], value));
							} else {
								System.out.println("Can't assign variable value because variable 2 is String");
							}
						}
					
					} else {
						// Can't assign variable value
						System.out.println("Can't assign variable value. They Both has value or both don't have");
						return;
					}
				}
			} catch(Exception e) {
				System.out.println("Parameter value not integer");
			}
		}
	}
}
