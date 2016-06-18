import configurator.computer.Computer;
import configurator.computer.ComputerManager;
import configurator.computer.ComputerManagerImpl;

import javax.sql.DataSource;
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


    private ComputerManager computerManager;
    private int pcCounter = 0;

    public PcManager() {
        super();
        DataSource dataSource = configurator.Main.createMemoryDatabase();
        computerManager = new ComputerManagerImpl(dataSource);
        setContentPane(mainPanel);
        pack();


        pcTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                if (me.getClickCount() == 2) {

                }
            }
        });

        //region PCBtns
        ActionListener createPC = e -> {
            Computer pc = new Computer(Integer.parseInt(pcSlotsField.getText()));
            try {
                computerManager.createComputer(pc);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            DefaultTableModel dtm = (DefaultTableModel) pcTable.getModel();
            dtm.addRow(new Object[]{"pc" + pcCounter, pcSlotsField.getText(), 0, 0});
            pcCounter++;


        };
        ActionListener delPC = e -> {
            DefaultTableModel dtm = (DefaultTableModel) pcTable.getModel();
            int[] arr = pcTable.getSelectedRows();
            for (int i = 0; i < arr.length; i++) {
                dtm.removeRow(arr[i] - i);
                pcCounter--;
            }


        };
        ActionListener delAllPC = e -> {
            DefaultTableModel dm = (DefaultTableModel) pcTable.getModel();
            int rowCount = dm.getRowCount();

            for (int i = rowCount - 1; i >= 0; i--) {
                dm.removeRow(i);
            }
            pcCounter = 0;
        };
        //endregion

        //region CompBtns
        ActionListener createComponent = e -> {
            DefaultTableModel dtm = (DefaultTableModel) compTable.getModel();
            dtm.addRow(new Object[]{componentNameField.getText(), componentHeatField.getText()
                    , componentEnergyField.getText(), componentPriceField.getText()});


        };
        ActionListener delComp = e -> {
            DefaultTableModel dtm = (DefaultTableModel) compTable.getModel();
            int[] arr = compTable.getSelectedRows();
            for (int i = 0; i < arr.length; i++) {
                dtm.removeRow(arr[i] - i);
            }
        };

        ActionListener delAllComps = e -> {
            DefaultTableModel dm = (DefaultTableModel) compTable.getModel();
            int rowCount = dm.getRowCount();

            for (int i = rowCount - 1; i >= 0; i--) {
                dm.removeRow(i);
            }
        };
        //endregion


        //region ListenersAdded
//        changeCompsBtn.addActionListener(changeComps);
        deleteComponentButton.addActionListener(delComp);
        createComponentButton.addActionListener(createComponent);
        createComputerButton.addActionListener(createPC);
        deleteComputerButton.addActionListener(delPC);
        deleteAllComputersButton.addActionListener(delAllPC);
        deleteAllComponentsButton.addActionListener(delAllComps);
        //endregion


    }

    private void createUIComponents() {
        DefaultTableModel dm = new DefaultTableModel();
        String header[] = new String[]{"Name", "Slots", "Cooling", "Price"};
        dm.setColumnIdentifiers(header);
        pcTable = new JTable(dm);
        pcTable.setDefaultEditor(Object.class, null);


        dm = new DefaultTableModel();
        header = new String[]{"Name", "Heat", "Energy", "Price"};
        dm.setColumnIdentifiers(header);
        compTable = new JTable(dm);
        compTable.setDefaultEditor(Object.class, null);


    }


    private class ComponentPane extends JPanel{

        private String name;
        private int price;
        private int heat ;
        private int energy;

        @Override
        public String getName() {
            return name;
        }

        public ComponentPane(String name, int price, int heat , int energy) {

            super();

            setLayout(new FlowLayout());
            this.name=name;
            this.price=price;
            this.heat=heat;
            this.energy=energy;

            int x=120;
            JLabel nameLbl = new JLabel("Name:", JLabel.LEFT);
            JTextField nameField =new JTextField(name);
            nameField.setPreferredSize(new Dimension(x,20));


            JLabel priceLbl = new JLabel("Price:", JLabel.LEFT);
            JTextField priceField =new JTextField(Integer.toString(price));
            priceField.setPreferredSize(new Dimension(x,20));



            JLabel heatLbl = new JLabel("Heat:", JLabel.LEFT);
            JTextField heatField =new JTextField(Integer.toString(heat));
            heatField.setPreferredSize(new Dimension(x,20));

            JLabel energyLbl = new JLabel("Energy:", JLabel.LEFT);
            JTextField energyField =new JTextField(Integer.toString(energy));
            energyField.setPreferredSize(new Dimension(x,20));



            JButton updateBtn = new JButton("Update!");
            JButton addComponentToPc = new JButton("Add component to PC");


            ActionListener addCompToPc = e -> {


            };




            add(nameLbl);
            add(nameField);


            add(priceLbl);
            add(priceField);

            add(heatLbl);
            add(heatField);

            add(energyLbl);
            add(energyField);
            addComponentToPc.addActionListener(addCompToPc);
            add(updateBtn);
            add(addComponentToPc);

            pack();

    }

}

    private class PCPane extends JPanel{


        private int price;
        private int cooling ;
        private int slots;
        private int freeSlots;


        public JTabbedPane getComponentTabPane() {
            return componentTabPane;
        }

        private JTabbedPane componentTabPane;



        public PCPane( int slots) {
            super();


            setLayout(new FlowLayout());

            freeSlots=slots;
            this.slots=slots;

            JLabel priceLbl = new JLabel("Price: "+cooling, JLabel.LEFT);
            JLabel coolLbl = new JLabel("Cooling: "+cooling, JLabel.LEFT);
            JLabel slotsLbl = new JLabel("Slots: "+slots+ "  Free Slots : "+freeSlots, JLabel.LEFT);

            JButton removeCompFromPc = new JButton("Remove Component");
            ActionListener remComp = e -> {

            };

            componentTabPane = new JTabbedPane();

            //componentTabPane.setLayout(new FlowLayout());


            add(priceLbl);
            add(coolLbl);
            add(slotsLbl);

            add(removeCompFromPc);
            removeCompFromPc.addActionListener(remComp);

            add(componentTabPane);
            pack();






        }



    }




}
