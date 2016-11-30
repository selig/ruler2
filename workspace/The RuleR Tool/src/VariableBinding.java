
public class VariableBinding {
	public enum Type {integer,string};
	
	private final Variable variable;
	private final String variableValueString;
	private final Integer variableValueInt;
	private final Type flag;

	
	public VariableBinding(Variable newVariable, String newVariableValue) {
		this.variable = newVariable;
		this.variableValueString = newVariableValue;
		this.variableValueInt = null;
		flag = Type.string;
	}
	
	public VariableBinding(Variable newVariable, int newVariableValue) {
		this.variable = newVariable;
		this.variableValueInt = newVariableValue;
		this.variableValueString = null;
		flag = Type.integer;
	}
	
	public int getKey() {
		return this.variable.getKey();
	}

	public String getVariableValueString() {
		return variableValueString;
	}

	public Integer getVariableValueInt() {
		return variableValueInt;
	}
	
	public boolean isInt() {
		return variableValueInt != null;
	}

	public Object getVariableValue() {
		if(variableValueInt == null){
			return variableValueString;
		} else {
			return variableValueInt;
		}
	}
	
	public Type getType() {
		return flag;
	}
	
	public boolean isString() {
		return flag == Type.string;
	}
	
	public boolean isInteger() {
		return flag == Type.integer;
	}
	
	public boolean hasValue() {
		return variableValueString != null || variableValueInt != null;
	}
	
	public String getVariableName() {
		return this.variable.getName();
	}
}

