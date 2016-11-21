/**
 * This Class is representation of Update Algorithm
 * @author Mantas
 *
 */
import java.util.ArrayList;
import java.util.Map;

public class Update {
	
	public static boolean update(Event event){

		System.out.println("");
		System.out.println("-----------------------------------");
		System.out.println("");
		
		ActiveRuleSet activeRuleSet = Interface.activeRuleSet;
		
		ArrayList<RuleActivation> tempActivations = new ArrayList<RuleActivation>();
		
		// For all Active rules
		for(RuleActivation activation : activeRuleSet.getArrayOfRuleActivations()){
			// search rule with the event() (some fancy efficient way)
			System.out.println("Rule Activation: " + activation.getRule().getRuleName());
			boolean activationFired = false;
			// Get rule of active rule
			Rule rule = activation.getRule();
			
			// For all Rule Bindings
			rulebindingloop: for(RuleBinding binding : rule.getRuleBinding()){
				System.out.println("  Match: " + event.getEvent() + " vs " + binding.getEventName());
				if(event.getEvent().equals(binding.getEventName())){
					activationFired = true;
					System.out.println("    True. ActivationFired: " + activationFired);
					
					for(int bindingIndex : binding.getEventParameterIndexes()) {
						for(int ruleIndex : rule.getRuleParameterIndexes()) {
							if(bindingIndex == ruleIndex) {
								// get Event Param Value
								String eventValue = event.getEventParameter(binding.getEventParameterIndex(bindingIndex));
								// get Rule Param Value
								String ruleValue = activation.getParameterValue(ruleIndex);
							}
						}
					}
					
					System.exit(0);
					
					// Parameters
					Map<Integer, ParameterBinding> parameterValues = activation.getParameterBindings();
					// Get Event Parameters
					String[] eventParam = event.getEventParameters();
					
					// Get Binding Parameters
					int[] bindingParam = binding.getEventParameterIndexes();
					
					if(eventParam.length == bindingParam.length) {
						System.out.println("      Parameter Length Ok");
						int i=0;
						
						for(int index : bindingParam){
							// Get Parameter
							Parameter param = rule.getParameter(index);
							
							// Create and add new ParameterBinding to temp Set
							parameterValues.put(index,new ParameterBinding(param, eventParam[i],activation));
							
							i++;				
						}	
					} else {
						System.out.println("      Parameter Length Not OK");
						continue;
					}
					
					// Conditions
					for(Condition condition : binding.getEventConditions()){
						System.out.println("      Condition");
						if(condition.getConditionType() == Condition.ConditionType.rule){
							System.out.println("        Rule");
							// Deal with Rule condition
							// Get rule
							String ruleName = condition.getCondition();
							
							boolean existanceOfRule = activeRuleSet.activeRuleExist(ruleName);
							System.out.println("          Result of Search - "+ existanceOfRule);
							
							if(condition.getConditionNegation() == Condition.ConditionNegate.yes) {
								if(!existanceOfRule) {
									System.out.println("          Rule Condition Accepted (yes)");
								} else {
									System.out.println("          Rule Condition Not Accepted (yes)");
									continue rulebindingloop;
								}
							} else {
								if(existanceOfRule) {
									System.out.println("          Rule Condition Accepted (no)");
								} else {
									System.out.println("          Rule Condition Not Accepted (no)");
									continue rulebindingloop;
								}
							}
						} else if(condition.getConditionType() == Condition.ConditionType.compare){
							System.out.println("        Normal");
							//Deal with Conditions
							// Get Condition Parameter Indexes
							int[] conditionParameters = condition.getParameterIndexes();
							
							ParameterBinding[] parameterBindings = new ParameterBinding[conditionParameters.length];
							
							for(int i=0;i>parameterBindings.length;i++) {
								parameterBindings[i] = activation.getParameterBinding(conditionParameters[i]);
							}
							
							int i=0;
							for(ParameterBinding paramBinding : parameterBindings){
								if(paramBinding == null) {
									// Get Parameter from Rules
									Parameter parameter1 = rule.getParameter(conditionParameters[0]);
									
									// Get Parameter Variable indexes
									Integer[] varIndexes1 = parameter1.getParameterVariables();
									
									// Get Variable1 value
									int variable1Value = activation.getVariableValue(varIndexes1[0]);
									
									// Get Variable1 value
									int variable2Value = activation.getVariableValue(varIndexes1[1]);
									
									// Get parameter 1 value
									int param1Value = parameter1.getParameterValue(variable1Value, variable2Value);
									
									parameterValues.put(conditionParameters[0],new ParameterBinding(parameter1,param1Value+"",activation));
									
									parameterBindings[i] = parameterValues.get(conditionParameters[0]);
								}
								i++;
							}
							
							// Get Condition result
							if(!condition.isTrue(parameterBindings[0].getParameterValue(), parameterBindings[1].getParameterValue())) {
								System.out.println("          Condition Not ok");
								continue rulebindingloop;
							} else {
								System.out.println("          Condition OK");
							}
							
						}
					}
					
					for(ConsequentRule consequentRule : binding.getConsequentRules()){
						System.out.println("      Consequent Rule");
						// Check if Consequence is Fail
						if(consequentRule.isFail()) {
							System.out.println("        Fail");
							return false;
						}
						
						// Check if is a rule
						if(!consequentRule.isOK()) {
							System.out.println("++++++++Rule Add");
							// Add new RuleActivation to tempArray
							tempActivations.add(new RuleActivation(consequentRule.getRuleName()+event.getEventParametersSize()
										, parameterValues));
						} else {
							System.out.println("        Rule - OK");
						}
					}
					
					
					
					
				}
				else {
					System.out.println("    False");
					continue;
				}
			}
			// Check (and Delete) Activation
			Rule.Modifier modifier = rule.getRuleModifier();
			if(modifier != Rule.Modifier.Always) {
				System.out.println("  Modifier Not Always"); 
				if(modifier == Rule.Modifier.State && !activationFired) {
					System.out.println("    Do not delete");
					continue;
				}
				else {
					System.out.println("    Delete");
					// Delete Activation
					activeRuleSet.deleteActivation(activation);
				}
			} else {
				System.out.println("  Modifier Always"); 
			}
		}
		
		// Add New Activations
		for(RuleActivation activation : tempActivations){
			System.out.println("Add New Activation + + " + activation.getRule().getRuleName() + " " + activation.getRule().getRuleNameID() );
			activeRuleSet.addNewActivation(activation.getRule().getRuleNameID(), activation);
		}
		
		System.out.println("");
		System.out.println("-----------------------------------");
		System.out.println("");
		
		return true;
	}
}
