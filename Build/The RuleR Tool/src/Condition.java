
public class Condition {
	
	private final static String GREATERTHAN = ">";
	private final static String GREATEROREQUAL = ">=";
	private final static String LESSTHAN = "<";
	private final static String LESSOREQUAL = "<=";
	private final static String EQUALS = "=";
	private final static String NOTEQUAL = "!=";
	
	public enum CompareOperation{greaterThan, lessThan, greaterOrEqual, lessOrEqual, equals, notEqual};
	public enum ConditionType{compare,rule,none};
	public enum ConditionNegate{yes,no};

	private String condition;
	private String conditionRuleNameID;
	private final ConditionType conditionType;
	private final CompareOperation conditionOperator;
	private final ConditionNegate conditionNegation;
	private String[] conditionParameterStrings;
	private int[] conditionParamIndexes = null;
	
	
	public Condition(String condition) {
		this.condition = GlobalFunctions.removeWhiteSpaces(condition);

		if(!this.condition.equals("")) {
			this.conditionNegation = this.condition.substring(0, 1).equals("!") ? ConditionNegate.yes : ConditionNegate.no;
			this.condition = this.condition.replaceAll("!", "");
		}
		else
			this.conditionNegation = null;
		
		this.conditionOperator= getConditionOperation(this.condition);
		this.conditionType = setConditionType();
		this.conditionParameterStrings = getParametersToStringArray(this.condition);
	}
	
	private ConditionType setConditionType() {
		if(conditionOperator != null){
			return ConditionType.compare;
		}
		else {
			if(this.condition.equals("")) {
				return ConditionType.none;
			}
			else {
				//System.out.println("Condition: " + this.condition);
				// If it is rule, convert condition to Rule Name for ruleNameID
				this.conditionRuleNameID = this.condition.split("\\(")[0] + 
							this.condition.replaceAll("\\)", "").split("\\(")[1].split(",").length;
				//System.out.println("Condition Rule Name ID: " + this.conditionRuleNameID);
				
				return ConditionType.rule;
			}
		}
	}

	private CompareOperation getConditionOperation(String conditionString) {
		
		if(conditionString.contains(GREATEROREQUAL)) {
			return CompareOperation.greaterOrEqual;	
		} else if(conditionString.contains(LESSOREQUAL)) {
			return CompareOperation.lessOrEqual;
		} else if(conditionString.contains(GREATERTHAN)) {
			return CompareOperation.greaterThan;
		} else if(conditionString.contains(LESSTHAN)) {
			return CompareOperation.lessThan;
		} else if(conditionString.contains(EQUALS) /*|| conditionString.contains("==")*/ ) {
			return CompareOperation.equals;
		} else if(conditionString.contains(NOTEQUAL)) {
			return CompareOperation.notEqual;
		} else {
			return null;
		}
	}
	
	private String[] getParametersToStringArray(String conditionString) {
		
		if(conditionType == ConditionType.compare) {
			return GlobalFunctions.removeWhiteSpaces(conditionString).split(getOperatorString());
		} else if(conditionType == ConditionType.rule) {
			return this.condition.replaceAll("\\)", "").split("\\(")[1].split(",");
		} 
		else return null;
	}

	private String getOperatorString() {
		switch (this.conditionOperator) {
		case greaterThan:
			return GREATERTHAN;
		case lessThan:
			return LESSTHAN;
		case greaterOrEqual:
			return GREATEROREQUAL;
		case lessOrEqual:
			return LESSOREQUAL;
		case equals:
			return EQUALS;
		case notEqual:
			return NOTEQUAL;
		default:
			return "";
		}
	}

	public void initializeParameterIndexes(Parameter[] tempParamArray) {
		if (conditionType == ConditionType.compare || conditionType == ConditionType.rule) {
			conditionParamIndexes = new int[conditionParameterStrings.length];
						
			for(int i=0;i<conditionParamIndexes.length;i++) {
				conditionParamIndexes[i] = GlobalFunctions.getParamIndex(conditionParameterStrings[i],tempParamArray);
			}
			
			// to remove link for GC to free up memory
			conditionParameterStrings = null;
		}
	}
	
	public String toString() {
		return this.condition;
	}
	
	public String getCondition() {
		return this.condition;
	}
	
	public int getConditionRuleNameID() {
		return GlobalFunctions.hashName(this.conditionRuleNameID);
	}

	public ConditionType getConditionType() {
		return conditionType;
	}

	public CompareOperation getConditionOperator() {
		return conditionOperator;
	}

	public String[] getParameterArray() {
		return this.conditionParameterStrings;
	}
	
	public int[] getParameterIndexes() {
		return this.conditionParamIndexes;
	}

	public ConditionNegate getConditionNegation() {
		return conditionNegation;
	}

	@SuppressWarnings("static-access")
	public boolean isTrue(String parameterValue, String parameterValue2) {
		
		boolean result;
		// try to convert to int
		try{
			int param1 = Integer.parseInt(parameterValue);
			int param2 = Integer.parseInt(parameterValue2);
			
			switch (this.conditionOperator) {
			case greaterThan:
				Interface.log("result = " + param1 +" > "+ param2);
				result = param1 > param2;
				break;
			case lessThan:
				Interface.log("result = " + param1 +" < "+ param2);
				result = param1 < param2;
				break;
			case greaterOrEqual:
				Interface.log("result = " + param1 +" >= "+ param2);
				result = param1 >= param2;
				break;
			case lessOrEqual:
				Interface.log("result = " + param1 +" <= "+ param2);
				result = param1 <= param2;
				break;
			case equals:
				Interface.log("result = " + param1 +" == "+ param2);
				result = param1 == param2;
				break;
			case notEqual:
				Interface.log("result = " + param1 +" != "+ param2);
				result = param1 != param2;
				break;
			default:
				result = false;
				break;
			}
			
		} catch(NumberFormatException e){
			// It must be text thus only equal is available
			if(this.conditionOperator == CompareOperation.equals) {
				result = parameterValue.equals(parameterValue2);
			}
			else result = false;
		}
		
		if(getConditionNegation() == conditionNegation.yes) {
			return !result;
		} else {
			return result;
		}
	}
	
}
