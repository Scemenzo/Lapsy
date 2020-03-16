package scemenzo.view;

import javax.swing.*;
import java.awt.*;

/**
 * Questa finestra è stata creata dal GUI helper.
 * È stata aggiunta l'ereditarietà a JFrame e
 * la logica nel costruttore.
 */
public class FinestraTest extends JFrame {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JTextField textField1;
    private JComboBox comboBox1;
    private JTree tree1;
    private JComboBox comboBox2;
    private JTable table1;

    public FinestraTest() throws HeadlessException {
        super("Finestra di test");
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
    }

}
