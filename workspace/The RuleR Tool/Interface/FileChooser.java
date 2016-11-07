import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class FileChooser extends JPanel
						implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	static private final String newline = "\n";
	JButton openButton, saveButton;
	JFileChooser fc;
	
	private final String type;
	
	public FileChooser(String file) {

		super(new BorderLayout());
		
		this.type = file;
				
		//Create a file chooser
		fc = new JFileChooser();
		
		openButton = new JButton("Import a File...");
		openButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel(); //use FlowLayout
		buttonPanel.add(openButton);
		
		add(buttonPanel, BorderLayout.PAGE_START);
	}
	
	public void actionPerformed(ActionEvent e) {
		
		//Handle open button action.
		if (e.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(FileChooser.this);
		
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				//log.append("Opening: " + file.getName() + "." + newline);
				
				switch (this.type) {
				case "event":
					Interface.EVENTS_FILE = file;
					Interface.events = Interface.readFile(file);
					
					Interface.eventLog.setText("");
					
					for(String event : Interface.events){
						Interface.eventLog.append(event + ", ");
					}
					Interface.eventLog.append(newline);
					break;
				case "rule":
					Interface.RULE_FILE = file;
					
					ArrayList<String> rules = Interface.readFile(file);
				     
			       for(String rule : rules) {
			    	   Interface.ruleSystem.addPredifinedRules(rule);
			       }
			       
			       //log.append("Rules Added" + newline);
			       
			       Interface.ruleSystem.activateRules(Interface.activeRuleSet);
			       
			       Interface.Interface.updateRuleSystemGUI();
			       Interface.Interface.activeRuleGUI();
					break;
				default:
					break;
				}
			} else {
				//log.append("Open command cancelled by user." + newline);
			}
		//log.setCaretPosition(log.getDocument().getLength());
		}
	}
}