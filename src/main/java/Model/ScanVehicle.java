package Model;

import POJO.Vehicle.Vehicle;
import org.opencv.core.*;
import org.opencv.dnn.DetectionModel;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ScanVehicle {

    private String modelString;

    private ArrayList<Vehicle> vehicles;

    private String modelWeightsPath;
    private String modelConfigurationPath;
    private String cocoLabelPath;

    public ScanVehicle() {
        nu.pattern.OpenCV.loadLocally();

        String dataPath = System.getProperty("user.dir") + "\\src\\main\\java\\Data\\";
        modelWeightsPath = dataPath + "yolov7.weights";
        modelConfigurationPath = dataPath + "yolov7.cfg";
        cocoLabelPath = dataPath + "coco.names";
    }

    public void initialization() {}

    public void scan(String base64Str) {
        byte[] imgData = Base64.getDecoder().decode(base64Str);
        Mat imgMat = new Mat(1, imgData.length, CvType.CV_8UC1);
        imgMat.put(0, 0, imgData);

        Mat img = Imgcodecs.imdecode(imgMat, Imgcodecs.IMREAD_COLOR);

        List<String> classes;
        try {
            classes = Files.readAllLines(Paths.get(cocoLabelPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Net net = Dnn.readNetFromDarknet(modelConfigurationPath, modelWeightsPath);

        DetectionModel model = new DetectionModel(net);
        model.setInputParams(1 / 255.0, new Size(1402, 832), new Scalar(0), true);

        MatOfInt classIds = new MatOfInt();
        MatOfFloat scores = new MatOfFloat();
        MatOfRect boxes = new MatOfRect();
        model.detect(img, classIds, scores, boxes, 0.5f, 0.5f);

        for (int i = 0; i < classIds.rows(); i++) {
            Rect box = new Rect(boxes.get(i, 0));
            Imgproc.rectangle(img, box, new Scalar(0, 255, 0), 2);

            int classId = (int) classIds.get(i, 0)[0];
            double score = scores.get(i, 0)[0];
            String text = String.format("%s: %.2f", classes.get(classId), score);
            System.out.println(text);
            Imgproc.putText(img, text, new Point(box.x, box.y - 5), Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 255, 0), 2);
        }
    }

    public int getVehicleCount() {return 0;}

    public void markVehicle() {}

    public void calTypeOfVehicle() {}

    public static void main(String[] args) {
        ScanVehicle scanVehicle = new ScanVehicle();
    }
}
