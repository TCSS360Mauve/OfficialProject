package view;

import controller.DocumentController;
import model.Document;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EventObject;
import java.util.HashMap;
import java.text.DecimalFormat;

/**
 * The "Documents" tab of a project, which allows users to add and view documents to their project.
 * @author Tin Phu
 * @author Riley Bennett
 * @version 0.3
 */
public class DocumentTab extends JPanel {

    /**
     * The ID of the project this document tab belongs to.
     */
    private String projectID;

    /**
     * The list of documents belonging to a certain project.
     */
    private HashMap<String, Document> listOfDocs;

    /**
     * Table model to implement the document table with.
     */
    private final DefaultTableModel model;

    /**
     * Table to display documents in.
     */
    private final JTable table;

    /**
     * Formatter to round costs to 2 decimal places.
     */
    private final DecimalFormat df = new DecimalFormat("#.##");


    JButton openDocument = new JButton("Open Document");
    JPanel buttonPanel = new JPanel();
    JButton addButton = new JButton("Add Document");

    JButton deleteDocument = new JButton("Delete Document");

    JButton readDocument = new JButton("Read Document");


    /**
     * Constructor for the documents tab.
     * @author Tin Phu
     * @author Riley Bennett
     * @param theProjectID The ID of the current project.
     */
    public DocumentTab(final String theProjectID){

        projectID = theProjectID;

        // Get documents already associated with this project (if any)
        listOfDocs = DocumentController.getDocsByProjectID(theProjectID);
//        DefaultListModel<String> listModel = new DefaultListModel<>();
//        JList<String> itemList = new JList<>(listModel);    // List of documents/items
        setLayout(new GridBagLayout());
    
        // Create the table model
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Description");
        model.addColumn("Date");
        model.addColumn("Cost ($)");

        // Create the table and set the model
        table = new JTable(model);


        table.setPreferredScrollableViewportSize( new Dimension(700, 300));
        // Set a cell editor for the ID column to lock it from being edited
        TableColumn lockedColumn = table.getColumnModel().getColumn(0);
        lockedColumn.setCellEditor(new LockedColumnEditor());
        //Set a cell editor for the date column to lock it from being edited
        lockedColumn = table.getColumnModel().getColumn(3);
        lockedColumn.setCellEditor(new LockedColumnEditor());

        // Set column widths
        TableColumn column = table.getColumnModel().getColumn(2); // Description column
        column.setPreferredWidth(150);
        column = table.getColumnModel().getColumn(0); // Date column
        column.setPreferredWidth(10);
        column = table.getColumnModel().getColumn(3); // Date column
        column.setPreferredWidth(30);
        column = table.getColumnModel().getColumn(4); // Total Cost column
        column.setPreferredWidth(30);

        /**
         * Export data when table is changed by the user
         * @author Tin Phu
         */
        model.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {

                // Check if the user updated something in the table
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() >= 0 && e.getFirstRow() >= 0) {

                    int row = e.getFirstRow();
                    String id = table.getValueAt(row,0 ).toString();    // ID of changed document
                    String name = model.getValueAt(row, 1).toString();  // Name of changed document
                    String description = model.getValueAt(row, 2).toString();   // Description of changed document
                    String totalCost = model.getValueAt(row, 4).toString();     // Cost of changed document

                    // Error if name is empty
                    if (name.equals("")) {
                        JOptionPane.showMessageDialog(DocumentTab.this, "Name cannot be empty!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                        model.setValueAt(listOfDocs.get(id).getDocumentName(), row, e.getColumn());
                        return;
                    }

                    // Set cost, formatted to 2 decimal places
                    double totalCostRound;
                    BigDecimal decimalTest = new BigDecimal(0);
                    try {
                        totalCostRound = Double.valueOf(totalCost);
                        totalCostRound = Double.valueOf(df.format(totalCostRound));

                        decimalTest = new BigDecimal(totalCostRound);
                        // If cost negative, display error message
                        if (totalCostRound < 0) {
                            throw new NumberFormatException();
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(DocumentTab.this, "Invalid Price!", "Error", 
                        JOptionPane.ERROR_MESSAGE);
                        model.setValueAt(listOfDocs.get(id).getTotalCost(), row, e.getColumn());
                        return;
                    }

                    listOfDocs.get(id).setTotalCost(decimalTest.setScale(2, RoundingMode.HALF_EVEN));
                    listOfDocs.get(id).setDocumentName(name);               // Set name
                    listOfDocs.get(id).setDocumentDescription(description); // Set Description
                    
                    
                    DocumentController.addDocument(listOfDocs.get(id));     // Add document to list of documents
                    updateTable(id);
                }
            }
        });

        // Load each document into the table
        listOfDocs.forEach((k, e)-> {
            addRow(k, e.getDocumentName(), e.getDocumentDescription(), e.getDate().toString(), 
            e.getTotalCost().setScale(2, RoundingMode.HALF_EVEN).toString());
        });

        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem readItem = new JMenuItem("Read");

        // Add action listener to openItem
        openItem.addActionListener(new openDocActionListener());

        // Add action listener to readItem
        readItem.addActionListener(new readDocActionListener());

        // Add action listener to deleteItem
        deleteItem.addActionListener(new deleteDocActionListener());

        // Add the menu items to the popup menu
        popupMenu.add(openItem);
        popupMenu.add(readItem);
        popupMenu.add(deleteItem);

        /**
         * Add a mouse listener to show the popup menu when the user right-clicks
         * @author Tin Phu
         */
        table.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = table.rowAtPoint(e.getPoint());

                    // Check that row is actually in the table
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);    // Select row where user clicked
                        popupMenu.show(table, e.getX(), e.getY());  // Show popup where user clicked
                    }
                }
            }
        });

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        JScrollPane scrollPane = new JScrollPane(table);    // Make table scrollable
        c.gridx = 0;
        c.gridy = 1;
        scrollPane.setMinimumSize(new Dimension(800,500));
        add(scrollPane, c);

        // Add document button
        c.gridx = 0;
        c.gridy = 2;

        /**
         * Action listener for the add document button.
         *  @author Tin Phu
         */
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DocumentCreationFormPopUp(theProjectID) ;
            }
        });
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(openDocument);
        buttonPanel.add(readDocument);
        buttonPanel.add(deleteDocument);

        //add(addButton,c);

        // Open document button
        c.gridx = 0;
        c.gridy = 3;

        openDocument.addActionListener(new openDocActionListener());

        //add(openDocument,c);

        // Read document button
        c.gridx = 0;
        c.gridy = 4;

        readDocument.addActionListener(new readDocActionListener());
        //add(openDocument,c);

        // Delete document button
        c.gridx = 0;
        c.gridy = 5;

        deleteDocument.addActionListener(new deleteDocActionListener());

        add(buttonPanel,c);




    }

    /**
     * Adds a document in a new row to the table.
     * @author Tin Phu
     * @author Riley Bennett
     * @param id The id of the document to be added.
     * @param name The name of the document to be added.
     * @param description The description of the document to be added.
     * @param date The date of the document to be added.
     * @param totalCost The cost of the document to be added.
     */
    private void addRow(final String id, final String name, final String description, final String date, 
                        final String totalCost) {

        Object[] rowData = {id, name, description, date,totalCost};
        model.addRow(rowData);
    }

    /**
     * Updates the table.
     * @author Tin Phu
     * @param theProjectID The ID of the project this table belongs to.
     */
    public void updateTable(final String theProjectID) {
//        System.out.println("Total Cost: " +ProjectController.updateTotalCostByID(theProjectID).toString());

        // Clear existing rows
        model.setRowCount(0);

        // Populate the table with the updated data from the list
        listOfDocs.forEach((k, e)->{
            addRow(k, e.getDocumentName(), e.getDocumentDescription(), e.getDate().toString(), 
            e.getTotalCost().setScale(2, RoundingMode.HALF_EVEN).toString());
        });
    }

    /**
     * Editor to lock the table columns with.
     * @author Tin Phu
     * @author Riley Bennett
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
            return true;    // Disallow cell editing
        }

        // Non-needed methods from interface left empty.
        public void cancelCellEditing() {
        }

        public void addCellEditorListener(CellEditorListener l) {
        }

        public void removeCellEditorListener(CellEditorListener l) {
        }
    }

    /**
     * Popup for creating a document.
     * @author Tin Phu
     * @author Riley Bennett
     */
    public class DocumentCreationFormPopUp extends JPanel{

        /**
         * Text box for the document name.
         */
        private final JTextField documentNameField;

        /**
         * Text box for the document description.
         */
        private final JTextArea documentDescriptionField;

        /**
         * Text box for the document cost.
         */
        private final JTextField totalCostField;

        /**
         * The file path of the document (if file is selected).
         */
        private String srcFileString = "";

        /**
         * Dialog of the popup window.
         */
        private final JDialog dialog = new JDialog();

        /**
         * Constructor for creating the popup.
         * @author Tin Phu
         * @author Riley Bennett
         * @param theProjectID The ID of the project to add a document to.
         */
        public DocumentCreationFormPopUp(final String theProjectID) {

            // Create components
            JLabel documentNameLabel = new JLabel("Document Name:");
            documentNameField = new JTextField(20);

            JLabel documentDescriptionLabel = new JLabel("Document Description:");
            documentDescriptionField = new JTextArea(7,40);
            documentDescriptionField.setWrapStyleWord(true);
            documentDescriptionField.setLineWrap(true);
            JLabel totalCostLabel = new JLabel("Total Cost:");
            totalCostField = new JTextField(10);

            JLabel fileSrcStringLabel = new JLabel("(Optional)");

            // Set max width for file name
            Dimension d = fileSrcStringLabel.getPreferredSize();
            fileSrcStringLabel.setPreferredSize(new Dimension(d.width + 90, d.height));
            
            JButton fileSelectorButton = new JButton("Select File");

            JButton createButton = new JButton("Create Document");

            // Create layout
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.WEST;
            constraints.insets = new Insets(10, 10, 10, 10);

            // Add components to the panel
            constraints.gridx = 0;
            constraints.gridy = 0;
            panel.add(documentNameLabel, constraints);

            constraints.gridx = 1;
            panel.add(documentNameField, constraints);

            constraints.gridx = 0;
            constraints.gridy = 1;
            panel.add(documentDescriptionLabel, constraints);

            constraints.gridx = 1;
            JScrollPane scrollPane = new JScrollPane(documentDescriptionField);

            panel.add(scrollPane, constraints);

            constraints.gridx = 0;
            constraints.gridy = 2;
            panel.add(totalCostLabel, constraints);

            constraints.gridx = 1;
            panel.add(totalCostField, constraints);

            constraints.gridx = 0;
            constraints.gridy = 3;
            panel.add(fileSrcStringLabel, constraints);
            constraints.gridx = 1;
            constraints.gridy = 3;
            constraints.gridwidth = 2;
            panel.add(fileSelectorButton, constraints);

            constraints.gridy = 4;
            constraints.anchor = GridBagConstraints.CENTER;
            panel.add(createButton, constraints);

            /**
             * Action listener for the file selector button
             * @author Tin Phu
             */
            fileSelectorButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
                    JFileChooser fileChooser = new JFileChooser(desktopPath);
                    int result = fileChooser.showOpenDialog(panel);

                    // Check if user chose a file
                    if(result == JFileChooser.APPROVE_OPTION){
                        srcFileString = fileChooser.getSelectedFile().toString();
                        File file = new File(srcFileString);
                        fileSrcStringLabel.setText(file.getName());
                    }
                }
            });

            /**
             * Action listener for the create button.
             * @author Tin Phu
             */
            createButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String documentName = documentNameField.getText();
                    String documentDescription = documentDescriptionField.getText();
                    Document newDoc = null;

                    // Check if any fields are empty
                    if(documentName.isEmpty() || documentDescription.isEmpty() || totalCostField.getText().isEmpty() ){
                        JOptionPane.showMessageDialog(DocumentTab.this, "Please enter required information!");

                    } else if(documentDescription.length() >= 1300){
                        JOptionPane.showMessageDialog(DocumentTab.this, "The Description is too long!");

                    } else {

                        double totalCost;

                        try {
                            // Round to 2 decimal places
                            totalCost = Double.parseDouble(totalCostField.getText());
                            totalCost = Double.valueOf(df.format(totalCost));

                            if(totalCost < 0){
                                JOptionPane.showMessageDialog(DocumentTab.this, "The Total cost can not be negative!");
                            }else {
                                // Create new document with no file if no file selected
                                if(srcFileString.isEmpty()){
                                    newDoc = new Document(documentName,documentDescription,theProjectID,"",
                                            BigDecimal.valueOf(totalCost));

                                    // Create new document with file if user selected file
                                } else {
                                    newDoc = new Document(documentName,documentDescription,theProjectID,"",
                                            BigDecimal.valueOf(totalCost), srcFileString );

                                }

                                DocumentController.addDocument(newDoc);     // Add document to database
                                listOfDocs.put(newDoc.id(), newDoc);        // Add document to list
                                dialog.setVisible(false);                 // Close dialog
                                updateTable(theProjectID);
                            }


                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(DocumentTab.this, 
                            "Something went wrong! The File could not be copied.");
                        } catch (NumberFormatException ex2) {
                            JOptionPane.showMessageDialog(DocumentTab.this, "Invalid Price!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
            });

            // Set popup attributes
            dialog.setLocationRelativeTo(DocumentTab.this);
            dialog.setContentPane(panel);
            dialog.setTitle("Document Creation Form");
            dialog.setSize(700, 500);
            dialog.setLocationRelativeTo(null); // Center the dialog on the screen
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);

        }
    }

    /**
     * Action listener for the Open Document buttons.
     * @author Tin Phu
     * @author Riley Bennett
     */
    private class openDocActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();

            if (row >= 0) {
                String id = (String) table.getValueAt(row, 0);
                
                // Check if document has file attached
                if(!listOfDocs.get(id).openDoc()) {
                    JOptionPane.showMessageDialog(DocumentTab.this, "This document does not have an attached file!");
                }
            } else {
                JOptionPane.showMessageDialog(DocumentTab.this, "No document selected.");
            }
        }
    }

    /**
     * Action listener for the Read Document buttons.
     * @author Tin Phu
     * @author Riley Bennett
     */
    private class readDocActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();

            if (row >= 0) {
                String id = (String) table.getValueAt(row, 0);
                new DocumentTabRead(listOfDocs.get(id));    // Displays item information in a new popup
            } else {
                JOptionPane.showMessageDialog(DocumentTab.this, "No document selected.");
            }
        }
    }

    /**
     * Action listener for the Delete Document buttons.
     * @author Tin Phu
     * @author Riley Bennett
     */
    private class deleteDocActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();

            if (row >= 0) {
                int result = JOptionPane.showConfirmDialog(DocumentTab.this, "Are you sure you want to Delete the document?",
                "Confirmation", JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
//                    String name = model.getValueAt(row, 1).toString();
//                    System.out.println("Deleting " + name);
                    String id = (String) table.getValueAt(row, 0);

                    DocumentController.deleteADocument(listOfDocs.get(id));     // Delete the specified document from the database
                    listOfDocs.remove(id);                                      // Remove document from the list
                    updateTable(projectID);                                     // Update table
                }
            } else {
                JOptionPane.showMessageDialog(DocumentTab.this, "No document selected.");
            }
        }
    }
}






