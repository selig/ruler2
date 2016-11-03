
public class Condition {
	
	private final static String GREATERTHAN = ">";
	private final static String LESSTHAN = "<";
	private final static String EQUALS = "=";
	private final static String NOTEQUAL = "!=";
	
	public enum CompareOperation{greaterThan, lessThan, equals, notEqual};
	public enum ConditionType{compare,rule};

	private final String condition;
	private final ConditionType conditionType;
	private final CompareOperation conditionOperator;
	private String[] conditionParameterStrings;
	private int[] conditionParamIndexes;
	
	
	public Condition(String condition) {
		this.condition = condition;
		this.conditionOperator= getConditionOperation(condition);
		this.conditionType = setConditionType();
		this.conditionParameterStrings = getParametersToStringArray(condition);
	}
	
	private ConditionType setConditionType() {
		return conditionOperator == null ? ConditionType.rule : ConditionType.compare;
	}

	private CompareOperation getConditionOperation(String conditionString) {
		
		if(conditionString.contains(GREATERTHAN)) {
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
		} else return null;
	}

	private String getOperatorString() {
		switch (this.conditionOperator) {
		case greaterThan:
			return GREATERTHAN;
		case lessThan:
			return LESSTHAN;
		case equals:
			return EQUALS;
		case notEqual:
			return NOTEQUAL;
		default:
			return "";
		}
	}

	public void initializeParameterIndexes(Parameter[] tempParamArray) {
		if (conditionType == ConditionType.compare) {
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
		return condition;
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

	public boolean isTrue(String parameterValue, String parameterValue2) {
		// try to convert to int
		try{
			int param1 = Integer.parseInt(parameterValue);
			int param2 = Integer.parseInt(parameterValue2);
			
			switch (this.conditionOperator) {
			case greaterThan:
				return param1 > param2;
			case lessThan:
				return param1 < param2;
			case equals:
				return param1 == param2;
			case notEqual:
				return param1 != param2;
			default:
				return false;
			}
			
		} catch(NumberFormatException e){
			// It must be text thus only equal is available
			if(this.conditionOperator == CompareOperation.equals) {
				return parameterValue.equals(parameterValue2);
			}
			else return false;
		}
	}
	
}
