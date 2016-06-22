package gui;

import configurator.component.Component;
import configurator.component.ComponentManager;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zeman on 20-Jun-16.
 */
public class UpdateComponentDialog extends JDialog {
    private JButton updateBtn;
    private JPanel mainPane;
    private JTextField nameTextField;
    private JTextField priceTextField;
    private JTextField heatTextField;
    private JTextField energyTextField;


    private static final Logger logger = Logger.getLogger(UpdateComponentDialog.class.getName());


    private static final ResourceBundle bundle = ResourceBundle.getBundle("languages", Locale.getDefault());

    private ComponentManager componentManager = Main.getComponentManager();
    private Component component;

    public UpdateComponentDialog(long id) {
        super();
        logger.log(Level.FINE, "Starting editing window for component with id: " + id);
        setModal(true);
        setContentPane(mainPane);
        pack();
        setResizable(false);
        component = componentManager.getComponent(id);
        setupUI();
        setVisible(true);
    }

    private void setupUI() {
        nameTextField.setText(component.getName());
        priceTextField.setText(component.getPrice() + "");
        heatTextField.setText(component.getHeat() + "");
        energyTextField.setText(component.getEnergy() + "");
        updateBtn.setText(bundle.getString("button.update"));


        updateBtn.addActionListener(e -> {

            try {
                BigDecimal price = Main.parseBigDecimal(priceTextField.getText());

                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    logger.log(Level.FINE, "Error Msg is : " + bundle.getString("invalid.input.price"));
                    JOptionPane.showMessageDialog(this, bundle.getString("invalid.input.price"));

                    return;
                }
                logger.log(Level.FINE, "Updating component");
                componentManager.updateComponent(component
                        .setEnergy(Integer.parseInt(energyTextField.getText()))
                        .setHeat(Integer.parseInt(heatTextField.getText()))
                        .setPrice(price)
                        .setName(nameTextField.getText())
                );


                dispose();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, bundle.getString("invalid.input") + " :" + e1.getLocalizedMessage());
                logger.log(Level.SEVERE, e1.toString(), e1);
                e1.printStackTrace();
            }
        });
    }
}
