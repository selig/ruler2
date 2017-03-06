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
	private ArrayList<RuleBinding> ruleBindings;
	private int[] ruleParamIndexShared;
	private HashMap<Integer, Integer[]> eventToMachingParameterIndex;
	
	
	public Rule(String name,String mod, String extMod, String parameter, ArrayList<RuleBinding> bindings) {
		//ruleID = GlobalVariables.getNextRuleID();
		ruleName = name;
		ruleModifier = getModifier(mod);		
		extraModifier = getExtraModifier(extMod);
		
		//System.out.println("Parameters: "+parameter);
		String param = GlobalFunctions.removeWhiteSpaces(parameter);
		if(param.length() > 0) {
			ruleParameterStrings = param.split(COMMA);
		} else ruleParameterStrings = new String[0];
		
		//System.out.println("<< parameters : <<"+ parameter+ ">> size: " +ruleParameterStrings.length);
		ruleParameters = new HashMap<Integer, Integer>();
		//System.out.print("Rule Name ID - " + name+ruleParameterStrings.length + " ");
		ruleNameID = GlobalFunctions.hash(name+ruleParameterStrings.length);
		ruleBindings = bindings;
		ruleVariables = new HashMap<Integer,Variable>();
		parameters = getParameterArray();
		eventToMachingParameterIndex = new HashMap<Integer, Integer[]>();
		
		addEventToRuleParameterMatching();
	}
	
	public void addVariable(Variable newVariable) {
		ruleVariables.put(newVariable.getKey(), newVariable);
	}
	
	public Variable getVariable(int key) {
		return ruleVariables.get(key);
	}
	
	public boolean containsVariable(String variableName) {
		
		Integer key = new Integer(GlobalFunctions.hash(variableName));
		
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
		for(RuleBinding ruleBind : ruleBindings) {
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
		
		for(RuleBinding ruleBind : ruleBindings) {
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
			//System.out.println("--- Parameters size - " + parameters.length + " we were looking for - " + index);
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
		this.ruleBindings.add(ruleBinding);
	}
	
	public String getRuleBindingsString() {
		String bindingString = "";
		
		for(RuleBinding binding : ruleBindings) {
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
	
	public ArrayList<RuleBinding> getRuleBindings() {
		return ruleBindings;
	}
	
	public int getRuleParamIndex(int key) {
		return ruleParameters.get(key);
	}
	
	public Parameter[] getParameters() {
		return parameters;
	}

	public void addEventToRuleParameterMatching(){
		
		int[] tempArray = null;
		
		for(RuleBinding ruleBinding : ruleBindings) {
			
			int eventNameID = GlobalFunctions.hash(ruleBinding.getEventName()+ruleBinding.getNoOfEventParameters());
			
			StringBuilder allMatchingIndexes = new StringBuilder();
			
			Integer traceIndex;
			Integer[] matchingIndexes = new Integer[0];
			
			if(ruleParameterIndexes.length > 0) {
				
				int countRule= 0;
				
				int[] tempArray2 = new int[ruleParameterIndexes.length];
				
				for(int index : ruleParameterIndexes) {
					
					if((traceIndex = ruleBinding.getEventParameterIndex(index)) != null) {
						allMatchingIndexes.append(traceIndex).append(COMMA);
						
						/** Add rule parameter index which match */
						tempArray2[countRule] = index;
						countRule++;
					}
				}
				
				String[] strIndex = allMatchingIndexes.toString().split(COMMA); 
				matchingIndexes = new Integer[strIndex.length];
				int count= 0;
				for(String string : strIndex){
					if(!string.equals("")) {
						matchingIndexes[count] = Integer.parseInt(string);
						count++;
					}
				}
				
				if(count == 0) {
					matchingIndexes = new Integer[0];
				}
				
				if(tempArray == null) {
					tempArray = new int[countRule];
					for(int i=0;i< countRule;i++) {
						tempArray[i] = tempArray2[i];
					}
				} else {
					int[] tempArray3 = new int[tempArray.length];
					int ruleCount = 0;
					
					for(int i=0;i< countRule;i++) {
						for(Integer ind1 : tempArray) {
							if(ind1 == tempArray[i]) {
								/* We have match */
								tempArray3[ruleCount] = ind1;
								ruleCount++;
							}
						}
					}
					
					if(tempArray3.length > ruleCount) {
						int[] tempArray4 = new int[ruleCount];
						for(int i = 0; i< ruleCount;i++) {
							tempArray4[i] = tempArray3[i];
						}
						tempArray = tempArray4;
					} else {
						tempArray = tempArray3;	
					}
				}
				
				
			}
			
			ruleParamIndexShared = tempArray;
			
			eventToMachingParameterIndex.put(eventNameID, matchingIndexes);
			
		}
		
		
		
		
	} 
	
	public Integer[] getEventToRuleParameterMatching(int eventNameKey){
		return eventToMachingParameterIndex.get(eventNameKey);
	} 
	
	public String toString() {
		return extraModifier + " " +ruleModifier + " " + ruleName + "("+ GlobalFunctions.getParameters(ruleParameterStrings) +") { " + getRuleBindingsString() + " }";
	}

	public boolean isAssert() {
		return extraModifier == ExtraModifier.Assert;
	}
	
	public HashMap<Integer, Integer[]> getEventToMachingParameterIndex() {
		return eventToMachingParameterIndex;
	}
	
	public int[] getRuleMatchingParameterIndexArray() {
		return ruleParamIndexShared;
	}

}
