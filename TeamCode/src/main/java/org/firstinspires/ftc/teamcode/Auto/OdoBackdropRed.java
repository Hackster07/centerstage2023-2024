package org.firstinspires.ftc.teamcode.Auto;

import android.util.Size;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Auto.RobotBodyParts.Arm;
import org.firstinspires.ftc.teamcode.Auto.RobotBodyParts.Drive;
import org.firstinspires.ftc.teamcode.processor.PropPipeline;
import org.firstinspires.ftc.teamcode.stuff.Globals;
import org.firstinspires.ftc.teamcode.stuff.Location;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

@Autonomous(name = "Red Backdrop", preselectTeleOp = "Final2023")
public class OdoBackdropRed extends LinearOpMode {

    private PropPipeline propPipeline;
    private Location propPlacement;

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
                //Change processors
                .addProcessors(propPipeline)
                .setCameraResolution(new Size(1280, 720))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .enableLiveView(true)
                .setAutoStopLiveView(true)
                .build();

        Globals.ALLIANCE = Location.RED;

        Drive drive = new Drive(hardwareMap, 12, -61, Math.toRadians(90));
        Arm arm = new Arm(hardwareMap);

        Vector2d tapeScoring = null;
        double tapeHeading = Math.toRadians(90);
        Vector2d backdropScoring = null;

        //Vision generated
        switch (propPlacement) {
            case LEFT:
                tapeScoring = new Vector2d(6,-34);
                tapeHeading = Math.toRadians(135);
                backdropScoring = new Vector2d(48,-30);
                break;
            case CENTER:
                tapeScoring = new Vector2d(12,-30);
                tapeHeading = Math.toRadians(90);
                backdropScoring = new Vector2d(48,-36);
                break;
            case RIGHT:
                tapeScoring = new Vector2d(18,-30);
                tapeHeading = Math.toRadians(45);
                backdropScoring = new Vector2d(48,-42);
                break;
            default:
                break;
        }
        myVisionPortal.close();

        if(opModeIsActive()) {
            Actions.runBlocking(
                    new SequentialAction(
                            drive.tapePixel(tapeScoring,tapeHeading),
                            new ParallelAction(
                                    drive.toBackdrop(backdropScoring),
                                    arm.extend()
                            ),
                            new SleepAction(.5),
                            arm.clawOpen(),
                            new SleepAction(1),
                            drive.toBackPark()
                    )
            );
        }
    }
}
