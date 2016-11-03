/**
 * This Class is representation of Update Algorithm
 * @author Mantas
 *
 */
import java.util.ArrayList;
import java.util.Map;

public class Update {
	
	public static boolean update(Event event){
		
		ActiveRuleSet activeRuleSet = Interface.activeRuleSet;
		
		ArrayList<RuleActivation> tempActivations = new ArrayList<RuleActivation>();
		
		for(RuleActivation activation : activeRuleSet.getArrayOfRuleActivations()){
			// search rule with the event() (some fancy efficient way)
			
			boolean activationFired = false;
			
			Rule rule = activation.getRule();
			rulebindingloop: for(RuleBinding binding : rule.getRuleBinding()){
				if(event.getEvent().equals(binding.getEventName())){
					activationFired = true;
					
					// Parameters
					Map<Integer, ParameterBinding> parameterValues = activation.getParameterBindings();
					// Get Event Parameters
					String[] eventParam = event.getEventParameters();
					
					// Get Binding Parameters
					int[] bindingParam = binding.getEventParameterIndexes();
					
					if(eventParam.length == bindingParam.length) {
						int i=0;
						
						for(int index : bindingParam){
							// Get Parameter
							Parameter param = rule.getParameter(index);
							
							// Create and add new ParameterBinding to temp Set
							parameterValues.put(index,new ParameterBinding(param, eventParam[i],activation));
							
							i++;				
						}	
					} else continue;
					
					// Conditions
					for(Condition condition : binding.getEventConditions()){
						if(condition.getConditionType() == Condition.ConditionType.rule){
							// Deal with Rule condition
/* ******* */							
						} else {
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
							if(!condition.isTrue(parameterBindings[0].getParameterValue(), parameterBindings[1].getParameterValue()))
								continue rulebindingloop;
							
						}
					}
					
					for(ConsequentRule consequentRule : binding.getConsequentRules()){
						// Check if Consequence is Fail
						if(consequentRule.isFail()) return false;
						
						// Check if is a rule
						if(!consequentRule.isOK())
							// Add new RuleActivation to tempArray
							tempActivations.add(new RuleActivation(consequentRule.getRuleName()
										, parameterValues));
					}
					
					
					
					
				}
				else continue;
			}
			// Check (and Delete) Activation
			Rule.Modifier modifier = rule.getRuleModifier();
			if(modifier != Rule.Modifier.Always) {
				if(modifier == Rule.Modifier.Skip && !activationFired) continue;
				else {
					// Delete Activation
					activeRuleSet.deleteActivation(activation);
				}
			}
		}
		
		// Add New Activations
		for(RuleActivation activation : tempActivations){
			activeRuleSet.addNewActivation(activation.getRule().getRuleID(), activation);
		}
		
		return true;
	}
}
