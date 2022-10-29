package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

@Autonomous
public class FirstColorSensorAuto extends LinearOpMode {
    private DcMotor lf;
    private DcMotor lb;
    private DcMotor rf;
    private DcMotor rb;

    NormalizedColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        lf = hardwareMap.dcMotor.get("LF");
        lb = hardwareMap.dcMotor.get("LB");
        rf = hardwareMap.dcMotor.get("RF");
        rb = hardwareMap.dcMotor.get("RB");

        lf.setDirection(DcMotorSimple.Direction.FORWARD);
        lb.setDirection(DcMotorSimple.Direction.FORWARD);
        rf.setDirection(DcMotorSimple.Direction.REVERSE);
        rb.setDirection(DcMotorSimple.Direction.REVERSE);

        double red = 0.12;
        double blue = 0.15;
        double grey = 0.08;
        double white = 0.6;

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color");
        colorSensor.setGain(15);

        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight)colorSensor).enableLight(false);
        }

        while (opModeInInit()) {

            // Send telemetry message to signify robot waiting;
            telemetry.addData("Status", "Ready to drive to white line.");    //

            // Display the light level while we are waiting to start
            getBrightness();
        }

        lf.setPower(0.3);
        lb.setPower(0.3);
        rf.setPower(0.3);
        rb.setPower(0.3);

        while(opModeIsActive() && (getBrightness() <= blue)) {
            /*
            if (getBrightness() > red) {
                telemetry.addData("red", getBrightness());

            } else if (getBrightness() > blue) {
                telemetry.addData("blue", getBrightness());

            } else if (getBrightness() > grey) {
                telemetry.addData("grey", getBrightness());

            } else if (getBrightness() > white) {
                telemetry.addData("white", getBrightness());
                break;
            }
            */
            sleep(5);
        }

        lf.setPower(0);
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);
    }

    double getBrightness() {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        telemetry.addData("Light Level (0 to 1)",  "%4.2f", colors.alpha);
        telemetry.update();

        return colors.alpha;
    }
}
