
public class VariableNumber extends Variable {
	
	private final int value;
	
	public VariableNumber(String name, int newValue) {
		super(name);
		this.value = newValue;
	}
	
	public int getValue() {
		return this.value;
	}

}
