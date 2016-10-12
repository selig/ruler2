import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;


public class Interface {
	
	private static String RULE_EVENT_ERROR = "There was some error while initialising rule event";
	
	private static String RULE_NAME = "Open";
	private static String RULE_PARAM = "file";
	private static String EVENT_NAME = "write";
	private static String EVENT_CONDITION = "";
	private static String EVENT_PARAMETERS = "file";
	private static String CONSEQUENT_NAME = "Write";
	private static String CONSEQUENT_PARAM = "file";
	
	public static RuleSystem ruleSystem;
	

	private JFrame mainFrame;
    private JLabel ruleModifierLabel;
    private JLabel ruleNameLabel;
    private JLabel ruleParameterLabel;
    private JLabel ruleEventLabel;
    private JLabel ruleConseqLabel;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private JPanel controlPanel2;
    private JPanel controlPanel4;
    private JPanel controlPanel5;
    private JPanel fieldPanel;
    private JPanel fieldPanel2;
    private JPanel fieldPanel1;
    private JPanel fieldPanel3;
    private JLabel ObjectLabel;
    private ButtonGroup modifierGroup;
    private ButtonGroup extraModifierGroup;
    private JPanel inside;
    private JScrollPane scrollFrame;
    private JPanel topPanel;
    private JTextArea RuleNameArea;
    private JTextArea RuleParameterArea;
    
    
    public Interface(){
       prepareGUI();
    }
    
    public static void main(String[] args){
       Interface Interface = new Interface();
       
       ruleSystem = new RuleSystem();
       
       //Interface.showEventDemo();       
    }
       
    private void prepareGUI(){
 	      mainFrame = new JFrame("Java SWING Examples");
 	      mainFrame.setSize(1000,800);
 	      mainFrame.setLayout(new FlowLayout());
    
 	      headerLabel = new JLabel("",JLabel.CENTER );
 	      statusLabel = new JLabel("",JLabel.CENTER); 
 	      
 	      ruleModifierLabel = new JLabel("Rule Modifier",JLabel.LEFT);
 	      ruleNameLabel = new JLabel("Rule Name",JLabel.LEFT);
 	      ruleParameterLabel = new JLabel("Rule Parameter",JLabel.LEFT);
 	      
 	      RuleNameArea = new JTextArea(RULE_NAME,2,10);
 	      RuleParameterArea = new JTextArea(RULE_PARAM,2,10);
	 
 	      //statusLabel.setSize(350,100);
 	      mainFrame.addWindowListener(new WindowAdapter() {
 	         public void windowClosing(WindowEvent windowEvent){
 		        System.exit(0);
 	         }        
 	      });
 	      
 	      topPanel = new JPanel();
 	      topPanel.setLayout(new FlowLayout());
 	      //topPanel.setBackground(Color.yellow);
 	      topPanel.add(headerLabel);
 	      
 	      controlPanel = new JPanel();
 	      controlPanel.setLayout(new FlowLayout());
 	      
 	      JRadioButton startButton = new JRadioButton("Start");
 	      startButton.setMnemonic(KeyEvent.VK_P);
 	      startButton.setActionCommand("Start");
 	      JRadioButton forbiddenButton = new JRadioButton("Forbidden");
 	      forbiddenButton.setMnemonic(KeyEvent.VK_P);
 	      forbiddenButton.setActionCommand("Forbidden");
 	      JRadioButton assertButton = new JRadioButton("Assert");
 	      assertButton.setMnemonic(KeyEvent.VK_P);
 	      assertButton.setActionCommand("Assert");
 	      JRadioButton noneButton = new JRadioButton("None");
 	      noneButton.setMnemonic(KeyEvent.VK_P);
 	      noneButton.setActionCommand("None");
	 
 	      //Group the radio buttons.
 	      extraModifierGroup = new ButtonGroup();
 	      extraModifierGroup.add(startButton);
 	      extraModifierGroup.add(forbiddenButton);
 	      extraModifierGroup.add(assertButton);
 	      extraModifierGroup.add(noneButton);
 	      
 	      fieldPanel1 = new JPanel();
	      fieldPanel1.setLayout(new GridLayout(0,1));
	      fieldPanel1.add(startButton);
	      fieldPanel1.add(forbiddenButton);
	      fieldPanel1.add(assertButton);
	      fieldPanel1.add(noneButton);
 	      
 	      JRadioButton alwaysButton = new JRadioButton("Always");
	      alwaysButton.setMnemonic(KeyEvent.VK_P);
	      alwaysButton.setActionCommand("Always");
	      JRadioButton stepButton = new JRadioButton("Step");
	      stepButton.setMnemonic(KeyEvent.VK_P);
	      stepButton.setActionCommand("Step");
	      JRadioButton skipButton = new JRadioButton("Skip");
	      skipButton.setMnemonic(KeyEvent.VK_P);
	      skipButton.setActionCommand("Skip");
	 
	      //Group the radio buttons.
	      modifierGroup = new ButtonGroup();
	      modifierGroup.add(alwaysButton);
	      modifierGroup.add(stepButton);
	      modifierGroup.add(skipButton);
 	      
 	      
 	      fieldPanel = new JPanel();
 	      fieldPanel.setLayout(new GridLayout(0,1));
 	      fieldPanel.add(alwaysButton);
 	      fieldPanel.add(stepButton);
 	      fieldPanel.add(skipButton);
 	      
 	      
 	      fieldPanel2 = new JPanel();
 	      fieldPanel2.setLayout(new GridLayout(0,1));
 	      fieldPanel2.add(ruleNameLabel);
 	      fieldPanel2.add(RuleNameArea);
 	      
 	      
 	      fieldPanel3 = new JPanel();
 	      fieldPanel3.setLayout(new GridLayout(0,1));
 	      fieldPanel3.add(ruleParameterLabel);
 	      fieldPanel3.add(RuleParameterArea);
 	      	      
 	      controlPanel.add(fieldPanel);
 	      controlPanel.add(fieldPanel1);
 	      controlPanel.add(fieldPanel2);
 	      controlPanel.add(fieldPanel3);
 	      
 	      
 	     
 	      inside = new JPanel();
 	      inside.setLayout(new GridLayout(0,1));
 	      scrollFrame = new JScrollPane(inside);
 	      scrollFrame.setPreferredSize(new Dimension(1000,600));
 	      inside.setAutoscrolls(true);
 	      	     
 	      inside.add(controlPanel);
 	      
 	      JButton newEventButton = new JButton("+ New Event");
 	      newEventButton.setActionCommand("Add");
 	      newEventButton.addActionListener(new ActionListener() {
 	          public void actionPerformed(ActionEvent e) {
 	        	  JPanel controlPanels = new JPanel();
 	    	      controlPanels.setLayout(new FlowLayout());
 	    	      controlPanels.setBackground(Color.red);
 	        	  
 	        	  inside.add(new eventPanele());
 	        	  inside.revalidate();
 	        	  inside.repaint();
 	          }
 	      });
 	      
 	      controlPanel4 = new JPanel();
 	      controlPanel4.setLayout(new FlowLayout());
 	      
 	      JButton addButton = new JButton("Add");
 	      addButton.setActionCommand("Add");
 	      addButton.addActionListener(new ActionListener() {
 	          public void actionPerformed(ActionEvent e) {
 	        	  AddRule();
 	          }
 	        });  
 	      
 	      controlPanel4.add(newEventButton);
 	      controlPanel4.add(addButton);
 	      
 	      ObjectLabel = new JLabel("",JLabel.LEFT);
 	      ObjectLabel.setLayout(new GridLayout(0,3));
 	      
 	      controlPanel5 = new JPanel();
 	      controlPanel5.setLayout(new FlowLayout());
 	      controlPanel5.setPreferredSize(new Dimension(1000, 100));
 	      
 	      controlPanel5.add(ObjectLabel);
 	      
 	      mainFrame.add(topPanel);
 	      mainFrame.add(scrollFrame);
 	      mainFrame.add(controlPanel4);
 	      mainFrame.add(controlPanel5);
 	      mainFrame.setVisible(true);  
 	   }
 	   
	 
    
	private void AddRule() {
		
		/*for (Component c : inside.getComponents()) {
	    	  System.out.println(">" + c);
	    	  if (c instanceof eventPanele) {
	    		  RuleBinding binding = ((eventPanele) c).ruleBinding(1);
	    		  if(binding == null) eventError();
	    		  //else myRule.addRuleBinding(binding);
	    	  }
		  }
		
		end();*/
		
		
		
		String ruleName = RuleNameArea.getText();
  	  
		boolean selectedModifier = modifierGroup.getSelection() != null;
		
  	  	        	  
  	  	if(!ruleName.equals("") && selectedModifier) {
  		  headerLabel.setBackground(Color.WHITE);
  		  
  		  String ruleModifier = modifierGroup.getSelection().getActionCommand();
  		  String ruleParameters = RuleParameterArea.getText();
  		  String selectedExtraModifier = extraModifierGroup.getSelection() != null 
  				  						 ? extraModifierGroup.getSelection().getActionCommand() : "None";
    	  
  		  
  		  RuleNameArea.setBackground(Color.WHITE);
  		  
      	  Rule myRule = new Rule(ruleName, ruleModifier,selectedExtraModifier, ruleParameters);
      	  
      	  for (Component c : inside.getComponents()) {
	    	  //System.out.println(">" + c);
	    	  if (c instanceof eventPanele) {
			  	RuleBinding binding = ((eventPanele) c).ruleBinding(myRule.getRuleID());
    		  	if(binding == null) eventError();
    		  	else myRule.addRuleBinding(binding);
	    	  }
		  }

      	  ruleSystem.addNewRule(myRule);
      	  
          statusLabel.setText( "Added Rule "+ruleName);
            
	      showRules();
	  	}
	  	else {
  		  initialiseError();
  		  
  		  if(ruleName.equals("")){
  			  RuleNameArea.setBackground(Color.RED);
  			  addError("Rule Name is empty");
  		  }else {RuleNameArea.setBackground(Color.WHITE);}
  		  
  		  if (!selectedModifier) {
  			emptyRuleModifier();
  		  }
	  	}
	}

	private void initialiseError() {
		headerLabel.setOpaque(true);
		headerLabel.setBackground(Color.RED);
		headerLabel.setText("");		
	}
	
	private void addError(String newMessage) {
		headerLabel.setText(headerLabel.getText() + " " + newMessage);
	}
	
	private void eventError() {
		initialiseError();
		headerLabel.setText(headerLabel.getText() + " " + RULE_EVENT_ERROR);
	}

	private void emptyRuleModifier() {
	   addError(" Rule Modifier is not selected");
	}

	private void showRules(){
	      headerLabel.setText("Rule Added"); 
	      ObjectLabel.setText(ObjectLabel.getText() + " | " + ruleSystem.getRules());      
	   }

	protected void end() {
		System.exit(0);	
	}
	
	
	class eventPanele extends JPanel {
	   
	   private JPanel eventPanel;
	   private JPanel eventPanel2;
	   private JPanel eventPanel3;
	   private JPanel eventPanel5;
	   private JPanel eventPanel4Container;
	   private JLabel eventName;
	   private JLabel eventCondition;
	   private JLabel eventParameter;
	   private JTextArea eventNameArea;
	   private JTextArea eventConditionArea;
	   private JTextArea eventParameterArea;
		
	   public eventPanele() {
			
			eventName = new JLabel("Event Name",JLabel.LEFT);
			eventCondition = new JLabel("Event Condition",JLabel.LEFT);
			eventParameter = new JLabel("Event Parameter",JLabel.LEFT);
			
			eventNameArea 	   = new JTextArea(EVENT_NAME,2,10);
			eventConditionArea = new JTextArea(EVENT_CONDITION,2,10);
			eventParameterArea = new JTextArea(EVENT_PARAMETERS,2,10);
			
			eventPanel = new JPanel();
		    eventPanel.setLayout(new GridLayout(0,1));
		    eventPanel.add(eventName);
		    eventPanel.add(eventNameArea);
		      
		    eventPanel2 = new JPanel();
		    eventPanel2.setLayout(new GridLayout(0,1));
		    eventPanel2.add(eventCondition);
		    eventPanel2.add(eventConditionArea);
		    
		    eventPanel3 = new JPanel();
		    eventPanel3.setLayout(new GridLayout(0,1));
		    eventPanel3.add(eventParameter);
		    eventPanel3.add(eventParameterArea);
		    
		    
		    eventPanel4Container = new JPanel();
		    eventPanel4Container.setLayout(new GridLayout(0,1));
		    eventPanel4Container.setBackground(Color.blue);
		    
		    eventPanel4Container.add(new consequentPanel());
		    
		    eventPanel5 = new JPanel();
		    eventPanel5.setLayout(new GridLayout(0,1));
		    JButton newConsequentButton = new JButton("+ Consequent");
		    newConsequentButton.addActionListener(new ActionListener() {
		    	@Override
				public void actionPerformed(ActionEvent e) {  
		    		eventPanel4Container.add(new consequentPanel());
		    		eventPanel4Container.revalidate();
		    		eventPanel4Container.repaint();					
				}
			});
		    eventPanel5.add(newConsequentButton);
		    
		
		    add(eventPanel);
		    add(eventPanel2);
		    add(eventPanel3);
		    add(eventPanel4Container);
		    add(eventPanel5);
		}
		
		
		public RuleBinding ruleBinding(int RuleID) {
			
			String eventName = eventNameArea.getText();
			String eventParameterString = eventParameterArea.getText();
			
			int eventParameterCount = eventParameterString.split(",").length;
			
			
			//System.out.println("--> " + eventName + " " + eventParameterString + " " + eventParameterCount);
			
			ArrayList<ConsequentRule> consequentRules = new ArrayList<ConsequentRule>();
			
			for (Component c : eventPanel4Container.getComponents()) {
		    	  //System.out.println(">" + c);
		    	  if (c instanceof consequentPanel) {
				  	ConsequentRule consequent = ((consequentPanel) c).getConsequentRule();
	    		  	if(consequent != null) consequentRules.add(consequent);
		    	  }
			}
			
			if(eventName.length() > 0 )	return new RuleBinding(RuleID,eventName,eventName.length(),eventParameterCount,consequentRules);
			else return null;
		}
		
	}
	
	class consequentPanel extends JPanel {
		
		private JPanel eventPanel4;
		private JLabel eventConsequent;
		private JLabel eventConsequentParameter;
		private JTextArea eventConsequentArea;
		private JTextArea eventConsequentParameterArea;
		
		public consequentPanel() {

			eventConsequent = new JLabel("Event Consequent",JLabel.LEFT);
			eventConsequentParameter = new JLabel("Event Consequent Parameter",JLabel.LEFT);

			eventConsequentArea 		 = new JTextArea(CONSEQUENT_NAME ,2,10);
			eventConsequentParameterArea = new JTextArea(CONSEQUENT_PARAM,2,10);
			
			
			eventPanel4 = new JPanel();
		    eventPanel4.setLayout(new GridLayout(2,2));
		    
		    eventPanel4.add(eventConsequent);
		    eventPanel4.add(eventConsequentParameter);
		    
		    eventPanel4.add(eventConsequentArea);
		    eventPanel4.add(eventConsequentParameterArea);
		    
		    add(eventPanel4);
		}
		
		public ConsequentRule getConsequentRule() {
			
			String consequentNameString = eventConsequentArea.getText();
			int eventConsequentParameters = eventConsequentParameterArea.getText().split(",").length;
			
			if(!consequentNameString.equals(""))
				return new ConsequentRule(consequentNameString, eventConsequentParameters);
			else return null;
		}
	}
}
