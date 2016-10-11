import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class Interface {
	
	public static RuleSystem rules;
	

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
	   private JPanel fieldPanel3;
	   /*private JPanel eventPanel;
	   private JPanel eventPanel2;
	   private JPanel eventPanel3;
	   private JPanel eventPanel4;
	   private JPanel eventPanel5;
       private JLabel eventName;
       private JLabel eventCondition;
	   private JLabel eventParameter;
	   private JLabel eventConsequent;
	   private JLabel eventConsequentParameter;*/
	   private JLabel ObjectLabel;
	   private ButtonGroup modifierGroup;
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
	      
	      rules = new RuleSystem();
	      
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
	      
	      RuleNameArea = new JTextArea("",2,10);
	      RuleParameterArea = new JTextArea("",2,10);
	
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
	      final ButtonGroup modifierGroup = new ButtonGroup();
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
		
		for (Component c : inside.getComponents()) {
	    	  System.out.println(">" + c);
	    	  if (c instanceof eventPanele) {
		    	  for (Component a : ((Container) c).getComponents()) {
		    		   	((eventPanele) a).ruleBinding();
		    	  }
	    	  }
		  }
		
		end();
		
		
		
		String ruleName = RuleNameArea.getText();
  	  
		boolean selectedModifier = modifierGroup.getSelection() != null;
  	  
  	  	        	  
  	  	if(!ruleName.equals("") && selectedModifier) {
  		  headerLabel.setBackground(Color.WHITE);
  		  
  		  String ruleModifier = modifierGroup.getSelection().getActionCommand();
  		  String ruleParameters = RuleParameterArea.getText();
  		  
  		  RuleNameArea.setBackground(Color.WHITE);
  		  
      	  Rule myRule = new Rule(1, ruleName, ruleModifier, ruleParameters);
      	  
      	  for (Component c : inside.getComponents()) {
	    	  System.out.println(">" + c);
	    	  if (c instanceof eventPanele) {
		    	  for (Component a : ((Container) c).getComponents()) {
		    		   	((eventPanele) a).ruleBinding();
		    	  }
	    	  }
		  }

      	  rules.addNewRule(myRule);
      	  
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

	private void emptyRuleModifier() {
	   addError(" Rule Modifier is not selected");
	}

	private void showRules(){
	      headerLabel.setText("Rule Added"); 
	      //ObjectLabel.setText(rules.getRules());      
	   }

	   /*private class ButtonClickListener implements ActionListener{
	      public void actionPerformed(ActionEvent e) {
	         String command = e.getActionCommand();  
	         if( command.equals( "Add" ))  {
	            statusLabel.setText("Added Rule "+ RuleNameArea.getText());
	         }  	
	      }		
	   }*/
	protected void end() {
		System.exit(0);	
	}
	
	
	class eventPanele extends JPanel {
	   
	   private JPanel eventPanel;
	   private JPanel eventPanel2;
	   private JPanel eventPanel3;
	   private JPanel eventPanel4;
	   private JPanel eventPanel5;
       private JLabel eventName;
       private JLabel eventCondition;
	   private JLabel eventParameter;
	   private JLabel eventConsequent;
	   private JLabel eventConsequentParameter;
	   private JTextArea eventNameArea;
	   private JTextArea eventConditionArea;
	   private JTextArea eventParameterArea;
	   private JTextArea eventConsequentArea;
	   private JTextArea eventConsequentParameterArea;
		
		public eventPanele() {
			
			eventName = new JLabel("Event Name",JLabel.LEFT);
		    eventCondition = new JLabel("Event Condition",JLabel.LEFT);
		    eventParameter = new JLabel("Event Parameter",JLabel.LEFT);
		    eventConsequent = new JLabel("Event Consequent",JLabel.LEFT);
		    eventConsequentParameter = new JLabel("Event Consequent Parameter",JLabel.LEFT);
			
			eventNameArea = new JTextArea("",2,10);
		    eventConditionArea = new JTextArea("",2,10);
		    eventParameterArea = new JTextArea("",2,10);
		    eventConsequentArea = new JTextArea("",2,10);
		    eventConsequentParameterArea = new JTextArea("",2,10);
			
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
		     
		    eventPanel4 = new JPanel();
		    eventPanel4.setLayout(new GridLayout(0,1));
		    eventPanel4.add(eventConsequent);
		    eventPanel4.add(eventConsequentArea);
		    
		    eventPanel5 = new JPanel();
		    eventPanel5.setLayout(new GridLayout(0,1));
		    eventPanel5.add(eventConsequentParameter);
		    eventPanel5.add(eventConsequentParameterArea);
	    
		    add(eventPanel);
		    add(eventPanel2);
		    add(eventPanel3);
		    add(eventPanel4);
		    add(eventPanel5);
		}
		
		
		public RuleBinding ruleBinding() {
			return new RuleBinding();
			*//*
		}
		
	}
	
}
