import configurator.component.Component;
import configurator.component.ComponentManager;
import configurator.computer.Computer;
import configurator.computer.ComputerManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private JButton updatePC;


    private String[] pcHeader = new String[]{"Id", "Slots", "Cooling", "Price", "Energy", "Components"};
    private String[] compHeader = new String[]{"Id", "Name", "Heat", "Energy", "Price"};


    static private ComputerManager computerManager = Main.getComputerManager();
    static private ComponentManager componentManager = Main.getComponentManager();
    private int pcCounter = 0;

    public PcManager() {
        super();
        setContentPane(mainPanel);
        pack();

        refreshUI();


        pcTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                if (me.getClickCount() == 2) {
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
                    System.out.println((long) compTable.getModel().getValueAt(row, 0));
                    new UpdateComponentDialog((long) compTable.getModel().getValueAt(row, 0));
                    refreshUI();
                }

            }
        });

        //region PCBtns
        ActionListener createPC = e -> {
            try {
                int tmp = Integer.parseInt(pcSlotsField.getText());
                if (tmp <= 0) {
                    errorMsg.setText("Number of slots cannot be negative or 0!");
                    return;
                }

                Computer pc = new Computer(tmp);
                pc = computerManager.createComputer(pc);
                refreshUI();
            } catch (Exception ex) {
                errorMsg.setText(ex.toString());
                ex.printStackTrace();
            }


            pcCounter++;


        };
        ActionListener delPC = e -> {
            if (pcTable.getSelectedRow() < 0) return;
            try {
            for (int i = 0; i < pcTable.getSelectedRows().length; i++) {


                computerManager.removeComputer((long) pcTable.getModel().getValueAt(i, 0));
                refreshUI();
            }

            } catch (Exception e1) {
                errorMsg.setText(e1.toString());
                e1.printStackTrace();
            }
            pcCounter--;



        };
        ActionListener delAllPC = e -> {
            try {
                computerManager.removeAllComputers();
            } catch (Exception e1) {
                errorMsg.setText(e1.toString());
                e1.printStackTrace();
            }
            pcCounter = 0;
            refreshUI();
        };
        //endregion

        //region CompBtns
        ActionListener createComponent = e -> {

            try {
                int tmp = Integer.parseInt(componentPriceField.getText());
                if (tmp < 0) {
                    errorMsg.setText("Price cannot be negative number!");
                    return;
                }
                Component component = new Component(componentNameField.getText()
                    , Integer.parseInt(componentHeatField.getText())
                    , Integer.parseInt(componentEnergyField.getText())
                        , tmp);
                component = componentManager.createComponent(component);
                refreshUI();
            } catch (Exception e1) {
                errorMsg.setText(e1.toString());
                e1.printStackTrace();
            }


        };

        ActionListener delComp = e -> {
            if (compTable.getSelectedRow() < 0) return;
            int[] arr = compTable.getSelectedRows();
            for (int i = 0; i < arr.length; i++) {
                try {
                    componentManager.removeComponentById((long) compTable.getModel().getValueAt(i, 0));
                    refreshUI();
                } catch (Exception e1) {
                    errorMsg.setText(e1.toString());
                    e1.printStackTrace();
                }
            }

        };

        ActionListener delAllComps = e -> {
            try {
                componentManager.removeAllComponents();
                refreshUI();
            } catch (Exception e1) {
                errorMsg.setText(e1.toString());
                e1.printStackTrace();
            }

        };
        //endregion


        //region ListenersAdded

        deleteComponentButton.addActionListener(delComp);
        createComponentButton.addActionListener(createComponent);
        createComputerButton.addActionListener(createPC);
        deleteComputerButton.addActionListener(delPC);
        deleteAllComputersButton.addActionListener(delAllPC);
        deleteAllComponentsButton.addActionListener(delAllComps);
        //endregion


    }


    private void refreshUI() {
        try {
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
                pcCounter++;
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
