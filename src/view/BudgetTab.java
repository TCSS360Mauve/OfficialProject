package view;

import controller.DocumentController;
import controller.ProjectController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.text.DecimalFormat;
import model.Document;

/**
 * The budget tab for a project in the FileNtro program.
 * @author Thinh Le
 * @author Riley Bennett
 * @author Tin Phu (fixing bugs only)
 * @version 0.3
 */
public class BudgetTab extends JPanel {

    /**
     * The ID of the project this tab belongs to.
     */
    private String theProjectID;

    /**
     * The total cost of this project.
     */
    public double theTotalCost;

    /**
     * The total budget of this project.
     */
    public double totalBudget;

    /**
     * The label to display the total cost with.
     */
    private JLabel totalLabel;

    /**
     * This is the label for total budget.
     */
    private JLabel totalBudgetLabel;

    /**
     * Button to set the budget with.
     */
    private JButton setBudgetButton;

    /**
     * Mapping of Document ID/Document object pairs.
     */
    public HashMap<String, Document> myDoc;

    /**
     * The model used to implement the table.
     */
    public final DefaultTableModel model;
    /**
     * The table to display items in.
     */
    public final JTable table;

    /**
     * Formatter to display doubles with 2 decimal places.
     */
    private final DecimalFormat df = new DecimalFormat("#.##");

    /**
     * This is the constructor to create the Budget Tab.
     *
     * @param theProjectID The ID of the project
     * @author Thinh Le
     * @author Riley Bennett
     */
    public BudgetTab(final String theProjectID) {

        this.theProjectID = theProjectID;

        // Get documents associated with the project.
        myDoc = DocumentController.getDocsByProjectID(theProjectID);
        totalBudget = ProjectController.getTotalBudgetByID(theProjectID).doubleValue();

        setLayout(new GridBagLayout());

        // Create the table model
        model = new DefaultTableModel();
        // Add id column
        model.addColumn("ID");
        // Add Name column
        model.addColumn("Name");
        // Add Total cost column
        model.addColumn("Cost ($)");

        // Create the table and set the model
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(350, 350));

        totalLabel = new JLabel("Current Cost: $0.00");
        totalLabel.setFont(new Font("Verdana", Font.PLAIN, 18));

        updateTotalCost();

        // Put list of documents into table
        myDoc.forEach((k, e) -> {
            addRowToTable(k, e.getDocumentName(), e.getTotalCost().toString());
        });

        // Make table uneditable
        table.setDefaultEditor(Object.class, null);

        // Add total budget label
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(15, 15, 15, 15);
        totalBudgetLabel = new JLabel("Total Budget: $" + totalBudget);
        totalBudgetLabel.setFont(new Font("Verdana", Font.PLAIN, 18));

        add(totalBudgetLabel, c);

        c.gridx = 2;
        c.gridy = 2;

        add(totalLabel, c);

        setBudgetButton = new JButton("Set Budget");
        c.gridx = 0;
        c.gridy = 1;

        add(setBudgetButton, c);
        JScrollPane scrollPane = new JScrollPane(table);
        c.gridx = 0;
        c.gridy = 2;
        scrollPane.setMinimumSize(new Dimension(350, 350));
        add(scrollPane, c);

        // Add document button
        JButton addButton = new JButton("Add Document");
        c.gridx = 0;
        c.gridy = 3;
        add(addButton, c);

        // Delete button
        JButton deleteButton = new JButton("Delete");
        c.gridx = 1;
        c.gridy = 3;
        add(deleteButton, c);

        JButton changeButton = new JButton("Change Name");
        c.gridx = 2;
        c.gridy = 3;
        add(changeButton, c);

        JButton changeCostButton = new JButton("Change Cost");
        c.gridx = 3;
        c.gridy = 3;
        add(changeCostButton, c);

        JButton addCostButton = new JButton("Add Cost only");
        c.gridx = 0;
        c.gridy = 4;
        add(addCostButton, c);

        updateTotalBudgetLabel();

        /**
         * Action listener for add cost only button.
         */
        addCostButton.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {

                // Prompt the user for item name and price
                String itemName = JOptionPane.showInputDialog("Enter Item Name:");
                if (itemName == null) {
                    return; // User clicked Cancel
                }

                String priceStr = JOptionPane.showInputDialog("Enter Price:");
                if (priceStr == null) {
                    return; // User clicked Cancel
                }

                // Convert the price to a double
                double price;
                try {
                    price = Double.parseDouble(priceStr);
                    
                    // Round to 2 decimal places
                    price = Double.valueOf(df.format(price));
                } catch (NumberFormatException ex) {
                         JOptionPane.showMessageDialog(BudgetTab.this, "Invalid Price!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(price < 0){
                    JOptionPane.showMessageDialog(BudgetTab.this, "The Price can not be negative!");
                } else {
                    Document doc = null;

                    doc = new Document(itemName, "", theProjectID, "", BigDecimal.valueOf(price));
                    DocumentController.addDocument(doc);
                    myDoc.put(doc.id(), doc);

                    theTotalCost += myDoc.get(doc.id()).getTotalCost().doubleValue();

                    theTotalCost = Double.valueOf(df.format(theTotalCost));
                    totalLabel.setText("Current Cost: $" + theTotalCost);

                    updateTable();

                    // //This is a warning message when it is over your budget.
                    // if(theTotalCost>totalBudget){

                    //     JOptionPane.showMessageDialog(BudgetTab.this,"You are over your budget!","Reminder",JOptionPane.WARNING_MESSAGE);
                    // }
                }


            }
        });


        /**
         * Action Listener for add document button.
         */
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DocumentCreationFormPopUp(theProjectID);
            }
        });

        /**
         * Action Listener for set budget button.
         */
        setBudgetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String input = JOptionPane.showInputDialog("Enter new budget:");
                try {
                    double newBudget = Double.parseDouble(input);
                    if(newBudget < 0){
                        JOptionPane.showMessageDialog(BudgetTab.this, "The Budget can not be negative!");
                    } else {
                        // Round to 2 decimal places
                        newBudget = Double.valueOf(df.format(newBudget));

                        ProjectController.setTotalBudgetByID(theProjectID, BigDecimal.valueOf(newBudget));
                        setTotalBudget(newBudget);
                        updateTable();
                    }


                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(BudgetTab.this, "Please enter a numeric value.");

                }
            }
        });

        /**
         * Action Listener for delete button.
         */
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int result = JOptionPane.showConfirmDialog(BudgetTab.this, "Are you sure you want to delete this item?",
                "Confirmation", JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
//                  System.out.println("Deleting " + name);
                    String id = (String) table.getValueAt(row, 0);
                    theTotalCost -= myDoc.get(id).getTotalCost().doubleValue();

                    // Round to 2 decimal places
                    theTotalCost = Double.valueOf(df.format(theTotalCost));
                    totalLabel.setText("Current Cost: $" + theTotalCost); 
                    DocumentController.deleteADocument(myDoc.get(id));

                    myDoc.remove(id);

                    updateTable();
                }
            }
        });

        /**
         * Action listener for change name button.
         */
        changeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "No item selected!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String currentID = (String) table.getValueAt(selectedRow, 0); //CURRENT ID
            String currentName = (String) table.getValueAt(selectedRow, 1); //CURRENT Name
            String newMessage = JOptionPane.showInputDialog(this, "Enter New Name:", currentName); //NEW name
            String currentDecription = DocumentController.findDocbyID(currentID).getDocumentDescription();
            LocalDate currentDate =  DocumentController.findDocbyID(currentID).getDate();
            String currentPath = DocumentController.findDocbyID(currentID).getFilePath();
            if (newMessage == null) {
                return;
            }

            Document doc2 = new Document(newMessage, currentDecription, theProjectID, "",
                                         myDoc.get(currentID).getTotalCost(),currentID, currentDate, currentPath  );

            //DocumentController.deleteADocument(myDoc.get(currentID));
            //myDoc.remove(currentID);

            DocumentController.addDocument(doc2);
            myDoc.put(currentID, doc2);

            updateTable();
        });

        /**
         * Action listener for change cost button.
         */
        changeCostButton.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "No item selected!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String currentID = (String) table.getValueAt(selectedRow, 0); //CURRENT ID

            String currentItemPrice = (String) table.getValueAt(selectedRow, 2); //CURRENT PRICE
            double price = Double.parseDouble(currentItemPrice);

            //Round to 2 decimal places
            price = Double.valueOf(df.format(price));

            String message = JOptionPane.showInputDialog(this, "Enter New Price:", price); //NEW PRICE

            if (message == null) {
                return;
            }

            double newPrice;
            newPrice = Double.parseDouble(message);

            //Round to 2 decimal places
            newPrice = Double.valueOf(df.format(newPrice));
            if(newPrice < 0) {
                JOptionPane.showMessageDialog(BudgetTab.this, "The New Price can not be negative!");
            } else {
                String currentName = DocumentController.findDocbyID(currentID).getDocumentName();

                String currentDecription = DocumentController.findDocbyID(currentID).getDocumentDescription();
                LocalDate currentDate =  DocumentController.findDocbyID(currentID).getDate();
                String currentPath = DocumentController.findDocbyID(currentID).getFilePath();

                Document newDoc2 = new Document(currentName, currentDecription,
                        theProjectID, "", new BigDecimal(newPrice).setScale(2, RoundingMode.HALF_EVEN),
                        currentID,currentDate,currentPath);

                DocumentController.addDocument(newDoc2);
                myDoc.put(currentID, newDoc2);
                updateTable();
            }


        });
    }

    /**
     * Adds a row to the table.
     * @author Thinh Le
     * @param id The ID of the item to be added.
     * @param name The name of the item to be added.
     * @param totalCost The cost of the item to be added.
     *
     */
    public void addRowToTable(String id, String name, String totalCost) {
        BigDecimal temp = new BigDecimal(Double.valueOf(totalCost));
        String formattedCost = temp.setScale(2, RoundingMode.HALF_EVEN).toString();

        Object[] row = {id, name, formattedCost};
        model.addRow(row);
    }

    /**
     * Updates the table with new data.
     * @author Thinh Le
     * @author Riley Bennett
     */
    public void updateTable() {
        // Clear the rows
        model.setRowCount(0);

        //Update each row
        myDoc.forEach((k, e) -> {
            addRowToTable(k, e.getDocumentName(), df.format(e.getTotalCost()));
        });

        BigDecimal pTotalCost= ProjectController.updateTotalCostByID(this.theProjectID).setScale(2, RoundingMode.HALF_EVEN);
        theTotalCost = Double.valueOf(pTotalCost.doubleValue());
        
        setCostLabel();
    }

    /**
     * Sets the total budget of the project.
     * @author Thinh Le
     * @param budget The budget to set to.
     */
    public void setTotalBudget(double budget) {
        totalBudget = budget;
        updateTotalBudgetLabel();
    }

    /**
     * Updates the label of the total budget.
     * @author Thinh Le
     */
    public void updateTotalBudgetLabel() {
        BigDecimal temp = new BigDecimal(totalBudget);
        totalBudgetLabel.setText("Total Budget: $" + temp.setScale(2, RoundingMode.HALF_EVEN));
    }

    /**
     * Updates the total cost of this project.
     * @author Thinh Le
     * @author Riley Bennett
     */
    public void updateTotalCost() {
        myDoc.forEach((k, b) -> {
            theTotalCost += b.getTotalCost().doubleValue();
        });

        // Round to 2 decimal places
        theTotalCost = Double.valueOf(df.format(theTotalCost));
        setCostLabel();
    }

    /**
     * Sets the cost label to red if the current cost is over budget, or black if cost is less than budget.
     * @author Riley Bennett
     */
    public void setCostLabel() {
        BigDecimal temp = new BigDecimal(theTotalCost);

        // If cost is over budget, set to red text
        if (theTotalCost > totalBudget) {
            totalLabel.setText("Current Cost (Over budget!): $" + temp.setScale(2, RoundingMode.HALF_EVEN));
            totalLabel.setForeground(Color.RED);

        // If cost less than budget, normal black text
        } else {
            totalLabel.setText("Current Cost: $" + temp.setScale(2, RoundingMode.HALF_EVEN));
            totalLabel.setForeground(Color.BLACK);
        }
    }

    /**
     * Class to create the document creation popup.
     * @author Tin Phu
     * @author Riley Bennett
     */
    public class DocumentCreationFormPopUp extends JPanel{

        /**
         * Name field of the popup.
         */
        public final JTextField documentNameField;

        /**
         * Description field of the popup.
         */
        public final JTextArea documentDescriptionField;

        /**
         * Cost field of the popup.
         */
        public final JTextField totalCostField;

        /**
         * File path associated with this document (if any)
         */
        private String srcFileString = "";

        /**
         * Dialog to create this popup with.
         */
        public final JDialog dialog = new JDialog();

        /**
         * Constructor to create the popup.
         * @author Tin Phu
         * @param theProjectID
         */
        public DocumentCreationFormPopUp(String theProjectID) {
            // NOTE: See DocumentTab for more in-depth inline comments.

            // Create components
            JLabel documentNameLabel = new JLabel("Document Name:");
            documentNameField = new JTextField(20);

            JLabel documentDescriptionLabel = new JLabel("Document Description:");
            documentDescriptionField = new JTextArea(7,40);

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
             * Action Listener for the file selector button.
             * @author Tin Phu
             */
            fileSelectorButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    int result = fileChooser.showOpenDialog(panel);
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
                    if(documentName.isEmpty() || documentDescription.isEmpty() || totalCostField.getText().isEmpty() ){
                        JOptionPane.showMessageDialog(BudgetTab.this, "Please enter required information!");

                    } else if(documentDescription.length() >= 1300){
                        JOptionPane.showMessageDialog(BudgetTab.this, "The Description is too long!");

                    } else {

                        try {
                            double totalCost = Double.parseDouble(totalCostField.getText());
                        
                            // Round to 2 decimal places
                            totalCost = Double.valueOf(df.format(totalCost));

                            if(totalCost < 0){
                                JOptionPane.showMessageDialog(BudgetTab.this, "The Total cost can not be negative!");
                            } else {
                                if(srcFileString.isEmpty()){
                                    newDoc = new Document(documentName,documentDescription,theProjectID,"",
                                            BigDecimal.valueOf(totalCost));

                                } else {
                                    newDoc = new Document(documentName,documentDescription,theProjectID,"",
                                            BigDecimal.valueOf(totalCost), srcFileString );
                                }

                                DocumentController.addDocument(newDoc);
                                myDoc.put(newDoc.id(), newDoc);
                                dialog.setVisible(false);

                                theTotalCost += myDoc.get(newDoc.id()).getTotalCost().doubleValue();

                                // Round to 2 decimal places
                                theTotalCost = Double.valueOf(df.format(theTotalCost));

                                updateTable();
                            }


                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(BudgetTab.this, "Something went wrong! The File could not be coppied");
                        } catch (NumberFormatException ex2) {
                            JOptionPane.showMessageDialog(BudgetTab.this, "Invalid Price!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            dialog.setLocationRelativeTo(BudgetTab.this);
            dialog.setContentPane(panel);
            dialog.setTitle("Document Creation Form");
            dialog.setSize(700, 500);
            dialog.setLocationRelativeTo(null); // Center the dialog on the screen
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }
    }
}