#include "mainwindow.h"
#include "ui_mainwindow.h"

#include <string>

#include <QtMultimedia/QMediaPlayer>
#include <QFileDialog>
#include <QDragEnterEvent>
#include <QMimeData>
#include <QMessageBox>

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
    const double OFPS = ui->fPSBox->value();
    const double ODuration = QTime(0,0,0,0).msecsTo(ui->durationBox->time()) / 1000.0;
    QProgressBar * bar =  ui->progressBar;
    bar->setValue(0);

    //Check the validity of the user inserted settings
    if(ODuration > 0 && ui->fPSBox->value() > 0){
        //Take path without extension
        QFileInfo fileInfo(filePath);
        QString noExtPath = fileInfo.absolutePath() + "/" + fileInfo.completeBaseName();

        //Opening input video
        cv::VideoCapture inputVideo(filePath.toStdString());
        if(inputVideo.isOpened()) {
            qDebug() << "Input video is opened";

            //Creting output video path
            cv::VideoWriter outputVideo;
            std::string path = noExtPath.append("_lapsed.").append(fileInfo.suffix()).toStdString();

            outputVideo.open(
                        path,
                        static_cast<int>(inputVideo.get(cv::CAP_PROP_FOURCC)),     //Codec type
                        ui->fPSBox->value(),
                        cv::Size(int(inputVideo.get(cv::CAP_PROP_FRAME_WIDTH)),int(inputVideo.get(cv::CAP_PROP_FRAME_HEIGHT))));

            if(outputVideo.isOpened()){
                qDebug() << "Video output opened";

                lockAll();

                //Obtain data from files
                const double ICount = inputVideo.get(cv::CAP_PROP_FRAME_COUNT);
                const double OCount = OFPS * ODuration;
                const double IOCountRatio = ICount/OCount;
                int framePicker = 0;

                qDebug() << "----Conversion data obtained\n"<<
                            "\nInput frame count: "<<ICount<<
                            "\nOutput FPS: "<<OFPS<<
                            "\nOutput duration: "<<ODuration<<
                            "\nOutput frame count: "<<OCount;

                //Frame quantization calculation
                //Write first frame
                cv::Mat frame;
                inputVideo.read(frame);
                outputVideo.write(frame);
                framePicker++;
                //for every increment of ratio
                for (double icnt = IOCountRatio; icnt < ICount; icnt += IOCountRatio){
                    //skip frames until you reach the approximation of the ratio multiplier
                    while (framePicker < floor(icnt)) {
                        inputVideo.grab();
                        framePicker++;
                    }
                    //capture the spicy frame
                    inputVideo.retrieve(frame);
                    outputVideo.write(frame);
                    bar->setValue(ceil(double(icnt)/double(ICount)*100.0));
                }

                bar->setValue(0);

                unlockAll();

                popupMessage("The procedure is finished", "Success");
            }
            else {
                popupMessage("A problem occurred while loading the input video");
            }
        }
        else popupMessage("A problem occurred while saving the output video");
    }
    else {
        popupMessage("The output settings are not valid");
    }
}

void MainWindow::popupMessage(QString msg, QString title, int width, int height){
    QMessageBox messageBox;
    messageBox.information(nullptr,title,msg);
    messageBox.setFixedSize(width, height);
}

void MainWindow::lockAll(){
    ui->durationBox->setEnabled(false);
    ui->fPSBox->setEnabled(false);
    ui->pushButton->setEnabled(false);
    ui->pushButton_2->setEnabled(false);
}

void MainWindow::unlockAll(){
    ui->durationBox->setEnabled(true);
    ui->fPSBox->setEnabled(true);
    ui->pushButton->setEnabled(true);
    ui->pushButton_2->setEnabled(true);
}
