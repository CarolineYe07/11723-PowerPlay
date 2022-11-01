package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Autonomous
public class VuforiaExampleStolenFromInternet extends LinearOpMode {
    private VuforiaLocalizer vuforiaLocalizer;
    private VuforiaLocalizer.Parameters parameters;
    private VuforiaTrackables visionTargets;
    private VuforiaTrackable target;
    private VuforiaTrackableDefaultListener listener;

    private OpenGLMatrix lastKnownLocation;
    private OpenGLMatrix phoneLocation;

    public static final String VUFORIA_KEY =
            "ASe2iwz/////AAABmeHBsAW2uUcNt5jOrRunotQpfDI92yTXWsUz3Hr4vE4HA5uZHCa8wHoHvgflwDmAvbiprx9okp4Hubfx6GGKvMoF1QjZXsQemk6tta2n2SoC3s7aUx++tyy+866va7KsJFKMbQ6Zy73N5TA7OwvbJzwp1wfB/CoaiRccCQWGfN3DipfdAuu0namlhr++Ui/p90Fvp2ErUiM0zgSF/5IlBnTEu2nzGEbz3Y66VqK/pqVY5bp7+3opF1IlpTcv9oXW7ihjkiwk2KqboGRnEB2Hq2m2N6YEi2G+6HrV8lWAei1NEhwCy43/v9uDpB2L487S22B48B+3PEumiT4GBoaMGrlXp7A9AUVo3RxZSQM8chCN";

    // keep track of these to display them
    private float robotX = 0;
    private float robotY = 0;
    private float robotAngle = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        setupVuforia();

        lastKnownLocation = createMatrix(0,0,0,0,0,0);

        waitForStart();

        visionTargets.activate();

        while(opModeIsActive()) {
            OpenGLMatrix latestLocation = listener.getUpdatedRobotLocation();

            // returns null if vuforia loses track of target, want to make sure that doesn't happen because it will casue an error
            if (latestLocation != null) {
                lastKnownLocation = latestLocation;

                float[] coordinates = lastKnownLocation.getTranslation().getData();

                robotX = coordinates[0];
                robotY = coordinates[1];
                robotAngle = Orientation.getOrientation(lastKnownLocation, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;

                telemetry.addData("Tracking", target.getName(), listener.isVisible());
                telemetry.addData("Last Known Location", formatMatrix(lastKnownLocation));
            }
        }
    }

    public void setupVuforia() {
        // set up parameters for vuforia localizer
        parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId); // cameraMonitorViewId turns on the display on the phone
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.useExtendedTracking = false; // optional thing to do for more accuracy (I think?)
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);

        // put in string name of navigation target in xml file which is supposed to be in the assets folder but isn't. Gotta sort that out at some point
        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset("FTC_2016-17");
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4); // change targets it's looking for

        // set up vision target like this
        target = visionTargets.get(0); // input index in xml file
        target.setName("Wheels Target");
        // do math and measurement stuff to figure out coords, keep in mind rotation order
        target.setLocation(createMatrix(0, 0, 0, 0, 0, 0));

        // similar method to tell phone's location on robot
        // ^ see setting target location
        phoneLocation = createMatrix(0, 0, 0, 0, 0, 0);

        // must be typecast
        listener = (VuforiaTrackableDefaultListener) target.getListener();
        listener.setPhoneInformation(phoneLocation, parameters.cameraDirection);
    }

    /*
    Method to create a matrix that takes a coordinate given by XYZ and rotations UVW
    No idea how this works --> assume it's magic and move on
     */
    public OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w) {
        return OpenGLMatrix.translation(x, y, z).
                multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, u, v, w));
    }

    // format matrices into strings
    public String formatMatrix(OpenGLMatrix matrix) {
        return matrix.formatAsTransform();
    }
}
