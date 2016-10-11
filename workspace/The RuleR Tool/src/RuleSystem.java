import java.util.ArrayList;


public class Rule_System {
	
	private ArrayList<Rule> list;
	
	
	public Rule_System() {
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
