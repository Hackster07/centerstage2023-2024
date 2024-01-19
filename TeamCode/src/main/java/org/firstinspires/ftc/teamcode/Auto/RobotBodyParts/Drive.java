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

    public Action tapePixel(Vector2d tape,double header){
        return new Action(){
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket){
                switch (ALLIANCE) {
                    case RED:
                        Actions.runBlocking(
                                drive.actionBuilder(drive.pose)
                                        .lineToY(-48)
                                        .splineTo(tape,header)
                                        .lineToY(-36)
                                        .build());
                        break;
                    case BLUE:
                        Actions.runBlocking(
                                drive.actionBuilder(drive.pose)
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
                                        .splineTo(backdrop,Math.toRadians(180))
                                        .build());
                        break;
                    default:
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
                                        .strafeTo(new Vector2d(46,-60))
                                        .strafeTo(new Vector2d(54,-60))
                                        .build());
                        break;
                    case BLUE:
                        Actions.runBlocking(
                                drive.actionBuilder(drive.pose)
                                        .lineToX(46)
                                        .waitSeconds(.5)
                                        .strafeTo(new Vector2d(46,60))
                                        .strafeTo(new Vector2d(54,60))
                                        .build());
                        break;
                    default:
                        break;
                }
                return false;
            }
        };
    }

}
