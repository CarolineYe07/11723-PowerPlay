package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Autonomous
public class VuforiaVisionTest extends LinearOpMode {
    private DcMotor lf;
    private DcMotor lb;
    private DcMotor rf;
    private DcMotor rb;

    private WebcamName webcam;

    private OpenGLMatrix lastLocation   = null;
    private VuforiaLocalizer vuforia    = null;
    private VuforiaTrackables targets   = null;

    private static final String VUFORIA_KEY = "ASe2iwz/////AAABmeHBsAW2uUcNt5jOrRunotQpfDI92yTXWsUz3Hr4vE4HA5uZHCa8wHoHvgflwDmAvbiprx9okp4Hubfx6GGKvMoF1QjZXsQemk6tta2n2SoC3s7aUx++tyy+866va7KsJFKMbQ6Zy73N5TA7OwvbJzwp1wfB/CoaiRccCQWGfN3DipfdAuu0namlhr++Ui/p90Fvp2ErUiM0zgSF/5IlBnTEu2nzGEbz3Y66VqK/pqVY5bp7+3opF1IlpTcv9oXW7ihjkiwk2KqboGRnEB2Hq2m2N6YEi2G+6HrV8lWAei1NEhwCy43/v9uDpB2L487S22B48B+3PEumiT4GBoaMGrlXp7A9AUVo3RxZSQM8chCN";


    @Override
    public void runOpMode() throws InterruptedException {
        lf = hardwareMap.dcMotor.get("LF");
        lb = hardwareMap.dcMotor.get("LB");
        rf = hardwareMap.dcMotor.get("RF");
        rb = hardwareMap.dcMotor.get("RB");

        webcam = hardwareMap.get(WebcamName.class, "Webcam 1");

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = webcam;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        targets = this.vuforia.loadTrackablesFromAsset("UltimateGoal");
        targets.get(0).setName("BlueTowerGoal");
        targets.get(1).setName("RedTowerGoal");
        targets.get(2).setName("RedAlliance");
        targets.get(3).setName("BlueAlliance");
        targets.get(4).setName("FrontWall");

        VuforiaTrackableDefaultListener blueTower = (VuforiaTrackableDefaultListener) targets.get(0).getListener();
        VuforiaTrackableDefaultListener redTower = (VuforiaTrackableDefaultListener) targets.get(1).getListener();
        VuforiaTrackableDefaultListener redAlliance = (VuforiaTrackableDefaultListener) targets.get(2).getListener();
        VuforiaTrackableDefaultListener blueAlliance = (VuforiaTrackableDefaultListener) targets.get(3).getListener();
        VuforiaTrackableDefaultListener frontWall = (VuforiaTrackableDefaultListener) targets.get(4).getListener();

        targets.activate();

        waitForStart();

        while (opModeIsActive()) {
            if (blueTower.getPose() != null) {
                telemetry.addLine("Blue tower goal");

            } else if (redTower.getPose() != null) {
                telemetry.addLine("Red tower goal");

            } else if (redAlliance.getPose() != null) {
                telemetry.addLine("Red alliance");

            } else if (blueAlliance.getPose() != null) {
                telemetry.addLine("Blue alliance");

            } else if (frontWall.isVisible()) {
                telemetry.addLine("Front wall");

            } else {
                telemetry.addLine("No nav images seen");
            }

            telemetry.update();
        }

    }
}
