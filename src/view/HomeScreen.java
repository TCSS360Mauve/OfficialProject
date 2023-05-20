package view;

import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.AppInfoController;

/**
 * A class to create the Home screen of the FileNtro project.
 *
 * @author Riley Bennett
 * @version 0.1
 */
public class HomeScreen extends JPanel {

    /**
     * Constructor to create the Home screen.
     *
     * @param user The user of the app
     * @param cardPanel The panels to swap to/from
     * @param cardLayout The layout used to swap to/from panels
     */
    public HomeScreen(User user, JPanel cardPanel, CardLayout cardLayout){

        setLayout(new BorderLayout());

        // Creating labels/button
        JLabel homeLabel = new JLabel("Welcome " + user.getName() + "!");
        JButton projectButton = new JButton("Go to projects...");
        JButton aboutButton = new JButton("About...");

        // Setting up look of GUI
        homeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        homeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        add(homeLabel,BorderLayout.NORTH);
    
        // Use GridBagLayout to organize buttons in a panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 0;
        buttonPanel.add(projectButton, cons);
        
        //Add project screen to card, switch to project screen when button is pressed
        cardPanel.add(new ProjectScreen(user, cardPanel, cardLayout), "ProjectScreen");
        projectButton.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ProjectScreen");
            }
        }));
        
        // Add about screen to cards, switch to about screen when button is pressed
        cardPanel.add(new AboutScreen(user, cardPanel, cardLayout), "AboutScreen");
        cons.insets = new Insets(30, 0, 0, 0);
        cons.gridy = 1;
        aboutButton.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel,"AboutScreen");
            }
        }));
        buttonPanel.add(aboutButton, cons);
        
        // Switch back to log in screen when logout button is pressed
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "LogInScreen");
            }
        }));
        cons.gridy = 2;
        cons.insets = new Insets(60, 0, 0, 0);
        JButton importButton = new JButton("Import Data");
        importButton.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AppInfoController.importData();
                JOptionPane.showMessageDialog(buttonPanel, "JSON files imported.");
            }
        }));
        buttonPanel.add(importButton, cons);

        cons.gridy = 3;
        cons.insets = new Insets(30, 0, 0, 0);
        JButton exportButton = new JButton("Export Data");
        exportButton.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AppInfoController.importData();
                JOptionPane.showMessageDialog(buttonPanel, "Check program folder for exported JSON files.");
            }
        }));
        buttonPanel.add(exportButton, cons);

        cons.gridy = 4;
        cons.insets = new Insets(100, 0, 0, 0);
        buttonPanel.add(logoutButton, cons);

        add(buttonPanel, BorderLayout.CENTER);
     }
}
