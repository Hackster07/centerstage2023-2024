package org.firstinspires.ftc.teamcode.Auto;

import android.util.Size;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
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
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.processor.RealPropPipeline;
import org.firstinspires.ftc.teamcode.stuff.Globals;
import org.firstinspires.ftc.teamcode.stuff.Location;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

@Autonomous(name = "Blue Backdrop", preselectTeleOp = "ActualFinalCode")
public class OdoBackdropBlue extends LinearOpMode {

    private RealPropPipeline propPipeline;
    private Location propPlacement;

    public void runOpMode() throws InterruptedException {


        propPipeline = new RealPropPipeline();
        VisionPortal myVisionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                //Change processors
                .addProcessors(propPipeline)
                .setCameraResolution(new Size(640, 480))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .enableLiveView(true)
                .setAutoStopLiveView(true)
                .build();

        Globals.ALLIANCE = Location.BLUE;

        Drive drive = new Drive(hardwareMap, 12, 61, Math.toRadians(270));
        Arm arm = new Arm(hardwareMap);



        //DISPLAYS "INITIALIZING" WHILE CAMERA IS STILL INITIALIZING
        while (myVisionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addLine("initializing... please wait");
            telemetry.update();
        }
        //RETURNS PROP POSITION: LEFT, MIDDLE, & RIGHT
        while (opModeInInit()) {
            telemetry.addLine("ready");
            telemetry.addData("position", propPipeline.getLocation());
            telemetry.update();
        }
        //DECLARES VARIABLES
        Vector2d tapeScoring = null;
        double tapeHeading = Math.toRadians(270);
        Vector2d backdropScoring = null;

        //SETS VARIABLE TO EITHER LEFT, MIDDLE, OR RIGHT DEPENDING ON CAMERA INPUT
        propPlacement = propPipeline.getLocation();

        //DECIDES PATH BASED ON PROP PLACEMENT
        switch (propPlacement) {
            case LEFT:
                //PUTS PIXEL ON TAPE
                tapeScoring = new Vector2d(18,34);
                //TURNS
                tapeHeading = Math.toRadians(315);
                //GOES TO BACKDROP
                backdropScoring = new Vector2d(48.5,38);
                break;
            case CENTER:
                //PUTS PIXEL ON TAPE
                tapeScoring = new Vector2d(12,30);
                //TURNS
                tapeHeading = Math.toRadians(270);
                //GOES TO BACKDROP
                backdropScoring = new Vector2d(48.5,32);
                break;
            case RIGHT:
                //PUTS PIXEL ON TAPE
                tapeScoring = new Vector2d(6,34);
                //TURNS
                tapeHeading = Math.toRadians(225);
                //GOES TO BACKDROP
                backdropScoring = new Vector2d(48.5,26);
                break;
            //DEFAULTS TO RIGHT IF NOTHING DETECTED
            default:
                break;
        }
        //DISABLES CAMERA
        myVisionPortal.close();

        //THE REAL CODE FOR THE PATH FOLLOWED
        if(opModeIsActive()) {
            Actions.runBlocking(
                    //"SEQUENTIAL ACTION" RUNS ALL COMMANDS ONE AT A TIME
                    new SequentialAction(
                            //CALLS "TAPEPIXEL" METHOD IN "DRIVE.JAVA" CLASS
                            //FEEDS IT TWO VARIABLES: THE TARGET TAPE POSITION AND HOW MUCH TO ROTATE
                            drive.tapePixel(tapeScoring,tapeHeading),
                            //WAITS 2 SECONDS
                            new SleepAction(1),
                            //"PARALLEL ACTION" DOES BOTH ACTIONS AT THE SAME TIME
                            new ParallelAction(
                                    //DRIVES TO BACKDROP USING "TOBACKDROP" METHOD IN "DRIVE.JAVA"
                                    drive.toBackdrop(backdropScoring),
                                    //EXTENDS ARM USING "EXTEND" METHOD IN "ARM.JAVA"
                                    arm.extend()
                            ),
                            //WAITS FOR .5 SECONDS
                            new SleepAction(.5),
                            //OPENS CLAW USING "CLAWOPEN" METHOD IN "ARM.JAVA"
                            arm.clawOpen(),
                            //WAITS 1 SECOND
                            new SleepAction(1),
                            //PARKS AT THE SIDE USING "TOBACKPARK" METHOD IN "DRIVE.JAVA"
                            drive.toBackPark()

                    )
            );
            TelemetryPacket packet = new TelemetryPacket();
            MecanumDrive.drawRobot(packet.fieldOverlay(), drive.getPose());
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
        }
    }
}
