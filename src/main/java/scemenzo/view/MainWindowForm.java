package scemenzo.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainWindowForm extends JFrame {

    private JPanel panel1;
    private JProgressBar progressBar1;
    private JButton importVideoButton;
    private JSpinner durationSpinner;
    private JSpinner framerateSpinner;
    private JButton startButton;
    private JLabel videoPathLabel;

    public MainWindowForm() throws HeadlessException {
        super("Lapsy");
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
    }
}
