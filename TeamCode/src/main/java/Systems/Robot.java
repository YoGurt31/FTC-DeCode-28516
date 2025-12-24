package Systems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

/**
 * Title: Robot - Container Class for Decode Robot
 * Desc: Container Class for ALL Robot Subsystems
 * Includes Universal and General Functions
 *
 * @Author Saavin
 * @Version 1.5
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

        public void mecDrive(double Drive, double Strafe, double Rotate) {
            double frontLeftPower = Drive - Strafe - Rotate;
            double frontRightPower = Drive + Strafe + Rotate;
            double backLeftPower = Drive + Strafe - Rotate;
            double backRightPower = Drive - Strafe + Rotate;

            // Prevents Motors from Exceeding 100% Power
            double[] powers = {frontLeftPower, frontRightPower, backLeftPower, backRightPower};
            double maxPower = 0.0;
            for (double p : powers) {
                maxPower = Math.max(maxPower, Math.abs(p));
            }

            if (maxPower > 1.0) {
                frontLeftPower /= maxPower;
                frontRightPower /= maxPower;
                backLeftPower /= maxPower;
                backRightPower /= maxPower;
            }

            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);
        }

        public void brake() {
            frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        }

    }

    public class ScoringMechanisms {
        public DcMotorEx flyWheel1, rollerIntake1;
        public CRServo rollerIntake2;
        public void init(HardwareMap hardwareMap) {

            double F = 15.000;
            double P = 2 * F;
            double I = 00.000;
            double D = 00.000;

            flyWheel1 = hardwareMap.get(DcMotorEx.class, "fW");
            flyWheel1.setDirection(DcMotorEx.Direction.FORWARD);
            flyWheel1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            flyWheel1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            flyWheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            flyWheel1.setVelocityPIDFCoefficients(P, I, D, F);

            rollerIntake1 = hardwareMap.get(DcMotorEx.class, "rI");
            rollerIntake1.setDirection(DcMotorEx.Direction.FORWARD);
            rollerIntake1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            rollerIntake1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            rollerIntake1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }
    }

    public class Vision {
        public VisionPortal visionPortal;
        public AprilTagProcessor aprilTag;
        public AprilTagDetection desiredTag = null;

        public void init(HardwareMap hardwareMap) {
            aprilTag = AprilTagProcessor.easyCreateWithDefaults();
            visionPortal = VisionPortal.easyCreateWithDefaults(hardwareMap.get(WebcamName.class, "Webcam"), aprilTag);
        }
    }


    // Created Instances of Subsystems
    public DriveTrain driveTrain = new DriveTrain();
    public ScoringMechanisms scoringMechanisms = new ScoringMechanisms();
    public Vision vision = new Vision();


    // Initialize Hardware
    public void init(HardwareMap hwMap) {
        driveTrain.init(hwMap);
        scoringMechanisms.init(hwMap);
        vision.init(hwMap);
    }
}
