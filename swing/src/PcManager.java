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
    private JButton changeCompsBtn;


    private String[] pcHeader = new String[]{"Id", "Name", "Slots", "Cooling", "Price"};
    private String[] compHeader = new String[]{"Id", "Name", "Heat", "Energy", "Price"};


    static private ComputerManager computerManager = Main.getComputerManager();
    static private ComponentManager componentManager = Main.getComponentManager();
    private int pcCounter = 0;

    public PcManager() {
        super();
        setContentPane(mainPanel);
        pack();

        refreshTables();


        pcTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                if (me.getClickCount() == 2) {
                    new CompManager((Long) pcTable.getModel().getValueAt(row, 0));
                }
            }
        });

        //region PCBtns
        ActionListener createPC = e -> {
            Computer pc = new Computer(Integer.parseInt(pcSlotsField.getText()));
            try {
                pc = computerManager.createComputer(pc);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            DefaultTableModel dtm = (DefaultTableModel) pcTable.getModel();
            dtm.addRow(new Object[]{pc.getId(), pc.getSlots(), pc.getCooling(), pc.getPrice()});
            pcCounter++;


        };
        ActionListener delPC = e -> {
            DefaultTableModel dtm = (DefaultTableModel) pcTable.getModel();
            int[] arr = pcTable.getSelectedRows();
            for (int i = 0; i < arr.length; i++) {
                dtm.removeRow(arr[i] - i);
                try {
                    computerManager.removeComputer(Long.parseLong((String) dtm.getValueAt(i, 0)));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                pcCounter--;
            }


        };
        ActionListener delAllPC = e -> {
            DefaultTableModel dm = (DefaultTableModel) pcTable.getModel();
            int rowCount = dm.getRowCount();

            for (int i = rowCount - 1; i >= 0; i--) {
                dm.removeRow(i);
            }
            try {
                computerManager.removeAllComputers();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            pcCounter = 0;
        };
        //endregion

        //region CompBtns
        ActionListener createComponent = e -> {
            Component component = new Component(componentNameField.getText()
                    , Integer.parseInt(componentHeatField.getText())
                    , Integer.parseInt(componentEnergyField.getText())
                    , Integer.parseInt(componentPriceField.getText()));
            try {
                component = componentManager.createComponent(component);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            DefaultTableModel dtm = (DefaultTableModel) compTable.getModel();
            dtm.addRow(new Object[]{component.getId()
                    , component.getName()
                    , component.getHeat()
                    , component.getEnergy()
                    , component.getPrice()});



        };
        ActionListener delComp = e -> {


            DefaultTableModel dtm = (DefaultTableModel) compTable.getModel();

            int[] arr = compTable.getSelectedRows();
            for (int i = 0; i < arr.length; i++) {

                try {
                    componentManager.removeComponentById(Long.parseLong((String) dtm.getValueAt(i, 0)));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                dtm.removeRow(arr[i] - i);

            }
        };

        ActionListener delAllComps = e -> {
            DefaultTableModel dm = (DefaultTableModel) compTable.getModel();
            int rowCount = dm.getRowCount();

            for (int i = rowCount - 1; i >= 0; i--) {
                dm.removeRow(i);
            }
            try {
                componentManager.removeAllComponents();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        };
        //endregion

        ActionListener changeComps = e -> {
            CompManager tmp = new CompManager(pcTable.getSelectedRow());
        };


        //region ListenersAdded

        changeCompsBtn.addActionListener(changeComps);
        deleteComponentButton.addActionListener(delComp);
        createComponentButton.addActionListener(createComponent);
        createComputerButton.addActionListener(createPC);
        deleteComputerButton.addActionListener(delPC);
        deleteAllComputersButton.addActionListener(delAllPC);
        deleteAllComponentsButton.addActionListener(delAllComps);
        //endregion


    }


    private void refreshTables() {
        try {
            DefaultTableModel dm = new DefaultTableModel();
            dm.setColumnIdentifiers(pcHeader);
            for (Computer pc : computerManager.getAllComputers()) {
                dm.addRow(new Object[]{pc.getId(), pc.getSlots(), pc.getCooling(), pc.getPrice()});
                pcCounter++;
            }
            pcTable.setModel(dm);


            dm = new DefaultTableModel();

            dm.setColumnIdentifiers(compHeader);
            for (configurator.component.Component comp : componentManager.getAllComponents()) {
                dm.addRow(new Object[]{comp.getId(), comp.getName(), comp.getHeat(), comp.getEnergy(), comp.getPrice()});
            }
            compTable.setModel(dm);
        } catch (Exception e) {

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
