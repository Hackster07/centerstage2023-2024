package org.firstinspires.ftc.teamcode.processor;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.stuff.Location;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

    public class RealPropPipeline implements VisionProcessor {

        public RealPropPipeline() {
            this(null);
        }

        public RealPropPipeline(Telemetry telemetry) {
            this.telemetry = telemetry;
        }
        public Rect rectLeft = new Rect(85, 200, 75, 75);
        public Rect rectMiddle = new Rect(275, 190, 75, 75);
        public Rect rectRight = new Rect(465, 200, 75, 75);
        private volatile Location location = Location.RIGHT;

        Telemetry telemetry;

        Mat submat = new Mat();
        Mat hsvMat = new Mat();

        @Override
        public void init(int width, int height, CameraCalibration calibration) {
        }

        @Override
        public Object processFrame(Mat frame, long captureTimeNanos) {
            Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV);

            double satRectLeft = getAvgSaturation(hsvMat, rectLeft);
            double satRectMiddle = getAvgSaturation(hsvMat, rectMiddle);
            double satRectRight = getAvgSaturation(hsvMat, rectRight);

            if ((satRectLeft > satRectMiddle) && (satRectLeft > satRectRight)) {
                location = Location.LEFT;
            } else if ((satRectMiddle > satRectLeft) && (satRectMiddle > satRectRight)) {
                location = Location.CENTER;
            }else {
                location = Location.RIGHT;
            }

            if (telemetry != null) {
                telemetry.addData("leftColor", satRectLeft);
                telemetry.addData("centerColor", satRectMiddle);
                telemetry.addData("rightColor", satRectRight);
                telemetry.addData("analysis", location.toString());
                telemetry.update();
            }
            return location;
        }
        protected double getAvgSaturation(Mat input, Rect rect) {
            submat = input.submat(rect);
            Scalar color = Core.mean(submat);
            return color.val[1];
        }

        private android.graphics.Rect makeGraphicsRect(Rect rect, float scaleBmpPxToCanvasPx) {
            int left = Math.round(rect.x * scaleBmpPxToCanvasPx);
            int top = Math.round(rect.y * scaleBmpPxToCanvasPx);
            int right = left + Math.round(rect.width * scaleBmpPxToCanvasPx);
            int bottom = top + Math.round(rect.height * scaleBmpPxToCanvasPx);

            return new android.graphics.Rect(left, top, right, bottom);
        }

        @Override
        public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight,
                                float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext){
            Paint selectedPaint = new Paint();
            selectedPaint.setColor(Color.RED);
            selectedPaint.setStyle(Paint.Style.STROKE);
            selectedPaint.setStrokeWidth(scaleCanvasDensity * 4);

            Paint nonSelectedPaint = new Paint(selectedPaint);
            nonSelectedPaint.setColor(Color.GREEN);

            android.graphics.Rect drawRectangleLeft = makeGraphicsRect(rectLeft, scaleBmpPxToCanvasPx);
            android.graphics.Rect drawRectangleMiddle = makeGraphicsRect(rectMiddle, scaleBmpPxToCanvasPx);
            android.graphics.Rect drawRectangleRight = makeGraphicsRect(rectRight, scaleBmpPxToCanvasPx);

            switch (location) {
                case LEFT:
                    canvas.drawRect(drawRectangleLeft, selectedPaint);
                    canvas.drawRect(drawRectangleMiddle, nonSelectedPaint);
                    canvas.drawRect(drawRectangleRight, nonSelectedPaint);
                    break;
                case CENTER:
                    canvas.drawRect(drawRectangleLeft, nonSelectedPaint);
                    canvas.drawRect(drawRectangleMiddle, selectedPaint);
                    canvas.drawRect(drawRectangleRight, nonSelectedPaint);
                break;
                case RIGHT:
                    canvas.drawRect(drawRectangleLeft, nonSelectedPaint);
                    canvas.drawRect(drawRectangleMiddle, nonSelectedPaint);
                    canvas.drawRect(drawRectangleRight, selectedPaint);
                    break;
                }
            }
            public Location getLocation() {
                return location;
            }
            public enum Selected {
                NONE,
                LEFT,
                MIDDLE,
                RIGHT
            }
}

