package view;

import controller.DocumentController;
import model.Document;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * SearchResultDialog (demo)
 * which is shown up after the search button is clicked.
 * @author Tin Phu
 */
public class SearchResultDialog extends JDialog {
    private JLabel searchKeyLabel;
    private JPanel resultsPanel;

    /**
     * Storing result from the search.
     * demo: results = all document in json.
     */
    private Map<String, Document> results = new HashMap<String,Document>();

    /**
     * JDialog Set up
     * @param searchKey
     */

    public SearchResultDialog(String searchKey) {
        JLabel searchK= new JLabel("Search Key: " + searchKey);
        searchK.setFont(new Font(searchK.getFont().getName(), Font.BOLD, searchK.getFont().getSize() + 4));
        add(searchK);
        results = DocumentController.getAllDocs();


        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 300);

        // Create the search key label
        searchKeyLabel = new JLabel("Search Key: " + searchKey);
        searchKeyLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create the results panel and add the found results
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        //Adding each document that is found.
        for (Map.Entry<String, Document>k : results.entrySet() ) {
            Document currentDoc = results.get(k.getKey());
             JButton openButton = new JButton("Open");
             JButton readButton = new JButton("Read");
            readButton.addActionListener(e->{
                new DocumentTabRead(currentDoc);
            });
            openButton.addActionListener(e->{
                if(!currentDoc.openDoc()) {
                    JOptionPane.showMessageDialog(this, "This document does not have an attached file!");
                }
            });

            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
            String resultItem = String.format("%-50s %-12s $%.2f", currentDoc.getDocumentName(), currentDoc.getDate()+"|", currentDoc.getTotalCost());
            JLabel resultLabel = new JLabel( resultItem);
            itemPanel.add(resultLabel);
            itemPanel.add(openButton);
            itemPanel.add(readButton);
            resultsPanel.add(itemPanel);
        }

        // Create a scroll pane for the results panel
        JScrollPane scrollPane = new JScrollPane(resultsPanel);

        // Create a main panel and add the search key label and scroll pane
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(searchKeyLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setLocationRelativeTo(SearchResultDialog.this);
        setTitle("Searching For " + "\"" + searchKey + "\"");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(true);



    }
}
