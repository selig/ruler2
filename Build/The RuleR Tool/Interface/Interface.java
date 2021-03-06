import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.*;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * Optimised Implementation
 * 
 * @author Mantas
 */

public class Interface {
	private static boolean logOn = true;
	public static File RULE_FILE = null;
	public static File EVENTS_FILE = null;
	public static File FOLDER = null;
	public static String TEST_OPTION = null;
	public static TreeMap<Integer,String> TestResultTable = new TreeMap<Integer, String>();
	
	private static int numberOfTestRuns = 1;
	
	private static long startTime = 0;
	private static long finishTime = 0;
	
	private static final String RULE_EXIST = "This RULE with same PARAMETERS already exists.";
	private static final String RULE_EVENT_ERROR = "There was some error while initialising rule event.";
	private static final String START_RULE_PARAMETER = "Parameters are empty on Start rule.";
	private static final String THEREARE = "There are ";
	private static final String RULESINRULESYSTEM = " rules in Rule System";
	private static final String RULESINACTIVERULESET = " Active Rules in Active Rule Set";
	
	private static String RULE_NAME = "";
	private static String RULE_PARAM = "";
	private static String EVENT_NAME = "";
	private static String EVENT_CONDITION = "";
	private static String EVENT_PARAMETERS = "";
	private static String CONSEQUENT_NAME = "";
	private static String CONSEQUENT_PARAM = "";
	
	public static RuleSystem ruleSystem;
	public static ActiveRuleSet activeRuleSet;
	
	public static ArrayList<String[]> tests = null;
	

	private JFrame mainFrame;
	private JLabel ruleNameLabel;
	private JLabel eventLabel;
    private JLabel ruleParameterLabel;
    private JPanel controlPanel;
    private JPanel controlPanel4;
    private JPanel fieldPanel;
    private JPanel fieldPanel2;
    private JPanel fieldPanel1;
    private JPanel fieldPanel3;
    private JPanel eventPanel;
    private JPanel eventPane2;
    private JPanel eventPane3;
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
    private JComponent panel0;
    private JComponent panel1;
    private JComponent panel2;
    private JComponent panel3;
    private JComponent panel4;
    private JComponent panel5;
    private JTabbedPane TabbedPane;
    private JLabel ruleSystemGUIHeader; 
    private JLabel activeRuleSetGUIHeader;
    private ButtonGroup testGroup;
    public static JTextArea eventLog;
    public static JTextArea eventLog2;
    public static JTextArea log;
    public static Interface Interface;
    
    public static String successSound = "Sounds/sound95.wav";
    public static String failureSound = "Sounds/sound100.wav";
    
    
    
    public Interface(){
       prepareGUI();
    }
    
    public static void main(String[] args){
    	
       Interface = new Interface();
       
       ruleSystem = new RuleSystem();
       
       activeRuleSet = new ActiveRuleSet(); 
    }
       
    private void prepareGUI(){
   	      mainFrame = new JFrame("RuleR Tool - Basic Implementation");
 	      mainFrame.setSize(1000,800);
 	      mainFrame.add(new Tabbed(), BorderLayout.CENTER);
 	      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 	      mainFrame.setVisible(true); 
 	   }
    
    private void ResetActiveRules() {
    	//delete activations
    	activeRuleSet = new ActiveRuleSet();
    	
    	// add Rules
    	activeRuleSet = ruleSystem.activateRules(activeRuleSet);
    	
    	activeRuleGUI();
    }
    
    private void ResetRuleSystem() {
    	//delete Rule System
    	ruleSystem = new RuleSystem();
    	updateRuleSystemGUI();
    	
    	ResetActiveRules();
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
	  		  
	  	  	  String ruleModifier = modifierGroup.getSelection().getActionCommand();	
	  		  RuleNameArea.setBackground(Color.WHITE);
	  		  
	      	  ArrayList<RuleBinding> ruleBindigsArrayList = new ArrayList<RuleBinding>();
	  		  
	      	  for (Component c : inside.getComponents()) {
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
		
	public void error(String errorMessage) {
		JOptionPane.showMessageDialog(mainFrame,
			    errorMessage + " Please fix the problem",
			    "Inane error",
			    JOptionPane.ERROR_MESSAGE);
		System.out.println("Err: " + errorMessage);
	}

	private void updateRuleSystemGUI(Integer ruleNameID){
		
		System.out.println(ruleSystem.getOneRule(ruleNameID));
		ruleSystemInside.add(new RuleString(ruleSystem.getOneRule(ruleNameID)));
		ruleSystemInside.revalidate();
		ruleSystemInside.repaint();
		
		ruleSystemGUIHeader.setText(THEREARE + ruleSystem.getNumberOfRules() + RULESINRULESYSTEM); 
	}
	
	public void updateRuleSystemGUI(){
		
		ruleSystemInside.removeAll();
		ruleSystemInside.revalidate();
		ruleSystemInside.repaint();
		System.out.println("Rule System:");
		for(String rule : ruleSystem.getRules()) {
			System.out.println("  " + rule);
			ruleSystemInside.add(new RuleString(rule));
			ruleSystemInside.revalidate();
			ruleSystemInside.repaint();
		}
		ruleSystemGUIHeader.setText(THEREARE + ruleSystem.getNumberOfRules() + RULESINRULESYSTEM);
		System.out.println("");
	}
	
	public void activeRuleGUI(){
		
		activeRuleSetInside.removeAll();
		activeRuleSetInside.revalidate();
		activeRuleSetInside.repaint();
		
		String[] activeRuleSetArray = activeRuleSet.getActivations();
		
		for(String activation : activeRuleSetArray) {
			activeRuleSetInside.add(new RuleString(activation));
			activeRuleSetInside.revalidate();
			activeRuleSetInside.repaint();
		}
		
		activeRuleSetGUIHeader.setText(THEREARE + activeRuleSet.getNumberOfActivations() + RULESINACTIVERULESET);
	}

	private String time() {
		finishTime = System.currentTimeMillis();
		
		long time = finishTime - startTime;
		
		startTime = System.currentTimeMillis();
		
		return (time)+"ms";
	}
	
	public static void log(String message){
		if(logOn) {
			log.append(message);
		}
	}
	
	public void logNonStatic(String message){
		log.append(message);
	}
	
	public static ArrayList<String[]> readLine(File fileName) {
		ArrayList<String[]> lines = new ArrayList<String[]>();
		FileReader file = null;
		BufferedReader input = null;
		
		try {
			file = new FileReader(fileName);
			
			input = new BufferedReader(file);
			
			String line;
			while((line = input.readLine()) != null) {
				String[] array = line.split("\\.");
				lines.add(array);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		return lines;
	}

	public static ArrayList<String> readFile(File fileName) {
		
		ArrayList<String> lines = new ArrayList<String>();
		FileReader file = null;
		BufferedReader input = null;
		
		try {
			file = new FileReader(fileName);
			
			input = new BufferedReader(file);
			
			String line;
			while((line = input.readLine()) != null) {
				lines.add(line);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return lines;
	}
	
	protected static void end() {
		System.exit(0);	
	}
	
	
	class EventForm extends JPanel {
	   
	   
		private static final long serialVersionUID = 1L;
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
		
		
		private static final long serialVersionUID = 1L;
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
			
			if(!consequentNameString.equals(""))
				return new ConsequentRule(consequentNameString, eventConsequentParameters);
			else return null;
		}
	}

	class Tabbed extends JPanel {
	    
		
		private static final long serialVersionUID = 1L;

		public Tabbed() {
	        super(new GridLayout(1, 1));
	        
	        TabbedPane = new JTabbedPane();
	        ImageIcon icon = null;
	        
	        panel0 = new Reset();
	        TabbedPane.addTab("Reset Tool", icon, panel0,
	        		"Reset Tool");
	        TabbedPane.setMnemonicAt(0, KeyEvent.VK_0);
	        
	        panel1 = new AddRules();
	        TabbedPane.addTab("Add Rules", icon, panel1,
	                "Add Rules");
	        TabbedPane.setMnemonicAt(1, KeyEvent.VK_1);
	        
	        panel2 = new RuleSystemGUI();
	        TabbedPane.addTab("View Rules", icon, panel2,
	                "Does twice as much nothing");
	        TabbedPane.setMnemonicAt(2, KeyEvent.VK_2);
	        
	        panel3 = new ActiveRuleSetGUI();
	        TabbedPane.addTab("Active Rule Set", icon, panel3,
	                "Still does nothing");
	        TabbedPane.setMnemonicAt(3, KeyEvent.VK_3);
	        
	        panel4 = new AddEventGUI();
	        panel4.setPreferredSize(new Dimension(410, 50));
	        TabbedPane.addTab("Add Event", icon, panel4,
	                "Does nothing at all");
	        TabbedPane.setMnemonicAt(4, KeyEvent.VK_4);
	        
	        panel5 = new LogGUI();
	        panel5.setPreferredSize(new Dimension(410, 50));
	        TabbedPane.addTab("Log", icon, panel5,
	                "Log");
	        TabbedPane.setMnemonicAt(5, KeyEvent.VK_5);
	        
	        //Add the Tabbed pane to this panel.
	        add(TabbedPane);
	        
	        TabbedPane.setSelectedIndex(1);
	        
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
	}
	
	class Reset extends JPanel {
		
		private static final long serialVersionUID = 1L;

		public Reset() {
			super(new FlowLayout());
	        			
			JButton resetRS = new JButton("Reset Rule System");
			resetRS.setActionCommand("resetRS");
			resetRS.addActionListener(new ActionListener() {
 	          public void actionPerformed(ActionEvent e) {
 	        	 ResetRuleSystem();
 	          }
			});
			
			JButton resetARS = new JButton("Reset Active Rule System");
			resetARS.setActionCommand("resetARS");
			resetARS.addActionListener(new ActionListener() {
 	          public void actionPerformed(ActionEvent e) {
 	        	  ResetActiveRules();
 	          }
			});
			
			JButton resetAll = new JButton("Reset All");
			resetAll.setActionCommand("resetAll");
			resetAll.addActionListener(new ActionListener() {
 	          public void actionPerformed(ActionEvent e) {
 	        	  ResetActiveRules();
 	        	  ResetRuleSystem();
 	          }
			});
	 	      
	 	      	      
			add(resetAll);
			add(resetRS);
	 	    add(resetARS);
			
		}
	}

	class AddRules extends JPanel {
		
		private static final long serialVersionUID = 1L;

		public AddRules() {
			super(new FlowLayout());
	        
			add(new RuleForm());
			add(new RuleButtons());
		}
	}
	
	class RuleForm extends JPanel {
		
		private static final long serialVersionUID = 1L;

		public RuleForm() {
			super(new FlowLayout());
			
			ruleNameLabel = new JLabel("Rule Name",JLabel.LEFT);
	 	    ruleParameterLabel = new JLabel("Rule Parameter",JLabel.LEFT);
	 	      
	 	    RuleNameArea = new JTextArea(RULE_NAME,2,10);
	 	    RuleParameterArea = new JTextArea(RULE_PARAM,2,10);
	 	    
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
		      JRadioButton stepButton = new JRadioButton("State");
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
	 	      scrollFrame = new JScrollPane(inside);
	 	      scrollFrame.setPreferredSize(new Dimension(1000,600));
	 	      inside.setAutoscrolls(true);
	 	      	     
	 	      inside.add(controlPanel);
	 	      
	 	      add(scrollFrame);
			
		}
		
	}

	class RuleButtons extends JPanel {
		
		private static final long serialVersionUID = 1L;

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
	 	      
	 	     JButton activateRules = new JButton("Activate Rules");
	 	     activateRules.setActionCommand("activate");
	 	     activateRules.addActionListener(new ActionListener() {
	 	          public void actionPerformed(ActionEvent e) {
	 	        	 activeRuleSet = ruleSystem.activateRules(activeRuleSet);
	 	        	activeRuleGUI();
	 	        	
	 	          }
	 	        }); 
	 	    JButton saveRules = new JButton("Save Rules");
	 	    saveRules.setActionCommand("saveRule");
	 	    saveRules.addActionListener(new ActionListener() {
	 	          public void actionPerformed(ActionEvent e) {
	 	          }
	 	        }); 
	 	      
	 	      controlPanel4.add(new FileChooser("rule"));
	 	      controlPanel4.add(new JLabel(" OR ",JLabel.LEFT));
	 	      controlPanel4.add(addButton);
	 	      controlPanel4.add(activateRules);
	 	      controlPanel4.add(saveRules);
	 	      
	 	      controlPanel4.setPreferredSize(new Dimension(1000, 100));
	 	      
	 	      add(controlPanel4);
		}
		
	}

	class RuleSystemGUI extends JPanel {
		
		private static final long serialVersionUID = 1L;

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
		
		private static final long serialVersionUID = 1L;

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
		
		private static final long serialVersionUID = 1L;
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
		
		
		private static final long serialVersionUID = 1L;

		public AddEventGUI() {
			super(new GridLayout(0,1));
			
			eventLabel = new JLabel("Event",JLabel.LEFT);
			eventArea = new JTextArea("",2,10);
			
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
 	        	
 	        	 if(Update.update(new Event(event)))
 	        		Interface.success("True");
 	        	 else
 	        		Interface.error("False");
 	        	 
 	        	 Interface.activeRuleGUI();
 	          }
 	        });  
	 	      
 	        eventPanel.add(addButton);
 	        
			eventPane2 = new JPanel();
			eventPane2.setLayout(new FlowLayout());
			
			JRadioButton oneTest = new JRadioButton("CSV Tests");
			oneTest.setMnemonic(KeyEvent.VK_P);
			oneTest.setActionCommand("CSV");
	 	    JRadioButton multipleTests = new JRadioButton("Multiple Tests");
	 	    multipleTests.setMnemonic(KeyEvent.VK_P);
	 	    multipleTests.setActionCommand("multipleTests");
			
	 	    testGroup = new ButtonGroup();
	 	    testGroup.add(oneTest);
	 	    testGroup.add(multipleTests);
	 	      
	 	    JPanel panel = new JPanel();
	 	    panel.setLayout(new GridLayout(0,1));
	 	    panel.add(oneTest);
	 	    panel.add(multipleTests);
	 	    
			eventPane2.add(panel);
			eventPane2.add(new FileChooser("event"));
			
			JButton runFile = new JButton("Run From File");
			runFile.setActionCommand("runFile");
			runFile.addActionListener(new ActionListener() {
 	          public void actionPerformed(ActionEvent e) {
 	        	 if(tests != null && tests.size() > 0){
 	        		 
 	        		TabbedPane.setSelectedIndex(5);
 	 	        	
 	 	        	TestRun();
 	 	        	
 	        	 }
 	          }
 	        });  
 	       eventPane2.add(runFile);
 	       
 	      eventLog = new JTextArea(10,85);
 	     eventLog.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(eventLog);
		eventPane2.add(logScrollPane, BorderLayout.CENTER);
 	    
		
		eventPane3 = new JPanel();
		eventPane3.setLayout(new FlowLayout());
		eventPane3.add(new FileChooser("folder"));
		
		JButton runFolder = new JButton("Run All From Folder");
			runFolder.setActionCommand("folder");
			runFolder.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
	        	  
	        	  TabbedPane.setSelectedIndex(4);
	        	  
	        	  File folder = FOLDER;
	        	  File[] listOfFiles = folder.listFiles();

	        	  TestResultTable = new TreeMap<Integer, String>();
	        	  
	        	  for (File file : listOfFiles) {
	        	      if (file.isFile()) {
	        	    	  TEST_OPTION = "CSV";
	        	          System.out.println(file.getName());
	        	          Interface.logNonStatic("\n-----------------------\n");
	        	          Interface.logNonStatic("File: " + file.getName());
	        	          Interface.logNonStatic("\n-----------------------\n");
	        	          tests = readLine(file);
	        	          TestRun();
	        	      }
	        	  }
	        	  printTestResultTable();
	        	  playSuccess();
	          }
	        }); 
			eventPane3.add(runFolder);
			
			eventLog2 = new JTextArea(5,60);
	 	     eventLog2.setEditable(false);
			JScrollPane logScrollPane2 = new JScrollPane(eventLog2);
			eventPane3.add(logScrollPane2, BorderLayout.CENTER);
			
		
 	       
			
			add(eventPanel);
			add(eventPane2);
			add(eventPane3);
		}
	}
	
	public static void playSound(String sound) 
	  throws Exception
	  {
	    // open the sound file as a Java input stream
	    InputStream in = new FileInputStream(sound);

	    // create an audiostream from the inputstream
	    AudioStream audioStream = new AudioStream(in);

	    // play the audio clip with the audioplayer class
	    AudioPlayer.player.start(audioStream);
	  }
	
	public static void playSuccess() {
		try {
			playSound(successSound);
	   } catch (Exception e) {
			e.printStackTrace();
	   }
	}
	
	public void TestRun() {
		String eventLogs = "";
      	int eventCount = 0;
      	
 		boolean result = false;

 		Interface.ResetActiveRules();
 		
 		long startTime;
 		long endTime;
 		int eventsCount = 0;
 		
 		long[] testResults = new long[numberOfTestRuns];
 		
	 	for(int no = 0; no<numberOfTestRuns;no++){
	 		
	 		/** multipleTests or CSV */
	 		
			System.out.println("--------------------------== New Test ==----------------------------------");
		
			Interface.ResetActiveRules();
			eventLogs = "";
			eventsCount = 0;
			startTime = System.nanoTime();
	 			
	 		tests : for(String[] events : tests){
	 			
 				eventCount++;
 				
 				if(TEST_OPTION.equals("multipleTests")) {
 					startTime = System.nanoTime();
 	 				eventLogs = "";
 				}
	 				
	     		for(String event : events) {
	     			//eventLogs += event + ".";
	     			eventsCount++;
	     			/*if(eventsCount % 100000  == 0) {
	     				Interface.logNonStatic("Event Count" + eventsCount);
	     				System.out.println("Event Count" + eventsCount);
	     			}*/
	     			
					if(Update.update(new Event(event,TEST_OPTION))) {
						result = true;
					}
					else {
						result = false;
						System.out.println("eventCount: " + eventCount);
						System.out.println("event: " + event);
						if(TEST_OPTION.equals("CSV"))
							break tests;
						else
							break;
					} // else
	     		} // for
	     		
	     		if(TEST_OPTION.equals("multipleTests")) {
		     		endTime = System.nanoTime();
		     		printTestResults(result,eventsCount,startTime,endTime,testResults,no,eventLogs);
		     		Interface.ResetActiveRules();
	     		}
	 		}
	 		if (TEST_OPTION.equals("CSV")) {
	 			endTime = System.nanoTime();
	 			printTestResults(result,eventsCount,startTime,endTime,testResults,no,eventLogs);
			}
	 	}
	}
	
	private void printTestResults(boolean result, int eventsCount, long startTime, long endTime,
			long[] testResults, int no, String eventLogs) {
     		
	     			
		Interface.activeRuleGUI();
			
		Interface.logNonStatic("\n***********\n");
		Interface.logNonStatic("*************\n");
		
		if(result) {
			for(RuleActivation ruleAct : activeRuleSet.getArrayOfRuleActivations()){
				if(ruleAct.getRule().getExtraModifier() == Rule.ExtraModifier.Forbidden) {
					result = false;
					Interface.logNonStatic("**  Active Rule With Forbidden State : " + ruleAct.getRule().getRuleName() +"\n");
					break;
				}
			}
		}
		
		Interface.logNonStatic("**  Status : " + result+"\n");
		Interface.logNonStatic("**  Event Executed : " + eventsCount+"\n");
		long finaltime = ((endTime - startTime) / 1000000);
		Interface.logNonStatic("**  Total execution time: " + finaltime + "ms\n" );
		Interface.logNonStatic("***********\n");
		Interface.logNonStatic("***********\n");
		
		
		if (TEST_OPTION.equals("CSV")) {
			testResults[no] = finaltime;
			
			if(numberOfTestRuns == no+1) {
		 	
			 	String text = "";
			 	long average = 0;
			 	for(long num : testResults) {
			 		text += num + ", ";
			 		average += num;
			 	}
			 	
			 	TestResultTable.put(eventsCount,(float)(average/numberOfTestRuns) +","+ text);
			}
		}
 		if(no+1 >= numberOfTestRuns) {
 			tests = null;
 			eventLog.setText("No Events Left");
 		}
 		
	}
	
	public void printTestResultTable() {
		Interface.logNonStatic("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
		for(Integer key : TestResultTable.keySet()){
			Interface.logNonStatic(key + " | " + TestResultTable.get(key) + "\n");
		}
		Interface.logNonStatic("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
	}

	@SuppressWarnings("serial")
	class LogGUI extends JPanel {
		public LogGUI() {
			super(new GridLayout(0,1));
			
			log = new JTextArea(0,1);
	 	    log.setEditable(false);
			
	 	    JScrollPane scroll = new JScrollPane(log);
	 	    
	 	    add(scroll);
		}
 	    
	}

	public boolean setEventsTestGroup() {
		if(testGroup.getSelection() != null) {
			TEST_OPTION = testGroup.getSelection().getActionCommand();
			return true;
		} else return false;
	}
}
