package gui;

import configurator.component.Component;
import configurator.component.ComponentManager;
import configurator.computer.Computer;
import configurator.computer.ComputerManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zeman on 18-Jun-16.
 */
public class CompManager extends JDialog {
    private JTable compsInPcTable;
    private JTable freeCompsTable;
    private JButton addCompBtn;
    private JButton removeCompBtn;
    private JButton doneBtn;
    private JPanel mainPane;
    private JLabel currPcStatsLabel;
    private JTextField updateNumberOfSlotsTextField;
    private JButton updateComputerButton;
    private JLabel errorMsg;

    private String[] compHeader = new String[]{"Id", "Name", "Heat", "Energy", "Price"};
    private ComputerManager computerManager = Main.getComputerManager();
    private ComponentManager componentManager = Main.getComponentManager();

    private Computer pc;
    private static final Logger logger = Logger.getLogger(CompManager.class.getName());


    public CompManager(long id) {
        super();
        logger.log(Level.FINE, "Starting editing window for computer with id: " + id);
        setModal(true);
        setResizable(false);
        setContentPane(mainPane);
        pack();
        pc = computerManager.getComputer(id);
        refreshUI();



        //region BtnListeners

        doneBtn.addActionListener(e -> {
            logger.log(Level.FINE, "Disposing editing window for computer with id: " + id);
            dispose();
        });

        updateComputerButton.addActionListener(e -> {

            updateSlots();
            refreshUI();
        });

        addCompBtn.addActionListener(e -> {
            if (freeCompsTable.getSelectedRow() < 0) return;

            for (int i = 0; i < freeCompsTable.getSelectedRows().length; i++) {
                Component comp = componentManager
                        .getComponent((long) freeCompsTable.getModel().getValueAt(i, 0));
                try {
                    if (pc.getComponents().size() < pc.getSlots()) {
                        logger.log(Level.FINE, "Adding component(id:" + comp.getId() + ") to computer (id:" + id + ")");


                        componentManager.addComponentToComputer(comp, pc.getId());

                        pc = computerManager.getComputer(pc.getId());

                        refreshUI();
                    } else return;
                } catch (Exception e1) {
                    logger.log(Level.SEVERE, e1.toString(), e1);
                    errorMsg.setText(e1.toString());
                }
            }
        });


        removeCompBtn.addActionListener(e -> {
            if (compsInPcTable.getSelectedRow() < 0) return;
            for (int i = 0; i < compsInPcTable.getSelectedRows().length; i++) {
                Component comp = componentManager
                        .getComponent((long) compsInPcTable.getModel()
                                .getValueAt(i, 0));
                logger.log(Level.FINE, "Removing component(id:" + comp.getId() + ") from computer (id:" + id + ")");
                try {
                    componentManager.removeComponentFromComputer(comp, pc.getId());
                    refreshUI();
                } catch (Exception e1) {
                    errorMsg.setText(e1.toString());
                    logger.log(Level.SEVERE, e1.toString(), e1);
                    e1.printStackTrace();
                }
                pc = computerManager.getComputer(pc.getId());
            }
        });

        //endregion
        setVisible(true);
    }

    private void refreshUI() {
        try {
            System.out.println("refreshTables2");
            logger.log(Level.FINE, "Refreshing UI");
            DefaultTableModel dm = new DefaultTableModel();
            dm.setColumnIdentifiers(compHeader);
            for (configurator.component.Component comp : componentManager.getAllComponents()) {
                if (comp.getPid() != null && comp.getPid().equals(pc.getId())) {
                    dm.addRow(new Object[]{comp.getId(), comp.getName(), comp.getHeat(), comp.getEnergy(), comp.getPrice()});
                }
            }

            compsInPcTable.setModel(dm);



            dm = new DefaultTableModel();

            dm.setColumnIdentifiers(compHeader);
            for (configurator.component.Component comp : componentManager.getAllFreeComponents()) {
                dm.addRow(new Object[]{comp.getId(), comp.getName(), comp.getHeat(), comp.getEnergy(), comp.getPrice()});
            }
            freeCompsTable.setModel(dm);
            currPcStatsLabel.setText(pc.toString());
            updateNumberOfSlotsTextField.setText(pc.getSlots() + "");
            errorMsg.setText("");
            System.out.println("refreshTables2Ends");
        } catch (Exception e) {
            errorMsg.setText(e.toString());
            logger.log(Level.SEVERE, e.toString(), e);
            e.printStackTrace();
        }
    }

    private void createUIComponents() {
        DefaultTableModel dm = new DefaultTableModel();
        dm.setColumnIdentifiers(compHeader);
        compsInPcTable = new JTable(dm);
        compsInPcTable.setDefaultEditor(Object.class, null);


        dm = new DefaultTableModel();
        dm.setColumnIdentifiers(compHeader);
        freeCompsTable = new JTable(dm);
        freeCompsTable.setDefaultEditor(Object.class, null);
    }


    private void updateSlots() {
        try {
            int tmp = Integer.parseInt(updateNumberOfSlotsTextField.getText());
            if (tmp <= 0) {
                errorMsg.setText("Number of slots cannot be negative or 0!");
                logger.log(Level.FINE, "ErrorMsg is :Number of slots cannot be negative or 0!");

                return;
            }
            pc = pc.setSlots(tmp);
            logger.log(Level.FINE, "Updating computer with id:" + pc.getId() + " and slots :" + tmp);
            computerManager.updateComputer(pc);
        } catch (Exception ex) {
            errorMsg.setText(ex.toString());
            logger.log(Level.SEVERE, ex.toString(), ex);
            ex.printStackTrace();
        }
    }
}
