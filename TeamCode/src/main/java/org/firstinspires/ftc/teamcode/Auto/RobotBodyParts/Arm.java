package org.firstinspires.ftc.teamcode.Auto.RobotBodyParts;


import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.stuff.PIDController;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Arm {
    private DcMotorEx armMotor;
    private Servo clawServo;
    private Telemetry telemetry;




    public Arm(HardwareMap hardwareMap){
        armMotor = hardwareMap.get(DcMotorEx.class, "armMotor");
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        clawServo.setPosition(0.8);
    }

    public Action clawOpen(){
        return new Action(){
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                clawServo.setPosition(1.0);
                return true;
            }
        };
    }

    public Action extend(){
        return new Action(){

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket){
                armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                armMotor.setTargetPosition(-2281);
                armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armMotor.setPower(-0.3);

                while(armMotor.getCurrentPosition() > -2281 && armMotor.isBusy()){
                }

                armMotor.setPower(0);
                armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


                return armMotor.getCurrentPosition() >= -2200;

            }
        };
    }
    public Action extendPID(){
        return new Action(){

            PIDController control = new PIDController(0.6,1.2,0.075);

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket){

                armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                double command = control.update(-2281,armMotor.getCurrentPosition());

                // assign motor the PID output
                armMotor.setPower(command);

                return armMotor.getCurrentPosition() >= -2281;
            }
        };
    }
}
