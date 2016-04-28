import javax.swing.*;
import java.awt.*;

/**
 * Created by zeman on 27-Apr-16.
 */
public class Main {


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
