package org.firstinspires.ftc.teamcode.Auto;


import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MecanumDrive;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Drive {
    private Pose2d beginPose;

    private MecanumDrive drive;

    public Drive(HardwareMap hardwareMap, int posX, int posY, Double posHeading){

        beginPose = new Pose2d(posX, posY, posHeading);
        drive = new MecanumDrive(hardwareMap, beginPose);
    }
    public Action redBackdrop(){
        return new Action(){
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket){
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                .lineToY(-30)
                                .lineToY(-37)
                                .turn(Math.toRadians(90))
                                .lineToX(49)
                                .build()
                );
                return false;
            }
        };
    }
}
