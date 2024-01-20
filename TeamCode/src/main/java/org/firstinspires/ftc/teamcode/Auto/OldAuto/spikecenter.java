package org.firstinspires.ftc.teamcode.Auto.OldAuto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

//Undisable if you want to use code
@Disabled
@Autonomous(name = "spikecenter (Blocks to Java)", group = "left", preselectTeleOp = "Final2023")
public class spikecenter extends LinearOpMode {

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
    clawServo.setPosition(0.85);
    rightBack.setDirection(DcMotor.Direction.REVERSE);
    rightFront.setDirection(DcMotor.Direction.REVERSE);
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        armMotor.setPower(0);
        // Put loop blocks here.
        leftFront.setPower(0.4);
        rightBack.setPower(0.4);
        leftBack.setPower(0.4);
        rightFront.setPower(0.4);
        sleep(1550);
        leftBack.setPower(0.2);
        rightBack.setPower(-0.2);
        leftFront.setPower(0.2);
        rightFront.setPower(-0.2);
        sleep(700);
        rightFront.setPower(0.4);
        rightBack.setPower(0.4);
        leftBack.setPower(0.4);
        rightFront.setPower(0.4);
        sleep(600);
        rightBack.setPower(-0.2);
        leftBack.setPower(-0.2);
        leftFront.setPower(-0.2);
        leftBack.setPower(-0.2);
        sleep(900);
        telemetry.update();
        requestOpModeStop();
      }
    }
  }
}
