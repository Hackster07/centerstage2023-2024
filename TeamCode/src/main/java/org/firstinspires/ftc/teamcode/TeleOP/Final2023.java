package org.firstinspires.ftc.teamcode.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp
public class Final2023 extends LinearOpMode {


  private Servo planeServo;
  private Servo clawServo;
  private DcMotor leftFront;
  private DcMotor rightFront;
  private DcMotor leftBack;
  private DcMotor rightBack;
  private DcMotor armMotor;
  @Override

  public void runOpMode() {
    planeServo = hardwareMap.get(Servo.class, "planeServo");
    clawServo = hardwareMap.get(Servo.class, "clawServo");
    rightFront = hardwareMap.dcMotor.get("rightFront");
    leftBack = hardwareMap.dcMotor.get("leftBack");
    leftFront = hardwareMap.dcMotor.get("leftFront");
    rightBack = hardwareMap.dcMotor.get("rightBack");
    armMotor = hardwareMap.dcMotor.get("armMotor");
    //these wheels have the wrong rotation because of the belts
    rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
    rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
    //makes sure robot doesn't slide
    leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
   
    rotate();
    telemetry.update();
  }

  private void rotate() {
    waitForStart();
    /*puts the plane servo into loaded position. the range makes sure that
    it does not turn a full 180*/
    planeServo.setPosition(0.47);
    planeServo.scaleRange(0.47, 1);
    //opens the claw
    clawServo.setPosition(1);
    //makes it open only as much as we need it to
    clawServo.scaleRange(0.80,1);
    //sets to full speed
    double speed = 1;
    //sets to forward mode (claw in front)
    double direction = 1;

    if (opModeIsActive()) {
      while (opModeIsActive()) {
        //forward and backward (negative because of the way our wheels are laid out)
        double y = -gamepad1.left_stick_y*direction;
        //rotation
        double rx = gamepad1.right_stick_x * 1.1;
        //side to side
        double x = gamepad1.left_stick_x*direction;

        double denominator = Math.max(Math.abs(y)+Math.abs(x)+Math.abs(rx),1);
        //changes to half speed if pressed
        if (gamepad1.x){
          speed = 0.5;
        }
        //changes to full speed if pressed
        if (gamepad1.y){
          speed = 1;
        }
        //changes to forward mode if pressed
        if (gamepad1.a){
          direction = 1;
        }
        //changes to backward mode if pressed
        if (gamepad1.b){
          direction = -1;
        }
        //sets movement of robot (forward +- side to side +- rotation)
        leftFront.setPower(((y + x + rx) / denominator)*speed);
        leftBack.setPower(((y - x + rx) / denominator)*speed);
        rightFront.setPower(((y - x - rx) / denominator)*speed);
        rightBack.setPower(((y + x - rx) / denominator)*speed);
        //set variable for triggers; they return values from 0 to 1 depending on how much you press
        double leftTrigger = gamepad2.left_trigger;
        double rightTrigger = gamepad2.right_trigger;
        //right goes up and left goes down. divide by 2 for half speed arm
        double powerTotal = (rightTrigger - leftTrigger)/2;
        armMotor.setPower(powerTotal);
        //initialize variable for claw
        double claw = clawServo.getPosition();
        //open claw when you press a
        if (gamepad2.a) {
          clawServo.setPosition(claw+0.1);
        }
        //close claw when you press b
        if (gamepad2.b) {
          clawServo.setPosition(claw-0.1);
        }
        double plane = planeServo.getPosition();
        //launch plane on right bumper
        if (gamepad1.right_bumper) {
          planeServo.setPosition(plane + 0.1);
        }
        //reset plane on left bumper
        else if (gamepad1.left_bumper) {
          planeServo.setPosition(plane - 0.1);
        }
        telemetry.addData("Arm currently at",  " at %7d",
                armMotor.getCurrentPosition());
        telemetry.addData("Claw currently at",  " at %7f",
                clawServo.getPosition());
        telemetry.update();
      }
    }
  }
}
