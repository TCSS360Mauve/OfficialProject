package view;

import controller.AppInfoController;
import controller.UserController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import model.User;

/**
 * Creates the log-in screen for the FileNtro program.
 * @author Riley Bennett
 * @author Tin Phu
 * @author Bairu Li
 * @version 0.3
 */
public class LogInScreen extends JPanel {
    /**
     * Field for inputting a name.
     */
    private JTextField nameField;

    /**
     * Field for inputting an email.
     */
    private JTextField emailArea;

    /**
     * Creates the panel for the user to log in.
     * @author Riley Bennett
     * @author Tin Phu
     * @author Bairu Li
     * @param cardPanel The cardpanel to be used.
     * @param cardLayout The cardlayout to be used.
     */
    public LogInScreen(JPanel cardPanel, CardLayout  cardLayout){

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Set padding

        JLabel titleLabel = new JLabel("Welcome to FileNtro");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel usernameLabel = new JLabel("Username:");
        nameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Email:");
        emailArea = new JTextField(20);
        JButton loginButton = new JButton("Log In");

        // Action for logging in
        final Action attemptLogin = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                String name = nameField.getText();
                String email = emailArea.getText();

                //Show Error message if one of user inputs isEmpty()
                if(name.isEmpty() || email.isEmpty()){
                    JOptionPane.showMessageDialog(LogInScreen.this, "Please fill in all the fields.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(!emailValidation(email)){
                    JOptionPane.showMessageDialog(LogInScreen.this, "Invalid Email", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(!userNameValidation(name)){
                    JOptionPane.showMessageDialog(LogInScreen.this, "Invalid Username " +
                            "\n 6-20 characters, no special characters are allowed ", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                User theUser = UserController.findUser(name, email);

                if (theUser == null) {
                    JOptionPane.showMessageDialog(LogInScreen.this, "User does not exist.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //AboutScreen is created with new User() as argument
                //cardPanel, cardLayout are passed to AboutScreen, so we can switch back to previous JPanel.

                cardPanel.add(new HomeScreen(theUser, cardPanel, cardLayout), "HomeScreen");
                AppInfoController.setUser(theUser);
                nameField.setText("");
                emailArea.setText("");
                //Switch Trigger Here

                cardLayout.show(cardPanel, "HomeScreen");
                AppUI.toggleFileMenu(true);
            }
        };

        // logs in by pressing enter key
        nameField.addActionListener(attemptLogin);
        emailArea.addActionListener(attemptLogin);

        loginButton.addActionListener(attemptLogin);

        JLabel newUserLabel = new JLabel("New to FileNtro?");
        JButton createAcct = new JButton("Sign Up");

        // Create account button action listener
        createAcct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardPanel.add(new CreateProfile(cardPanel, cardLayout), "CreateProfileScreen");
                nameField.setText("");
                emailArea.setText("");
                cardLayout.show(cardPanel, "CreateProfileScreen");
            }
        });


        // Add components to the panel using GridBagConstraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(emailArea, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(newUserLabel, gbc);

        gbc.gridy = 5;
        add(createAcct, gbc);

    }
    /**
     * Email Validation using regex Expression
     * @author Tin Phu
     * @param emailString The email to check
     * @return boolean Whether the given email is valid
     */
    public static boolean emailValidation(String emailString){
        String regex = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailString);
        return matcher.matches();
    }
    /**
     *  Username Validation using regex Expression
     *  @author Tin Phu
     * @param theUsername
     * @return
     */
    public static boolean userNameValidation(String theUsername){
        String regex = "^(?=.{6,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(theUsername);
        return matcher.matches();
    }
}
