/**
 * This class represents Parameter object in order to handle situations
 * when parameter = parameter +1 
 * 
 * @author mbax2md4
 *
 */
public class Parameter {
	
	private final String PLUS_REGEX = "\\+";
	private final String MINUS_REGEX = "\\-";
	private final String MULTIPLY_REGEX = "\\*";
	private final String DIVIDE_REGEX = "\\/";
	
	private final String PLUS = "+";
	private final String MINUS = "-";
	private final String MULTIPLY = "*";
	private final String DIVIDE = "/";
	
	private enum Operator{plus,minus,multiply,divide};
	
	private Integer[] parameterVariables;
	private Operator parameterOperation = null;
	private final String name;
	private final Rule rule;
	//private final paramType;
	
	/**
	 * Constructor for Parameter
	 * @param name
	 */
	public Parameter(String name, Rule rule) {
		
		this.rule = rule;
		
		// parameter; like param+1 
		this.name = GlobalFunctions.removeWhiteSpaces(name);
		//this.paramType = newParamType;s
		
		// Get operator such as +
		parameterOperation = operation(name);
		
		String OperatorCharToSplit;
		String[] splitName;
		
		// Split parameter String by operator if exists one
		if((OperatorCharToSplit = getRegexChar(parameterOperation)) != null)
			splitName = GlobalFunctions.removeWhiteSpaces(name).split(OperatorCharToSplit);
		else {
			splitName = new String[1];
			splitName[0] = GlobalFunctions.removeWhiteSpaces(name);
		}
		
		// Create parameterVariables array of indexes
		parameterVariables = new Integer[splitName.length];
		
		
		int index = 0;
		
		// For each parameter
		for(String variableName : splitName){
			// If array of variables in Rule contains this variable name
			if(rule.containsVariable(variableName)) {
				// Assign index as hashName of variable
				parameterVariables[index] = GlobalFunctions.hashName(variableName);
			} else {
				// Otherwise, create new instance of variable
				Variable newVariable = createVariableInstance(variableName);
				// Assign index as hashName of variable
				parameterVariables[index] = newVariable.getKey();
				// Add variable to the Rule array
				rule.addVariable(newVariable);
			}
			index++;
		}

		
		
	}
	
	private Variable createVariableInstance(String variableName) {
		try {
			int variableValue = Integer.parseInt(variableName);
			return new VariableNumber(variableName, variableValue);
		} catch (Exception e) {
			return new Variable(variableName);
		}
	}

	private String getRegexChar(Operator operator) {
		if(operator == null) return null;
		switch (operator) {
		case plus:
			return PLUS_REGEX;
		case minus:
			return MINUS_REGEX;
		case multiply:
			return MULTIPLY_REGEX;
		case divide:
			return DIVIDE_REGEX;
		default:
			return null;
		}
	}

	private String getChar(Operator operator) {
		if(operator == null) return "";
		switch (operator) {
		case plus:
			return PLUS;
		case minus:
			return MINUS;
		case multiply:
			return MULTIPLY;
		case divide:
			return DIVIDE;
		default:
			return null;
		}
	}
	
	/**
	 * Method returns Operation if exist one, otherwise null
	 * 
	 * @param parameterName
	 * @return operation
	 */
	private Operator operation(String parameterName) {
		
		if(parameterName.contains(PLUS))
			return Operator.plus;
		else if (parameterName.contains(MINUS))
			return Operator.minus;
		else if (parameterName.contains(MULTIPLY))
			return Operator.multiply;
		else if (parameterName.contains(DIVIDE))
			return Operator.divide;
		else		
			return null;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Integer[] getParameterVariables() {
		return parameterVariables;
	}
	
	public Rule getRule() {
		return this.rule;
	}

	public String toString() {
		if (parameterOperation == null) {
			return this.rule.getVariable(parameterVariables[0]).getName();
		} else return this.rule.getVariable(parameterVariables[0]).getName() 
						 + getChar(parameterOperation) 
						 + this.rule.getVariable(parameterVariables[1]).getName();
	}

	public int getVariableValue(int result, int number) {
		switch(parameterOperation){
			case plus:
				return result - number;
			case minus:
				return result + number;
			case multiply:
				return result / number;
			case divide:
				return result * number;
			default:
			 	return -1;
		}
	}
	
	public String getParameterValue(String variable1, String variable2) {
		int variableValues;
		int variableValues2;
		try{
			variableValues = Integer.parseInt(variable1);
			variableValues2 = Integer.parseInt(variable2);
			
			switch(parameterOperation){
			case plus:
				return (variableValues + variableValues2) + "";
			case minus:
				return (variableValues - variableValues2) + "";
			case multiply:
				return (variableValues / variableValues2) + "";
			case divide:
				return (variableValues * variableValues2) + "";
			default:
			 	return null;
			}
		} catch (Exception e) {
			if(parameterOperation != Operator.plus) {
				System.out.println("Can't Do non plus operation on String");
				return null;
			} else {
				return variable1 + variable2;
			}
		}
	}
	
	public Integer getParameterVariable() {
		if((!(parameterVariables.length > 1)) && parameterVariables.length > 0) {
			return parameterVariables[0];
		} else return null;
	}
	
	public String getParameterValue(RuleActivation activation) {
		// parameter value = variable1 operation variable2
		// or just variable1
		int i = 0;
		String[] variableValues =  new String[parameterVariables.length];
		// for each variable indexes
		for(int index : parameterVariables) {
			variableValues[i] = activation.getVariableValue(index);
			if(variableValues[i] == null) {
				Variable variable = this.rule.getVariable(index); 
				if(variable.isNumber()) {
					variableValues[i] = variable.getValue()+"";
					activation.addVariableBinding(new VariableBinding(variable, variable.getValue()));
				}
				else System.out.println("Can't find value of this parameter - " + this.name);
			}
			i++;
		}
		
		if(variableValues.length > 1) {
			return getParameterValue(variableValues[0],variableValues[1])+"";
		}else {
			return variableValues[0]+"";
		}
	}
}
