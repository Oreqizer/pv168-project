package gui;

import configurator.component.Component;
import configurator.component.ComponentManager;
import configurator.computer.Computer;
import configurator.computer.ComputerManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Locale;
import java.util.ResourceBundle;
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
    private JLabel updateLabel;
    private JLabel compsInPcLabel;
    private JLabel freeCompsLabel;
    private static final ResourceBundle bundle = ResourceBundle.getBundle("languages", Locale.getDefault());

    private String[] compHeader = new String[]{"Id"
            , bundle.getString("label.name"), bundle.getString("label.price")
            , bundle.getString("label.heat"), bundle.getString("label.energy")};
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
        localizeUI();
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
                Component comp =
                        componentManager.getComponent((long) freeCompsTable.getModel()
                                .getValueAt(freeCompsTable.getSelectedRows()[i], 0));
                try {
                    if (pc.getComponents().size() < pc.getSlots()) {
                        logger.log(Level.FINE, "Adding component(id:" + comp.getId() + ") to computer (id:" + id + ")");

                        componentManager.addComponentToComputer(comp, pc.getId());

                        pc = computerManager.getComputer(pc.getId());

                    } else {
                        refreshUI();
                        JOptionPane.showMessageDialog(this, bundle.getString("notEnoughSlots"));

                        return;
                    }
                } catch (Exception e1) {
                    logger.log(Level.SEVERE, e1.toString(), e1);
                    JOptionPane.showMessageDialog(this, bundle.getString("exception.addComponentToPc"));
                }
            }
            refreshUI();
        });


        removeCompBtn.addActionListener(e -> {
            if (compsInPcTable.getSelectedRow() < 0) return;
            for (int i = 0; i < compsInPcTable.getSelectedRows().length; i++) {
                Component comp = componentManager
                        .getComponent((long) compsInPcTable.getModel()
                                .getValueAt(compsInPcTable.getSelectedRows()[i], 0));
                logger.log(Level.FINE, "Removing component(id:" + comp.getId() + ") from computer (id:" + id + ")");
                try {
                    componentManager.removeComponentFromComputer(comp, pc.getId());

                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(this, bundle.getString("exception.removeComponentFromPc"));
                    logger.log(Level.SEVERE, e1.toString(), e1);
                    e1.printStackTrace();
                }
                pc = computerManager.getComputer(pc.getId());
            }
            refreshUI();
        });

        //endregion
        setVisible(true);
    }

    private void localizeUI() {
        updateLabel.setText(bundle.getString("label.update.slots"));
        compsInPcLabel.setText(bundle.getString("label.table.compsInPc"));
        freeCompsLabel.setText(bundle.getString("label.table.freeComps"));
        doneBtn.setText(bundle.getString("button.done"));
        addCompBtn.setText(bundle.getString("button.addCompToPC"));
        removeCompBtn.setText(bundle.getString("button.removeCompFromPc"));
    }

    private void refreshUI() {

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
        updateNumberOfSlotsTextField.setText(pc.getSlots() + "");
        currPcStatsLabel.setText("PC: Id :" + pc.getId() + ", " + bundle.getString("label.price") + ": " + pc.getPrice() + ", "
                + bundle.getString("label.cooling") + ": " + pc.getCooling() + ", "
                + bundle.getString("label.energy") + ": " + pc.getEnergy() + ", "
                + bundle.getString("label.slots") + ": " + pc.getSlots() + ", "
                + bundle.getString("label.components") + ": " + pc.getComponents().size()
        );
        System.out.println("refreshTables2Ends");

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
                JOptionPane.showMessageDialog(this, bundle.getString("invalid.input.slots"));
                logger.log(Level.FINE, "ErrorMsg is " + bundle.getString("invalid.input.slots"));

                return;
            }
            pc = pc.setSlots(tmp);
            logger.log(Level.FINE, "Updating computer with id:" + pc.getId() + " and slots :" + tmp);
            computerManager.updateComputer(pc);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, bundle.getString("invalid.input"));
            logger.log(Level.SEVERE, ex.toString(), ex);
            ex.printStackTrace();
        }
    }
}
