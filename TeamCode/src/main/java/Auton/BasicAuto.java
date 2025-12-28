package Auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import Systems.Robot;

@Autonomous(name = "Basic Auto (3 Shots)", group = "Robot")
public class BasicAuto extends LinearOpMode {

    private final Robot robot = new Robot();

    private static final double TICKS_PER_REV = 28.0;
    private static final double TARGET_FLYWHEEL_RPS = 58.0;
    private static final double RPS_TOLERANCE = 1.5;
    private static final double STABLE_TIME_SEC = 0.20;
    private static final double SPINUP_TIMEOUT_SEC = 3.0;
    private static final double INTAKE_FEED_POWER = 0.75;
    private static final double DRIVE_BACK_POWER = -0.50;

    // TODO: Tune These Times
    private static final long DRIVE_BACK_TIME_MS_1 = 1000;
    private static final long DRIVE_BACK_TIME_MS_2 = 1000;
    private static final long FEED_TIME_MS = 1000;
    private static final long BETWEEN_SHOTS_PAUSE_MS = 1000;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);

        waitForStart();
        if (isStopRequested()) return;

        // Drive Backwards
        robot.driveTrain.mecDrive(DRIVE_BACK_POWER, 0.0, 0.0);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < DRIVE_BACK_TIME_MS_1)) {
        }

        // Shoot 3 Artifacts
        for (int i = 0; i < 3 && opModeIsActive(); i++) {
            waitForFlywheelAtSpeed(TARGET_FLYWHEEL_RPS);
            feedOnce();
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < BETWEEN_SHOTS_PAUSE_MS)) {
            }
        }

        // Drive Backwards
        robot.driveTrain.mecDrive(DRIVE_BACK_POWER, 0.0, 0.0);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < DRIVE_BACK_TIME_MS_2)) {
        }

        // Stop
        robot.driveTrain.mecDrive(0.0, 0.0, 0.0);
        robot.scoringMechanisms.rollerIntake1.setPower(0.0);
        robot.scoringMechanisms.flyWheel1.setVelocity(0.0);
    }

    private void feedOnce() {
        robot.scoringMechanisms.rollerIntake1.setPower(INTAKE_FEED_POWER);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < FEED_TIME_MS)) {
        }
        robot.scoringMechanisms.rollerIntake1.setPower(0.0);
    }

    private void waitForFlywheelAtSpeed(double targetFlywheelRps) {
        robot.scoringMechanisms.flyWheel1.setVelocity(targetFlywheelRps * TICKS_PER_REV);
        ElapsedTime timeout = new ElapsedTime();
        ElapsedTime stable = new ElapsedTime();
        stable.reset();
        while (opModeIsActive() && timeout.seconds() < SPINUP_TIMEOUT_SEC) {
            double measuredRps = robot.scoringMechanisms.flyWheel1.getVelocity() / TICKS_PER_REV;
            boolean within = Math.abs(targetFlywheelRps - measuredRps) <= RPS_TOLERANCE;
            if (within) {
                if (stable.seconds() >= STABLE_TIME_SEC) {
                    return;
                }
            } else {
                stable.reset();
            }
            sleep(20);
        }
    }
}
