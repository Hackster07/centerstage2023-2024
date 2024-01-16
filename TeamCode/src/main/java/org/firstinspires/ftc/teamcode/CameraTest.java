package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.processor.RedPropThreshold;
import org.firstinspires.ftc.vision.VisionPortal;

@Autonomous()
public class CameraTest extends OpMode {
    private RedPropThreshold visionProcessor;
    private VisionPortal visionPortal;
    @Override
    public void init() {
        visionProcessor = new RedPropThreshold();
        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), visionProcessor);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
        telemetry.addData("Identified", visionProcessor.getSelection());
    }
}
