package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.motors.NeveRest20Gearmotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class FirstTeleOp2022 extends OpMode {
    private DcMotor leftFront; // declares motors
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;

    private DcMotor lift;
    private CRServo liftarm;
    private DcMotor pulley;
    private CRServo ducks;

    private float leftPower, rightPower, xValue, yValue;

    @Override
    /* setup stuff (ex: setting servos to the right angle) */
    public void init() {
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");

        lift = hardwareMap.dcMotor.get("lift");
        liftarm = hardwareMap.crservo.get("liftarm");
        pulley = hardwareMap.dcMotor.get("pulley");
        ducks = hardwareMap.crservo.get("ducks");

        /*
        pulley = hardwareMap.servo.get("pulley");
        ducks = hardwareMap.crservo.get("ducks");
        grabber = hardwareMap.servo.get("grabber");
        */

        /*
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            -- makes the motor brake/lock in place
            -- good for motors for lifts/intakes

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            -- will not stop right away
            -- coast/glide to a stop
            -- default probably
        */



        pulley.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // must reverse one side of drivetrain

        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // for display on phone
        telemetry.addLine("Initialized");
        telemetry.addLine("Recursion my Beloved");
        telemetry.addData("LeftFront Power:", leftFront.getPower());
        telemetry.update();

    }

    @Override
    /* run when the driver presses play */
    public void loop() {
        // tank drive: left stick = left motors, right stick = right motors
        /*
        leftFront.setPower(-gamepad1.left_stick_y);
        leftBack.setPower(-gamepad1.left_stick_y);
        rightFront.setPower(-gamepad1.right_stick_y);
        rightBack.setPower(-gamepad1.right_stick_y);
        */

        // arcade drive??? Hopefully functions...
        yValue = gamepad1.right_stick_y * -1;
        xValue = gamepad1.right_stick_x * -1;

        leftPower =  yValue - xValue;
        rightPower = yValue + xValue;

        leftFront.setPower(Range.clip(leftPower, -1.0, 1.0)/2);
        leftBack.setPower(Range.clip(leftPower, -1.0, 1.0)/2);
        rightFront.setPower(Range.clip(rightPower, -1.0, 1.0)/2);
        rightBack.setPower(Range.clip(rightPower, -1.0, 1.0)/2);

        // strafe
        if (gamepad1.right_trigger > 0) {
            leftFront.setPower(1);
            leftBack.setPower(-1);
            rightFront.setPower(-1);
            rightBack.setPower(1);
        }

        if (gamepad1.left_trigger > 0) {
            leftFront.setPower(-1);
            leftBack.setPower(1);
            rightFront.setPower(1);
            rightBack.setPower(-1);
        }

        // duck spinner, both directions
        if (gamepad2.left_bumper) {
            ducks.setPower(1);
        } else if (gamepad2.right_bumper) {
            ducks.setPower(-1);
        } else {
            ducks.setPower(0);
        }

        // grabber arm
        if (gamepad2.a) {
            liftarm.setPower(1);
        } else if (gamepad2.b) {
            liftarm.setPower(-1);
        } {
            liftarm.setPower(0);
        }

        // pulley
        if (gamepad2.right_trigger > 0) {
            lift.setPower(1);
        } else if (gamepad2.left_trigger > 0) {
            lift.setPower(-1);
        } else {
            lift.setPower(0);
        }

        /*
        if (gamepad1.left_bumper) {
            ducks.setPower(1);
        } else if (gamepad1.right_bumper) {
            ducks.setPower(-1);
        } else {
            ducks.setPower(0);
        }

         */
        /*
        int servoPosition = 0;
        while (gamepad1.dpad_up) {
            servoPosition += 15;
            pulley.setPosition(servoPosition);
        }
        while (gamepad1.dpad_down) {
            servoPosition -= 15;
            pulley.setPosition(servoPosition);
        }

        double grabberPosition = 0;
        while (gamepad1.a) {
            grabberPosition += 5;
            grabber.setPosition(grabberPosition);
        }
        while (gamepad1.b) {
            grabberPosition -= 5;
            grabber.setPosition(grabberPosition);
        }
         */
    }
}
