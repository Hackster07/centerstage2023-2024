package org.firstinspires.ftc.teamcode.Auto.RobotBodyParts;


import static org.firstinspires.ftc.teamcode.stuff.Globals.ALLIANCE;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MecanumDrive;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Drive {
    private Pose2d beginPose;

    private MecanumDrive drive;



    public Drive(HardwareMap hardwareMap, int posX, int posY, Double posHeading){

        beginPose = new Pose2d(posX, posY, posHeading);
        drive = new MecanumDrive(hardwareMap, beginPose);
    }
    //METHOD "TAPEPIXEL" IS FED THE POSITION OF THE FROG AND THE ROTATION. THIS COMES FROM
    //EITHER ODOBACKDROPBLUE.JAVA OR ODOBACKDROPRED.JAVA
    //METHOD ONLY RUNS WHEN CALLED (REFERENCED)
    public Action tapePixel(Vector2d tape,double header){
        return new Action(){
            @Override
            //STARTS TRUE, RETURNS FALSE SO IT DOESN'T REPEAT
            public boolean run(@NonNull TelemetryPacket telemetryPacket){
                switch (ALLIANCE) {
                    //IF ON RED ALLIANCE
                    case RED:
                        Actions.runBlocking(
                                drive.actionBuilder(drive.pose)
                                        //GOES STRAIGHT TO Y=-48
                                        .lineToY(-48)
                                        //DRIVES TO COORDINATE AND ROTATES TO A POSITION SPECIFIED
                                        //IN THE OP MODES
                                        .splineTo(tape,header)
                                        //GOES STRAIGHT TO Y=-36
                                        .lineToY(-36)
                                        .build());
                        break;
                        //IF ON BLUE ALLIANCE
                    case BLUE:
                        Actions.runBlocking(
                                drive.actionBuilder(drive.pose)
                                        //GOES STRAIGHT TO Y=48
                                        .lineToY(48)
                                        .splineTo(tape,header)
                                        .lineToY(36)
                                        .build());
                        break;
                    default:
                        break;
                }
                return false;
            }
        };
    }

    public Pose2d getPose (){
        return drive.pose;
    }
    public Action toBackdrop(Vector2d backdrop){
        return new Action(){
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket){
                switch (ALLIANCE) {
                    case RED:
                        Actions.runBlocking(
                                drive.actionBuilder(drive.pose)
                                        .lineToY(-37)
                                        .splineTo(backdrop,Math.toRadians(0))
                                        .build());
                        break;
                    case BLUE:
                        Actions.runBlocking(
                                drive.actionBuilder(drive.pose)
                                        .lineToY(37)
                                        .splineTo(backdrop,Math.toRadians(0))
                                        .build());
                        break;
                }
                return false;
            }
        };
    }
    public Action toBackPark(){
        return new Action(){
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket){
                switch (ALLIANCE) {
                    case RED:
                        Actions.runBlocking(
                                drive.actionBuilder(drive.pose)
                                        .lineToX(46)
                                        .waitSeconds(.5)
                                        .strafeTo(new Vector2d(46,-63))
                                        .strafeTo(new Vector2d(50,-63))
                                        .build());
                        break;
                    case BLUE:
                        Actions.runBlocking(
                                drive.actionBuilder(drive.pose)
                                        .lineToX(46)
                                        .waitSeconds(.5)
                                        .strafeTo(new Vector2d(46,63))
                                        .strafeTo(new Vector2d(50,63))
                                        .build());
                        break;
                }
                return false;
            }
        };
    }

}
