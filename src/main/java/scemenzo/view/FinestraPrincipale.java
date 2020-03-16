package scemenzo.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Questa finestra è stata creata da zero, è autonoma e
 * completamente strutturata tramite codice
 */
public class FinestraPrincipale extends JFrame {

    public FinestraPrincipale() throws HeadlessException {

        //Settings per la finestra
        super("Finestra Principale");
        this.setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR));
        this.setSize(new Dimension(400, 100));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //È sempre meglio mettere un panel principale ad ogni finestra
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());

        //Esempio di aggiunta components ad un container
        mainPanel.add(new JButton("Bottone"));
        mainPanel.add(new JCheckBox("Checkbox"));
        mainPanel.add(new JLabel("Testo"));

        //Esempio di toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.add(new JComboBox<String>());
        this.getContentPane().add(toolBar, BorderLayout.NORTH);

        this.add(mainPanel);

    }
}
