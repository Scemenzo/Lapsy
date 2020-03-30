package scemenzo.control;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import javax.swing.*;

public class VideoConverter {

    private JProgressBar progressBar;
    private Mat image;

    public VideoConverter(JProgressBar progressBar1) {
        this.progressBar = progressBar1;
        progressBar.setValue(0);
    }

    public void convertVideo(String inputPath, String outputPath, double fps, double duration) {
        //Validazione
        VideoCapture inputVideo = new VideoCapture(inputPath);
        VideoWriter outputVideo = new VideoWriter();
        if(!inputVideo.isOpened()) {
            System.out.println("Non è stato possibile aprire il video selezionato");
            return;
        } else if (!outputVideo.isOpened()) {
            System.out.println("Non è stato possibile aprire il video in uscita");
            return;
        }

        //Preparazione dati
        double inputFrameCount = inputVideo.get(Videoio.CAP_PROP_FRAME_COUNT);
        double outputFrameCount = fps*duration;
        double frameCountRatio = inputFrameCount/outputFrameCount;

        System.out.println("Dati di conversione:\n" +
                        "input frame count: " + inputFrameCount +"\n" +
                        "output frame count: " + outputFrameCount +"\n" +
                        "output duration: " + duration);

        //Procedura di conversione
        outputVideo.open(outputPath, (int) inputVideo.get(Videoio.CAP_PROP_FOURCC), fps, new Size(inputVideo.get(Videoio.CAP_PROP_FRAME_WIDTH), inputVideo.get(Videoio.CAP_PROP_FRAME_HEIGHT)));

        //Scrittura esplicita del primo frame
        int framePicker = 0;
        Mat currentFrame = new Mat();
        inputVideo.read(currentFrame);
        outputVideo.write(currentFrame);
        framePicker++;

        for (double icnt = frameCountRatio; icnt < inputFrameCount; icnt += frameCountRatio) {
            //Skippa i frame finchè non raggiungi l'approssimazione del multiplo della ratio
            while (frameCountRatio<Math.floor(icnt)) {
                inputVideo.grab();
                framePicker++;
            }
            inputVideo.retrieve(currentFrame);
            outputVideo.write(currentFrame);
            progressBar.setValue((int) Math.ceil(icnt/inputFrameCount*100));
        }

        progressBar.setValue(0);
    }

}
