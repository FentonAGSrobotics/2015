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
 * Opmode Name:	FFAutoDemo1e.java
 * Author: 		FRC 503-FrogForce
 * Description:	Demonstrates sample autonomous mode of Driving robot straight
 * 				using wheel encoders for 2880 steps
 ******************************************************************************/
public class FFAutoDemo1e extends OpMode {
	//Define objects for motors
	DcMotorController motorControl;
	DcMotor motorRight;
	DcMotor motorLeft;

	/* set right and left power to 50%  */
	float leftPower  = 0.5f;
	float rightPower  = 0.5f;
	int l = 0 ;
	int r = 0;

	/**
	 * Constructor
	 */
	public FFAutoDemo1e() {

	}

	/*
	 * Code to run when the op mode is initialized goes here
	 */
	@Override
	public void init() {
		/**********
		 * Map java objects to motor names in Robot Controller
		 * Make sure to reverse right motor so robot moves forward
		 */
		motorControl = hardwareMap.dcMotorController.get("drive_controller");
		motorRight = hardwareMap.dcMotor.get("right_drive");
		motorLeft = hardwareMap.dcMotor.get("left_drive");
		motorRight.setDirection(DcMotor.Direction.REVERSE);

		// Reset the motor encoders on the drive wheels
		reset_drive_encoders ();
	}

	/*
	 * This method will be called repeatedly in a loop
	 */
	@Override
	public void loop() {
		run_using_encoders ();
		l = get_left_encoder_count();
		r = get_right_encoder_count();
		if (have_drive_encoders_reached (3000, 3000))
		{
			//
			// Reset the encoders to ensure they are at a known good value.
			//
			reset_drive_encoders ();

			// Stop the motors
			leftPower = 0.0f;
			rightPower = 0.0f;
		}
		/* write the values to the motors */
		motorRight.setPower(rightPower);
		motorLeft.setPower(leftPower);

		// Send telemetry data back to driver station.
		telemetry.addData("Text", "*** Robot Data***");
		telemetry.addData("Left Enc", "Left Enc = " + String.format("%01d", l));
		telemetry.addData("Right Enc", "Right Enc = " + String.format("%01d",r));
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
			motorControl.setMotorChannelMode( 1, DcMotorController.RunMode.RUN_USING_ENCODERS);
		}

		l_mode = motorControl.getMotorChannelMode( 2);
		if (l_mode == DcMotorController.RunMode.RESET_ENCODERS)
		{
			motorControl.setMotorChannelMode( 2, DcMotorController.RunMode.RUN_USING_ENCODERS);
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

}
