package view;

import controller.AppInfoController;
import controller.ProjectController;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.HashMap;
import model.Project;
import model.User;

/**
 * A class to create the Home screen of the FileNtro project.
 * @author Riley Bennett
 * @author Tin Phu
 * @version 0.3
 */
public class HomeScreen extends JPanel {

    /**
     * A mapping of project IDs to project objects.
     */
    HashMap<String, Project> listOfProjects = new HashMap<String, Project>();

    /**
     * A table model used to implement the project table.
     */
    private DefaultTableModel projectTableModel;

    /**
     * Label for the welcome.
     */
    private static JLabel welcomeLabel;

    /**
     * The table to display the projects in.
     */
    private JTable projectTable;

    /**
     * The button to open a project with.
     */
    private JButton openButton;

    /**
     * The button to delete a project with.
     */
    private JButton deleteButton;

    /**
     * The button to logout with.
     */
    private JButton logoutButton;

    /**
     * The button to create a project with.
     */
    private JButton createButton;

    /**
     * Constructor to create the Home screen.
     * @author Tin Phu
     * @author Riley Bennett
     * @param user The user of the app.
     * @param cardPanel The panels to swap to/from.
     * @param cardLayout The layout used to swap to/from panels.
     */
    public HomeScreen(final User user, final JPanel cardPanel, final CardLayout cardLayout) {
   
        User thisUser = user;

        // Get projects associated with this user
        listOfProjects = ProjectController.getProjectsByUserID(user.getId());
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the project table model and table
        projectTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Date"}, 0);
        projectTable = new JTable(projectTableModel);
        projectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        projectTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);

        // Set a cell editor for the ID column to make it uneditable.
        TableColumn lockedColumn = projectTable.getColumnModel().getColumn(0);
        lockedColumn.setCellEditor(new LockedColumnEditor());
        // Set a cell editor for the date column to make it uneditable.
        lockedColumn =  projectTable.getColumnModel().getColumn(2);
        lockedColumn.setCellEditor(new LockedColumnEditor());

        // Load project list into the table
        setProjects(listOfProjects);
        
        // Set size of table columns
        TableColumnModel columnModel = projectTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(10);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(projectTable);     // Make table scrollable
        scrollPane.setPreferredSize(new Dimension(400, 200));

        add(scrollPane, BorderLayout.CENTER);

        projectTableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {

                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() >= 0 && e.getFirstRow() >= 0) {
                    int row = e.getFirstRow();
                    String id = projectTableModel.getValueAt(row, 0).toString();
                    String name = projectTableModel.getValueAt(row, 1).toString();

                    // Check if name is empty
                    if (name.equals("")) {
                        JOptionPane.showMessageDialog(HomeScreen.this, "Name cannot be empty!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                        projectTableModel.setValueAt(listOfProjects.get(id).getProjectName(), row, 1);
                        return;
                    }

                    listOfProjects.get(id).setProjectName(name);
                }
            }
        });

        // Make action buttons
        openButton = new JButton("Open Project");
        createButton = new JButton("Create");
        deleteButton = new JButton("Delete");
        logoutButton = new JButton("Log Out");

        Dimension buttonSize = new Dimension(120, 30);
        openButton.setPreferredSize(buttonSize);
        createButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        logoutButton.setPreferredSize(buttonSize);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(openButton);
        buttonPanel.add(createButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(logoutButton);

        welcomeLabel = new JLabel("Welcome Back, " + user.getName()+"!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Top panel for welcome label and buttons
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
                
        // Action listener for the open project button
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int selectedRow = projectTable.getSelectedRow();

                // Check if selected row is in the table
                if (selectedRow != -1) {
                    String projectID = projectTable.getValueAt(selectedRow, 0).toString();
//                    String projectName = projectTable.getValueAt(selectedRow, 1).toString();
//                    System.out.println("Opening project ID: " + projectID);

                    // Open project screen
                    ProjectScreen projectScreen = new ProjectScreen(thisUser, cardPanel, cardLayout, projectID);
                    cardPanel.add(projectScreen, "ProjectScreen");
                    cardLayout.show(cardPanel, "ProjectScreen");
                } else {
                    JOptionPane.showMessageDialog(HomeScreen.this, "No project selected.");
                }
            }
        });

        // Action listener for the create project button
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String projectName = JOptionPane.showInputDialog(HomeScreen.this, "Enter project name: ");

                // Check if name was inputted
                if (projectName != null && !projectName.isEmpty()) {

                    // Create project object
                    Project newProject = new Project(projectName, BigDecimal.valueOf(0), BigDecimal.valueOf(0), user.getId());

                    // Add project to database and list
                    ProjectController.addProject(newProject);
                    listOfProjects.put(newProject.getId(),newProject);

                    setProjects(listOfProjects);    // Update table

                // Check if user did not input a name
                } else if (projectName != null) {
                    JOptionPane.showMessageDialog(HomeScreen.this, "Please enter a valid project name.");
                }
            }
        });

        // Action listener for the delete project button
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int selectedRow = projectTable.getSelectedRow();

                // Check if selected row is in the table
                if (selectedRow != -1) {

                    int result = JOptionPane.showConfirmDialog(HomeScreen.this, "Are you sure you want to delete this" +
                    " project?","Confirmation", JOptionPane.YES_NO_OPTION);

                    // Check if user confirmed
                    if (result == JOptionPane.YES_OPTION) {
                        String projectID = projectTable.getValueAt(selectedRow, 0).toString();

                        try {
                            ProjectController.deleteProjectByID(projectID);     // Delete project from database
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(HomeScreen.this, "Something went wrong," +
                            " the project folder could not be deleted!");
                        }

                        listOfProjects.remove(projectID);       // Remove project from list
                        setProjects(listOfProjects);            // Update table
                    }
                } else {
                    JOptionPane.showMessageDialog(HomeScreen.this, "No project selected.");
                }
                
            }
        });

        // Action listener for the logout button
        logoutButton.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                cardLayout.show(cardPanel, "LogInScreen");      // Move to log-in screen
                AppInfoController.logout();                          // Log out user
                AppUI.toggleFileMenu(false);
            }
        }));
    }

    /**
     * Updates the table with all projects currently in the project list.
     * @author Hassan Abbas
     * @param map The HashMap of project id to project object pairs.
     */
    public void setProjects(final HashMap<String, Project> map) {
        projectTableModel.setRowCount(0);   // Start with 0 rows

        // For each project, display data in the table in a new row
        for (Project project : map.values()) {
            Object[] rowData = {project.getId(), project.getProjectName(), project.getDate()};
            projectTableModel.addRow(rowData);
        }
    }

    /**
     * Column editor to lock certain columns from being edited.
     * @author Tin Phu
     */
    private class LockedColumnEditor implements TableCellEditor {
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return null; // Return null to make the cell non-editable
        }

        public Object getCellEditorValue() {
            return null;
        }

        public boolean isCellEditable(EventObject anEvent) {
            return false; // Return false to prevent editing
        }

        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        public boolean stopCellEditing() {
            return true;    // Prevents editing
        }

        // Non-needed methods left empty
        public void cancelCellEditing() {
        }

        public void addCellEditorListener(CellEditorListener l) {
        }

        public void removeCellEditorListener(CellEditorListener l) {
        }
    }

    /**
     * Updates welcome lable whenever username is changed.
     * @author Bairu Li
     */
    public static void updateWelcome() {
        welcomeLabel.setText("Welcome Back, " + AppInfoController.getCurrentUser().getName()+"!");
    }
}