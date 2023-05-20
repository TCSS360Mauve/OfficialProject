package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DocumentTab extends JPanel {
    private List<String> items;
    private DefaultListModel<String> listModel;
    private JList<String> itemList;
    public DocumentTab(){
        items = new ArrayList<>();
        listModel = new DefaultListModel<>();
        itemList = new JList<>(listModel);

        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(itemList);
        add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("Add Document");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newItem = JOptionPane.showInputDialog(DocumentTab.this, "Enter Document name:");
                if (newItem != null && !newItem.isEmpty()) {
                    items.add(newItem);
                    listModel.addElement(newItem);
                }
            }
        });
        add(addButton, BorderLayout.SOUTH);
    }
}
