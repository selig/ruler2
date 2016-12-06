/**
 * This Class is representation of Update Algorithm
 * @author Mantas
 *
 */
import java.util.ArrayList;
import java.util.Map;

public class Update {
	
	public static boolean update(Event event){

		Interface.log("\n" +"");
		Interface.log("\n" +"-----------------------------------");
		Interface.log("\n" +"");
		Interface.log("\n" +" The Event - " + event.toString());
		Interface.log("\n" +"");
		
		ActiveRuleSet activeRuleSet = Interface.activeRuleSet;
		RuleSystem ruleSystem = Interface.ruleSystem;
		
		boolean assertAccepted;
		
		if(ruleSystem.getAssertArraySize() > 0)
			assertAccepted = false;
		else
			assertAccepted = true;
		
		ArrayList<RuleActivation> tempActivations = new ArrayList<RuleActivation>();
		ArrayList<RuleActivation> ruleActivationsToDelete = new ArrayList<RuleActivation>();
		
		// For all Active rules
		for(RuleActivation activation : activeRuleSet.getArrayOfRuleActivations()){
			// search rule with the event() (some fancy efficient way)
			Interface.log("\n" +"Rule Activation: " + activation.getRule().getRuleName());
			boolean activationFired = false;
			// Get rule of active rule
			Rule rule = activation.getRule();
			
			// For all Rule Bindings
			rulebindingloop: for(RuleBinding binding : rule.getRuleBinding()){
				Interface.log("\n" +"  Match: " + event.getEvent() + " vs " + binding.getEventName());
				if(event.getEvent().equals(binding.getEventName())){
					
					Interface.log("\n" +"    True");	
					

					// Get Event Parameter list of indexes
					int[] eventPIndexes = binding.getEventParameterIndexes();
// check if event matches rule
					if(rule.getRuleParameterIndexes().length > 0) {
						Interface.log("\n" +"      Rule Has Parameters");
						
						int eventParamIndex = 0;
						for(int index : eventPIndexes) {
							Interface.log("\n" +"        Event index = "+index);
							String ruleActivationValue = activation.getParameterBindingValue(index);
							if(ruleActivationValue != null) {
								Interface.log("\n" +"        Parameter Indexes Match");	
								//get event value
								String eventValue = event.getEventParameter(eventParamIndex);
								Interface.log("\n" +"        Rule Value: "+ruleActivationValue+" vs "+ eventValue + " : event value");	
								if(!ruleActivationValue.equals(eventValue)){
									Interface.log("\n" +"          Don't Match");	
									continue rulebindingloop;
								} else {
									Interface.log("\n" +"          Match");	
								}
							}
							eventParamIndex++;
						}
					}
					
					//System.exit(0);
					
// ---------------- Parameters
					Map<Integer, ParameterBinding> parameterValues = activation.getParameterBindings();
					// Get Event Parameters
					String[] eventParam = event.getEventParameters();
					
					// Get Binding Parameters
					int[] bindingParam = binding.getEventParameterIndexes();
					
					if(eventParam != null) {		
						if(eventParam.length == bindingParam.length) {
							Interface.log("\n" +"      Parameter Length Ok");
							int i=0;
							
							for(int index : bindingParam){
								// Get Parameter
								Parameter param = rule.getParameter(index);
								
								// Create and add new ParameterBinding to temp Set
								parameterValues.put(index,new ParameterBinding(param, eventParam[i],activation));
								
								i++;				
							}	
						} else {
							Interface.log("\n" +"      Parameter Length Not OK");
							continue;
						}
					}
					
// ---------------- Conditions
					for(Condition condition : binding.getEventConditions()){
						Interface.log("\n" +"      Condition: " + condition.getCondition());
						if(condition.getConditionType() == Condition.ConditionType.rule){
							Interface.log("\n" +"        Rule");
							// Deal with Rule condition
							// Get rule
							int ruleNameID = condition.getConditionRuleNameID();
							
							//Rule theConditionRule = GlobalFunctions.getRule(ruleNameID);
							
							String paramValues = "";
							
							int[] condIndexes = condition.getParameterIndexes();
							
							Interface.log("\n" +"length: " + condIndexes.length);
							
							// Counter to determine if all the condition Parameters have values from event
							int[] sharedParamIndex = new int[condIndexes.length];
							int counter = 0;
							
							//check if event and condition share the same parameters
							for(int conInd : condIndexes) {
								int i = 0;
								for(int eventIndex : eventPIndexes)  {
									if(eventIndex == conInd) {
										Interface.log("\n" +"index: " + i + " paramRuleIndex: " +eventIndex + " is shared and it's value "+ event.getEventParameter(i));
										paramValues += event.getEventParameter(i)+",";
										sharedParamIndex[counter] = conInd;
										counter++;
									}
									else {
										Interface.log("\n" +"index " + eventIndex + " not shared");
									}
									i++;
								}
							}
							
							boolean notFoundFlag = false;
							
							//Check if Condition Parameters have values from event
							if(counter != condIndexes.length) {
								paramValues = "";
								//check if the Condition Parameters has values in values Map
								for(int conInd : condIndexes) {
									ParameterBinding par = parameterValues.get(conInd);
									if(par == null){
										notFoundFlag = true;
										break;
									} else {
										paramValues += par.getParameterValue() + ",";
									}
								}
							}
							
							boolean existanceOfRule;
							
							if(notFoundFlag) {
								// Linear Search all The active rules looking for Rule with shared parameters and ignoring not shared ones
								existanceOfRule = activeRuleSet.findMatchingRule(sharedParamIndex,ruleNameID,parameterValues);
							} else {
															
								paramValues = GlobalFunctions.subStringLast(paramValues, 1);
								Interface.log("\n" +"All Parameters found : " + paramValues);
								
								String activeRuleSearchID = ruleNameID+paramValues;
								Interface.log("\n" +"active Rule Search ID : " + activeRuleSearchID);
								
								existanceOfRule = activeRuleSet.activeRuleExist(activeRuleSearchID);
								Interface.log("\n" +"          Result of Search - "+ existanceOfRule);
							}
							
							if(condition.getConditionNegation() == Condition.ConditionNegate.yes) {
								if(!existanceOfRule) {
									Interface.log("\n" +"          Rule Condition Accepted (yes)");
								} else {
									Interface.log("\n" +"          Rule Condition Not Accepted (yes)");
									continue rulebindingloop;
								}
							} else {
								if(existanceOfRule) {
									Interface.log("\n" +"          Rule Condition Accepted (no)");
								} else {
									Interface.log("\n" +"          Rule Condition Not Accepted (no)");
									continue rulebindingloop;
								}
							}
							
						} else if(condition.getConditionType() == Condition.ConditionType.compare){
							Interface.log("\n" +"        Normal");
							//Deal with Conditions
							// Get Condition Parameter Indexes
							int[] conditionParameters = condition.getParameterIndexes();
							
							ParameterBinding[] parameterBindings = new ParameterBinding[conditionParameters.length];
							
							for(int i=0;i<parameterBindings.length;i++) {
								parameterBindings[i] = activation.getParameterBinding(conditionParameters[i]);
							}
							
							int i=0;
							for(ParameterBinding paramBinding : parameterBindings){
								if(paramBinding == null) {
									// Get Parameter from Rules
									Parameter parameter1 = rule.getParameter(conditionParameters[0]);
									
									String parameterValue = parameter1.getParameterValue(activation);
									/*// Get Parameter Variable indexes
									Integer[] varIndexes1 = parameter1.getParameterVariables();
									
									// Get Variable1 value
									int variable1Value = activation.getVariableValue(varIndexes1[0]);
									
									// Get Variable1 value
									int variable2Value = activation.getVariableValue(varIndexes1[1]);
									
									// Get parameter 1 value
									int param1Value = parameter1.getParameterValue(variable1Value, variable2Value);*/
									
									parameterValues.put(conditionParameters[0],new ParameterBinding(parameter1,parameterValue,activation));
									
									parameterBindings[i] = parameterValues.get(conditionParameters[0]);
								}
								i++;
							}
							
							// Get Condition result
							if(!condition.isTrue(parameterBindings[0].getParameterValue(), parameterBindings[1].getParameterValue())) {
								Interface.log("\n" +"          Condition Not ok");
								continue rulebindingloop;
							} else {
								Interface.log("\n" +"          Condition OK");
							}
							
						}
					}
					
// ---------------- Consequent Rules
					
					//Parameter[] RuleParameterArray = rule.getParameters();
								
				/*	for(Integer key : activation.getVariableBindings().keySet()){
						VariableBinding var = activation.getVariableBinding(key);
						System.out.println("[ "+key+" "+var.getVariableName() + " - " + var.getVariableValue() + " ]");
					}
					*/
					for(ConsequentRule consequentRule : binding.getConsequentRules()){
						
						int[] consequentIndexes = consequentRule.getConsequentRuleParameterIndexes();
						
						//Check if Parameter Values Assigned for consequent rules
						for(int index : consequentIndexes) {
							if(!parameterValues.containsKey(index)) {
								Parameter parameter = rule.getParameter(index);
								parameterValues.put(index, new ParameterBinding(parameter, parameter.getParameterValue(activation) ,activation));
							}
						}
						
						
						Interface.log("\n" +"      Consequent Rule");
						// Check if Consequence is Fail
						if(consequentRule.isFail()) {
							Interface.log("\n" +"        Fail");
							Interface.log("\n");
							return false;
						}
						
						// Check if is a rule
						if(!consequentRule.isOK()) {
							Interface.log("\n" +"++++++++Rule Add");
							// Add new RuleActivation to tempArray
							tempActivations.add(new RuleActivation(consequentRule.getRuleName()+consequentRule.getConsequentRuleParameterSize()
										, parameterValues,consequentIndexes));
						} else {
							Interface.log("\n" +"        Rule - OK");
						}
					}
					
					
					activationFired = true;
					Interface.log("\n" +"    ActivationFired: " + activationFired);
				}
				else {
					Interface.log("\n" +"    False");
					continue;
				}
			}
//--------- Check (and Delete) Activation
			Rule.Modifier modifier = rule.getRuleModifier();
			Interface.log("\n" +"  Modifier - " + modifier);
			if(modifier != Rule.Modifier.Always) {
				Interface.log("\n" +"  Modifier Not Always");
				Interface.log("\n" +"  M: "+ (modifier == Rule.Modifier.State) + " && fired: " + (!activationFired));				
				if(modifier == Rule.Modifier.State && !activationFired) {
					Interface.log("\n" +"    Do not delete");
					continue;
				}
				else {
					Interface.log("\n" +"    To Delete " + activation.toString());
					// Delete Activation
					ruleActivationsToDelete.add(activation);
				}
			} else {
				Interface.log("\n" +"  Modifier Always"); 
			}
// -------- Check Assert Array of Rules
			if(activationFired && rule.isAssert()) {
				assertAccepted = true;
			}
		}
		
		for(RuleActivation activation : ruleActivationsToDelete) {
			if(activeRuleSet.deleteActivation(activation))
				Interface.log("\n" +"      Deleted - " + activation.toString()+ "\n");
			else
				Interface.log("\n" +"      Not Deleted - " + activation.toString() + "\n");
		}
		
		
		// Add New Activations
		for(RuleActivation activation : tempActivations){
			Interface.log("\n" +"!! Add New Activation + + " + activation );
			activeRuleSet.addNewActivation(activation);
		}
		
		Interface.log("\n" +"");
		Interface.log("\n" +"-----------------------------------");
		Interface.log("\n" +"");
		
		if(assertAccepted)
			return true;
		else
			return false;
	}
}
