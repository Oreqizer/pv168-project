import configurator.component.Component;
import configurator.component.ComponentManager;
import configurator.computer.Computer;
import configurator.computer.ComputerManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;

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

    public CompManager(long id) {
        super();
        setModal(true);
        setContentPane(mainPane);
        pack();


        pc = computerManager.getComputer(id);

        updateNumberOfSlotsTextField.setText(pc.getSlots() + "");

        //region BtnListeners
        ActionListener done = e -> dispose();

        ActionListener updateSlots = e -> {
            updateSlots();
            refreshUI();
        };

        ActionListener add = e -> {
            if (freeCompsTable.getSelectedRow() < 0) return;

            for (int i = 0; i < freeCompsTable.getSelectedRows().length; i++) {
                try {
                    if (pc.getComponents().size() < pc.getSlots()) {

                        Component comp = componentManager
                                .getComponent((long) freeCompsTable.getModel()
                                        .getValueAt(i, 0));

                        componentManager.addComponentToComputer(comp, pc.getId());

                        pc = computerManager.getComputer(pc.getId());

                        refreshUI();
                    } else return;
                } catch (Exception e1) {
                    errorMsg.setText(e1.toString());

                }

            }

        };

        ActionListener remove = e -> {
            if (compsInPcTable.getSelectedRow() < 0) return;
            for (int i = 0; i < compsInPcTable.getSelectedRows().length; i++) {
                Component comp = componentManager
                        .getComponent((long) compsInPcTable.getModel()
                                .getValueAt(i, 0));
                try {
                    componentManager.removeComponentFromComputer(comp, pc.getId());
                    refreshUI();
                } catch (Exception e1) {
                    errorMsg.setText(e1.toString());
                    e1.printStackTrace();
                }
                pc = computerManager.getComputer(pc.getId());
            }


        };

        removeCompBtn.addActionListener(remove);
        addCompBtn.addActionListener(add);
        doneBtn.addActionListener(done);
        updateComputerButton.addActionListener(updateSlots);
        //endregion

        refreshUI();
        setVisible(true);
    }

    private void refreshUI() {
        try {
            System.out.println("refreshTables2");
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
            errorMsg.setText("");
            System.out.println("refreshTables2Ends");
        } catch (Exception e) {
            errorMsg.setText(e.toString());
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
                return;
            }
            pc = pc.setSlots(tmp);
            computerManager.updateComputer(pc);
        } catch (Exception ex) {
            errorMsg.setText(ex.toString());
            ex.printStackTrace();
        }
    }
}
