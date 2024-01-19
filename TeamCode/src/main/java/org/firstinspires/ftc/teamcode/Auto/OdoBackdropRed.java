package org.firstinspires.ftc.teamcode.Auto;

import android.util.Size;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.processor.PropPipeline;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

@Autonomous
public class OdoBackdropRed extends LinearOpMode {

    private DcMotor armMotor;
    private Servo clawServo;
    private PropPipeline propPipeline;

    public void runOpMode() throws InterruptedException {

        AprilTagProcessor.Builder myAprilTagProcessorBuilder = new AprilTagProcessor.Builder();
        AprilTagLibrary myAprilTagLibrary = AprilTagGameDatabase.getCurrentGameTagLibrary();
        myAprilTagProcessorBuilder.setTagLibrary(myAprilTagLibrary);
        AprilTagProcessor myAprilTagProcessor = myAprilTagProcessorBuilder
                .setDrawTagID(true)
                .setLensIntrinsics(445.906,445.906,322.763,240.936)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .build();

        TfodProcessor myTfodProcessor = new TfodProcessor.Builder()
                .setMaxNumRecognitions(10)
                .setUseObjectTracker(true)
                .setTrackerMaxOverlap((float) 0.2)
                .setTrackerMinSize(16)
                .build();

        VisionPortal myVisionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessors(propPipeline)
                .setCameraResolution(new Size(1280, 720))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .enableLiveView(true)
                .setAutoStopLiveView(true)
                .build();

        Drive drive = new Drive(hardwareMap, 12, -61, Math.PI / 2);
        Arm arm = new Arm(hardwareMap);

        waitForStart();
        if(opModeIsActive()) {

            Actions.runBlocking(
                    new SequentialAction(
                            drive.redBackdrop(),
                            arm.extend(),
                            arm.clawOpen()
                    )
            );
        }
    }
}
