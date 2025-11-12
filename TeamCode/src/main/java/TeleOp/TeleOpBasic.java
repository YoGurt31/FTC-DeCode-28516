package TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

import Systems.Robot;

/**
 * Title: TeleOpBasic - Designed for FTC Decode 2025-26
 * Desc: Basic TeleOp for a 4-Motor Mecanum DriveTrain
 * Includes Driving, Strafing, and Rotating
 * <p>
 * Controls (GamePad1):
 * - Left Analog X:      Rotates Robot Left and Right
 * - Left Analog Y:      N/A
 * - Right Analog X:     Strafes Robot Left and Right
 * - Right Analog Y:     Drives Robot Forward and Backwards
 * - Left Bumper:        Returns Function
 * - Left Trigger:       Returns Function
 * - Right Bumper:       Starts & Stops (Toggles) Fly Wheel
 * - Right Trigger:      Returns Function
 * - DPad Up:            Increases FlyWheel RPS by 1
 * - DPad Down:          Decreases FlyWheel RPS by 1
 * - DPad Left:          Returns Function
 * - DPad Right:         Returns Function
 * - FaceButton Up:      Returns Function
 * - FaceButton Down:    Returns Function
 * - FaceButton Left:    Returns Function
 * - FaceButton Right:   Returns Function
 *
 * @Author Saavin
 * @Version 1.5
 */

@TeleOp(name = "Basic Mecanum", group = "TeleOp")
public class TeleOpBasic extends LinearOpMode {

    // Robot Instance
    private final Robot robot = new Robot();

    // FlyWheel Variables
    private boolean flyWheelOn = false;
    private int RPS = 25;
    private static final double TicksPerRev = 145.1;
    private double gearRatioMotorToFlyWheel = 0.2;
    private double gearRatioFlyWheelToMotor = 5.0;

    // AprilTag / Vision Variables
    final double distance = 12.0; //  this is how close the camera should get to the target (inches)

    final double driveGain  =  0.020;   //  Forward Speed Control "Gain". e.g. Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double strafeGain =  0.015;   //  Strafe Speed Control "Gain".  e.g. Ramp up to 37% power at a 25 degree Yaw error.   (0.375 / 25.0)
    final double rotateGain =  0.010;   //  Turn Control "Gain".  e.g. Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double maxDrive  = 0.50;   //  Clip the approach speed to this max value (adjust for your robot)
    final double maxStrafe = 0.50;   //  Clip the strafing speed to this max value (adjust for your robot)
    final double maxRotate = 0.25;   //  Clip the turn speed to this max value (adjust for your robot)

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);
        FtcDashboard.getInstance().startCameraStream(robot.vision.visionPortal, 20);

        double drive = 0, strafe = 0, rotate = 0;
        boolean targetFound = false;

        telemetry.addLine("Status: Initialized. Ready to start.");
        telemetry.update();

        // Wait for the Start button to be pressed on the Driver Station.
        waitForStart();

        while (opModeIsActive()) {

            // AprilTag Targeting
            boolean activeTargeting = gamepad1.left_bumper;

            List<AprilTagDetection> currentDetections = robot.vision.aprilTag.getDetections();
            for (AprilTagDetection detection : currentDetections) {
                if (detection.metadata != null) {
                    if (detection.id == -1) {
                        targetFound = true;
                        robot.vision.desiredTag = detection;
                        break;
                    }
                } else {
                    telemetry.addData("Unknown", "Tag ID %d is not in TagLibrary", detection.id);
                }
            }

            if (targetFound) {
                telemetry.addData("Found", "ID %d (%s)", robot.vision.desiredTag.id, robot.vision.desiredTag.metadata.name);
                telemetry.addData("Range",  "%5.1f inches", robot.vision.desiredTag.ftcPose.range);
                telemetry.addData("Bearing","%3.0f degrees", robot.vision.desiredTag.ftcPose.bearing);
                telemetry.addData("Yaw","%3.0f degrees", robot.vision.desiredTag.ftcPose.yaw);
            }

            if (activeTargeting && targetFound) {
                double rangeError    = (robot.vision.desiredTag.ftcPose.range - distance);
                double headingError  = (robot.vision.desiredTag.ftcPose.bearing);
                double yawError      = (robot.vision.desiredTag.ftcPose.yaw);

                drive  = Range.clip(rangeError * driveGain, -maxDrive, maxDrive);
                strafe = Range.clip(headingError * strafeGain, -maxStrafe, maxStrafe);
                rotate = Range.clip(yawError * rotateGain, -maxRotate, maxRotate);
                
                telemetry.addData("Auto","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, rotate);
            } else {
                drive  = -gamepad1.right_stick_y;
                strafe = -gamepad1.right_stick_x;
                rotate = -gamepad1.left_stick_x;

                telemetry.addData("Manual","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, rotate);
            }

            robot.driveTrain.mecDrive(drive, strafe, rotate);

            // Intake Control
            if (gamepad1.dpad_left/*gamepad1.right_trigger != 0*/) {
                robot.scoringMechanisms.rollerIntake.setPower(0.75);
            } else if (gamepad1.dpad_right/*gamepad1.left_trigger != 0*/) {
                robot.scoringMechanisms.rollerIntake.setPower(-0.75);
            } else {
                robot.scoringMechanisms.rollerIntake.setPower(0.0);
            }

            // Fly Wheel Control
            if (gamepad1.rightBumperWasPressed()) {
                flyWheelOn = !flyWheelOn;
            }

            if (gamepad1.dpadUpWasPressed() && (RPS < 300)) {
                RPS++;
            }

            if (gamepad1.dpadDownWasPressed() && (RPS > 0)) {
                RPS--;
            }

            double targetFlywheelRps = flyWheelOn ? RPS : 0.0;
            double measuredFlywheelRps = (robot.scoringMechanisms.flyWheel.getVelocity() / TicksPerRev) * gearRatioFlyWheelToMotor;
            robot.scoringMechanisms.flyWheel.setVelocity((targetFlywheelRps * TicksPerRev) * gearRatioMotorToFlyWheel);

            telemetry.addData("Flywheel", flyWheelOn ? "(ON)" : "(OFF)");
            telemetry.addData("Target Velocity (RPS)", targetFlywheelRps);
            telemetry.addData("Measured Velocity (RPS)", measuredFlywheelRps);
            telemetry.update();

        }

        if (robot.vision.visionPortal != null) {
            try { robot.vision.visionPortal.close(); } catch (Exception ignored) {}
        }
    }
}