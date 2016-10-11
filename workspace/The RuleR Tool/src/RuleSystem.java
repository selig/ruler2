import java.util.ArrayList;


public class RuleSystem {
	
	private ArrayList<Rule> list;
	
	
	public RuleSystem() {
		list = new ArrayList<Rule>();
	}

	public void addNewRule(Rule myRule) {
		list.add(myRule);
	}
	
	public String getRules() {
		
		String allRules = "";
		
		for (Rule oneRule : list) {
			allRules += "<"+oneRule.toString()+"> ; ";
		}
		
		return allRules;
	}

}
