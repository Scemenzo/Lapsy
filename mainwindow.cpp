#include "mainwindow.h"
#include "ui_mainwindow.h"

#include <string>

#include <QtMultimedia/QMediaPlayer>
#include <QFileDialog>
#include <QDragEnterEvent>
#include <QMimeData>

#include <opencv4/opencv2/opencv.hpp>

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    setAcceptDrops(true);
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::dragEnterEvent(QDragEnterEvent *e)
{
    if (e->mimeData()->hasUrls()) {
        e->acceptProposedAction();
    }
}

void MainWindow::dropEvent(QDropEvent *e)
{
    foreach (const QUrl &url, e->mimeData()->urls()) {
        QString fileName = url.toLocalFile();
        ui->label_2->setText(fileName);
    }
}

void MainWindow::on_pushButton_clicked()
{
    QString filePath = QFileDialog::getOpenFileName(this,tr("Open file"));
    ui->label_2->setText(filePath);
}

void MainWindow::on_pushButton_2_clicked()
{
    inputToOutput(ui->label_2->text());
}

void MainWindow::inputToOutput(QString filePath){
    QFileInfo fileInfo(filePath);
    QString noExtPath = fileInfo.absolutePath() + "/" + fileInfo.completeBaseName();
    qDebug() << "Path passato: " << filePath << endl;
    qDebug() << "Path senza extension: " << noExtPath << endl;

    //Opening input video
    cv::VideoCapture inputVideo(filePath.toStdString());
    if(inputVideo.isOpened()){
        qDebug() << "File aperto";

        //Taking quantized frames
        qDebug() << "cattura frame";
        cv::Mat frame;
        for (int i=0; i < 100; i++) {
            inputVideo.grab();
        }
        inputVideo.retrieve(frame);

        //Creting output video
        cv::VideoWriter outputVideo;
        std::string path = noExtPath.append("_lapsed.avi").toStdString();
        qDebug() << "Path di memorizzazione: " << QString::fromStdString(path) << endl;

        outputVideo.open(
                    path,
                    static_cast<int>(inputVideo.get(cv::CAP_PROP_FOURCC)),     // Get Codec Type
                    inputVideo.get(cv::CAP_PROP_FPS),
                    cv::Size(int(inputVideo.get(cv::CAP_PROP_FRAME_WIDTH)),int(inputVideo.get(cv::CAP_PROP_FRAME_HEIGHT))));

        if(outputVideo.isOpened()){
            qDebug() << "Video output aperto con successo";
            for (int i = 0; i < 1000; i++) {
                outputVideo.write(frame);
            }
        }
        else {
            qDebug() << "errore nell'apertura video";
        }
    }
    else qDebug() << "File non aperto correttamente";

    qDebug() << "Procedura inputToOutput terminata";

}
