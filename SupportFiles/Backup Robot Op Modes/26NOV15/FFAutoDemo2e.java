/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/******************************************************************************
 * Opmode Name:	FFAutoDemo2e.java
 * Author: 		FRC 503-FrogForce
 * Description:	Demonstrates sample autonomous mode of turning robot 180 degrees
 * 				using wheel encoders
 *
 ******************************************************************************/
public class FFAutoDemo2e extends OpMode {
    //Define objects for motors
	DcMotorController motorControl;
	DcMotor motorRight;
	DcMotor motorLeft;

	/* set right and left power to 50%  */
	float leftPower  = -0.5f;
	float rightPower  = 0.5f;
	int action = 0;
	int stat = 0;
	int l = 0 ;
	int r = 0;
	/**
	 * Constructor
	 */
	public FFAutoDemo2e() {

	}

	/*
	 * Code to run when the op mode is initialized goes here
	 */
	@Override
	public void init() {
		/**********
		 * Map java objects to motor names in Robot Controller
		 * Right motor will go in reverse to ensure both wheels are going in same direction
		 */
		motorControl = hardwareMap.dcMotorController.get("drive_controller");
		motorRight = hardwareMap.dcMotor.get("right_drive");
		motorLeft = hardwareMap.dcMotor.get("left_drive");
		motorRight.setDirection(DcMotor.Direction.REVERSE);
	}

	/*
	 * This method will be called repeatedly in a loop
	 */
	@Override
	public void loop() {

		switch (action) {
			case 0:
				// Reset the encoders to ensure they are at a known good value
				reset_drive_encoders ();
				//increment action by 1
				action++;
				break;    //End of case 0
			case 1:
				//Start the motors, left power is negative to turn the robot
				run_using_encoders();
				// Start the drive wheel motors at 50% power
				set_drive_power(0.5f, 5-0.f);

				l = get_left_encoder_count();
				r = get_right_encoder_count();

				//Determine if the robot has turned 180 degrees
				if (have_drive_encoders_reached (20000, 20000))//was 5000 each.
				{
					stat++;
					// Reset the encoders to ensure they are at a known good value.
					//reset_drive_encoders ();
					// Stop the motors
					set_drive_power(0.0f, 0.0f);
					//Increment action by 1
					action++;
				}
				break;   //End of Case 1
			case 2:
				//it is good practice to test to ensure the encoders are reset before proceeding
				//if (have_drive_encoders_reset ())
				{
					action++;
				}
				break;
			default:
				// Perform no action - stay in this case until the OpMode is stopped.
				break;
		}  // End switch

		// Send telemetry data back to driver station.
		telemetry.addData("Text", "*** Robot Data***");
		telemetry.addData("Encoders", "Encoders: " + String.format("%01d",stat));
		telemetry.addData("Left Enc", "Left Enc = " + String.format("%01d",l));
		telemetry.addData("Right Enc", "Right Enc = " + String.format("%01d",r));
		telemetry.addData("Action",  "Action: " + String.format("%01d", action));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", leftPower));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", rightPower));
	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 */
	@Override
	public void stop() {

	}
	//--------------------------------------------------------------------------
	// run_using_encoders
	//--------------------------------------------------------------------------
	/**
	 * Sets both drive wheel encoders to run, if the mode is appropriate.
	 */
	void run_using_encoders ()

	{
		DcMotorController.RunMode l_mode = motorControl.getMotorChannelMode(1);
		if (l_mode == DcMotorController.RunMode.RESET_ENCODERS)
		{
			motorControl.setMotorChannelMode(1, DcMotorController.RunMode.RUN_USING_ENCODERS);
		}

		l_mode = motorControl.getMotorChannelMode(2);
		if (l_mode == DcMotorController.RunMode.RESET_ENCODERS)
		{
			motorControl.setMotorChannelMode(2, DcMotorController.RunMode.RUN_USING_ENCODERS);
		}

	} // End run_using_encoders

	//--------------------------------------------------------------------------
	// reset_drive_encoders
	//--------------------------------------------------------------------------
	void reset_drive_encoders ()
	{
		//
		// Reset the motor encoders on the drive wheels.
		//
		motorControl.setMotorChannelMode(1, DcMotorController.RunMode.RESET_ENCODERS);
		motorControl.setMotorChannelMode(2, DcMotorController.RunMode.RESET_ENCODERS);

	} // End reset_drive_encoders

	//--------------------------------------------------------------------------
	// have_drive_encoders_reset
	//--------------------------------------------------------------------------
	boolean have_drive_encoders_reset ()
	{
		// Assume failure.
		boolean l_status = false;

		// Have the encoders reached zero?
		if ((get_left_encoder_count() == 0) && (get_right_encoder_count() == 0))
		{
			// Set the status to a positive indication.
			l_status = true;
		}

		// Return status to caller.
		return l_status;

	} // End have_drive_encoders_reset


	//-------------------------------------------------------------------------
	// have_drive_encoders_reached
	//-------------------------------------------------------------------------
	boolean have_drive_encoders_reached(double p_left_count, double p_right_count)
	{
		// Assume failure
		boolean l_status = false;

		// Have the encoders reached the specified values?
		if ((Math.abs (motorLeft.getCurrentPosition ()) > p_left_count) &&
				(Math.abs (motorRight.getCurrentPosition ()) > p_right_count))
		{
			// Set the status to a positive indication
			l_status = true;
		}

		// Return status to caller
		return l_status;

	} // End have_encoders_reached

	//-------------------------------------------------------------------------
	// Get the left encoder's count.
	//-------------------------------------------------------------------------
	int get_left_encoder_count ()
	{
		return motorLeft.getCurrentPosition ();

	} // End get_left_encoder_count

	//--------------------------------------------------------------------------
	// Get the right encoder's count
	// -------------------------------------------------------------------------
	int get_right_encoder_count ()
	{
		return motorRight.getCurrentPosition ();

	} // End get_right_encoder_count

	//--------------------------------------------------------------------------
	// set_drive_power
	//--------------------------------------------------------------------------
	void set_drive_power (double p_left_power, double p_right_power)
	{
		motorLeft.setPower (p_left_power);
		motorRight.setPower (p_right_power);

	} // End set_drive_power



}
