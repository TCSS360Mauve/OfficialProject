package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.User;

/**
 * A class to set up the Project Screen.
 * 
 * @author Hassan Abbas
 * @version 0.1
 */
public class ProjectScreen extends JPanel {
    
    /**
     * Constructor to create the Project screen.
     *
     * @param user The user of the app
     * @param cardPanel The panels to swap to/from
     * @param cardLayout The layout used to swap to/from panels
     */
    public ProjectScreen(User user, JPanel cardPanel, CardLayout cardLayout) {
        setLayout(new BorderLayout());
        
        //Setting up panels
        JPanel projectsPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel folderPanel = new JPanel();
        JPanel projectBtnPanel = new JPanel();
        JPanel addDocumentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel projectSelectionPanel = new JPanel();
        
        //Setting color for panels
        projectsPanel.setBackground(Color.red);
        topPanel.setBackground(Color.darkGray);
        folderPanel.setBackground(Color.yellow);
        projectBtnPanel.setBackground(Color.white);
        
        //Setting size for panels
        projectsPanel.setPreferredSize(new Dimension(100,100));
        topPanel.setPreferredSize(new Dimension(100,30));
        folderPanel.setPreferredSize(new Dimension(150,100));
        projectBtnPanel.setPreferredSize(new Dimension(50,35));
        projectSelectionPanel.setPreferredSize(new Dimension(150,30));
        addDocumentPanel.setPreferredSize(new Dimension(125,125));
        
        //Setting up labels
        JLabel projectNameLabel = new JLabel("Project Name: (Name)");
        JLabel projectSelectionLabel = new JLabel("Project Selection");
        JLabel searchLabel = new JLabel("Search: ");
        JLabel accountNameLabel = new JLabel("(Account name)");
        
        //Setting up JButtons for tabs
        JButton documentsButton = new JButton("Documents");
        JButton budgetButton = new JButton("Budget");
        JButton membersButton = new JButton("Members");
        JButton addDocument = new JButton("Add Document...");
        
        //Search text box
        JTextField searchBar = new JTextField(16);
        //Get name of user
        String userName = user.getName();
        
        //Adding menu items
        JMenuItem exitButton = new JMenuItem("Exit");
        JMenuBar accountMenuBar = new JMenuBar();
        JMenu accountMenu = new JMenu(userName);
        
        //Changing fonts
        accountMenu.setFont(new Font("Arial", Font.PLAIN, 16));        
        projectNameLabel.setForeground(new Color(255,255,255));
        projectNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        searchLabel.setForeground(new Color(255,255,255));
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        accountNameLabel.setForeground(new Color(255,255,255));
        accountNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        //Setting size for buttons on the center panel
        documentsButton.setPreferredSize(new Dimension(206,25));
        budgetButton.setPreferredSize(new Dimension(206,25));
        membersButton.setPreferredSize(new Dimension(206, 25));
        addDocument.setPreferredSize(new Dimension(135, 125));
        
        //Adding the three buttons on the center panel
        projectBtnPanel.add(documentsButton, BorderLayout.NORTH);
        projectBtnPanel.add(budgetButton, BorderLayout.NORTH);
        projectBtnPanel.add(membersButton, BorderLayout.NORTH);
        projectsPanel.add(projectBtnPanel, BorderLayout.NORTH);
        
        //Making the top panel (Where the search function goes)
        topPanel.add(projectNameLabel);
        topPanel.add(Box.createHorizontalStrut(100));
        topPanel.add(searchLabel);
        topPanel.add(searchBar);
        topPanel.add(Box.createHorizontalStrut(100));
        topPanel.add(accountMenuBar);
        
        //Adding button to menu
        accountMenu.add(exitButton);
        accountMenuBar.add(accountMenu);
        
        //Add label to project selection panel (White area behind "Project Selection" label)
        //Then add label to the panel
        projectSelectionPanel.add(projectSelectionLabel, BorderLayout.NORTH);
        folderPanel.add(projectSelectionPanel, BorderLayout.NORTH);
        //Setting up "Add document" button on panel
        addDocumentPanel.add(addDocument);
        projectsPanel.add(addDocumentPanel);
        
        //Adding action listeners to the exit, documents, budget, and members buttons.
        //Adding members screen to cards
        //  cardPanel.add(new MembersScreen(), "MembersScreen");
        // membersButton.addActionListener((new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         cardLayout.show(cardPanel, "MembersScreen");
        //     }
        // }));
        //Adding documents screen to cards
        cardPanel.add(new DocumentsScreen(), "DocumentsScreen");
        documentsButton.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "DocumentsScreen");
            }
        }));
        //Adding budget screen to cards
        cardPanel.add(new BudgetScreen(), "BudgetScreen");
        budgetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "BudgetScreen");
            }
        });
        //Adding exit button to go back to home screen
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "HomeScreen");
            }
        });


        //Adding other panels on the main panel
        add(topPanel, BorderLayout.NORTH);
        //add(folderPanel, BorderLayout.WEST);
        //add(new ProjectSelector(), BorderLayout.WEST);
        //add(projectsPanel, BorderLayout.CENTER);
       add(new TabsPanels(), BorderLayout.CENTER);
    }
}
