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
    frameFromVideo(ui->label_2->text());
}

void MainWindow::frameFromVideo(QString filePath){
    const char * constCharFilePath = filePath.toLocal8Bit().data();
    cv::VideoCapture inputVideo(constCharFilePath);
    if(inputVideo.isOpened()){

        qDebug() << "File aperto";

        cv::Mat edges;
        cv::namedWindow("edges",1);

        qDebug() << "cattura frame";
        cv::Mat frame;
        inputVideo >> frame;

        qDebug() << "conversione frame";
        cv::cvtColor(frame, edges, CV_8U);

        qDebug() << "display frame";
        cv::imshow("edges", edges);
        cv::waitKey(30);

        //Ora il video

        QFileInfo fileInfo(filePath);
        QString noExtPath = fileInfo.completeBaseName();

        cv::VideoWriter outputVideo;
        std::string path = noExtPath.append("_lapsed.avi").toStdString();
        outputVideo.open(
                    path,
                    static_cast<int>(inputVideo.get(cv::CAP_PROP_FOURCC)),     // Get Codec Type- Int form
                    inputVideo.get(cv::CAP_PROP_FPS),
                    cv::Size(int(inputVideo.get(cv::CAP_PROP_FRAME_WIDTH)),int(inputVideo.get(cv::CAP_PROP_FRAME_HEIGHT))));

        if(outputVideo.isOpened()){
            qDebug() << "video aperto con successo";
            for (int i = 0; i <1000; i++) {
                outputVideo.write(frame);
            }
        }
        else {
            qDebug() << "errore nell'apertura video";
        }
    }
    else qDebug() << "File non aperto correttamente";
}
