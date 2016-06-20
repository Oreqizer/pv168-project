import configurator.component.Component;
import configurator.component.ComponentManager;

import javax.swing.*;
import java.awt.event.ActionListener;

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
    private JLabel errorMsg;


    private ComponentManager componentManager = Main.getComponentManager();
    private Component component;

    public UpdateComponentDialog(long id) {
        super();
        setModal(true);
        setContentPane(mainPane);
        pack();
        component = componentManager.getComponent(id);
        nameTextField.setText(component.getName());
        priceTextField.setText(component.getPrice() + "");
        heatTextField.setText(component.getHeat() + "");
        energyTextField.setText(component.getEnergy() + "");

        ActionListener update = e -> {

            try {
                int price = Integer.parseInt(priceTextField.getText());
                if (price < 0) {
                    errorMsg.setText("Price cannot be negative number!");
                    return;
                }
                componentManager.updateComponent(component
                        .setEnergy(Integer.parseInt(energyTextField.getText()))
                        .setHeat(Integer.parseInt(heatTextField.getText()))
                        .setPrice(price)
                        .setName(nameTextField.getText())
                );

                dispose();
            } catch (Exception e1) {
                errorMsg.setText(e1.toString());
                e1.printStackTrace();
            }
        };


        updateBtn.addActionListener(update);
        setVisible(true);
    }
}
