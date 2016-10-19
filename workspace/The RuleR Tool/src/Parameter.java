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
	
	private Integer[] parameterVariables = new Integer[2];
	private Operator parameterOperation = null;
	private final String name;
	private final Rule rule;
	
	/**
	 * Constructor for Parameter
	 * @param name
	 */
	public Parameter(String name, Rule rule) {
		
		this.rule = rule;
		this.name = GlobalFunctions.removeWhiteSpaces(name);
		
		parameterOperation = operation(name);
		
		String charToSplit;
		String[] splitName;
		
		if((charToSplit = getRegexChar(parameterOperation)) != null)
			splitName = GlobalFunctions.removeWhiteSpaces(name).split(charToSplit);
		else {
			splitName = new String[1];
			splitName[0] = name;
		}
		
		int index = 0;
		
		for(String variableName : splitName){
			
			if(rule.containsVariable(variableName)) {
				parameterVariables[index] = GlobalFunctions.hashName(variableName);
			} else {
				Variable newVariable = createVariableInstance(variableName);
				parameterVariables[index] = newVariable.getKey();
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
	
	public String toString() {
		if (parameterOperation == null) {
			return this.rule.getVariable(parameterVariables[0]).getName();
		} else return this.rule.getVariable(parameterVariables[0]).getName() 
						 + getChar(parameterOperation) 
						 + this.rule.getVariable(parameterVariables[1]).getName();
	}
	
}
