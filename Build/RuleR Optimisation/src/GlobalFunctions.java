import java.util.ArrayList;


public class GlobalFunctions {

	public static int hash(String ruleName) {
		final int prime = 31;
		final int prime2 = 13;
		char[] characters = ruleName.toCharArray();
		int hashKey = 0;
		for(char character : characters) {
			hashKey += (int)((character * prime) / prime2);
		}
		return hashKey;
	}
	
	public static boolean exists(String string, ArrayList<Parameter> parameters) {
		for(Parameter stringFromArray : parameters) {
			if(string.equals(stringFromArray.getName())) return true;
		}
		return false;
	}

	public static int getParamIndex(String string, Parameter[] array) {
		
		string = removeWhiteSpaces(string);
		
		for(int i=0;i<array.length;i++){
			if(string.equals(array[i].toString())) return i;
		}
				
		return -1;
	}
	
	public static String removeWhiteSpaces(String string) {
		return string.replaceAll("\\s+", "");
	}

	public static String getParameters(String[] array) {
		
		try {
			
			String finalString = "";
			
			for(String arrayString : array) {
				finalString += arrayString + ", ";
			}
				
			finalString = subStringLast(finalString,2);
			
			return finalString;
		} catch(Exception e){
			return "";
		}
	}
	
	public static String subStringLast(String string, int length) {
		if(string.length() > length) {
			return string.substring(0, string.length()-length);
		} else return string;
	}

	public static Rule getRule(int ruleNameID) {
		
		RuleSystem ruleSystem = Interface.ruleSystem;
		
		return ruleSystem.getRule(ruleNameID);
	}

	public static String getValuesString(String[] paramArray) {
		if(paramArray == null)
			return "";
		
		StringBuilder finalString = new StringBuilder("");
		
		for(String arrayString : paramArray) {
			finalString.append(arrayString);
		}
		
		return finalString.toString();
	}
}
