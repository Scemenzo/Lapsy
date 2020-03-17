package scemenzo.utils;

import lombok.Getter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ResourceLoader {

    private static ResourceLoader singletonInstance = null;

    //Risorse
    @Getter private Font fontMain;

    /**
     * Creazione dell'istanza singleton del Resourceloader. È essenziale
     * inizializzare tutte le variabili private delle risorse in esso contenute, gestendo
     * opportunamente le eccezioni.
     */
    private ResourceLoader() {
        try {

            //Logica di caricamento delle risorse
            fontMain = Font.createFont(Font.TRUETYPE_FONT, loadResource("/CaviarDreams.ttf")).deriveFont(16f);

        } catch (Exception e) {
            System.out.println("ERRORE - Instanziazione di ResourceLoader, durante il caricamento delle risorse");
            e.printStackTrace();
        }
    }

    /**
     * Metodo per accedere all'istanza singleton di questa classe.
     * @return Istanza singleton globale di ResourceLoader.
     */
    public static ResourceLoader getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new ResourceLoader();
        }
        return singletonInstance;
    }

    private File loadResource(String resourcePath) throws IOException {
        URL url = ResourceLoader.class.getResource(resourcePath);
        File file = new File(url.getPath());
        if (!file.isFile()) {
            throw new IOException("L'URL " + resourcePath + " non punta ad un file");
        }
        System.out.println("Pre-caricato file: " + resourcePath);
        return file;
    }
}