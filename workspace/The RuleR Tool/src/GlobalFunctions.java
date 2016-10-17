
public class GlobalFunctions {

	public static int hashName(String ruleName) {
		return ruleName.hashCode();
	}
	
	public static boolean exists(String string, String[] stringArray) {
		for(String stringFromArray : stringArray) {
			if(string.equals(stringFromArray)) return true;
		}
		return false;
	}

	public static int getParamIndex(String string, Parameter[] array) {
		
		for(int i=0;i<array.length;i++){
			if(string.equals(array[i].getName())) return i;
		}
				
		return -1;
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
		return string.substring(0, string.length()-length);
	}
}
