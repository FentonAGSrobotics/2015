package org.firstinspires.ftc.teamcode;;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


/**
 ******************************************************************************
 * Opmode Name:	FFTank.java
 * Author: 		FRC 503-FrogForce
 * Description:	Demonstrates TeleOp tank drive mode using PushBot motor/server names
 *
 ******************************************************************************/

@TeleOp(name="Test TeleOp", group="TeleOp")  // @Autonomous(...) is the other common choice
//@Disabled

public class TestTeleopTT15 extends OpMode {

	/*
	 * Note: the configuration of the servos is such that
	 * as the arm servo approaches 0, the arm position moves up (away from the floor).
	 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
	 */
	// TETRIX VALUES.
	final static double ARM_MIN_RANGE  = 0.20;
	final static double ARM_MAX_RANGE  = 0.90;
	final static double CLAW_MIN_RANGE  = 0.20;
	final static double CLAW_MAX_RANGE  = 0.7;

	// position of the hand servos.
	double handPosition;

	// position of the tape rotation servo.
	double tapePosition;

	// amount to change the arm servo position.
	double armDelta = 0.1;

	// amount to change the claw servo position by
	double handDelta = 0.05;

	// amount to change the tape servo position by
	double tapedelta = 0.01;

	DcMotor motorRight;
	DcMotor motorLeft;
	DcMotor motorArm;
	DcMotor motorTurret;
	DcMotor winch1;
	DcMotor intake1;
	Servo leftHand;
	Servo rightHand;
	Servo tape1;



	/**
	 * Constructor
	 */
	public TestTeleopTT15() {

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */
	@Override
	public void init() {
		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

		motorRight = hardwareMap.dcMotor.get("right_drive");
		motorLeft = hardwareMap.dcMotor.get("left_drive");
		motorLeft.setDirection(DcMotor.Direction.REVERSE);
		motorArm = hardwareMap.dcMotor.get("left_arm");
		motorTurret = hardwareMap.dcMotor.get("right_arm");

		winch1 = hardwareMap.dcMotor.get("winch");
		intake1 = hardwareMap.dcMotor.get("intake");

		leftHand = hardwareMap.servo.get("left_hand");
		rightHand = hardwareMap.servo.get("right_hand");
		tape1 = hardwareMap.servo.get("tape");


		// assign the starting position of the left and right hand
		handPosition = 0.5;

		// assign the starting position of the tape rotation servo
		tapePosition = 0.5;
	}

	/*
	 * This method will be called repeatedly in a loop
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		/*
		 * Gamepad 1
		 *
		 * Gamepad 1 controls the motors via the left stick and right sticks
		 *
		 * GamePad 2
		 * it Controls the arm with the left joystick and the turret with right joystick
		 * if controls the left hand x,b buttons
		 */

		// tank drive
		// note that if y equal -1 then joystick is pushed all of the way forward.
		float left = -gamepad1.left_stick_y;
		float right = -gamepad1.right_stick_y;

		// clip the right/left values so that the values never exceed +/- 1
		right = Range.clip(right, -1, 1);
		left = Range.clip(left, -1, 1);

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		right = (float)scaleInput(right);
		left =  (float)scaleInput(left);

		// write the values to the motors
		motorRight.setPower(right);
		motorLeft.setPower(left);

		//Manage the ARM motor
		float arm_left_stick_x  = gamepad2.left_stick_x;
		float arm_power = (float)scaleInput(arm_left_stick_x);
		motorArm.setPower(arm_power);

		//Manage the Turret motor
		float arm_right_stick_y  = -gamepad2.right_stick_y;
		float turret_power = (float)scaleInput(arm_right_stick_y);
		motorTurret.setPower(turret_power);

		// Manage Tape Rotation
		// if X button it pressed rotate tape up
		// if B button is pressed rotate tape down
		if (gamepad1.x)
		{
			tapePosition += tapedelta;

		}
		if (gamepad1.b)
		{
			tapePosition -= tapedelta;
		}

		tapePosition = Range.clip(tapePosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
		tape1.setPosition(tapePosition);

		if (gamepad1.dpad_up)
		{
			tape1.setPosition(0.7);
		}
		if (gamepad1.dpad_down)
		{
			tape1.setPosition(0.2);
		}

		// Manage Tape Servo Motors
		// if X button it pressed increase hand position
		// if B button is pressed decrease hand position
		if (gamepad1.a)
		{
			handPosition  = leftHand.getPosition() + handDelta;
		}
		else if (gamepad1.y)
		{
			handPosition = leftHand.getPosition() - handDelta;
		}
		// Added this to code to stop continuous servo motor with button Y by setting to value of 0.5
		else
		{
			handPosition = 0.5;
		}


		// Manage Intake Motor
		// if X button it pressed intake objects into bucket
		// if B button is pressed eject objects from bucket
		if (gamepad2.right_bumper)
		{
			intake1.setPower(1);
		}
		else if (gamepad2.left_bumper)
		{
			intake1.setPower(-1);
		}
		// Added this to code to stop continuous servo motor with button Y by setting to value of 0.5
		else
		{
			intake1.setPower(0);
		}

		// Manage Winch Motor
		// if X button it pressed intake objects into bucket
		// if B button is pressed eject objects from bucket
		if (gamepad1.right_bumper)
		{
			winch1.setPower(1);
		}
		else if (gamepad1.left_bumper)
		{
			winch1.setPower(-1);
		}
		// Added this to code to stop continuous servo motor with button Y by setting to value of 0.5
		else
		{
			winch1.setPower(0);
		}
        //Set hand position
		//Ensure the values are legal
		double l_position = Range.clip
				(
						handPosition
						, Servo.MIN_POSITION
						, Servo.MAX_POSITION
				);
		//Set hand positions
		leftHand.setPosition(l_position);
		rightHand.setPosition(1.0 - l_position);


		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */

		telemetry.addData("Text", "*** Robot Data***");
		//telemetry.addData("arm", "arm:  " + String.format("%.2f", arm_Power));
		telemetry.addData("hand", "hand:  " + String.format("%.2f", handPosition));
		telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
		telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}

	/*
	 * This method scales the joystick input so for low joystick values, the
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
	double scaleInput(double dVal)  {
		double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
				0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

		// get the corresponding index for the scaleInput array.
		int index = (int) (dVal * 16.0);

		// index should be positive.
		if (index < 0) {
			index = -index;
		}

		// index cannot exceed size of array minus 1.
		if (index > 16) {
			index = 16;
		}

		// get value from the array.
		double dScale = 0.0;
		if (dVal < 0) {
			dScale = -scaleArray[index];
		} else {
			dScale = scaleArray[index];
		}

		// return scaled value.
		return dScale;
	}
}