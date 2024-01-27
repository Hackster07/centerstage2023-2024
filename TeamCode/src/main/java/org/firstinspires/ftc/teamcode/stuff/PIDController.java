package org.firstinspires.ftc.teamcode.stuff;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDController {

     double Kp;
     double Ki;
     double Kd;
     double integralSum = 0;
     double lastError = 0.0;

    ElapsedTime timer = new ElapsedTime();

    public PIDController(double Kp, double Ki, double Kd) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
    }

    public double update(double target, double state) {

        // calculate the error
        double error = target - state;

        // rate of change of the error
        double derivative = (error - lastError) / timer.seconds();

        // sum of all error over time
        integralSum = integralSum + (error * timer.seconds());

        double out = (Kp * error) + (Ki * integralSum) + (Kd * derivative);

        lastError = error;

        // reset the timer for next time
        timer.reset();

        return out;
    }
}
