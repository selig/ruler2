import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;


public class Interface {
	
	//Simple rule set 
	private static final String rule1 = "<None Always Open(file) { [open(file)<> ¬> isOpen(file)][close(file)<!isOpen(file)> ¬> Fail] }>";
	private static final String rule2 = "<None Step isOpen(file) { [open(file)<> ¬> Fail][close(file)<> ¬> Ok] }>";
	
	
	private static long startTime = 0;
	private static long finishTime = 0;
	
	private static final String RULE_EXIST = "This RULE with same PARAMETERS already exists.";
	private static final String RULE_EVENT_ERROR = "There was some error while initialising rule event.";
	private static final String START_RULE_PARAMETER = "Parameters are empty on Start rule.";
	private static final String THEREARE = "There are ";
	private static final String RULESINRULESYSTEM = " rules in Rule System";
	private static final String RULESINACTIVERULESET = " Active Rules in Active Rule Set";
	
	private static String RULE_NAME = "Open";
	private static String RULE_PARAM = "file";
	private static String EVENT_NAME = "write";
	private static String EVENT_CONDITION = "";
	private static String EVENT_PARAMETERS = "file";
	private static String CONSEQUENT_NAME = "Write";
	private static String CONSEQUENT_PARAM = "file";
	
	public static RuleSystem ruleSystem;
	public static ActiveRuleSet activeRuleSet;
	

	private JFrame mainFrame;
    //private JLabel ruleModifierLabel;
	private JLabel ruleNameLabel;
    private JLabel eventLabel;
    private JLabel ruleParameterLabel;
    //private JLabel ruleEventLabel;
    //private JLabel ruleConseqLabel;
    private JPanel controlPanel;
    //private JPanel controlPanel2;
    private JPanel controlPanel4;
    private JPanel fieldPanel;
    private JPanel fieldPanel2;
    private JPanel fieldPanel1;
    private JPanel fieldPanel3;
    private JPanel eventPanel;
    private ButtonGroup modifierGroup;
    private ButtonGroup extraModifierGroup;
    private JPanel inside;
    private JPanel ruleSystemInside;
    private JPanel activeRuleSetInside;
    private JScrollPane scrollFrame;
    private JScrollPane ruleSystemScrollFrame;
    private JScrollPane activeRuleSetScrollFrame;
    private JTextArea RuleNameArea;
    private JTextArea eventArea;
    private JTextArea RuleParameterArea;
    private JComponent panel1;
    private JComponent panel2;
    private JComponent panel3;
    private JComponent panel4;
    private JComponent panel5;
    private JTabbedPane TabbedPane;
    private JLabel ruleSystemGUIHeader; 
    private JLabel activeRuleSetGUIHeader;
    public static Interface Interface;
    
    
    
    public Interface(){
       prepareGUI();
    }
    
    public static void main(String[] args){
       
    	/*System.out.println(GlobalFunctions.hashName("Open"));
    	System.out.println(GlobalFunctions.hashName("isOpen"));
    	System.out.println(GlobalFunctions.hashName("Open"));
    	System.out.println(GlobalFunctions.hashName("isOpen"));
    	System.out.println(GlobalFunctions.hashName("Open"));
    	System.out.println(GlobalFunctions.hashName("isOpen"));
    	
    	end();*/
    	
       Interface = new Interface();
       
       ruleSystem = new RuleSystem();
       
       ruleSystem.addPredifinedRules(rule1);
       ruleSystem.addPredifinedRules(rule2);
       
       activeRuleSet = new ActiveRuleSet();
       
       ruleSystem.activateRules(activeRuleSet);
       
       System.out.println(activeRuleSet.getNumberOfActivations());
       
       Interface.updateRuleSystemGUI();
       Interface.activeRuleGUI();
       
       //Interface.showEventDemo();       
    }
       
    private void prepareGUI(){
 	      mainFrame = new JFrame("Java SWING Examples");
 	      mainFrame.setSize(1000,800);
 	      mainFrame.add(new Tabbed(), BorderLayout.CENTER);
 	      mainFrame.setVisible(true); 
 	   }
    
	private void AddRule() {
		
		time();
		
		String ruleName = RuleNameArea.getText();
  	  
		boolean selectedModifier = modifierGroup.getSelection() != null;
		
		String selectedExtraModifier = extraModifierGroup.getSelection() != null 
					 ? extraModifierGroup.getSelection().getActionCommand() : "None";
		String ruleParameters = RuleParameterArea.getText();
  	  	        	  
  	  	if(!ruleName.equals("") && selectedModifier) {
  	  		
  	  		System.out.println(selectedExtraModifier +" <" + ruleParameters + ">");
  	  		if(selectedExtraModifier.equals("Start") && ruleParameters.equals("")) {
  	  			error(START_RULE_PARAMETER);
  	  			return;
  	  		}
  	  	
	  		  //headerLabel.setBackground(Color.WHITE);
	  		  
	  	  	  String ruleModifier = modifierGroup.getSelection().getActionCommand();	
	  		  RuleNameArea.setBackground(Color.WHITE);
	  		  
	      	  ArrayList<RuleBinding> ruleBindigsArrayList = new ArrayList<RuleBinding>();
	  		  
	      	  for (Component c : inside.getComponents()) {
		    	  //System.out.println(">" + c);
		    	  if (c instanceof EventForm) {
				  	RuleBinding binding = ((EventForm) c).ruleBinding();
	    		  	if(binding == null) {
	    		  		error(RULE_EVENT_ERROR);
	    		  		return;
	    		  	}
	    		  	else ruleBindigsArrayList.add(binding);
	    		  	
	    		  	inside.remove(c);
	    		  	inside.revalidate();
		        	inside.repaint();
		    	  }
			  }
	      	  
	      	Rule myRule = new Rule(ruleName, ruleModifier,selectedExtraModifier, ruleParameters, ruleBindigsArrayList);
	
	      	  if(!ruleSystem.addNewRule(myRule)) {
	      		  error(RULE_EXIST);
	      		  return;
	  	  	  }
	      	  
	          //statusLabel.setText( "Added Rule "+ruleName);
	      	  success("Rule " + ruleName + " added successfully");
	      	  updateRuleSystemGUI(myRule.getRuleNameID());
  	  	
	  	}
	  	else {
  		  
  		  if(ruleName.equals("")){
  			  RuleNameArea.setBackground(Color.RED);
  			error("Rule Name is empty");
  		  }else {RuleNameArea.setBackground(Color.WHITE);}
  		  
  		  if (!selectedModifier) {
  			error("Rule Modifier is not selected");
  		  }
	  	}
	}

	private void success(String successMessage) {
		JOptionPane.showMessageDialog(mainFrame,
				successMessage);
		System.out.println("Suc: " + successMessage);
	}
	
	
	private void error(String errorMessage) {
		JOptionPane.showMessageDialog(mainFrame,
			    errorMessage + " Please fix the problem",
			    "Inane error",
			    JOptionPane.ERROR_MESSAGE);
		System.out.println("Err: " + errorMessage);
	}

	private void updateRuleSystemGUI(Integer ruleNameID){
	      //headerLabel.setText("Rule Added"); 
	      //ObjectLabel.setText(/*ObjectLabel.getText() + " | " +*/ ruleSystem.getRules() + "<! "+time() +" !>");      
		//System.out.println(ruleSystem.getOneRule(ruleNameID));
		ruleSystemInside.add(new RuleString(ruleSystem.getOneRule(ruleNameID)));
		ruleSystemInside.revalidate();
		ruleSystemInside.repaint();
		
		ruleSystemGUIHeader.setText(THEREARE + ruleSystem.getNumberOfRules() + RULESINRULESYSTEM); 
	}
	
	private void updateRuleSystemGUI(){
		
		ruleSystemInside.removeAll();
		ruleSystemInside.revalidate();
		ruleSystemInside.repaint();
		
		for(String rule : ruleSystem.getRules()) {
			ruleSystemInside.add(new RuleString(rule));
			ruleSystemInside.revalidate();
			ruleSystemInside.repaint();
			ruleSystemGUIHeader.setText(THEREARE + ruleSystem.getNumberOfRules() + RULESINRULESYSTEM);
		}
	}
	
	private void activeRuleGUI(){
		
		activeRuleSetInside.removeAll();
		activeRuleSetInside.revalidate();
		activeRuleSetInside.repaint();
		
		for(String activation : activeRuleSet.getActivations()) {
			activeRuleSetInside.add(new RuleString(activation));
			activeRuleSetInside.revalidate();
			activeRuleSetInside.repaint();
			activeRuleSetGUIHeader.setText(THEREARE + activeRuleSet.getNumberOfActivations() + RULESINACTIVERULESET);
		}
	}

	private String time() {
		finishTime = System.currentTimeMillis();
		
		long time = finishTime - startTime;
		
		startTime = System.currentTimeMillis();
		
		return (time)+"ms";
	}

	protected static void end() {
		
		//System.out.println(time());
		
		System.exit(0);	
	}
	
	
	class EventForm extends JPanel {
	   
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
		
	   public EventForm() {
		    super(new FlowLayout());
		    //super(new GridLayout(1, 1));
			
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
		    
		    eventPanel4Container.add(new ConsequentRuleForm());
		    
		    eventPanel5 = new JPanel();
		    eventPanel5.setLayout(new GridLayout(0,1));
		    JButton newConsequentButton = new JButton("+ Consequent");
		    newConsequentButton.addActionListener(new ActionListener() {
		    	@Override
				public void actionPerformed(ActionEvent e) {  
		    		eventPanel4Container.add(new ConsequentRuleForm());
		    		eventPanel4Container.revalidate();
		    		eventPanel4Container.repaint();					
				}
			});
		    eventPanel5.add(newConsequentButton);
		    
		
		    add(eventPanel);
		    add(eventPanel3);
		    add(eventPanel2);
		    add(eventPanel4Container);
		    add(eventPanel5);
		}
		
		
		public RuleBinding ruleBinding() {
			
			String eventName = eventNameArea.getText();
			String eventParameterString = eventParameterArea.getText();
			String eventConditionString = eventConditionArea.getText();
			
			/*
			 * Add Check that parameters are not the same
			 * isDuplicate(); 
			 * */
			
			String[] eventParameters = GlobalFunctions.removeWhiteSpaces(eventParameterString).split(",");
			String[] eventConditions = GlobalFunctions.removeWhiteSpaces(eventConditionString).split(",");
			
			ArrayList<ConsequentRule> consequentRules = new ArrayList<ConsequentRule>();
			
			for (Component c : eventPanel4Container.getComponents()) {
		    	  if (c instanceof ConsequentRuleForm) {
				  	ConsequentRule consequent = ((ConsequentRuleForm) c).getConsequentRule();
	    		  	if(consequent != null) consequentRules.add(consequent);
		    	  }
			}
			
			if(eventName.length() > 0 )	return new RuleBinding(eventName,eventParameters,eventConditions,consequentRules);
			else return null;
		}
		
	}
	
	public static JPanel CreateNewInside(){
		JPanel insider = new JPanel();
		insider.setLayout(new GridLayout(0,1));
		return insider;
	}
	
	
	class ConsequentRuleForm extends JPanel {
		
		private JPanel eventPanel4;
		private JLabel eventConsequent;
		private JLabel eventConsequentParameter;
		private JTextArea eventConsequentArea;
		private JTextArea eventConsequentParameterArea;
		
		public ConsequentRuleForm() {

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
			String[] eventConsequentParameters = GlobalFunctions.removeWhiteSpaces(eventConsequentParameterArea.getText()).split(",");
			
			/*
			 * Add Check that parameters are not the same 
			 * */
			
			if(!consequentNameString.equals(""))
				return new ConsequentRule(consequentNameString, eventConsequentParameters);
			else return null;
		}
	}

	class Tabbed extends JPanel {
	    
		public Tabbed() {
	        super(new GridLayout(1, 1));
	        
	        TabbedPane = new JTabbedPane();
	        ImageIcon icon = null;// = createImageIcon("images/middle.gif");
	        
	        panel1 = new AddRules();
	        TabbedPane.addTab("Add Rules", icon, panel1,
	                "Add Rules");
	        TabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
	        
	        panel2 = new RuleSystemGUI();
	        TabbedPane.addTab("View Rules", icon, panel2,
	                "Does twice as much nothing");
	        TabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
	        
	        panel3 = new ActiveRuleSetGUI();
	        TabbedPane.addTab("Active Rule Set", icon, panel3,
	                "Still does nothing");
	        TabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
	        
	        panel4 = new AddEventGUI();
	        panel4.setPreferredSize(new Dimension(410, 50));
	        TabbedPane.addTab("Add Event", icon, panel4,
	                "Does nothing at all");
	        TabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
	        
	        panel5 = makeTextPanel(
	                "Panel #4 (has a preferred size of 410 x 50).");
	        panel5.setPreferredSize(new Dimension(410, 50));
	        TabbedPane.addTab("Log", icon, panel5,
	                "Does nothing at all");
	        TabbedPane.setMnemonicAt(3, KeyEvent.VK_5);
	        
	        //Add the Tabbed pane to this panel.
	        add(TabbedPane);
	        
	        //The following line enables to use scrolling Tabbed.
	        TabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	    }
	    
	    protected JComponent makeTextPanel(String text) {
	        JPanel panel = new JPanel(false);
	        JLabel filler = new JLabel(text);
	        filler.setHorizontalAlignment(JLabel.CENTER);
	        panel.setLayout(new GridLayout(1, 1));
	        panel.add(filler);
	        return panel;
	    }
	    
	    /** Returns an ImageIcon, or null if the path was invalid. */
	    /*protected static ImageIcon createImageIcon(String path) {
	        java.net.URL imgURL = Tabbed.class.getResource(path);
	        if (imgURL != null) {
	            return new ImageIcon(imgURL);
	        } else {
	            System.err.println("Couldn't find file: " + path);
	            return null;
	        }
	    }*/
	}

	class AddRules extends JPanel {
		
		public AddRules() {
			super(new FlowLayout());			

			add(new RuleForm());
			add(new RuleButtons());
		}
	}
	
	class RuleForm extends JPanel {
		
		public RuleForm() {
			//super(new GridLayout(1, 1));
			super(new FlowLayout());
			
			ruleNameLabel = new JLabel("Rule Name",JLabel.LEFT);
	 	    ruleParameterLabel = new JLabel("Rule Parameter",JLabel.LEFT);
	 	      
	 	    RuleNameArea = new JTextArea(RULE_NAME,2,10);
	 	    RuleParameterArea = new JTextArea(RULE_PARAM,2,10);
	 	    
	 	    controlPanel = new JPanel();
	 	    //controlPanel.setLayout(new GridLayout(1,4));
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
	 	      
	 	     JButton newEventButton = new JButton("+ New Event");
	 	      newEventButton.setActionCommand("Add");
	 	      newEventButton.addActionListener(new ActionListener() {
	 	          public void actionPerformed(ActionEvent e) {
	 	        	  
	 	        	  inside.add(new EventForm());
	 	        	  inside.revalidate();
	 	        	  inside.repaint();
	 	          }
	 	      });
	 	      
	 	      	      
	 	      controlPanel.add(fieldPanel);
	 	      controlPanel.add(fieldPanel1);
	 	      controlPanel.add(fieldPanel2);
	 	      controlPanel.add(fieldPanel3);
	 	      controlPanel.add(newEventButton);
	 	      
	 	      inside = new JPanel();
	 	      inside.setLayout(new GridLayout(0,1));
	 	      //inside.setLayout(new FlowLayout());
	 	      scrollFrame = new JScrollPane(inside);
	 	      scrollFrame.setPreferredSize(new Dimension(1000,600));
	 	      inside.setAutoscrolls(true);
	 	      	     
	 	      inside.add(controlPanel);
	 	      
	 	      add(scrollFrame);
			
		}
		
	}

	class RuleButtons extends JPanel {
		
		public RuleButtons() {
			super(new GridLayout(1, 1));
	 	      
	 	      controlPanel4 = new JPanel();
	 	      controlPanel4.setLayout(new FlowLayout());
	 	      
	 	      JButton addButton = new JButton("Add Rule");
	 	      addButton.setActionCommand("Add");
	 	      addButton.addActionListener(new ActionListener() {
	 	          public void actionPerformed(ActionEvent e) {
	 	        	  AddRule();
	 	          }
	 	        });  
	 	      
	 	      //controlPanel4.add(newEventButton);
	 	      controlPanel4.add(addButton);
	 	      
	 	      controlPanel4.setPreferredSize(new Dimension(1000, 100));
	 	      
	 	      add(controlPanel4);
		}
		
	}

	class RuleSystemGUI extends JPanel {
		
		public RuleSystemGUI() {
			
			ruleSystemGUIHeader = new JLabel(THEREARE + 0 + RULESINRULESYSTEM,JLabel.LEFT);
			
			ruleSystemInside = CreateNewInside();
			ruleSystemScrollFrame = new JScrollPane(ruleSystemInside);
			ruleSystemScrollFrame.setPreferredSize(new Dimension(1000,600));
	 	    ruleSystemInside.setAutoscrolls(true);
	 	    
	 	    add(ruleSystemGUIHeader);
	 	    add(ruleSystemScrollFrame);
	 	    
		}
		
		public void recreate(){
			this.revalidate();
			this.repaint();
		}
	}
	
	class ActiveRuleSetGUI extends JPanel {
		
		public ActiveRuleSetGUI() {
			
			activeRuleSetGUIHeader = new JLabel(THEREARE + 0 + RULESINACTIVERULESET,JLabel.LEFT);
			
			activeRuleSetInside = CreateNewInside();
			activeRuleSetScrollFrame = new JScrollPane(activeRuleSetInside);
			activeRuleSetScrollFrame.setPreferredSize(new Dimension(1000,600));
	 	    activeRuleSetInside.setAutoscrolls(true);
	 	    
	 	    add(activeRuleSetGUIHeader);
	 	    add(activeRuleSetScrollFrame);
	 	    
		}
		
		public void recreate(){
			this.revalidate();
			this.repaint();
		}
		
	}
	
	class RuleString extends JPanel {
		
		private JLabel ruleString;
		private JPanel rulePanel;
		
		public RuleString(String Rule) {
			super(new GridLayout(0,1));
			
			ruleString = new JLabel(Rule,JLabel.LEFT);
			
			rulePanel = new JPanel();
			rulePanel.setLayout(new FlowLayout());
			
			rulePanel.add(ruleString);
			
			add(rulePanel);
		}
	}
	
	class AddEventGUI extends JPanel {
		
		private JLabel ruleString;
		private JPanel rulePanel;
		
		public AddEventGUI() {
			super(new GridLayout(0,1));
			
			eventLabel = new JLabel("Event",JLabel.LEFT);
			eventArea = new JTextArea("open(file)",2,10);
			
			eventPanel = new JPanel();
			eventPanel.setLayout(new FlowLayout());
			eventPanel.add(eventLabel);
			eventPanel.add(eventArea);
			
			JButton addButton = new JButton("Trigger Event");
 	        addButton.setActionCommand("event");
 	        addButton.addActionListener(new ActionListener() {
 	          public void actionPerformed(ActionEvent e) {
 	        	 String event = eventArea.getText();
 	        	 
 	        	 System.out.println("Event: " + event);
 	        	 
 	        	 /*String eventName = event.split("(")[0];
 	        	 String[] eventParameters = event.replaceAll(")", "").split("(")[1].split(",");
 	        	 */
 	        	 if(Update.update(new Event(event)))
 	        		Interface.success("True");
 	        	 else
 	        		Interface.error("False");
 	        	 
 	        	 Interface.activeRuleGUI();
 	          }
 	        });  
	 	      
 	        eventPanel.add(addButton);
			
			add(eventPanel);
		}
	}
}
