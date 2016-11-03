
public class VariableBinding {
	private final Variable variable;
	private final String variableValueString;
	private final Integer variableValueInt;

	
	public VariableBinding(Variable newVariable, String newVariableValue) {
		this.variable = newVariable;
		this.variableValueString = newVariableValue;
		this.variableValueInt = null;
	}
	
	public VariableBinding(Variable newVariable, int newVariableValue) {
		this.variable = newVariable;
		this.variableValueInt = newVariableValue;
		this.variableValueString = null;
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
}
