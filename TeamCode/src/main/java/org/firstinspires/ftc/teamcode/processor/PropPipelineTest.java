package org.firstinspires.ftc.teamcode.processor;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class PropPipelineTest implements VisionProcessor {
    private static final boolean DEBUG = false;
    private static String location = "RIGHT";
    private static String alliance = "RED";
    private static String side = "FAR";


    private final Mat hsv = new Mat();

    public static int redLeftX = 165;
    public static int redLeftY = 140;

    public static int redCenterX = 00;
    public static int redCenterY = 00;

    public static int blueLeftX = 0;
    public static int blueLeftY = 0;

    public static int blueCenterX = 0;
    public static int blueCenterY = 0;

    public static int leftWidth = 100;
    public static int leftHeight = 50;

    public static int centerWidth = 5;
    public static int centerHeight = 5;

    public static double BLUE_TRESHOLD = 70;
    public static double RED_TRESHOLD = 100;

    public double leftColor = 0.0;
    public double centerColor = 0.0;

    public Scalar left = new Scalar(0, 0, 0);
    public Scalar center = new Scalar(0, 0, 0);

    Rect leftZoneArea;
    Rect centerZoneArea;

    Telemetry telemetry;

//    Location ALLIANCE = Location.RED;

    public PropPipelineTest() {
        this(null);
    }

    public PropPipelineTest(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {

    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {


        if (alliance == "RED" && side == "FAR" || alliance == "BLUE" && side == "CLOSE") {
            leftZoneArea = new Rect(redLeftX, redLeftY, leftWidth, leftHeight);
            centerZoneArea = new Rect(redCenterX, redCenterY, centerWidth, centerHeight);
        } else {
            leftZoneArea = new Rect(blueLeftX, blueLeftY, leftWidth, leftHeight);
            centerZoneArea = new Rect(blueCenterX, blueCenterY, centerWidth, centerHeight);
        }

        Mat leftZone = frame.submat(leftZoneArea);
        Mat centerZone = frame.submat(centerZoneArea);


        if (DEBUG) {
            Imgproc.blur(frame, frame, new Size(5, 5));
            Imgproc.rectangle(frame, leftZoneArea, new Scalar(255, 255, 255), 2);
            Imgproc.rectangle(frame, centerZoneArea, new Scalar(255, 255, 255), 2);
        }

        Imgproc.blur(leftZone, leftZone, new Size(5, 5));
        Imgproc.blur(centerZone, centerZone, new Size(5, 5));

        left = Core.mean(leftZone);
        center = Core.mean(centerZone);

        if (telemetry != null) {
            telemetry.addData("leftColor", left.toString());
            telemetry.addData("centerColor", center.toString());
            telemetry.addData("analysis", location.toString());
            telemetry.update();
        }

        double threshold = alliance == "RED" ? RED_TRESHOLD : BLUE_TRESHOLD;
        int idx = alliance == "RED" ? 0 : 2;

        leftColor = left.val[idx];
        centerColor = center.val[idx];

        if (leftColor > threshold && (left.val[0] + left.val[1] + left.val[2] - left.val[idx] < left.val[idx])) {
            // left zone has it
            location = "LEFT";
            Imgproc.rectangle(frame, leftZoneArea, new Scalar(255, 255, 255), 10);
        } else if (centerColor > threshold && (center.val[0] + center.val[1] + center.val[2] - center.val[idx] < center.val[idx])) {
            // center zone has it
            location = "CENTER";
            Imgproc.rectangle(frame, centerZoneArea, new Scalar(255, 255, 255), 10);
        } else {
            // right zone has it
            location = "RIGHT";
        }

        leftZone.release();
        centerZone.release();

        return null;
    }

    private android.graphics.Rect makeGraphicsRect(Rect rect) {
        int left = Math.round(rect.x);
        int top = Math.round(rect.y);
        int right = left + Math.round(rect.width);
        int bottom = top + Math.round(rect.height);

        return new android.graphics.Rect(left, top, right, bottom);
    }
    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        Paint selectedPaint = new Paint();
        selectedPaint.setColor(Color.RED);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(scaleCanvasDensity * 4);

        Paint nonSelectedPaint = new Paint(selectedPaint);
        nonSelectedPaint.setColor(Color.GREEN);

        android.graphics.Rect drawRectangleLeft = makeGraphicsRect(leftZoneArea);
        android.graphics.Rect drawRectangleMiddle = makeGraphicsRect(centerZoneArea);

        switch (location) {
            case "LEFT":
                canvas.drawRect(drawRectangleLeft, selectedPaint);
                canvas.drawRect(drawRectangleMiddle, nonSelectedPaint);
                break;
            case "MIDDLE":
                canvas.drawRect(drawRectangleLeft, nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle, selectedPaint);
                break;
            case "RIGHT":
                canvas.drawRect(drawRectangleLeft, nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle, nonSelectedPaint);
                break;
        }
    }

    public String getLocation() {
        return this.location;
    }
}
