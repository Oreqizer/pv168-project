package gui;

import configurator.component.ComponentManager;
import configurator.component.ComponentManagerImpl;
import configurator.computer.ComputerManager;
import configurator.computer.ComputerManagerImpl;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

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


    public static BigDecimal parseBigDecimal(String str) throws ParseException {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();

        if (str.matches(".*[a-zA-Z]+.*")) throw new IllegalArgumentException("Can't parse string! ");
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        String pattern = "#,##0.0#";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);
        return (BigDecimal) decimalFormat.parse(str);
    }



    public static void main(String[] args) {


        EventQueue.invokeLater( ()-> {
                    JFrame frame = new PcManager();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("PCManager");
                    frame.setVisible(true);
                }
        );

    }

}
