package view;

import controller.ProjectController;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import model.User;

/**
 * A class to set up the Project Screen.
 * @author Hassan Abbas
 * @version 0.3
 */
public class ProjectScreen extends JPanel {

    /**
     * A tabbed pane for the projects
     */
    private JTabbedPane projectTab;
    /**
     * Constructor to create the Project screen.
     *
     * @param user The user of the app
     * @param cardPanel The panels to swap to/from
     * @param cardLayout The layout used to swap to/from panels
     */
    public ProjectScreen(User user, JPanel cardPanel, CardLayout cardLayout, String theProjectID) {



        setLayout(new BorderLayout());
        
        //Setting up panels
        JPanel projectsPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel topCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel folderPanel = new JPanel();
        JPanel projectBtnPanel = new JPanel();
        JPanel addDocumentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel projectSelectionPanel = new JPanel();
        
        //Setting up projects tab
        JPanel projectPanel = new JPanel(new BorderLayout());
        projectTab = new JTabbedPane();
        projectPanel.add(projectTab, BorderLayout.CENTER);
        JPanel addProjectPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addProjectPanel.setPreferredSize(new Dimension(0,0));
        
        //Setting color for panels
        projectsPanel.setBackground(Color.red);
        topPanel.setBackground(Color.darkGray);
        topLeftPanel.setBackground(Color.darkGray);
        topRightPanel.setBackground(Color.darkGray);
        topCenterPanel.setBackground(Color.darkGray);
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
        JLabel projectNameLabel = new JLabel("Project Name: " + ProjectController.findProjectByID(theProjectID).getProjectName());

        JLabel projectSelectionLabel = new JLabel("Project Selection");
        JLabel searchLabel = new JLabel("Search: ");
        
        //Setting up JButtons for tabs
        JButton documentsButton = new JButton("Documents");
        JButton budgetButton = new JButton("Budget");
        JButton membersButton = new JButton("Members");
        JButton addDocument = new JButton("Add Document...");
        JButton addProjectButton = new JButton("Add Project");
        JButton searchButton = new JButton("Search");
        searchButton.setSize(10,10);
        //Search text box
        JTextField searchBar = new JTextField(16);
        //Get name of user
        String userName = user.getName();
        
        //Adding menu items
        JMenuItem changeUsernameButton = new JMenuItem("Change Username");
        JMenuItem changeEmailButton = new JMenuItem("Change Email");
        JMenuItem homeExitButton = new JMenuItem("Back Home");
        JMenuItem exitButton = new JMenuItem("Log Out");
        JMenuBar accountMenuBar = new JMenuBar();
        JMenu accountMenu = new JMenu(userName);
        
        //Changing fonts
        accountMenu.setFont(new Font("Arial", Font.PLAIN, 16));        
        projectNameLabel.setForeground(new Color(255,255,255));
        projectNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        searchLabel.setForeground(new Color(255,255,255));
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        //Setting size for buttons on the center panel
        documentsButton.setPreferredSize(new Dimension(206,25));
        budgetButton.setPreferredSize(new Dimension(206,25));
        membersButton.setPreferredSize(new Dimension(206, 25));
        addDocument.setPreferredSize(new Dimension(135, 125));
        addProjectButton.setPreferredSize(new Dimension(70,30));
        
        projectTab.add(addProjectButton);
        projectTab.setTitleAt(0, "Add Project");
        projectTab.getTitleAt(projectTab.getSelectedIndex());
        
        //Adding the three buttons on the center panel
        projectBtnPanel.add(documentsButton, BorderLayout.NORTH);
        projectBtnPanel.add(budgetButton, BorderLayout.NORTH);
        projectBtnPanel.add(membersButton, BorderLayout.NORTH);
        
        //Making the top panel (Where the search function goes)
        topLeftPanel.add(projectNameLabel);
        topCenterPanel.add(searchLabel);

        topCenterPanel.add(searchBar);
        topCenterPanel.add(searchButton);
        topRightPanel.add(accountMenuBar);
        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(topCenterPanel, BorderLayout.CENTER);
        topPanel.add(topRightPanel, BorderLayout.EAST);
        
        //Adding button to menu
        accountMenu.add(changeUsernameButton);
        accountMenu.add(changeEmailButton);
        accountMenu.addSeparator();
        accountMenu.add(homeExitButton);
        accountMenu.add(exitButton);
        accountMenuBar.add(accountMenu);
        
        //Add label to project selection panel (White area behind "Project Selection" label) then add label to the panel
        projectSelectionPanel.add(projectSelectionLabel, BorderLayout.NORTH);
        folderPanel.add(projectSelectionPanel, BorderLayout.NORTH);
        //Setting up "Add document" button on panel
        addDocumentPanel.add(addDocument);

        // Adding exit button to go back to home screen
        homeExitButton.addActionListener(theE -> {
            cardLayout.show(cardPanel, "HomeScreen");
        });
        //Adding exit button to go back to the login screen
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "LogInScreen");
            }
        });
        // Add button to change username
        changeUsernameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	new ChangeCurrentUserData(accountMenu, ChangeCurrentUserData.USERNAME);
            }
        });
        // Add button to change user's email
        changeEmailButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	new ChangeCurrentUserData(accountMenu, ChangeCurrentUserData.EMAIL);
            }
        });
        
        addProjectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String projectName = JOptionPane.showInputDialog(ProjectScreen.this, "Enter project name: ");
                if (projectName != null && !projectName.isEmpty()) {
                    JPanel projectPanel = new JPanel();
                    projectPanel.setLayout(new BorderLayout());
                    projectPanel.add(new JScrollPane(new TabsPanels(theProjectID)), BorderLayout.CENTER);
                    projectTab.addTab(projectName, projectPanel);
                } else {
                    JOptionPane.showMessageDialog(ProjectScreen.this, "Please enter a valid project name: ");
                }
            }
        });
        
        projectTab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3 && projectTab.getSelectedIndex() > 0) {
                    rightClickMenu(e.getX(), e.getY());
                }
            }
        });
        
        projectTab.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int selectedTab = projectTab.getSelectedIndex();
                if (selectedTab > 0) {
                    String selectedTabTitle = projectTab.getTitleAt(selectedTab);
                    projectNameLabel.setText("Project Name: " + selectedTabTitle);
                }
            }
        });

        searchButton.addActionListener(e->{
            if(searchBar.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "Empty Search Key");
            } else {
                new  SearchResultDialog(searchBar.getText());
            }
        });
        
        add(topPanel, BorderLayout.NORTH);
        add(new TabsPanels(theProjectID), BorderLayout.CENTER);
    }
  
/**
     * Allows user to right-click the selected tab for a options menu
     * 
     * @author Hassan Abbas
     */
    private void rightClickMenu(int x, int y) {
        // Create a popup menu
        JPopupMenu popupMenu = new JPopupMenu();
        // Create menu items for renaming and deleting tabs
        JMenuItem renameTab = new JMenuItem("Rename");
        JMenuItem deleteTab = new JMenuItem("Remove");
        // Add action listeners to handle the menu item actions
        renameTab.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                renameTab(); // Call the method to handle renaming the tab
            }
        });
        deleteTab.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteTab(); // Call the method to handle deleting the tab
            }
        });
        // Add the menu items to the popup menu
        popupMenu.add(renameTab);
        popupMenu.add(deleteTab);
        popupMenu.show(projectTab, x, y);
    }
    /**
     * Allows user to rename the selected tab
     * 
     * @author Hassan Abbas
     */
    private void renameTab() {
        // Get the title of the currently selected tab
        String currentTabTitle = projectTab.getTitleAt(projectTab.getSelectedIndex());
        // Prompt the user to enter a new project name using an input dialog
        String newTabTitle = JOptionPane.showInputDialog(ProjectScreen.this, "Enter new project name:", currentTabTitle);
        // Check if the user entered a valid new project name
        if (newTabTitle != null && !newTabTitle.isEmpty()) {
            // Update the title of the selected tab with the new project name
            projectTab.setTitleAt(projectTab.getSelectedIndex(), newTabTitle);
        }
    }
    
    /**
     * Allows user to delete the selected tab
     * 
     * @author Hassan Abbas
     */
    private void deleteTab() {
        // Get the index of the currently selected tab
        int selectedTab = projectTab.getSelectedIndex();
        // Check if the selected tab is not the first tab
        if(selectedTab > 0) {
            // Display a confirmation dialog to ensure the user wants to delete the project
            int confirm = JOptionPane.showConfirmDialog(ProjectScreen.this, "Are you sure you want to delete this project?");
            // Check if the user confirmed the deletion
            if(confirm == JOptionPane.YES_OPTION) {
                // Remove the selected tab from the tabbed pane
                projectTab.remove(selectedTab);
            }
        }
    }
}
