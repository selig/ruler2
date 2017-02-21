
public class Variable {
	
	private final String name;
	private final Integer key;
	
	public Variable(String name) {
		this.name = name;
		this.key = GlobalFunctions.hashName(name);
	}
	
	public Integer getKey() {
		return this.key;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isNumber() {
		return false;
	}
	
	public int getValue() {
		return 0;
	}
}
