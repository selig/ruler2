
public class Condition {
	
	private static int ONE = 1;
	private static int TWO = 2;
	private static int THREE = 3;
	private static int FOUR = 4;

	private final String condition;
	/*
	 * This variable holds value of condition type
	 * 1 - equation
	 * 2 - rule
	 */
	private final int conditionType;
	/*
	 * This variable holds value of the operation type if condition type is 1
	 *  0 - (no operation)
	 *  1 - > (Greater Than)
	 *  2 - < (Less Than)
	 *  3 - = (Equals)
	 *  4 - != (Not Equal)
	 */
	private int conditionOperator;
	private String[] conditionParameterStrings;
	private int[] conditionParamIndexes;
	
	
	public Condition(String condition) {
		
		System.out.println(condition);
		
		this.condition = condition;
		this.conditionType = getConditionType(condition);
		this.conditionParameterStrings = getParametersToStringArray(condition);
	}
	
	private int getConditionType(String conditionString) {
		
		if(conditionString.contains(">")) {
			this.conditionOperator = ONE;
			return ONE;
		} else if(conditionString.contains("<")) {
			this.conditionOperator = TWO;
			return ONE;
		} else if(conditionString.contains("=") /*|| conditionString.contains("==")*/ ) {
			this.conditionOperator = THREE;
			return ONE;
		} else if(conditionString.contains("!=")) {
			this.conditionOperator = FOUR;
			return ONE;
		} else {
			return TWO;
		}
	}
	
	private String[] getParametersToStringArray(String conditionString) {
		
		if(conditionType == ONE) {
			return conditionString.trim().split(getOperatorString());
		} else return null;
	}

	private String getOperatorString() {
		switch (this.conditionOperator) {
		case 1:
			return ">";
		case 2:
			return "<";
		case 3:
			return "=";
		case 4:
			return "!=";
		default:
			return "";
		}
	}

	public void initializeParameterIndexes(Parameter[] tempParamArray) {
		if (conditionType == ONE) {
			conditionParamIndexes = new int[conditionParameterStrings.length];
			
			int paramIndex;
			
			for(int i=0;i<conditionParamIndexes.length;i++) {
				paramIndex = GlobalFunctions.getParamIndex(conditionParameterStrings[i],tempParamArray);
				
				if(paramIndex < 0) {
					try {
						conditionParameterStrings[i]);
					} catch (Exception e) {
						// TODO: handle exception
					}
				} else {
					conditionParamIndexes[i] = paramIndex;
				}
			}		
		}
	}
	
	public String toString() {
		return this.condition;
	}
}
