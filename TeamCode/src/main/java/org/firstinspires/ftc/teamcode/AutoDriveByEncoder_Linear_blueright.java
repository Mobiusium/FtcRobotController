package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="Auto Drive By Encoder blueRight", group="Pushbot")
//@Disabled
public class AutoDriveByEncoder_Linear_blueright extends LinearOpMode {

    /* Declare OpMode members. */

    // REPLACE HardwarePushbot WITH ALL THE DECLARATIONS IN THE MECHANUM TEST DRIVE CODE
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor rearLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor rearRightDrive = null;
    private DcMotor spinner=null;
    private DcMotor armMotor = null;
    private CRServo intake = null;
    // DECLARATIONS GO ABOVE

    static final double     COUNTS_PER_MOTOR_REV    = 537.7 ;    // goBuilda Motor 537.7 PPR
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */

        // REPLACE LINE BELOW WITH MAPPINGS FROM MECHANUM TEST DRIVE
        frontLeftDrive = hardwareMap.get(DcMotor.class, "left_drive1");
        rearLeftDrive  = hardwareMap.get(DcMotor.class, "left_drive2");
        frontRightDrive = hardwareMap.get(DcMotor.class, "right_drive1");
        rearRightDrive = hardwareMap.get(DcMotor.class, "right_drive2");
        armMotor = hardwareMap.get(DcMotor.class, "arm");
        intake = hardwareMap.get(CRServo.class, "intake");
        spinner = hardwareMap.get(DcMotor.class, "spinner");
        // REPLACE ABOVE.

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        rearLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        rearRightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rearLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rearLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                frontLeftDrive.getCurrentPosition(),
                frontRightDrive.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        //strafe right
        //right side toward each other left away
        encoderDrive(DRIVE_SPEED, 7,7,5.0);
        encoderDrive(TURN_SPEED,30   , -30, 5.0); //90 deg right
        encoderDrive(DRIVE_SPEED, 28,28,5.0);

        //right strafe
        frontRightDrive.setPower(-1);
        rearRightDrive.setPower(1);
        frontLeftDrive.setPower(1);
        rearLeftDrive.setPower(-1);
        sleep(100);
        frontRightDrive.setPower(0);
        rearRightDrive.setPower(0);
        frontLeftDrive.setPower(0);
        rearLeftDrive.setPower(0);

        spinner.setPower(-0.75);
        sleep(4000);
        spinner.setPower(0);
        encoderDrive(DRIVE_SPEED,-1,-1,5.0);

        //left strafe
        frontRightDrive.setPower(1);
        rearRightDrive.setPower(-1);
        frontLeftDrive.setPower(-1);
        rearLeftDrive.setPower(1);
        sleep(750);
        frontRightDrive.setPower(0);
        rearRightDrive.setPower(0);
        frontLeftDrive.setPower(0);
        rearLeftDrive.setPower(0);


        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newfrontLeftTarget;
        int newfrontRightTarget;
        int newrearLeftTarget;
        int newrearRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newfrontLeftTarget = frontLeftDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newfrontRightTarget = frontRightDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newrearLeftTarget = rearLeftDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newrearRightTarget = rearRightDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);

            frontLeftDrive.setTargetPosition(newfrontLeftTarget);
            frontRightDrive.setTargetPosition(newfrontRightTarget);
            rearLeftDrive.setTargetPosition(newrearLeftTarget);
            rearRightDrive.setTargetPosition(newrearRightTarget);

            // Turn On RUN_TO_POSITION
            frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rearLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rearRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            // reset the timeout time and start motion.
            runtime.reset();
            frontLeftDrive.setPower(Math.abs(speed));
            frontRightDrive.setPower(Math.abs(speed));
            rearLeftDrive.setPower(Math.abs(speed));
            rearRightDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                   (runtime.seconds() < timeoutS) &&
                   (frontLeftDrive.isBusy() && frontRightDrive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newfrontLeftTarget,  newfrontRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        frontLeftDrive.getCurrentPosition(),
                        frontRightDrive.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            frontLeftDrive.setPower(0);
            frontRightDrive.setPower(0);
            rearLeftDrive.setPower(0);
            rearRightDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rearLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rearRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

              sleep(1000);   // optional pause after each move
        }
    }
}
