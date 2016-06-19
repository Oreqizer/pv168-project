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

        //region BtnListeners
        ActionListener done = e -> dispose();

        ActionListener add = e -> {
            if (freeCompsTable.getSelectedRow() < 0) return;
            if (pc.getComponents().size() < pc.getSlots()) {
                try {
                    Component comp = componentManager
                            .getComponent((long) freeCompsTable.getModel()
                                    .getValueAt(freeCompsTable.getSelectedRow(), 0));

                    componentManager.addComponentToComputer(comp, pc.getId());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                pc = computerManager.getComputer(pc.getId());

                refreshUI();
            }

        };

        ActionListener remove = e -> {
            if (compsInPcTable.getSelectedRow() < 0) return;
            Component comp = componentManager
                    .getComponent((long) compsInPcTable.getModel()
                            .getValueAt(compsInPcTable.getSelectedRow(), 0));
            try {
                componentManager.removeComponentFromComputer(comp, pc.getId());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            pc = computerManager.getComputer(pc.getId());

            refreshUI();
        };

        removeCompBtn.addActionListener(remove);
        addCompBtn.addActionListener(add);
        doneBtn.addActionListener(done);
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
            System.out.println("refreshTables2Ends");
        } catch (Exception e) {
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
}
