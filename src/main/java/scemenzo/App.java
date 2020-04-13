package scemenzo;

import org.opencv.core.Core;
import org.opencv.videoio.Videoio;
import scemenzo.utils.GenericUtilities;
import scemenzo.utils.ResourceLoader;
import scemenzo.view.MainWindowForm;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

/**
 * Hello world!
 */
public class App {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        GenericUtilities.welcomePrint();
        JFrame.setDefaultLookAndFeelDecorated(true);

        SwingUtilities.invokeLater(() -> {
            try {

                UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel");
                GenericUtilities.setDefaultUIFont(new FontUIResource(ResourceLoader.getInstance().getFontMain()));

                MainWindowForm finestraPrincipale = new MainWindowForm();
                finestraPrincipale.setVisible(true);

            } catch (Exception e) {
                System.out.println("Substance Graphite failed to initialize");
                e.printStackTrace();
            }
        });
    }
}
