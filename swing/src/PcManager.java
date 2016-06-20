import configurator.component.Component;
import configurator.component.ComponentManager;
import configurator.computer.Computer;
import configurator.computer.ComputerManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zeman on 27-Apr-16.
 */
public class PcManager extends JFrame {
    private JPanel mainPanel;
    private JButton createComputerButton;
    private JTextField pcSlotsField;
    private JButton deleteComputerButton;
    private JButton createComponentButton;
    private JButton deleteComponentButton;
    private JTextField componentNameField;
    private JTextField componentPriceField;
    private JTextField componentHeatField;
    private JTextField componentEnergyField;
    private JPanel formPanel;
    private JPanel availableComponentPanel;
    private JPanel computerPanel;
    private JButton deleteAllComputersButton;
    private JButton deleteAllComponentsButton;
    private JTable compTable;
    private JTable pcTable;
    private JLabel errorMsg;



    private String[] pcHeader = new String[]{"Id", "Slots", "Cooling", "Price", "Energy", "Components"};
    private String[] compHeader = new String[]{"Id", "Name", "Heat", "Energy", "Price"};

    private static final Logger logger = Logger.getLogger(PcManager.class.getName());

    static private ComputerManager computerManager = Main.getComputerManager();
    static private ComponentManager componentManager = Main.getComponentManager();


    public PcManager() {
        super();
        logger.log(Level.FINE, "Starting MainWindow");
        setContentPane(mainPanel);
        pack();
        setResizable(false);
        refreshUI();


        //region tableClickEvents
        pcTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                if (me.getClickCount() == 2) {
                    logger.log(Level.FINE, "Editing Computer and its components of " +
                            pcTable.getModel().getValueAt(row, 0));

                    new CompManager((long) pcTable.getModel().getValueAt(row, 0));
                    refreshUI();
                }

            }
        });


        compTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                if (me.getClickCount() == 2) {
                    logger.log(Level.FINE, "Editing values of component of " +
                            compTable.getModel().getValueAt(row, 0));

                    new UpdateComponentDialog((long) compTable.getModel().getValueAt(row, 0));
                    refreshUI();
                }
            }
        });
        //endregion

        //region PCBtns

        createComputerButton.addActionListener(e -> {
            try {
                int tmp = Integer.parseInt(pcSlotsField.getText());
                if (tmp <= 0) {
                    logger.log(Level.FINE, "errorMsg is Number of slots cannot be negative or 0!");
                    errorMsg.setText("Number of slots cannot be negative or 0!");
                    return;
                }

                Computer pc = new Computer(tmp);

                logger.log(Level.FINE, "Creating new computer :" + pc);
                pc = computerManager.createComputer(pc);

                refreshUI();
            } catch (Exception ex) {
                errorMsg.setText(ex.toString());
                logger.log(Level.SEVERE, ex.toString(), ex);
                ex.printStackTrace();
            }

        });

        deleteComputerButton.addActionListener(e -> {
            if (pcTable.getSelectedRow() < 0) return;
            try {
                for (int i = 0; i < pcTable.getSelectedRows().length; i++) {
                    logger.log(Level.FINE, "deleting component with id:" + pcTable.getModel().getValueAt(i, 0));
                    computerManager.removeComputer((long) pcTable.getModel().getValueAt(i, 0));
                    refreshUI();
                }
            } catch (Exception e1) {
                errorMsg.setText(e1.toString());
                logger.log(Level.SEVERE, e1.toString(), e1);
                e1.printStackTrace();
            }
        });


        deleteAllComputersButton.addActionListener(e -> {
            try {
                logger.log(Level.FINE, "deleting all components from database");
                computerManager.removeAllComputers();
            } catch (Exception e1) {
                errorMsg.setText(e1.toString());
                logger.log(Level.SEVERE, e1.toString(), e1);
                e1.printStackTrace();
            }
            refreshUI();
        });

        //endregion

        //region CompBtns

        createComponentButton.addActionListener(e -> {
            try {
                int tmp = Integer.parseInt(componentPriceField.getText());
                if (tmp < 0) {
                    logger.log(Level.FINE, "ErrorMsg is Price cannot be negative number!");

                    errorMsg.setText("Price cannot be negative number!");

                    return;
                }
                Component component = new Component(componentNameField.getText()
                        , Integer.parseInt(componentHeatField.getText())
                        , Integer.parseInt(componentEnergyField.getText())
                        , tmp);
                logger.log(Level.FINE, "Creating new Component:" + component);

                component = componentManager.createComponent(component);

                refreshUI();
            } catch (Exception e1) {
                errorMsg.setText(e1.toString());
                logger.log(Level.SEVERE, e1.toString(), e1);
                e1.printStackTrace();
            }
        });

        deleteComponentButton.addActionListener(e -> {
            if (compTable.getSelectedRow() < 0) return;
            int[] arr = compTable.getSelectedRows();
            for (int i = 0; i < arr.length; i++) {
                try {
                    logger.log(Level.FINE, "Deleting component with id:" + compTable.getModel().getValueAt(i, 0));

                    componentManager.removeComponentById((long) compTable.getModel().getValueAt(i, 0));
                    refreshUI();
                } catch (Exception e1) {
                    errorMsg.setText(e1.toString());
                    logger.log(Level.SEVERE, e1.toString(), e1);
                    e1.printStackTrace();
                }
            }

        });

        deleteAllComponentsButton.addActionListener(e -> {
            try {
                logger.log(Level.FINE, "Deleting all components");

                componentManager.removeAllComponents();
                refreshUI();
            } catch (Exception e1) {
                errorMsg.setText(e1.toString());
                logger.log(Level.SEVERE, e1.toString(), e1);
                e1.printStackTrace();
            }

        });
        //endregion
        //TODO vlakna, lokalizaciu, logging
    }


    private void refreshUI() {
        try {
            logger.log(Level.FINE, "Refreshing UI");

            System.out.println("tableRefresh");
            DefaultTableModel dm = new DefaultTableModel();
            dm.setColumnIdentifiers(pcHeader);
            for (Computer pc : computerManager.getAllComputers()) {

                dm.addRow(new Object[]{pc.getId()
                        , pc.getSlots()
                        , pc.getCooling()
                        , pc.getPrice()
                        , pc.getEnergy()
                        , pc.getComponents().size()});

            }

            pcTable.setModel(dm);
            dm.fireTableDataChanged();

            dm = new DefaultTableModel();

            dm.setColumnIdentifiers(compHeader);
            for (configurator.component.Component comp : componentManager.getAllFreeComponents()) {
                dm.addRow(new Object[]{comp.getId()
                        , comp.getName()
                        , comp.getHeat()
                        , comp.getEnergy()
                        , comp.getPrice()});
            }

            compTable.setModel(dm);
            dm.fireTableDataChanged();

            errorMsg.setText("");
            System.out.println("tableRefreshEnds");
        } catch (Exception e) {
            errorMsg.setText(e.toString());
            e.printStackTrace();
        }
    }

    private void createUIComponents() {
        DefaultTableModel dm = new DefaultTableModel();
        dm.setColumnIdentifiers(pcHeader);
        pcTable = new JTable(dm);
        pcTable.setDefaultEditor(Object.class, null);


        dm = new DefaultTableModel();

        dm.setColumnIdentifiers(compHeader);
        compTable = new JTable(dm);
        compTable.setDefaultEditor(Object.class, null);



    }


}
