package view;

import controller.DocumentController;
import controller.ProjectController;
import model.Document;

import java.io.File;
import javax.swing.*;

import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

import java.io.IOException;
import java.math.BigDecimal;

import java.util.HashMap;
import java.text.DecimalFormat;

/**
 * This is the Budget tab for handles cost.
 * @Author Thinh Le
 * @version 0.1
 */
public class BudgetTab extends JPanel {

    public String theProjectID;
    public double theTotalCost;
    public double totalBudget;
    public JLabel totalLabel;

    /**
     * This is the label for total budget.
     */
    public JLabel totalBudgetLabel;
    /**
     * This is the text field for set budget.
     */
    JTextField setBudgetField;
    /**
     * List of Docs belong to a specific Project
     */
    public JButton setBudgetButton;
    public HashMap<String, Document> myDoc;

    /**
     * DefaultTableModel model
     */
    public final DefaultTableModel model;
    /**
     * JTable table
     */
    public final JTable table;

    private final DecimalFormat df = new DecimalFormat("#.##");

    /**
     * This is the constructor to create the Budget Tab.
     *
     * @param theProjectID
     * @author Thinh Le
     */
    public BudgetTab(String theProjectID) {

        this.theProjectID = theProjectID;

        //Initialize my doc from Document controller
        myDoc = DocumentController.getDocsByProjectID(theProjectID);

        setLayout(new GridBagLayout());
        // Create the table model
        model = new DefaultTableModel();
        //add id column
        model.addColumn("ID");
        //add Name
        model.addColumn("Name");
        //add Total cost
        model.addColumn("Total Cost");
        // Create the table and set the model
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(350, 350));


        totalLabel = new JLabel("CurrentCost:$0.00");
        totalLabel.setFont(new Font("Verdana", Font.PLAIN, 18));

        updateTotalCost();

        // put list of doc into table
        myDoc.forEach((k, e) -> {
            addRowToTable(k, e.getDocumentName(), e.getTotalCost().toString());
        });

        table.setDefaultEditor(Object.class, null);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(15, 15, 15, 15);
        totalBudgetLabel = new JLabel("Total Budget:$");

        add(totalBudgetLabel, c);

        setBudgetField = new JTextField();
        setBudgetField.setText("");
        setBudgetField.setEditable(false);
        c.gridx = 1;
        c.gridy = 0;

        setBudgetField.setColumns(5);

        add(setBudgetField, c);
        c.gridx = 2;
        c.gridy = 2;
        add(totalLabel, c);
        setBudgetButton = new JButton("Set Budget");
        c.gridx = 0;
        c.gridy = 1;
        //  setBudgetButton.addActionListener(this);
        add(setBudgetButton, c);
        JScrollPane scrollPane = new JScrollPane(table);
        c.gridx = 0;
        c.gridy = 2;
        add(scrollPane, c);
        JButton addButton = new JButton("Add Document Only");
        c.gridx = 0;
        c.gridy = 3;
        add(addButton, c);

        JButton addButton2 = new JButton("Delete");
        c.gridx = 1;
        c.gridy = 3;
        add(addButton2, c);
        JButton addButton3 = new JButton("Change Name");
        c.gridx = 2;
        c.gridy = 3;
        add(addButton3, c);
        JButton addButton4 = new JButton("Change Cost");
        c.gridx = 3;
        c.gridy = 3;
        add(addButton4, c);
        JButton addButton5 = new JButton("Add Cost only");
        c.gridx = 0;
        c.gridy = 4;
        add(addButton5, c);
        updateTotalBudgetLabel();
        /**
         * This ask for Name and price to add into table.
         */
        addButton5.addActionListener(new ActionListener() {


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

                Document doc = null;

                doc = new Document(itemName, "", theProjectID, "", BigDecimal.valueOf(price));


                DocumentController.addDocument(doc);
                myDoc.put(doc.id(), doc);



                theTotalCost += myDoc.get(doc.id()).getTotalCost().doubleValue();


                totalLabel.setText("CurrentCost:$" + theTotalCost);

                updateTable();
                //This is a warning message when it is over your budget.
                if(theTotalCost>totalBudget){

                    JOptionPane.showMessageDialog(BudgetTab.this,"It is over your budget","Reminder",JOptionPane.WARNING_MESSAGE);
                }

            }

        });
        //This is for add document cost only button.
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DocumentCreationFormPopUp(theProjectID);



            }
        });
        //This is the set budget action listener
        setBudgetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Document doc = null;


                String input = JOptionPane.showInputDialog("Enter new budget:");
                try {
                    double newBudget = Double.parseDouble(input);

                    // Round to 2 decimal places
                    newBudget = Double.valueOf(df.format(newBudget));
                   // doc = new Document("Total Budget:$", "", theProjectID, "", BigDecimal.valueOf(newBudget));
                    ProjectController.setTotalBudgetByID(theProjectID, BigDecimal.valueOf(newBudget));
                    setTotalBudget(newBudget);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(BudgetTab.this, "Please enter a numeric value.");

                }
            }
        });

        //This is the delete item action listener
        addButton2.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {


                String name = model.getValueAt(row, 1).toString();
                System.out.println("Deleting " + name);
                String id = (String) table.getValueAt(row, 0);
                theTotalCost -= myDoc.get(id).getTotalCost().doubleValue();
                totalLabel.setText("CurrentCost:$" + theTotalCost); //DO THIS IN ORDER TO WORK
                DocumentController.deleteADocument(myDoc.get(id));


                myDoc.remove(id);

                updateTable();


            }
        });
        //This is button action listener to change name.
        addButton3.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "No item selected!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String currentID = (String) table.getValueAt(selectedRow, 0); //CURRENT ID

            String currentName = (String) table.getValueAt(selectedRow, 1); //CURRENT Name

            String newMessage = JOptionPane.showInputDialog(this, "Enter New Name:", currentName); //NEW PRICE


            if (newMessage == null) {
                return;
            }




            Document doc = new Document(currentName, "", currentID, "", myDoc.get(currentID).getTotalCost());
            Document doc2 = new Document(newMessage, "", theProjectID, "", myDoc.get(currentID).getTotalCost());

            DocumentController.deleteADocument(myDoc.get(currentID));
            myDoc.remove(currentID);


            DocumentController.addDocument(doc2);
            myDoc.put(currentID, doc2);

            updateTable();
        });

        //This is to change the cost of item
        addButton4.addActionListener(e -> {


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

              //Update the price in the table

            theTotalCost -= myDoc.get(currentID).getTotalCost().doubleValue();

//            Document newDoc = new Document(myDoc.get(currentID).getDocumentName(), "", currentID, "", new BigDecimal(price));
            Document newDoc2 = new Document(myDoc.get(currentID).getDocumentName(), "", theProjectID, "", new BigDecimal(newPrice));

            DocumentController.deleteADocument(myDoc.get(currentID));
            myDoc.remove(currentID);


            DocumentController.addDocument(newDoc2);
            myDoc.put(currentID, newDoc2);
            theTotalCost += myDoc.get(currentID).getTotalCost().doubleValue();


            totalLabel.setText("CurrentCost:$" + theTotalCost);

            updateTable();

        });
    }


    /**
     * This method is to add row for table
     *
     * @param id
     * @param name
     * @param totalCost
     *
     */
    public void addRowToTable(String id, String name, String totalCost) {
        Object[] row = {id, name, totalCost};
        model.addRow(row);
    }

    /**
     * This method is to update Table with new data.
     *
     *
     */
    public void updateTable() {
        // clear the rows
        model.setRowCount(0);
        //update the row back.

        myDoc.forEach((k, e) -> {

            addRowToTable(k, e.getDocumentName(), df.format(e.getTotalCost()));
        });

    }

    /**
     * This is the set total budget setter
     * @param budget
     */
    public void setTotalBudget(double budget) {
        totalBudget = budget;
        updateTotalBudgetLabel();
    }

    /**
     * This method is to update total budget
     */
    public void updateTotalBudgetLabel() {

        setBudgetField.setText("" + ProjectController.findProjectByID(theProjectID).getBudget());

    }
    /**
     * This method is to update total Cost.
     */
    public void updateTotalCost() {
        myDoc.forEach((k, b) -> {
            theTotalCost += b.getTotalCost().doubleValue();

        });

        // Round to 2 decimal places
        theTotalCost = Double.valueOf(df.format(theTotalCost));

        totalLabel.setText("CurrentCost:$" + String.format("%.2f", theTotalCost));

    }

    /**
     * DocumentCreationFormPopUp
     * @author Tin Phu
     *
     */
    public class DocumentCreationFormPopUp extends JPanel{
        public final JTextField documentNameField;
        public final JTextField documentDescriptionField;
        public final JTextField totalCostField;
        private String srcFileString = "";
        public final JDialog dialog = new JDialog();

        /**
         * @Author Tin Phu
         * @param theProjectID
         */
        public DocumentCreationFormPopUp(String theProjectID) {
            // Create components
            JLabel documentNameLabel = new JLabel("Document Name:");
            documentNameField = new JTextField(20);

            JLabel documentDescriptionLabel = new JLabel("Document Description:");
            documentDescriptionField = new JTextField(20);

            JLabel totalCostLabel = new JLabel("Total Cost:");
            totalCostField = new JTextField(10);

            JLabel fileSrcStringLabel = new JLabel("(option)");

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
            panel.add(documentDescriptionField, constraints);

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

            //
            /**
             * set fileSelectorButton Action Listener
             * @Author Tin Phu
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
             * Create button eventLisener
             * @Author Tin Phu
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
                    }else {


                        double totalCost = Double.parseDouble(totalCostField.getText());
                        DecimalFormat df = new DecimalFormat("#.##");
                        
                        // Round to 2 decimal places
                        totalCost = Double.valueOf(df.format(totalCost));

                        try {
                            if(srcFileString.isEmpty()){
                                newDoc = new Document(documentName,documentDescription,theProjectID,"", BigDecimal.valueOf(totalCost) );

                            }else {
                                newDoc = new Document(documentName,documentDescription,theProjectID,"", BigDecimal.valueOf(totalCost), srcFileString );

                            }
                            DocumentController.addDocument(newDoc);
                            myDoc.put(newDoc.id(), newDoc);
                            dialog.setVisible(false);

                            theTotalCost += myDoc.get(newDoc.id()).getTotalCost().doubleValue() ;

                            totalLabel.setText("CurrentCost:$" + theTotalCost);
                            updateTable();
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(BudgetTab.this, "Something went wrong! The File could not be coppied");
                        }
                    }

                }
            });
            dialog.setLocationRelativeTo(BudgetTab.this);
            dialog.setContentPane(panel);
            dialog.setTitle("Document Creation Form");
            dialog.setSize(500, 400);
            dialog.setLocationRelativeTo(null); // Center the dialog on the screen
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);

        }
    }

}






