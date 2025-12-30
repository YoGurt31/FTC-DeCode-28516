//package Auton;

//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import Systems.Robot;
//
//@Autonomous(name = "Basic Auto (3 Shots)", group = "Robot")
//public class BasicAuto extends LinearOpMode {
//
//    private final Robot robot = new Robot();
//
//    private final ElapsedTime runtime = new ElapsedTime();
//    @Override
//    public void runOpMode() throws InterruptedException {
//
//        robot.init(hardwareMap);
//
//        waitForStart();
//        if (!opModeIsActive()) return;
//
//        // Drive Backwards
//        frontLeft = hardwareMap.get(DcMotor.class,"fW");
//        frontRight = hardwareMap.get(DcMotor.class,"fR");
//
//
//
//    }
//    private void waitms(long ms){
//        runtime.reset();
//        while (opModeIsActive() && runtime.milliseconds() <= ms){
//            idle();
//        }
//
//    }
//
//
//}
//version 2
//package Auton;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//
//@Autonomous(name="Mecanum Autonomous", group="Autonomous")
//public class BasicAuto extends LinearOpMode {
//
//    // Declare OpMode members
//    private DcMotor frontLeft, frontRight, backLeft, backRight;
//
//    @Override
//    public void runOpMode() {
//        // Initialize motors
//        frontLeft = hardwareMap.get(DcMotor.class, "fL");
//        frontRight = hardwareMap.get(DcMotor.class, "fR");
//        backLeft = hardwareMap.get(DcMotor.class, "bL");
//        backRight = hardwareMap.get(DcMotor.class, "bR");
//
//        // Set motor directions (adjust based on testing)
//        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
//        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
//        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
//        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
//
//        // Tells the robot to stop when the time of moving backwards is finished
//        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//
//        // Set all motors to run using encoders for accurate movement
//        setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        // Send telemetry message to signify robot waiting
//        telemetry.addData("Status", "Initialized");
//        telemetry.update();
//
//        // Wait for the game to start (Driver Station Press Play)
//        waitForStart();
//
//        // --- Autonomous commands go here ---
//        // Example: Move forward for a certain duration or distance
//        moveRobot(0.5, 0, 0, 1000); // Backward at half power for 1 second
//        // Add more commands as needed for your specific path
//    }
//
//    // Helper method to set motor modes
//    public void setMotorMode(DcMotor.RunMode mode) {
//        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//    }
//
//    // Helper method for movement (simplified, without full PID/encoder targeting)
//    public void moveRobot(double drive, double strafe, double turn, long durationMs) {
//        double frontLeftPower = drive + strafe + turn;
//        double frontRightPower = drive - strafe - turn;
//        double backLeftPower = drive - strafe + turn;
//        double backRightPower = drive + strafe - turn;
//
//        // Normalize powers to stay within -1 to 1 limits
//        double max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
//        max = Math.max(max, Math.abs(backLeftPower));
//        max = Math.max(max, Math.abs(backRightPower));
//
//        if (max > 1.0) {
//            frontLeftPower /= max;
//            frontRightPower /= max;
//            backLeftPower /= max;
//            backRightPower /= max;
//        }
//
//        frontLeft.setPower(frontLeftPower);
//        frontRight.setPower(frontRightPower);
//        backLeft.setPower(backLeftPower);
//        backRight.setPower(backRightPower);
//
//        // Wait for the specified duration
//        sleep(1000);
//
//        // Stop motors after duration
//        stopRobot();
//    }
//
//    public void stopRobot() {
//        frontLeft.setPower(0);
//        frontRight.setPower(0);
//        backLeft.setPower(0);
//        backRight.setPower(0);
//    }
//}
//




//version 3
//package Auton;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import Systems.Robot;
//
//@Autonomous(name = "Mecanum Autonomous", group = "Autonomous")
//public class BasicAuto extends LinearOpMode {
//
//    private static final double
//            TICKS_PER_REV = 28.0;
//    private static final double
//            TARGET_FLYWHEEL_RPS = 58.0;
//    private static final double
//            RPS_TOLERANCE = 1.5;
//    private static final double
//            STABLE_TIME_SEC = 0.20;
//    private static final double
//            SPINUP_TIMEOUT_SEC = 3.0;
//    private static final long
//            BETWEEN_SHOTS_PAUSE_MS = 1000;
//    private final ElapsedTime runtime = new ElapsedTime();
//
//    private DcMotor frontLeft, frontRight, backLeft, backRight;
//
//    @Override
//    public void runOpMode() {
//
//        // Map motors (names must match your Robot Configuration)
//        frontLeft = hardwareMap.get(DcMotor.class, "fL");
//        frontRight = hardwareMap.get(DcMotor.class, "fR");
//        backLeft = hardwareMap.get(DcMotor.class, "bL");
//        backRight = hardwareMap.get(DcMotor.class, "bR");
//
//        // Motor directions (typical mecanum)
//        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
//        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
//        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
//        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
//
//        // Stop quickly (no coasting)
//        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//        telemetry.addData("Status", "Ready");
//        telemetry.update();
//
//        waitForStart();
//
//        if (opModeIsActive()) {
//            // ⭐ DRIVE BACKWARD ⭐
//            frontLeft.setPower(-0.5);
//            frontRight.setPower(-0.5);
//            backLeft.setPower(-0.5);
//            backRight.setPower(-0.5);
//            sleep(2000);   // <-- 1 second
//
//            // Stop
//            frontLeft.setPower(0);
//            frontRight.setPower(0);
//            backLeft.setPower(0);
//            backRight.setPower(0);
//
//            class ScoringMechanisms {
//                private DcMotorEx flyWheel1, rollerIntake1;
//
//                private void init(HardwareMap hardwareMap) {
//
//                    flyWheel1 = hardwareMap.get(DcMotorEx.class, "fW");
//                    flyWheel1.setDirection(DcMotorEx.Direction.FORWARD);
//                    flyWheel1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
//                    flyWheel1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
//                    flyWheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//
//                    rollerIntake1 = hardwareMap.get(DcMotorEx.class, "rI");
//                    rollerIntake1.setDirection(DcMotorEx.Direction.FORWARD);
//                    rollerIntake1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
//                    rollerIntake1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
//                    rollerIntake1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//
//                    flyWheel1.setVelocity(0.0);
//                    flyWheel1.setVelocity(TARGET_FLYWHEEL_RPS * TICKS_PER_REV);
//                    ElapsedTime timeout = new ElapsedTime();
//                    ElapsedTime stable = new ElapsedTime();
//                    stable.reset();
//                    while (opModeIsActive() && timeout.seconds() < SPINUP_TIMEOUT_SEC) {
//                        double measuredRps = flyWheel1.getVelocity() / TICKS_PER_REV;
//                        boolean within = Math.abs(TARGET_FLYWHEEL_RPS - measuredRps) <= RPS_TOLERANCE;
//                        if (within) {
//                            if (stable.seconds() >= STABLE_TIME_SEC) {
//                                return;
//                            }
//                        } else {
//                            stable.reset();
//
//                        }
//                        sleep(20);
//                    }
//                }
//
//                private void waitMs(long ms) {
//                    runtime.reset();
//                    while (opModeIsActive() && runtime.milliseconds() < ms) {
//                        idle();
//                    }
//
//
//                }
//
//
//                    rollerIntake1.setPower(-0.75);
//                    sleep(2000);
//
//
//
//            }
//
//        }
//    }
//
//}
//
//
//version 4
//package Auton;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//@Autonomous(name = "Mecanum Autonomous", group = "Autonomous")
//public class BasicAuto extends LinearOpMode {
//
//    // ===== SHOOTER / TIMING CONSTANTS =====
//    private static final double TICKS_PER_REV        = 28.0;
//    private static final double TARGET_FLYWHEEL_RPS  = 58.0;   // tune this for your motor
//    private static final double RPS_TOLERANCE        = 1.5;
//    private static final double STABLE_TIME_SEC      = 0.20;
//    private static final double SPINUP_TIMEOUT_SEC   = 3.0;
//
//    private static final double INTAKE_FEED_POWER    = -0.75;  // direction might need flipping
//    private static final long   FEED_TIME_MS         = 500;    // how long to run intake per shot
//    private static final long   BETWEEN_SHOTS_PAUSE_MS = 500;  // time between shots
//
//    // ===== DRIVE CONSTANTS =====
//    private static final double DRIVE_BACK_POWER     = -0.5;   // negative = backward for most configs
//    private static final long   DRIVE_BACK_TIME_MS   = 2000;   // 1 second
//
//    private final ElapsedTime runtime = new ElapsedTime();
//
//    // Drive motors
//    private DcMotor frontLeft, frontRight, backLeft, backRight;
//
//    // Scoring motors
//    private DcMotorEx flyWheel1, rollerIntake1;
//
//    @Override
//    public void runOpMode() {
//
//        // ===== MAP DRIVE MOTORS =====
//        // These names MUST match your configuration
//        frontLeft  = hardwareMap.get(DcMotor.class,  "fL");
//        frontRight = hardwareMap.get(DcMotor.class,  "fR");
//        backLeft   = hardwareMap.get(DcMotor.class,  "bL");
//        backRight  = hardwareMap.get(DcMotor.class,  "bR");
//
//        // Typical mecanum directions (change if robot drives weird)
//        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
//        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
//        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
//        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
//
//        // Brake so we stop quickly
//        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//        // ===== MAP SCORING MOTORS =====
//        flyWheel1     = hardwareMap.get(DcMotorEx.class, "fW"); // flywheel
//        rollerIntake1 = hardwareMap.get(DcMotorEx.class, "rI"); // intake
//
//        // Flywheel setup
//        flyWheel1.setDirection(DcMotorSimple.Direction.FORWARD);
//        flyWheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        flyWheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        flyWheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//        flyWheel1.setVelocity(0.0);
//
//        // Intake setup (we just use power)
//        rollerIntake1.setDirection(DcMotorSimple.Direction.FORWARD);
//        rollerIntake1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        rollerIntake1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        rollerIntake1.setPower(0.0);
//
//        telemetry.addData("Status", "Initialized");
//        telemetry.update();
//
//        // ===== WAIT FOR START =====
//        waitForStart();
//        if (!opModeIsActive()) return;
//
//        // ===== STEP 1: DRIVE BACKWARD =====
//        setDrivePower(DRIVE_BACK_POWER);
//        waitMs(DRIVE_BACK_TIME_MS);   // 1 second
//        stopDrive();
//        sleep(150); // small settle
//
//        // ===== STEP 2 & 3: SPIN UP + SHOOT 3 RINGS =====
//        for (int i = 0; i < 3 && opModeIsActive(); i++) {
//            waitForFlywheelAtSpeed(TARGET_FLYWHEEL_RPS);
//            feedOnce();
//            waitMs(BETWEEN_SHOTS_PAUSE_MS);
//        }
//
//        // ===== FINAL STOP =====
//        stopDrive();
//        flyWheel1.setVelocity(0.0);
//        rollerIntake1.setPower(0.0);
//
//        telemetry.addData("Status", "Auto Complete");
//        telemetry.update();
//    }
//
//    // ===== HELPER METHODS =====
//
//    private void setDrivePower(double p) {
//        frontLeft.setPower(p);
//        frontRight.setPower(p);
//        backLeft.setPower(p);
//        backRight.setPower(p);
//    }
//
//    private void stopDrive() {
//        setDrivePower(0.0);
//    }
//
//    private void waitMs(long ms) {
//        runtime.reset();
//        while (opModeIsActive() && runtime.milliseconds() < ms) {
//            idle(); // let FTC internals run
//        }
//    }
//
//    private void feedOnce() {
//        rollerIntake1.setPower(INTAKE_FEED_POWER);
//        waitMs(FEED_TIME_MS);
//        rollerIntake1.setPower(0.0);
//    }
//
//    private void waitForFlywheelAtSpeed(double targetRps) {
//        // Command velocity in ticks per second
//        flyWheel1.setVelocity(targetRps * TICKS_PER_REV);
//
//        ElapsedTime timeout = new ElapsedTime();
//        ElapsedTime stable  = new ElapsedTime();
//        stable.reset();
//
//        while (opModeIsActive() && timeout.seconds() < SPINUP_TIMEOUT_SEC) {
//            double measuredRps = flyWheel1.getVelocity() / TICKS_PER_REV;
//            boolean within = Math.abs(targetRps - measuredRps) <= RPS_TOLERANCE;
//
//            if (within) {
//                if (stable.seconds() >= STABLE_TIME_SEC) {
//                    // Been in range long enough — good to shoot
//                    return;
//                }
//            } else {
//                // Lost the window, reset stable timer
//                stable.reset();
//            }
//
//            sleep(20);
//        }
//        // If we time out, we just return (may not be perfect speed, but won't hang)
//    }
//}
//
//
//
//
//
// version 5
package Auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Mecanum Autonomous", group = "Autonomous")
public class BasicAuto extends LinearOpMode {

    // ===== SHOOTER CONSTANTS =====
    private static final double TICKS_PER_REV        = 28.0;   // CHANGE if motor differs
    private static final double TARGET_FLYWHEEL_RPS  = 58.0;
    private static final double RPS_TOLERANCE        = 1.5;
    private static final double STABLE_TIME_SEC      = 0.20;
    private static final double SPINUP_TIMEOUT_SEC   = 3.0;

    private static final double INTAKE_FEED_POWER    = -0.75;
    private static final long   FEED_TIME_MS         = 500;
    private static final long   BETWEEN_SHOTS_PAUSE_MS = 500;

    // ===== DRIVE CONSTANTS =====
    private static final double DRIVE_BACK_POWER     = -0.5;
    private static final long   DRIVE_BACK_TIME_MS   = 2000;

    private final ElapsedTime runtime = new ElapsedTime();

    // ===== MOTORS =====
    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private DcMotorEx flyWheel1, rollerIntake1;

    @Override
    public void runOpMode() {

        // ===== MAP DRIVE MOTORS =====
        frontLeft  = hardwareMap.get(DcMotor.class, "fL");
        frontRight = hardwareMap.get(DcMotor.class, "fR");
        backLeft   = hardwareMap.get(DcMotor.class, "bL");
        backRight  = hardwareMap.get(DcMotor.class, "bR");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // ===== MAP FLYWHEEL =====
        flyWheel1 = hardwareMap.get(DcMotorEx.class, "fW");
        flyWheel1.setDirection(DcMotorSimple.Direction.FORWARD);
        flyWheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flyWheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flyWheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flyWheel1.setVelocity(0);

        // ===== MAP INTAKE =====
        rollerIntake1 = hardwareMap.get(DcMotorEx.class, "rI");
        rollerIntake1.setDirection(DcMotorSimple.Direction.FORWARD);
        rollerIntake1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rollerIntake1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rollerIntake1.setPower(0);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // ===== WAIT FOR START =====
        waitForStart();
        if (!opModeIsActive()) return;

        // ===== STEP 1: DRIVE BACKWARD =====
        setDrivePower(DRIVE_BACK_POWER);
        waitMs(DRIVE_BACK_TIME_MS);
        stopDrive();
        sleep(150);

        // ===== STEP 2: SHOOT 3 artifacts =====
        for (int i = 0; i < 3 && opModeIsActive(); i++) {
            waitForFlywheelAtSpeed(TARGET_FLYWHEEL_RPS);
            feedOnce();
            waitMs(BETWEEN_SHOTS_PAUSE_MS);
        }

        // ===== FINAL STOP =====
        stopDrive();
        flyWheel1.setVelocity(0);
        rollerIntake1.setPower(0);

        telemetry.addData("Status", "Auto Complete");
        telemetry.update();
        sleep(1000);
    }

    // ===== HELPER METHODS =====

    private void setDrivePower(double p) {
        frontLeft.setPower(p);
        frontRight.setPower(p);
        backLeft.setPower(p);
        backRight.setPower(p);
    }

    private void stopDrive() {
        setDrivePower(0);
    }

    private void waitMs(long ms) {
        runtime.reset();
        while (opModeIsActive() && runtime.milliseconds() < ms) {
            idle();
        }
    }

    private void feedOnce() {
        rollerIntake1.setPower(INTAKE_FEED_POWER);
        waitMs(FEED_TIME_MS);
        rollerIntake1.setPower(0);
    }

    private void waitForFlywheelAtSpeed(double targetRps) {
        flyWheel1.setVelocity(targetRps * TICKS_PER_REV);

        ElapsedTime timeout = new ElapsedTime();
        ElapsedTime stable  = new ElapsedTime();
        stable.reset();

        while (opModeIsActive() && timeout.seconds() < SPINUP_TIMEOUT_SEC) {
            double currentRps = flyWheel1.getVelocity() / TICKS_PER_REV;
            boolean within = Math.abs(targetRps - currentRps) <= RPS_TOLERANCE;

            telemetry.addData("Target RPS", targetRps);
            telemetry.addData("Current RPS", currentRps);
            telemetry.update();

            if (within) {
                if (stable.seconds() >= STABLE_TIME_SEC) {
                    return; // ready to shoot
                }
            } else {
                stable.reset();
            }

            sleep(20);
        }
        // timeout safety — continue anyway
    }
}

