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

@Autonomous(name = "Spike", preselectTeleOp = "ActualFinalCode")
public class SpikeBlue extends LinearOpMode {

    //DECLARES VARIABLES TO BE USED BUT DOESN'T GIVE THEM A VALUE
    private RealPropPipeline propPipeline;
    private Location propPlacement;

    public void runOpMode() throws InterruptedException {

        //WEBCAM STUFF

        //MAKES A NEW PROCESSOR CALLED PROPPIPELINE (FROM A DIFFERENT CLASS THAT CONTAINS
        //MACHINE LEARNING ALGORITHMS FOR THE FROGS
        propPipeline = new RealPropPipeline();
        //SETUP FOR WEBCAM
        VisionPortal myVisionPortal = new VisionPortal.Builder()
                //INITIALIZES WEBCAM
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                //Change processors
                .addProcessors(propPipeline)
                .setCameraResolution(new Size(640, 480))
                //CAMERA STREAMS BY SENDING JPEGS
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                //LETS YOU VIEW CAMERA STREAM LIVE
                .enableLiveView(true)
                //AUTO-STOPS LIVE STREAMING
                .setAutoStopLiveView(true)
                //RUNS CODE
                .build();

        //SETS ALLIANCE TO BLUE
        Globals.ALLIANCE = Location.BLUE;

        //MAKES AN INSTANCE OF THE DRIVE.JAVA CLASS THAT WILL BE REFERRED TO AS "drive"
        Drive drive = new Drive(hardwareMap, -36, 61, Math.toRadians(270));
        //MAKES AN INSTANCE OF THE ARM.JAVA CLASS THAT WILL BE REFERRED TO AS "arm"
        Arm arm = new Arm(hardwareMap);



        //DISPLAYS "INITIALIZING" WHILE CAMERA IS STILL GETTING READY
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
        Vector2d backdropPark = null;

        //SETS VARIABLE TO EITHER LEFT, MIDDLE, OR RIGHT DEPENDING ON CAMERA INPUT
        propPlacement = propPipeline.getLocation();

        //DECIDES PATH BASED ON PROP PLACEMENT
        switch (propPlacement) {
            case LEFT:
                //PIXEL POSITION
                tapeScoring = new Vector2d(-30,34);
                //ANGLE OF APPROACH
                tapeHeading = Math.toRadians(315);
                //BACKDROP POSITION
                backdropPark = new Vector2d(48,12);
                break;
            case CENTER:
                //PIXEL POSITION
                tapeScoring = new Vector2d(-36,30);
                //ANGLE OF APPROACH
                tapeHeading = Math.toRadians(270);
                //BACKDROP POSITION
                backdropPark = new Vector2d(48,12);
                break;
            case RIGHT:
                //PIXEL POSITION
                tapeScoring = new Vector2d(-42,34);
                //ANGLE OF APPROACH
                tapeHeading = Math.toRadians(225);
                //BACKDROP POSITION
                backdropPark = new Vector2d(48,12);
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
                            drive.tapePixel(tapeScoring,tapeHeading)
                            /*//WAITS 2 SECONDS
                            new SleepAction(1),
                            //PARKS AT THE SIDE USING "TOBACKPARK" METHOD IN "DRIVE.JAVA"
                            drive.audiencePark(backdropPark)*/

                    )
            );
            //SILLY LITTLE THINGS TO TRACK ROBOT ON DASHBOARD (NOT VERY IMPORTANT)
            TelemetryPacket packet = new TelemetryPacket();
            MecanumDrive.drawRobot(packet.fieldOverlay(), drive.getPose());
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
        }
    }
}
