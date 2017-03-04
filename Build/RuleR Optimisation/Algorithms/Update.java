/**
 * This Class is representation of Update Algorithm
 * @author Mantas
 *
 */
import java.util.ArrayList;
import java.util.Map;

public class Update {
	
	private static String NEWLINE = "\n";
	private static String COMMA = ",";
	
	private static Map<Integer, ParameterBinding> parameterValues;
	private static ArrayList<RuleActivation> matchingRules;
	private static ArrayList<Integer> missingIndexes;
	
	public static boolean update(Event event){

		Interface.log(NEWLINE +"");
		Interface.log(NEWLINE +"-----------------------------------");
		Interface.log(NEWLINE +"");
		Interface.log(NEWLINE +" The Event - " + event.toString());
		Interface.log(NEWLINE +"");
		
		int eventId = event.getEventId();
		
		ActiveRuleSet activeRuleSet = Interface.activeRuleSet;
		RuleSystem ruleSystem = Interface.ruleSystem;
		
		boolean assertAccepted;
		
		if(ruleSystem.getAssertArraySize() > 0)
			assertAccepted = false;
		else
			assertAccepted = true;
		
		ArrayList<RuleActivation> tempActivations = new ArrayList<RuleActivation>();
		ArrayList<RuleActivation> ruleActivationsToDelete = new ArrayList<RuleActivation>();
		
		ArrayList<Integer> ruleIds = ruleSystem.getEventFromEventToRuleMapping(eventId);
		
		
		if(ruleIds != null)	{	
			/* Loop Through Rule IDs Which was Found Next to Event */
			ruleIdsLoop : for(int ruleID : ruleIds) {
				
				Rule rule = ruleSystem.getRule(ruleID);
				
				/* Get Trace and Rule Matching Parameter indexes */
				Integer[] matchingIndexes = rule.getEventToRuleParameterMatching(eventId);
				
				/* Get Trace Matching Parameter Hash Value */
				int eventParameterHashValue = event.getEventParametersHashValue(matchingIndexes);
				
				/* Get Active Rule if Exists */
				RuleActivation activation = activeRuleSet.getRuleActivationFromMapping(ruleID, eventParameterHashValue);
				
				if(activation == null)
					continue ruleIdsLoop;
				
				Interface.log(NEWLINE +"Rule Activation: " + activation.getRule().getRuleName());
				
				boolean activationFired = false;
				
				/* For all Rule Bindings in the Rule */
				rulebindingloop: for(RuleBinding binding : rule.getRuleBindings()){
					Interface.log(NEWLINE +"  Match: " + event.getEvent() + " vs " + binding.getEventName());
					if(event.getEvent().equals(binding.getEventName())){
						Interface.log(NEWLINE +"    True");						
							
/* * * * * * * * * * * * * *  Parameters */
						
						parameterValues = activation.getRuleParameterBindings();
						/* Get Event Parameters */
						String[] eventParam = event.getEventParameters();
						
						/* Get Binding Parameters */
						int[] bindingParam = binding.getEventParameterIndexes();
						
						if(eventParam != null) {		
							if(eventParam.length == bindingParam.length) {
								Interface.log(NEWLINE +"      Parameter Length Ok");
								int i=0;
								
								for(int index : bindingParam){
									ParameterBinding existingParameterBinding;
									if (( existingParameterBinding = parameterValues.get(index)) != null) {
										String parValue1 = existingParameterBinding.getParameterValue();
										String parValue2 = eventParam[i];
										if(!parValue1.equals(parValue2)) {
											/* Fail */
											Interface.log(NEWLINE +"      Existing Parameter and New Parameter values for same parameter does not match ");
											continue rulebindingloop;
										}
									} else {
										Parameter param = rule.getParameter(index);
										parameterValues.put(index,new ParameterBinding(param, eventParam[i],activation));
									}
									i++;				
								}	
							} else {
								Interface.log(NEWLINE +"      Parameter Length Not OK");
								continue rulebindingloop;
							}
						}
								
/* * * * * * * * * * * * * *  Conditions */
						
						int matchingRuleCount = 0;
						matchingRules = new ArrayList<RuleActivation>();
						missingIndexes = new ArrayList<Integer>();
						
						multipleRulesMatch : do {
							
							//Remove Parameter Values which differ for different RuleActivations applicable for one event
							for(Integer paramIndex : missingIndexes) {
								parameterValues.remove(paramIndex);
							}
							
							for(Condition condition : binding.getEventConditions()){
								Interface.log(NEWLINE +"      Condition: " + condition.getCondition());
								if(condition.getConditionType() == Condition.ConditionType.rule){
									if(matchingRules.size() < 2) {
										Interface.log(NEWLINE +"        Rule");
										
										boolean existanceOfRule;
										
										int ruleNameID = condition.getConditionRuleNameID();
										
										Rule theConditionRule = GlobalFunctions.getRule(ruleNameID);
										
										/** Indexes Which Determines On Which Parameters
										 * 	The Rule are Hashed in Active Rule Set
										 * 	if null, all the variables of the RuleActivation Was used
										 */
										int[] ruleMatchingParameterIndexArray
											= theConditionRule.getRuleMatchingParameterIndexArray();
										
										/* Condition indexes by rule */
										int[] condIndexes = condition.getParameterIndexes();										
										
										/** Find if All values used to hash rule are known*/
										/*int[] tempArray = null;
										
										if(ruleMatchingParameterIndexArray != null) {
											tempArray = ruleMatchingParameterIndexArray;
										} else {
											tempArray = condIndexes;		
										}*/
										
										boolean allKnown = true;
										ParameterBinding parameterBinding;
										StringBuilder values = new StringBuilder();
										int sizeOfHashedIndexes = ruleMatchingParameterIndexArray.length;
										int count = 0;
										
										for(int paramIndexForRule : condIndexes) {
											
											parameterBinding = parameterValues.get(paramIndexForRule);
																		
											if(parameterBinding == null) {
												allKnown = false;
												break;
											} else {
												if(sizeOfHashedIndexes > count) {
													values.append(parameterBinding.getParameterValue()).append(COMMA);
													count++;
												}
											}
										}
										
										/** Simple Case: just make look up by hashing all the parameters*/
										if(allKnown) {
											/* Get rid of the comma in the end */
											String paramValues = GlobalFunctions.subStringLast(values.toString(),1);
											/* Get parameter hash value */
											int parameterHash = GlobalFunctions.hash(paramValues);
											/* Find The rule */
											RuleActivation activeRule = activeRuleSet.getRuleActivationFromMapping(ruleNameID, parameterHash);
											/* Decision */
											existanceOfRule = !(activeRule == null);
										}
										/** Not Simple Case 
										 *  Run through the Rule Activations
										 */
										else {
											
											matchingRules = activeRuleSet.findMatchingRules(theConditionRule,condIndexes,parameterValues);
											
											existanceOfRule = !(matchingRules.size() <= 0);
										}
										
										/* Check for Negation On a condition */
										if(condition.getConditionNegation() == Condition.ConditionNegate.yes) {
											existanceOfRule = !existanceOfRule;
										}
										
										/* Make a decision */
										if(existanceOfRule) {
											Interface.log(NEWLINE +"          Rule Condition Accepted (yes)");
											if(matchingRules.size() > 1) 
												continue multipleRulesMatch;
										} else {
											Interface.log(NEWLINE +"          Rule Condition Not Accepted (yes)");
											continue rulebindingloop;
										}
										
										
									}
									
								} else if(condition.getConditionType() == Condition.ConditionType.compare){
											
									RuleActivation tempActivation = matchingRules.size() > 0 ? matchingRules.get(matchingRuleCount) : activation;
									boolean conditionResult = ConditionCompare(condition,tempActivation,rule);
									
									/* Make a decision */
									if(conditionResult) {
										Interface.log(NEWLINE +"          Compare Condition Accepted (yes)");
									} else {
										Interface.log(NEWLINE +"          Compare Condition Not Accepted (yes)");
										matchingRuleCount++;
										continue multipleRulesMatch;
									}
								}
							}
								
/* * * * * * * * * * * * * *  Consequent Rules */
														
							for(ConsequentRule consequentRule : binding.getConsequentRules()) {
								
								int[] consequentIndexes = consequentRule.getConsequentRuleParameterIndexes();
								
								//Check if Parameter Values Assigned for consequent rules
								for(int index : consequentIndexes) {
									if(!parameterValues.containsKey(index)) {
										Parameter parameter = rule.getParameter(index);
										//System.out.println("------- > Consequent Parameter: " + index + " " + parameter.toString() + " " + parameter.getParameterValue(activation));
										parameterValues.put(index, new ParameterBinding(parameter, parameter.getParameterValue(activation) ,activation));
									}
								}
								
								
								Interface.log(NEWLINE +"      Consequent Rule");
								// Check if Consequence is Fail
								if(consequentRule.isFail()) {
									Interface.log(NEWLINE +"        Fail");
									Interface.log(NEWLINE);
									return false;
								}
							
								// Check if is a rule
								if(!consequentRule.isOK()) {
									// Check if rule is delete or not
									if(consequentRule.isDelete()) {
										
										RuleActivation toDeleteRuleActivation = 
												activeRuleSet.getRuleActivation(
														RuleActivation.getRuleActivationKey(
																consequentRule.getRule(), parameterValues));
										
										ruleActivationsToDelete.add(toDeleteRuleActivation);
									}
									else {
										Interface.log(NEWLINE +"++++++++Rule Add");
										// Add new RuleActivation to tempArray
										tempActivations.add(new RuleActivation(consequentRule.getRuleName()+consequentRule.getConsequentRuleParameterSize()
													, parameterValues,consequentIndexes));
									}
								} else {
									Interface.log(NEWLINE +"        Rule - OK");
								}
								activationFired = true;
							} // Consequent Rule
								
							matchingRuleCount++;
						} while(matchingRules.size() > matchingRuleCount);
							
						Interface.log(NEWLINE +"    ActivationFired: " + activationFired);
					}
					else {
						Interface.log(NEWLINE +"    False");
						continue;
					}
				}
				
/* * * * * * * * * * * * * *  Check (and Delete) Activation */
				
				Rule.Modifier modifier = rule.getRuleModifier();
				Interface.log(NEWLINE +"  Modifier - " + modifier);
				if(modifier != Rule.Modifier.Always) {
					Interface.log(NEWLINE +"  Modifier Not Always");
					Interface.log(NEWLINE +"  M: "+ (modifier == Rule.Modifier.State) + " && fired: " + (!activationFired));				
					if(modifier == Rule.Modifier.State && !activationFired) {
						Interface.log(NEWLINE +"    Do not delete");
						continue;
					}
					else {
						Interface.log(NEWLINE +"    To Delete " + activation.toString());
						/* Delete Activation */
						ruleActivationsToDelete.add(activation);
					}
				} else {
					Interface.log(NEWLINE +"  Modifier Always"); 
				}
				
/* * * * * * * * * * * * * *  Check Assert Array of Rules */
				
				if(activationFired && rule.isAssert()) {
					assertAccepted = true;
				}
			}
		}
		
		for(RuleActivation activation : ruleActivationsToDelete) {
			if(activeRuleSet.deleteActivation(activation))
				Interface.log(NEWLINE +"      Deleted - " + activation.toString()+ NEWLINE);
			else
				Interface.log(NEWLINE +"      Not Deleted - " + activation.toString() + NEWLINE);
		}
		
		
		/* Add New Activations */
		for(RuleActivation activation : tempActivations){
			Interface.log(NEWLINE +"!! Add New Activation + + " + activation );
			boolean ruleAdded = activeRuleSet.addNewActivation(activation);
			
			/*if(!ruleAdded) {
				System.out.println(event + " - did not work. Rule not Added");
				return false;
			}*/
			
		}
		
		Interface.log(NEWLINE +"");
		Interface.log(NEWLINE +"-----------------------------------");
		Interface.log(NEWLINE +"");
		
		if(assertAccepted)
			return true;
		else
			return false;
	}
	
	private static boolean ConditionCompare(Condition condition, RuleActivation activation, Rule rule) {
		Interface.log(NEWLINE +"        Normal");
		/* Deal with Conditions */
		/* Get Condition Parameter Indexes */
		int[] conditionParameters = condition.getParameterIndexes();
		
		ParameterBinding[] parameterBindings = new ParameterBinding[conditionParameters.length];
		
		for(int i=0;i<parameterBindings.length;i++) {
			parameterBindings[i] = parameterValues.get(conditionParameters[i]);
			if(parameterBindings[i] == null) {
				missingIndexes.add(conditionParameters[i]);
			}
		}
		
		int i=0;
		for(ParameterBinding paramBinding : parameterBindings){
			if(paramBinding == null) {
				
				Parameter parameter = rule.getParameter(conditionParameters[i]);
				
				String parameterValue = parameter.getParameterValue(activation);
				
				parameterValues.put(conditionParameters[i],new ParameterBinding(parameter,parameterValue,activation));
				
				parameterBindings[i] = parameterValues.get(conditionParameters[i]);
			}
			i++;
		}
		
		boolean conditionResult = condition.isTrue(parameterBindings[0].getParameterValue(), parameterBindings[1].getParameterValue());
		
		if(conditionResult) {
			Interface.log(NEWLINE +"          Compare Condition Accepted (yes)");
			return true;
		} else {
			Interface.log(NEWLINE +"          Compare Condition Not Accepted (yes)");
			return false;
		}
	}
}
