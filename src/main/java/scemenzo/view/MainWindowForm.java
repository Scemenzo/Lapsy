package scemenzo.view;

import org.apache.commons.io.FilenameUtils;
import scemenzo.control.VideoConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class MainWindowForm extends JFrame {

    private JPanel panel1;
    private JProgressBar progressBar1;
    private JButton importVideoButton;
    private JSpinner durationSpinner;
    private JSpinner framerateSpinner;
    private JButton startButton;
    private JLabel videoPathLabel;

    private VideoConverter videoConverter;

    public MainWindowForm() throws HeadlessException {
        super("Lapsy");
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        videoConverter = new VideoConverter(progressBar1);

        importVideoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.showDialog(panel1, "OK");
                if (fileChooser.getSelectedFile() != null) {
                    System.out.println("File selezionato: " + fileChooser.getSelectedFile().getPath());
                    videoPathLabel.setText(fileChooser.getSelectedFile().getPath());
                }
            }
        });

        //Drop handling per videoPathLabel
        DropTarget dropTarget = new DropTarget(this, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent event) {
                event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                try {
                    List<File> droppedFiles = (List<File>) event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for (File droppedFile : droppedFiles) {
                        System.out.println("File droppato: " + droppedFile.getPath());
                        videoPathLabel.setText(droppedFile.getPath());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Tasto di start
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if ((int) durationSpinner.getValue() <= 0 || (int) framerateSpinner.getValue() <= 0) {
                    popupError("I dati inseriti non sono validi per la conversione");
                }
                String inputFilePath = videoPathLabel.getText();
                String outputFilePath = FilenameUtils.getPath(inputFilePath) + FilenameUtils.getBaseName(inputFilePath) + "_lapsed." + FilenameUtils.getExtension(inputFilePath);
                popupInfo(outputFilePath);
                videoConverter.convertVideo(inputFilePath, outputFilePath, (int) framerateSpinner.getValue(), (int) durationSpinner.getValue());
            }
        });
    }

    private void popupInfo(String message) {
        System.out.println("Popup info: " + message);
        JOptionPane.showMessageDialog(this, message, "Informazione", JOptionPane.INFORMATION_MESSAGE);
    }

    private void popupError(String message) {
        System.out.println("Popup error: " + message);
        JOptionPane.showMessageDialog(this, message, "Attenzione!", JOptionPane.ERROR_MESSAGE);
    }
}
