import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class Interface {
	
	public static Rule_System rules;
	

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
	   private JPanel controlPanel3;
	   private JPanel controlPanel4;
	   private JPanel fieldPanel;
	   private JPanel fieldPanel2;
	   private JPanel fieldPanel3;
	   private JPanel eventPanel;
	   private JPanel eventPanel2;
	   private JPanel eventPanel3;
	   private JPanel eventPanel4;
       private JLabel eventName;
       private JLabel eventCondition;
	   private JLabel eventParameter;
	   private JLabel eventConsequent;
	   private JLabel ObjectLabel;
	   
	   

	   public Interface(){
	      prepareGUI();
	   }

	   public static void main(String[] args){
	      Interface Interface = new Interface();
	      
	      rules = new Rule_System();
	      
	      //Interface.showEventDemo();       
	   }
	      
	   private void prepareGUI(){
	      mainFrame = new JFrame("Java SWING Examples");
	      mainFrame.setSize(1000,800);
	      mainFrame.setLayout(new GridLayout(6, 2));

	      headerLabel = new JLabel("",JLabel.CENTER );
	      statusLabel = new JLabel("",JLabel.CENTER); 
	      
	      ruleModifierLabel = new JLabel("Rule Modifier",JLabel.LEFT);
	      ruleNameLabel = new JLabel("Rule Name",JLabel.LEFT);
	      ruleParameterLabel = new JLabel("Rule Parameter",JLabel.LEFT);
	      
	      final JTextArea RuleNameArea = new JTextArea("",2,10);
	      final JTextArea RuleModifierArea = new JTextArea("",2,10);
	      final JTextArea RuleParameterArea = new JTextArea("",2,10);
	      
	      
	      eventName = new JLabel("Event Name",JLabel.LEFT);
	      eventCondition = new JLabel("Event Condition",JLabel.LEFT);
	      eventParameter = new JLabel("Event Parameter",JLabel.LEFT);
	      eventConsequent = new JLabel("Event Consequent",JLabel.LEFT);
	      
	      final JTextArea eventNameArea = new JTextArea("",2,10);
	      final JTextArea eventConditionArea = new JTextArea("",2,10);
	      final JTextArea eventParameterArea = new JTextArea("",2,10);
	      final JTextArea eventConsequentArea = new JTextArea("",2,10);
	      

	      //statusLabel.setSize(350,100);
	      mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
		        System.exit(0);
	         }        
	      });    
	      controlPanel = new JPanel();
	      controlPanel.setLayout(new FlowLayout());
	      
	      fieldPanel = new JPanel();
	      fieldPanel.setLayout(new GridLayout());
	      fieldPanel.add(ruleModifierLabel);
	      fieldPanel.add(RuleModifierArea);
	      
	      
	      fieldPanel2 = new JPanel();
	      fieldPanel2.setLayout(new GridLayout());
	      fieldPanel2.add(ruleNameLabel);
	      fieldPanel2.add(RuleNameArea);
	      
	      
	      fieldPanel3 = new JPanel();
	      fieldPanel3.setLayout(new GridLayout());
	      fieldPanel3.add(ruleParameterLabel);
	      fieldPanel3.add(RuleParameterArea);
	      	      
	      controlPanel.add(fieldPanel);
	      controlPanel.add(fieldPanel2);
	      controlPanel.add(fieldPanel3);
	      
	      
	      controlPanel2 = new JPanel();
	      controlPanel2.setLayout(new FlowLayout());
	      //controlPanel2.setBackground(Color.RED);
	      
	      eventPanel = new JPanel();
	      eventPanel.setLayout(new GridLayout());
	      eventPanel.add(eventName);
	      eventPanel.add(eventNameArea);
	      
	      eventPanel2 = new JPanel();
	      eventPanel2.setLayout(new GridLayout());
	      eventPanel2.add(eventCondition);
	      eventPanel2.add(eventConditionArea);
	      
	      eventPanel3 = new JPanel();
	      eventPanel3.setLayout(new GridLayout());
	      eventPanel3.add(eventParameter);
	      eventPanel3.add(eventParameterArea);
	      
	      eventPanel4 = new JPanel();
	      eventPanel4.setLayout(new GridLayout());
	      eventPanel4.add(eventConsequent);
	      eventPanel4.add(eventConsequentArea);
	      
	      controlPanel2.add(eventPanel);
	      controlPanel2.add(eventPanel2);
	      controlPanel2.add(eventPanel3);
	      controlPanel2.add(eventPanel4);
	      
	      controlPanel3 = new JPanel();
	      controlPanel3.setLayout(new FlowLayout());
	      
	      JButton addButton = new JButton("Add");
	      addButton.setActionCommand("Add");
	      addButton.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
	        	  
	        	  String ruleName = RuleNameArea.getText();
	        	  int ruleModifier = Rule.modifierToInt(RuleModifierArea.getText());
	        	  
	        	  if(!ruleName.equals("") && ruleModifier != -1) {
	        		  
	        		  RuleNameArea.setBackground(Color.WHITE);
	        		  RuleModifierArea.setBackground(Color.WHITE);
	        		  
		        	  Rule myRule = new Rule(1, ruleName, ruleModifier);
		 
		        	  rules.addNewRule(myRule);
		        	  
		              statusLabel.setText( "Added Rule "+ruleName);
		              
		              showRules();
	        	  }
	        	  else {
	        		  if(ruleName.equals("")){
	        			  RuleNameArea.setBackground(Color.RED);
	        			  headerLabel.setText("Rule Name is empty");
	        		  }else {RuleNameArea.setBackground(Color.WHITE);}
	        		  
	        		  if (ruleModifier == -1) {
	        			RuleModifierArea.setBackground(Color.RED);
	        			headerLabel.setText(headerLabel.getText() +" Rule Modifier is empty");
	        		  } else {RuleModifierArea.setBackground(Color.WHITE);}
	        	  }
	           }
	        });  
	      
	      controlPanel3.add(addButton);
	      
	      ObjectLabel = new JLabel("",JLabel.LEFT);
	      
	      mainFrame.add(headerLabel);
	      mainFrame.add(controlPanel);
	      mainFrame.add(controlPanel2);
	      mainFrame.add(controlPanel3);
	      mainFrame.add(statusLabel);
	      mainFrame.add(ObjectLabel);
	      mainFrame.setVisible(true);  
	   }
	   
	   private void showRules(){
	      headerLabel.setText("Rules Added"); 
	      
	      ObjectLabel.setText(rules.getRules());
	      
	      mainFrame.add(ObjectLabel);
	      mainFrame.setVisible(true);	      
	   }

	   /*private class ButtonClickListener implements ActionListener{
	      public void actionPerformed(ActionEvent e) {
	         String command = e.getActionCommand();  
	         if( command.equals( "Add" ))  {
	            statusLabel.setText("Added Rule "+ RuleNameArea.getText());
	         }  	
	      }		
	   }*/
	
}
