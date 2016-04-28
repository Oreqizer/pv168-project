import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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
    private JTabbedPane computerTabbedPane;
    private JTabbedPane componentTabbedPane;
    private JPanel formPanel;
    private JPanel availableComponentPanel;
    private JPanel computerPanel;
    private JButton deleteAllComputersButton;
    private JButton deleteAllComponentsButton;


    private int pcCounter = 0;
    public PcManager() {
        super();

        setContentPane(mainPanel);
        pack();

        componentTabbedPane.removeAll();



        ActionListener createPC = e -> {

            pcCounter++;
            computerTabbedPane.addTab("PC num. "+pcCounter,new PCPane(
                    Integer.parseInt(pcSlotsField.getText())

            ));
        };
        ActionListener createComponent = e -> {



            componentTabbedPane.addTab(componentNameField.getText(),
                    new ComponentPane(componentNameField.getText()
                            ,Integer.parseInt(componentPriceField.getText())
                            ,Integer.parseInt(componentHeatField.getText())
                            ,Integer.parseInt(componentEnergyField.getText())
                            ));
        };
        ActionListener delComp = e -> {

            componentTabbedPane.remove(componentTabbedPane.getSelectedComponent());
        };
        ActionListener delPC = e -> {

            computerTabbedPane.remove(computerTabbedPane.getSelectedComponent());
        };
        ActionListener delAllPC = e -> {

            computerTabbedPane.removeAll();
        };
        ActionListener delAllComps = e -> {

            componentTabbedPane.removeAll();
        };

        deleteComponentButton.addActionListener(delComp);
        createComponentButton.addActionListener(createComponent);
        createComputerButton.addActionListener(createPC);
        deleteComputerButton.addActionListener(delPC);
        deleteAllComputersButton.addActionListener(delAllPC);
        deleteAllComponentsButton.addActionListener(delAllComps);


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


                ((PCPane)computerTabbedPane.getSelectedComponent()).getComponentTabPane().addTab(name,this);
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

                componentTabbedPane.addTab(componentTabPane.getSelectedComponent().getName(),componentTabPane.getSelectedComponent());
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
