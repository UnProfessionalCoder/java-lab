package com.newbig.ocr;


import javafx.util.Pair;
import org.bytedeco.javacpp.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
import static org.bytedeco.javacpp.opencv_imgproc.*;


public class OCR {
    public static String recognize(String imagePath){
        BytePointer outText = null;
        try {
            tesseract.TessBaseAPI api = new tesseract.TessBaseAPI();
            // Initialize tesseract-ocr with English, without specifying tessdata path
            if (api.Init(null, "eng") != 0) {
                System.err.println("Could not initialize tesseract.");
                System.exit(1);
            }

            opencv_core.IplImage image = opencv_imgcodecs.cvLoadImage(imagePath);
            opencv_core.IplImage imageresize = opencv_core.cvCreateImage(opencv_core.cvSize(image.width() * 3, image.height() * 3), opencv_core.IPL_DEPTH_8U, 3);
            opencv_imgproc.cvResize(image, imageresize, opencv_imgproc.CV_INTER_LINEAR);
            opencv_core.Mat resultImage = opencv_core.cvarrToMat(imageresize);
            //MatVector rgb = new MatVector();
            //cvtColor(rgb.get(1), grayImage, CV_BGR2GRAY);
//        opencv_core.Mat grayImage = new opencv_core.Mat();
//        cvtColor(resultImage, grayImage, CV_BGR2GRAY);
//        opencv_core.Mat erodeElement1 = getStructuringElement(MORPH_RECT, new opencv_core.Size(1,2));
//        erode(grayImage, grayImage, erodeElement1);
//        opencv_core.Mat erodeElement = getStructuringElement(MORPH_ELLIPSE, new opencv_core.Size(1,1));
//        erode(grayImage, grayImage, erodeElement);
//        threshold(resultImage, resultImage, 30, 30, CV_THRESH_BINARY);
            String tmp = "/work/tessdata/estate/tmp.png";
            imwrite(tmp, resultImage);
            File imgPath = new File(tmp);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedImage img = null;
            try {
                img = ImageIO.read(imgPath);
                ImageIO.write(img, "png", baos);
            } catch (IOException e) {
                System.err.println("Reading file or writing byte[] failed.");
                e.printStackTrace();
            }
            byte[] imageInByte = baos.toByteArray();

            lept.PIX image1 = lept.pixReadMemPng(imageInByte, imageInByte.length);

            api.SetImage(image1);

            // Get OCR result
            outText = api.GetUTF8Text();
//        System.out.println("OCR output:" + outText.getString().replace("\n",""));
            // Destroy used object and release memory
            api.End();
            outText.deallocate();
            pixDestroy(image1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return outText==null?null:outText.getString().replace("\n","");
    }

    public static Boolean isEligible(Rect candidate,int infoType){
        infoType=0;//默认为其他
        //const float aspect = 4.5/0.3; //长宽比
     int minArea = 100; //最小区域
     int maxArea = 100000;  //最大区域

        int area = candidate.height() * candidate.width();
        if( (area < minArea || area > maxArea)  )
            return false;
        else{//提取可能文字区域
            //根据文字区域根据相对位置及面积，判断身份信息类型
            //qDebug()<<"locate:"
            //        <<candidate.x<<","<<candidate.y<<","<<candidate.width<<","<<candidate.height;
            if(candidate.x()<35*1.5&&candidate.x()>35*0.5&&
                    candidate.y()<40*1.5&&candidate.y()>40*0.5&&
                    area<5000*1.5&&area>2500*0.5){//姓名
                infoType=1;
            }
            else if(candidate.x()<115*1.5&&candidate.x()>115*0.5&&
                    candidate.y()<40*1.5&&candidate.y()>40*0.5&&
                    area<4800*1.5&&area>2400*0.5){//姓名（不带Label）
                infoType=2;
            }
            else if(candidate.x()<35*1.5&&candidate.x()>35*0.5&&
                    candidate.y()<80*1.5&&candidate.y()>80*0.5&&
                    area<1500*1.5&&area>2400*0.5){//性别
                infoType=3;
            }
            else if(candidate.x()<115*1.5&&candidate.x()>115*0.5&&
                    candidate.y()<80*1.5&&candidate.y()>80*0.5&&
                    area<500*1.5&&area>500*0.5){//性别（不带Label）
                infoType=4;
            }
            else if(candidate.x()<180*1.5&&candidate.x()>180*0.5&&
                    candidate.y()<100*1.5&&candidate.y()>100*0.5&&
                    area<1800*1.5&&area>1800*0.5){//民族
                infoType=5;
            }
            else if(candidate.x()<230*1.5&&candidate.x()>230*0.5&&
                    candidate.y()<120*1.5&&candidate.y()>120*0.5&&
                    area<1200*1.5&&area>1200*0.5){//民族（不带Label）(x)
                infoType=6;
            }
            else if(candidate.x()<35*1.5&&candidate.x()>35*0.5&&
                    candidate.y()<120*1.5&&candidate.y()>120*0.5&&
                    area<4500*1.5&&area>4500*0.5){//出生
                infoType=7;
            }
            else if(candidate.x()<115*1.5&&candidate.x()>115*0.5&&
                    candidate.y()<145*1.5&&candidate.y()>145*0.5&&
                    area<4000*1.5&&area>4000*0.5){//出生（不带Label）
                infoType=8;
            }
            else if(candidate.x()<35*1.5&&candidate.x()>35*0.5&&
                    candidate.y()<160*1.5&&candidate.y()>160*0.5&&
                    area<25000*1.5&&area>12000*0.5){//住址
                infoType=9;
            }
            else if(candidate.x()<115*1.5&&candidate.x()>115*0.5&&
                    candidate.y()<190*1.5&&candidate.y()>190*0.5&&
                    area<30000*1.5&&area>14000*0.5){//住址（不带Label）
                infoType=10;
            }
            else if(candidate.x()<35*1.5&&candidate.x()>35*0.5&&
                    candidate.y()<250*1.5&&candidate.y()>250*0.5&&
                    area<7500*1.5&&area>7500*0.5){//公民身份号码
                infoType=11;
            }
            else if(candidate.x()<215*1.5&&candidate.x()>215*0.5&&
                    candidate.y()<250*1.5&&candidate.y()>250*0.5&&
                    area<8000*1.5&&area>8000*0.5){//公民身份号码（不带Label）
                infoType=12;
            }
            else{
                return false;
            }
            return true;
        }

    }
    public static int calculateDalteThresholdForAllImageMat(Mat normImage,Rect rect, int deltaRange, int deltaThreshold, float deltaRatio) {
            int hIndex = 0, wIndex = 0, Color = 0, Position = 0, threshold = 0;
            float[] deltaProb=new float[256];
            float sumPixelNum = 0, tempProbValue = 0, maxProbValue = 0;

     int deltaValue = 256;


            //基于输入图像的概率部分确定阈值
            for (hIndex = rect.y(); hIndex < rect.height() -1 ; hIndex++)
            {
                for (wIndex = rect.x(); wIndex < rect.width() -1; wIndex++)
                {
                    Position = hIndex * normImage.step(0) + wIndex;

                    //将当前区域小于阈值部分不考虑
                    if((normImage.data(new BytePointer(Position+"")).data().get() <= deltaThreshold))
                    continue;

                    //Color = currImage->imageData[Position] - normImage->imageData[Position];
                    Color = (char)(normImage.data(new BytePointer(Position+"")).data().get());
                    deltaProb[Color]++;
                    sumPixelNum++;
                }
            }

            //累积直方图概率和
            for (hIndex = 0; hIndex < deltaValue; hIndex++)
                deltaProb[hIndex] = deltaProb[hIndex]/sumPixelNum;

            int Imin = 0, Imax = 0, dImin = 0, dImax = 0;

            for (hIndex = 0; hIndex < deltaValue; hIndex++)
            {
                Imin = hIndex - deltaRange >= 0 ? hIndex - deltaRange : 0;
                Imax = hIndex;

                for (wIndex = Imin, tempProbValue = 0; wIndex <= Imax; wIndex++)
                    tempProbValue += deltaProb[wIndex];

                if(tempProbValue > maxProbValue )
                {
                    maxProbValue = tempProbValue;
                    dImax = Imax; dImin = Imin;
                }
            }

            if(maxProbValue > deltaRatio)
            {
                maxProbValue = 0;
                for (wIndex = dImin; wIndex <= dImax; wIndex++)
                {
                    if(maxProbValue < deltaProb[wIndex])
                    {
                        maxProbValue = deltaProb[wIndex];
                        threshold = wIndex;
                    }
                }
                if( threshold >= 245 )
                    threshold = threshold * 5/6;
                return threshold;
            }
            else
                return 0;
    }
    public static void ostuBeresenThreshold( Mat in, Mat out){
        double ostu_T = opencv_imgproc.threshold(in , out, 0,255 , opencv_imgproc.CV_THRESH_OTSU); //otsu获得全局阈值

        DoublePointer min = new DoublePointer();
        DoublePointer max = new DoublePointer();
        IntPointer minIdx = new IntPointer();
        IntPointer maxIdx = new IntPointer();
        minMaxIdx(in,min,max,minIdx,maxIdx,null);
        final double CI = 0.12;
        double beta = CI*(max.get() - min.get() +1)/128;
        double beta_lowT = (1-beta)*ostu_T;
        double beta_highT = (1+beta)*ostu_T;

        Mat doubleMatIn=new Mat();
        in.copyTo(doubleMatIn);
        int rows = doubleMatIn.rows();
        int cols = doubleMatIn.cols();
        double Tbn;
        for( int i = 0; i < rows; ++i)
        {
            //获取第 i行首像素指针
            BytePointer p = doubleMatIn.ptr(i);
            BytePointer outPtr = out.ptr(i);

            //对第i 行的每个像素(byte)操作
            for( int j = 0; j < cols; ++j )
            {

                if(i <2 | i>rows - 3 | j<2 | j>rows - 3)
                {

                    if( p.get(Long.valueOf(j)) <= beta_lowT )
                        outPtr.put(Long.valueOf(j),Byte.valueOf(0+""));
                    else
                        outPtr.put(Long.valueOf(j),Byte.valueOf(255+""));
                }
                else
                {
                    Tbn = opencv_core.sumElems((doubleMatIn.apply(new Rect(i-2,j-2,5,5)).col(0))).get()/25 ;  //窗口大小25*25
                    if(p.get(Long.valueOf(j)) < beta_lowT || (p.get(Long.valueOf(j))< Tbn &&  (beta_lowT <= p.get(Long.valueOf(j)) && p.get(Long.valueOf(j)) >= beta_highT)))
                        outPtr.put(Long.valueOf(j),Byte.valueOf(0+""));
                    if( p.get(Long.valueOf(j)) > beta_highT || (p.get(Long.valueOf(j))>= Tbn &&  (beta_lowT <= p.get(Long.valueOf(j)) && p.get(Long.valueOf(j)) >= beta_highT)))
                        outPtr.put(Long.valueOf(j),Byte.valueOf(255+""));
                }
            }
        }

    }
    public static Mat getRplane(final Mat in) {
        MatVector splitBGR= new MatVector(in.channels()); //容器大小为通道数3
        opencv_core.split(in,splitBGR);
        //return splitBGR[2];  //R分量

        if(in.cols() > 700 |in.cols() >600)
        {
            Mat resizeR= new Mat( 450,600 , opencv_core.CV_8UC1);
            opencv_imgproc.resize( splitBGR.get(Long.valueOf(2)) ,resizeR ,resizeR.size());

            return resizeR;
        }
        else
            return splitBGR.get(Long.valueOf(2));

    }
    public static void posDetect(final Mat in, Vector<Pair<Rect,Integer>> rectsWithType )
    {
        //Mat threshold_R;
        //OstuBeresenThreshold(in ,threshold_R ); //二值化
        //cv::imwrite(imageToOCRName,threshold_R);
        //cv::imshow("threshold",threshold_R);

        //利用阈值二值化
        Mat testRectClone = in.clone();
        opencv_imgproc.threshold(testRectClone, testRectClone, 120, 255, opencv_imgproc.CV_THRESH_BINARY);
        //imshow("threshold",testRectClone);
        //waitKey(0);
        //输入图像
        //输出图像
        //单元大小，这里是3*3的8位单元
        //腐蚀位置，为负值取核中心
        opencv_imgproc.erode(testRectClone,in,new Mat(3,3, opencv_core.CV_8U));
        //imshow("erodeMat",in);
        //waitKey(0);
        Mat imgInv= new Mat(in.size(),in.type(),new Scalar(255));
        Mat threshold_Inv=new Mat();
        in.copyTo(threshold_Inv,imgInv); //黑白色反转，即背景为黑色

        //cv::imshow("threshold_Inv",threshold_Inv);
        //waitKey(0);
        Mat element = opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_RECT ,new Size(18 ,13));  //闭形态学的结构元素
        opencv_imgproc.morphologyEx(threshold_Inv ,threshold_Inv, opencv_imgproc.CV_MOP_CLOSE,element);
        //cv::imshow("morphologyEx",threshold_Inv);
        //waitKey(0);

        MatVector contours=new MatVector();
        opencv_imgproc.findContours(threshold_Inv ,contours, opencv_imgproc.CV_RETR_EXTERNAL, opencv_imgproc.CV_CHAIN_APPROX_NONE);//只检测外轮廓
        //对候选的轮廓进行进一步筛选
        for(long i=0;i< contours.size();i++)
        {
            Rect mr = opencv_imgproc.boundingRect(new Mat(contours.get(i))); //返回每个轮廓的最小有界矩形区域
            int infoType=0;
            if(mr.size().width()==0||mr.size().height()==0){
                 continue;
            }
            if(!isEligible(mr,infoType))  //判断矩形轮廓是否符合要求
            {
                continue;
            }
            else
            {
                rectsWithType.add(new Pair<>(mr,infoType));
            }
        }

    }
}
