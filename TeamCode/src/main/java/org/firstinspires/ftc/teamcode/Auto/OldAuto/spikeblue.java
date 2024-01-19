package org.firstinspires.ftc.teamcode.Auto.OldAuto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

//Undisable if you want to use code
@Disabled
@Autonomous(name = "spikeblue", preselectTeleOp = "Final2023")
public class spikeblue extends LinearOpMode {

    private Servo clawServo;
    private DcMotor rightBack;
    private DcMotor rightFront;
    private DcMotor armMotor;
    private DcMotor leftFront;
    private DcMotor leftBack;


    /**
     * This function is executed when this OpMode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");

        // Put initialization blocks here.
        clawServo.setPosition(0.80);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        waitForStart();
        if (opModeIsActive()) {
            // Put run blocks here.
            while (opModeIsActive()) {
                //move forward
                leftFront.setPower(0.4);
                rightBack.setPower(0.4);
                leftBack.setPower(0.4);
                rightFront.setPower(0.4);
                sleep(1850);
                //stop
                rightBack.setPower(0);
                leftBack.setPower(0);
                leftFront.setPower(0);
                rightFront.setPower(0);
                sleep(100);
                //rotate
                leftFront.setPower(0.3);
                rightBack.setPower(-0.3);
                leftBack.setPower(0.3);
                rightFront.setPower(-0.3);
                sleep(2300);
                //move to backdrop
                leftFront.setPower(0.4);
                rightBack.setPower(0.4);
                leftBack.setPower(0.4);
                rightFront.setPower(0.4);
                sleep(2800);
                // position arm.
                armMotor.setPower(-0.5);
                leftFront.setPower(0.0);
                rightBack.setPower(0.0);
                leftBack.setPower(0.0);
                rightFront.setPower(0.0);
                sleep(1975);
                //move to backdrop
                armMotor.setPower(0.01);
                leftFront.setPower(0.4);
                rightBack.setPower(0.4);
                leftBack.setPower(0.4);
                rightFront.setPower(0.4);
                sleep(3500);
                //drop pixel
                leftFront.setPower(0);
                rightFront.setPower(0);
                rightBack.setPower(0);
                leftBack.setPower(0);
                clawServo.setPosition(1);
                sleep(400);
                //back up
                rightBack.setPower(-0.2);
                leftBack.setPower(-0.2);
                leftFront.setPower(-0.2);
                rightFront.setPower(-0.2);
                sleep(900);
                telemetry.update();
                requestOpModeStop();
            }
        }
    }
}