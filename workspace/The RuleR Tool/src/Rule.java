import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Rule {
	
	private static String COMMA = ",";
	
	public enum Modifier {Always, Step, State, Fail};
	public enum ExtraModifier {Start, Forbidden, Assert, None};
	
	
	//private final int ruleID;
	private final int ruleNameID;
	private final String ruleName;
	private final ExtraModifier extraModifier;
	private final Modifier ruleModifier;
	private final Parameter[] parameters;
	private HashMap<Integer, Variable> ruleVariables;
	private int[] ruleParameterIndexes;
	private Map<Integer, Integer> ruleParameters;
	private String[] ruleParameterStrings;
	private ArrayList<RuleBinding> ruleBinding;
	
	
	public Rule(String name,String mod, String extMod, String parameter, ArrayList<RuleBinding> bindings) {
		//ruleID = GlobalVariables.getNextRuleID();
		ruleName = name;
		ruleModifier = getModifier(mod);		
		extraModifier = getExtraModifier(extMod);
		
		System.out.println("Parameters: "+parameter);
		String param = GlobalFunctions.removeWhiteSpaces(parameter);
		if(param.length() > 0) {
			ruleParameterStrings = param.split(COMMA);
		} else ruleParameterStrings = new String[0];
		
		//System.out.println("<< parameters : <<"+ parameter+ ">> size: " +ruleParameterStrings.length);
		ruleParameters = new HashMap<Integer, Integer>();
		System.out.print("Rule Name ID - " + name+ruleParameterStrings.length + " ");
		ruleNameID = GlobalFunctions.hashName(name+ruleParameterStrings.length);
		ruleBinding = bindings;
		ruleVariables = new HashMap<Integer,Variable>();
		parameters = getParameterArray();
	}
	
	public void addVariable(Variable newVariable) {
		ruleVariables.put(newVariable.getKey(), newVariable);
	}
	
	public Variable getVariable(int key) {
		return ruleVariables.get(key);
	}
	
	public boolean containsVariable(String variableName) {
		
		Integer key = new Integer(GlobalFunctions.hashName(variableName));
		
		return ruleVariables.containsKey(key);
	}
	
	public Variable getVariable(Integer key) {
		return ruleVariables.get(key);		
	}
	
	private Parameter[] getParameterArray() {
		
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		
		// Get different parameters from Rule
		//int index = 0;
		for(String param : ruleParameterStrings) {
			Parameter parameter = new Parameter(param, this);
			parameters.add(parameter);
		}
		
		// Get different parameters from Event Parameters
		for(RuleBinding ruleBind : ruleBinding) {
			String[] allParameters;
			
			if((allParameters = ruleBind.getEventParamArray()) != null) {
			
				for(String param : allParameters) {
					if(GlobalFunctions.exists(param, parameters)) continue;
					else parameters.add(new Parameter(param, this));
				}
				
				// Get different parameters from Conditions
				for(Condition condition : ruleBind.getEventConditions()) {
					
					String[] allconditions;
					
					if((allconditions = condition.getParameterArray()) != null)
						for(String param : allconditions) {
							//System.out.println("-- --> Parameter : " + param);
							if(GlobalFunctions.exists(param, parameters)) continue;
							else parameters.add(new Parameter(param, this));
						}
				}
				
				// Get different parameters from Consequent Rule Parameters
				for(ConsequentRule consequent : ruleBind.getConsequentRules()) {
					
					String[] allconsequents;
					
					if((allconsequents = consequent.getConsequentRuleParameterStrings()) != null)
						for(String param : allconsequents) {
							//System.out.println("-- --> Parameter : " + param);
							if(GlobalFunctions.exists(param, parameters)) continue;
							else parameters.add(new Parameter(param, this));
						}
				}
			}
		}
		
		
		int numOfParameters = parameters.size();
		
		//System.out.println("--> There are " + numOfParameters + " in this rule");
		
		Parameter[] tempParamArray = new Parameter[numOfParameters];
		
		for(int i=0;i<numOfParameters;i++) {
			tempParamArray[i] = parameters.get(i);
		}
		
		ruleParameterIndexes = new int[ruleParameterStrings.length];
		
		//System.out.println("<< " + ruleParameterIndexes.length + " >>");
		
		for(int i=0; i< ruleParameterIndexes.length;i++) {
			int parameterIndex = GlobalFunctions.getParamIndex(ruleParameterStrings[i],tempParamArray);
			ruleParameterIndexes[i] = parameterIndex;
			ruleParameters.put(parameterIndex, i);
		}
		
		for(RuleBinding ruleBind : ruleBinding) {
			ruleBind.initializeParameterIndexes(tempParamArray);
		}
		
		return tempParamArray;
	}

	public Parameter getParameter(int index) {
		Parameter param;
		try{
			param = parameters[index];
		} catch (Exception e) {
			param = null;
			System.out.println("--- Parameters size - " + parameters.length + " we were looking for - " + index);
		}	
		return param;
	}
	
	private ExtraModifier getExtraModifier(String extMod) {
		
		switch(extMod) {
		case "Start":
			return ExtraModifier.Start;
		case "Forbidden":
			return ExtraModifier.Forbidden;
		case "Assert":
			return ExtraModifier.Assert;
		default:
			return ExtraModifier.None;
		}
	}
	
	private Modifier getModifier(String mod) {
		switch(mod) {
		case "Always":
			return Modifier.Always;
		case "State":
			return Modifier.State;
		case "Step":
			return Modifier.Step;
		case "Skip":
			return Modifier.Step;
		default:
			return Modifier.Fail;
		}
	}

	public void addRuleBinding(RuleBinding ruleBinding) {
		this.ruleBinding.add(ruleBinding);
	}
	
	public String getRuleBindingsString() {
		String bindingString = "";
		
		for(RuleBinding binding : ruleBinding) {
			bindingString+= "[" + binding.toString() + "]";
		}
		
		return bindingString;
	}

	/*public int getRuleID() {
		return ruleID;
	}*/

	public Integer getRuleNameID() {
		return ruleNameID;
	}

	public String getRuleName() {
		return ruleName;
	}
	
	public int[] getRuleParameterIndexes() {
		return ruleParameterIndexes;
	}
	
	public ExtraModifier getExtraModifier() {
		return extraModifier;
	}

	public Modifier getRuleModifier() {
		return ruleModifier;
	}
	
	public ArrayList<RuleBinding> getRuleBinding() {
		return ruleBinding;
	}
	
	public int getRuleParamIndex(int key) {
		return ruleParameters.get(key);
	}
	
	public Parameter[] getParameters() {
		return parameters;
	}

	public String toString() {
		return extraModifier + " " +ruleModifier + " " + ruleName + "("+ GlobalFunctions.getParameters(ruleParameterStrings) +") { " + getRuleBindingsString() + " }";
	}

	public boolean isAssert() {
		return extraModifier == ExtraModifier.Assert;
	}

}
