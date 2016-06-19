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

        //region PCBtns
        ActionListener createPC = e -> {
            try {
                Computer pc = new Computer(Integer.parseInt(pcSlotsField.getText()));
                pc = computerManager.createComputer(pc);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            refreshUI();
            pcCounter++;


        };
        ActionListener delPC = e -> {
            if (pcTable.getSelectedRow() < 0) return;
            for (int i = 0; i < pcTable.getSelectedRows().length; i++) {

                try {
                    computerManager.removeComputer((long) pcTable.getModel().getValueAt(i, 0));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                pcCounter--;
            }
            refreshUI();

        };
        ActionListener delAllPC = e -> {
            try {
                computerManager.removeAllComputers();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            pcCounter = 0;
            refreshUI();
        };
        //endregion

        //region CompBtns
        ActionListener createComponent = e -> {

            try {
                Component component = new Component(componentNameField.getText()
                    , Integer.parseInt(componentHeatField.getText())
                    , Integer.parseInt(componentEnergyField.getText())
                    , Integer.parseInt(componentPriceField.getText()));
                component = componentManager.createComponent(component);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            refreshUI();
        };

        ActionListener delComp = e -> {
            if (compTable.getSelectedRow() < 0) return;
            int[] arr = compTable.getSelectedRows();
            for (int i = 0; i < arr.length; i++) {
                try {
                    componentManager.removeComponentById((long) compTable.getModel().getValueAt(i, 0));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            refreshUI();
        };

        ActionListener delAllComps = e -> {
            try {
                componentManager.removeAllComponents();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            refreshUI();
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


            System.out.println("tableRefreshEnds");
        } catch (Exception e) {
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
