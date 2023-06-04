package view;

import controller.AppInfoController;
import controller.DocumentController;
import controller.ProjectController;
import controller.UserController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The main frame of the FileNtro program.
 * @author Tin Phu
 * @author Riley Bennett
 * @version 0.3
 */
public class AppUI {
    /** Constant for the default width of the frame.*/
    private static final int WIDTH = 400;

    /** Constant for the default height of the frame.*/
    private static final int HEIGHT = 300;

    /** Constant for the scale of the frame.*/
    private static final int SCALE = 3;

    /** Menu item for file for when the user is logged in.*/
    private static final JMenu fileMenu = new JMenu("File");
    private JFrame frame;
    /**
     * Panel containing every JPanel of the app.
     */
    private JPanel cardPanel;
    /**
     * The overall cardlayout of the app.
     */
    private CardLayout cardLayout;

    /**
     * Creates the JFrame() of the app.
     * @author Tin Phu
     * @author Riley Bennett
     */
    public void createAndShowGUI() {


        // Create the frame
        frame = new JFrame("FileNtro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a label with "Hello World" text
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);
        // Set the size of the frame to medium
        frame.setSize(SCALE * WIDTH, SCALE * HEIGHT);
        //Setup cardLayout and carPanel.
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel mainPanel = new LogInScreen(cardPanel, cardLayout);
        cardPanel.add(mainPanel, "LogInScreen");

        frame.add(cardPanel);

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        
        //Adding Import Menu Item
        JMenuItem importMenuItem = new JMenuItem("Import");
        JMenuItem exportMenuItem = new JMenuItem("Export");
        importMenuItem.setAccelerator(KeyStroke.getKeyStroke("control I"));
        exportMenuItem.setAccelerator(KeyStroke.getKeyStroke("control E"));

        // Action listener for import item
        importMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(frame, "While importing data," 
                + "you will be logged out to Project Screen.\nContinue?","Warning", JOptionPane.YES_NO_OPTION);

                // Check if user confirmed
                if (confirm == JOptionPane.YES_OPTION) {
                    AppInfoController.importData();
                    UserController.importData();
                    DocumentController.importData();
                    ProjectController.importData();
                    if(AppInfoController.getCurrentUser() == null){

                        cardLayout.show(cardPanel, "LogInScreen");
                    } else {
                        cardPanel.add(new HomeScreen(AppInfoController.getCurrentUser(), cardPanel, cardLayout), "HomeScreen");
//                        System.out.println(AppInfoController.getCurrentUser().getId());
                        cardLayout.show(cardPanel, "HomeScreen");
                    }
                    frame.dispose();
                    frame.setVisible(true);


                }
            }
        });

        fileMenu.add(importMenuItem);

        // Action listener for export item
        exportMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AppInfoController.exportData();
                UserController.exportData();
                DocumentController.exportData();
                ProjectController.exportData();
                JOptionPane.showMessageDialog(frame, "Check program folder for exported JSON files.");
            }
        });

        fileMenu.add(exportMenuItem);

        // Create the "About" menu
        JMenu aboutMenu = new JMenu("About");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                cardPanel.add(new AboutScreen(AppInfoController.getCurrentUser(), cardPanel, cardLayout ), "AboutScreen");
                cardLayout.show(cardPanel, "AboutScreen");
            }
        });
        aboutMenu.add(aboutMenuItem);
        
        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);
        toggleFileMenu(false);

        // Set the menu bar to the frame
        frame.setJMenuBar(menuBar);

        //frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        // Set the frame to full screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Pack and center the frame
        //frame.pack();
        frame.setLocationRelativeTo(null);
        //set logo
        String currentPath = System.getProperty("user.dir");

        ImageIcon icon = new ImageIcon(currentPath + "/logo.png");
        frame.setIconImage(icon.getImage());

        // Show the frame
        frame.setVisible(true);

        if (AppInfoController.getCurrentUser() != null) {
            cardPanel.add(new HomeScreen(AppInfoController.getCurrentUser(), cardPanel, cardLayout), "HomeScreen");
//            System.out.println(AppInfoController.getCurrentUser().getId());
            cardLayout.show(cardPanel, "HomeScreen");
            toggleFileMenu(true);
        }
    }

    public static void toggleFileMenu(final boolean toggle) {
        fileMenu.setVisible(toggle);
        fileMenu.setEnabled(toggle);
    }

    public static void main(String[] args) {
        // Run the GUI code in the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            AppUI app = new AppUI();
            app.createAndShowGUI();
        });
    }
}