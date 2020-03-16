package scemenzo.utils;

import org.pushingpixels.substance.api.SubstanceCortex;
import org.pushingpixels.substance.api.skin.SkinInfo;

import javax.swing.*;

public class GenericUtilities {

    /**
     * Metodo che si occupa di stampare all'avvio
     * tutte le informazioni essenziali per l'applicazione in questione.
     */
    public static void welcomePrint() {
        System.out.println(
                "--------------------\n" +
                "Applicazione avviata\n" +
                "--------------------\n\n" +
                "Temi di sistema:"
        );

        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            System.out.println("| " + info.getClassName());
        }

        System.out.println("\nTemi di Substance:");

        for (SkinInfo info : SubstanceCortex.GlobalScope.getAllSkins().values()) {
            System.out.println("| " + info.getClassName());
        }

        System.out.print("\n");
    }

    /**
     * Cambia tutti i font presenti nell'attuale lookAndFeel
     * @param font Il font da sostituire a quello di default
     */
    public static void setDefaultUIFont (javax.swing.plaf.FontUIResource font){
        java.util.Enumeration keys = UIManager.getLookAndFeelDefaults().keys();
        while (keys.hasMoreElements()) {
            Object uiKey = keys.nextElement();
            Object uiValue = UIManager.get(uiKey);
            if (uiValue instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (uiKey, font);
        }
    }

}
