/**
 * This class represents Parameter object in order to handle situations
 * when parameter = parameter +1 
 * 
 * @author mbax2md4
 *
 */
public class Parameter {
	private final String parameterNameString;
	private String parameterOperation = null;
	private String parameterOperationAddition = null;
	private int parameterOperationAdditionType;
	
	/**
	 * Constructor for Parameter
	 * @param name
	 */
	public Parameter(String name) {
		parameterOperation = operation(name);
		
		if(parameterOperation == null)
			this.parameterNameString = name;
		else {
			String[] splitName = name.trim().split(parameterOperation);
			parameterNameString = splitName[0];
			parameterOperationAddition = splitName[1];
			parameterOperationAdditionType = additionType(parameterOperationAddition);
		}
	}
	
	/**
	 * Method returns Addition type: 1 for integer, 2 for String which means it is another variable
	 * 
	 * @param parameterOperationAddition2
	 * @return addition type
	 */
	private int additionType(String parameterOperationAddition2) {
		try{
			Integer.parseInt(parameterOperationAddition2);
			return 1;
		} catch(Exception e) {
			return 2;
		}
	}

	/**
	 * Method returns Operation if exist one, otherwise null
	 * 
	 * @param parameterName
	 * @return operation
	 */
	private String operation(String parameterName) {
		
		if(parameterName.contains("+"))
			return "+";
		else if (parameterName.contains("-"))
			return "-";
		else if (parameterName.contains("*"))
			return "*";
		else if (parameterName.contains("/"))
			return "/";
		else		
			return null;
	}
	
	
	public String getName() {
		return this.parameterNameString;
	}
	
}
