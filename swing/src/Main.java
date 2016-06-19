import configurator.component.ComponentManager;
import configurator.component.ComponentManagerImpl;
import configurator.computer.ComputerManager;
import configurator.computer.ComputerManagerImpl;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.*;

/**
 * Created by zeman on 27-Apr-16.
 */
public class Main {
    static public ComputerManager getComputerManager() {
        return computerManager;
    }

    static public ComponentManager getComponentManager() {
        return componentManager;
    }

    static private DataSource dataSource = configurator.Main.createMemoryDatabase();
    static private ComputerManager computerManager = new ComputerManagerImpl(dataSource);
    static private ComponentManager componentManager = new ComponentManagerImpl(dataSource);

    public static void main(String[] args) {


        EventQueue.invokeLater( ()-> {
                    JFrame frame = new PcManager();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setTitle("Faggotina");
                    frame.setVisible(true);
                }
        );

    }

}
