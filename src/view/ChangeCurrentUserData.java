package view;

import controller.AppInfoController;
import controller.UserController;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;

/**
 * Popup for changing the user's email and/or username
 * @author Bairu Li
 * @version 0.3
 */
public class ChangeCurrentUserData extends JPanel {

	/** Constant for distinguishing changing username.*/
	public static final String USERNAME = "Username";
	
	/** Constant for distinguishing changing email.*/
	public static final String EMAIL = "Email";
	
	/**
	 * Constructs a popup for changing the email/username.
	 * 
	 * @author Bairu Li
	 * @param theMenu the menu bar 
	 * @param thePopupType the type of changed specified
	 */
	public ChangeCurrentUserData(final JMenu theMenu, final String thePopupType) {
		// panel
		final JPanel panel = new JPanel(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();
		final JDialog popup = new JDialog();
		
		// buttons and stuff
		final JLabel setNewDataLabel = new JLabel("Enter your new " + thePopupType);
		final JTextField newDataText = new JTextField(10);
		final JButton confirmButton = new JButton("Save");
		final JButton cancelButton = new JButton("Cancel");
		popup.setContentPane(panel);

		// making the gui look good
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(20,10,0,10);  
		c.weightx = 0.0;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(setNewDataLabel, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 1;
		panel.add(newDataText, c);
		
		c.insets = new Insets(0,0,10,0);
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridwidth = 1;
		c.weightx = 0.5;
		c.weighty = 1.0;
		c.gridy = 2;
		panel.add(cancelButton, c);
		
		
		c.gridx = 1;
		c.gridy = 2;
		panel.add(confirmButton, c);
		
		// action listeners
		cancelButton.addActionListener(theE -> {
			// closes the popup
			popup.dispatchEvent(new WindowEvent(popup, WindowEvent.WINDOW_CLOSING));
		});
		
		confirmButton.addActionListener(theE -> {
			String newData = newDataText.getText();

            // Show Error message if the user input isEmpty()
            if (newData.isEmpty()){
                JOptionPane.showMessageDialog(ChangeCurrentUserData.this, "Empty Text", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            
			if (thePopupType.equals(USERNAME)) {
				// changes the username 
				UserController.changeUserName(AppInfoController.getCurrentUser(), newData);
				AppInfoController.changeUserName(newData);
				// changes the name in the menu bar
				theMenu.setText(newData);
			} else {
				// Show error if user input not a valid email format
				if (!CreateProfile.emailValidation(newData)){
	                JOptionPane.showMessageDialog(ChangeCurrentUserData.this, "Invalid Email", "Error", JOptionPane.ERROR_MESSAGE);
	                return;
	            }
				UserController.changeUserEmail(AppInfoController.getCurrentUser(), newData);
				AppInfoController.changeUserEmail(newData);
			}
			// closes popup when done
			popup.dispatchEvent(new WindowEvent(popup, WindowEvent.WINDOW_CLOSING));
		});
        
        
        // Set popup properties
		popup.setTitle("Change " + thePopupType);
		popup.setSize(300, 250);
        popup.setLocationRelativeTo(null);
        popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        popup.setVisible(true);
	}
}
