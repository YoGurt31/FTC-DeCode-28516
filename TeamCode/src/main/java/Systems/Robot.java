package Systems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Title: Robot - Container Class for Decode Robot
 * Desc: Container Class for ALL Robot Subsystems
 * Includes Universal and General Functions
 *
 * @Author Saavin
 * @Version 1.0
 */

public class Robot {

    public class DriveTrain {
        // Hardware Devices
        public DcMotorEx frontLeft, frontRight, backLeft, backRight;

        public void init(HardwareMap hardwareMap) {

            // Initialize DcMotors - Name in " " should match Driver Station Configuration
            frontLeft = hardwareMap.get(DcMotorEx.class, "fL");
            frontRight = hardwareMap.get(DcMotorEx.class, "fR");
            backLeft = hardwareMap.get(DcMotorEx.class, "bL");
            backRight = hardwareMap.get(DcMotorEx.class, "bR");

            // Set Motor Directions - Positive Power should Drives Forward
            frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
            backLeft.setDirection(DcMotorEx.Direction.REVERSE);
            frontRight.setDirection(DcMotorEx.Direction.FORWARD);
            backRight.setDirection(DcMotorEx.Direction.FORWARD);

            // Brake when Power = 0 (Helps Negate Momentum)
            frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

            // Stops Motors and Resets Encoders - Motors will NOT Run unless Encoder Mode is Defined
            frontLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

            // Encoder Mode Definition - Run With or Without Encoders
            frontLeft.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
            frontRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
            backLeft.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
            backRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        }

        public void mecDrive(double flPower, double frPower, double blPower, double brPower) {
            frontLeft.setPower(flPower);
            frontRight.setPower(frPower);
            backLeft.setPower(blPower);
            backRight.setPower(brPower);
        }

        public void brake() {
            frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        }

    }

    public class ScoringMechanisms {
        // Hardware Devices
        public DcMotorEx flyWheel;

        public void init(HardwareMap hardwareMap) {

            flyWheel = hardwareMap.get(DcMotorEx.class, "fW");

            flyWheel.setDirection(DcMotorEx.Direction.REVERSE);

            flyWheel.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

            flyWheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

            flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    // Created Instances of Subsystems
    public DriveTrain driveTrain = new DriveTrain();
    public ScoringMechanisms scoringMechanisms = new ScoringMechanisms();

    // Initialize Hardware
    public void init(HardwareMap hwMap) {
        driveTrain.init(hwMap);
        scoringMechanisms.init(hwMap);
    }
}
