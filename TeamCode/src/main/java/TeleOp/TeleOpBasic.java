package TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import Systems.Robot;

/**
 * Title: TeleOpBasic - Designed for FTC Decode 2025-26
 * Desc: Basic TeleOp for a 4-Motor Mecanum DriveTrain
 * Includes Driving, Strafing, and Rotating
 *
 * Controls (GamePad1):
 * - Left Analog X:      Strafes Robot Left and Right
 * - Left Analog Y:      Drives Robot Forward and Backwards
 * - Right Analog X:     Rotates Robot Left and Right
 * - Right Analog Y:     N/A
 * - Left Bumper:        Returns Function
 * - Left Trigger:       Returns Function
 * - Right Bumper:       Starts & Stops (Toggles) Fly Wheel
 * - Right Trigger:      Returns Function
 * - DPad Up:            Returns Function
 * - DPad Down:          Returns Function
 * - DPad Left:          Returns Function
 * - DPad Right:         Returns Function
 * - FaceButton Up:      Returns Function
 * - FaceButton Down:    Returns Function
 * - FaceButton Left:    Returns Function
 * - FaceButton Right:   Returns Function
 *
 * @Author Saavin
 * @Version 1.0
 */

@TeleOp(name = "Basic Mecanum", group = "TeleOp")
public class TeleOpBasic extends LinearOpMode {

    // Robot Instance
    private final Robot robot = new Robot();

    private boolean flyWheelOn = false;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);

        telemetry.addLine("Status: Initialized. Ready to start.");
        telemetry.update();

        // Wait for the Start button to be pressed on the Driver Station.
        waitForStart();

        while (opModeIsActive()) {

            double drive   = -gamepad1.left_stick_y;
            double strafe  =  gamepad1.left_stick_x;
            double rotate  =  gamepad1.right_stick_x;

            double frontLeftPower  = drive + strafe + rotate;
            double frontRightPower = drive - strafe - rotate;
            double backLeftPower   = drive - strafe + rotate;
            double backRightPower  = drive + strafe - rotate;

            // Prevents Motors from Exceeding 100% Power
            double[] powers = { frontLeftPower, frontRightPower, backLeftPower, backRightPower };
            double maxPower = 0.0;
            for (double p : powers) {
                maxPower = Math.max(maxPower, Math.abs(p));
            }

            if (maxPower > 1.0) {
                frontLeftPower  /= maxPower;
                frontRightPower /= maxPower;
                backLeftPower   /= maxPower;
                backRightPower  /= maxPower;
            }

            robot.driveTrain.mecDrive(frontLeftPower, frontRightPower, backLeftPower, backRightPower);

            if (drive == 0 && strafe == 0) {
                robot.driveTrain.brake();
            }

            // Fly Wheel Control
            if (gamepad1.rightBumperWasPressed()) {
                flyWheelOn = !flyWheelOn;
            }

            int rotations = 25;
            robot.scoringMechanisms.flyWheel.setVelocity(flyWheelOn ? (rotations * (360)) : (0), AngleUnit.DEGREES);

            double velocity = 5 * robot.scoringMechanisms.flyWheel.getVelocity(AngleUnit.DEGREES);
            telemetry.addData("Flywheel", flyWheelOn ? "(ON)" : "(OFF)");
            telemetry.addData("Target Velocity (deg/s)", flyWheelOn ? (rotations * (5 * 360)) : (0));
            telemetry.addData("Measured Velocity (deg/s)", velocity);
            telemetry.addData("Measured Velocity (rev/s)", velocity / 360.0);
            telemetry.update();

//            telemetry.addData("Flywheel", flyWheelOn ? ("(ON) Speed at " + robot.scoringMechanisms.flyWheel.getVelocity(AngleUnit.DEGREES) + " per second") : ("(OFF) Speed at " + robot.scoringMechanisms.flyWheel.getVelocity(AngleUnit.DEGREES) + " per second"));
//            telemetry.update();

        }
    }
}
