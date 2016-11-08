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

/******************************************************************************
 * Opmode Name:	FFAutoDemo1.java
 * Author: 		FRC 503-FrogForce
 * Description:	Demonstrates sample autonomous mode of Driving robot straight
 * 				for 2 seconds
 ******************************************************************************/
public class FFAutoDemo1 extends OpMode {
    //Define objects for motors
	DcMotor motorRight;
	DcMotor motorLeft;
	//define pause time variable
	double pauseTime;
	/**
	 * Constructor
	 */
	public FFAutoDemo1() {

	}

	/*
	 * Code to run when the op mode is initialized goes here
	 */
	@Override
	public void init() {
		/**********
		 * Map java objects to motor names in Robot Controller
		 * Make sure to reverse left motor so robot moves forward
		 */
		motorRight = hardwareMap.dcMotor.get("right_drive");
		motorLeft = hardwareMap.dcMotor.get("left_drive");
		motorRight.setDirection(DcMotor.Direction.REVERSE);

		// Get the current time and add 2 seconds - this is our stop time
		pauseTime = time + 2.0;
	}

	/*
	 * This method will be called repeatedly in a loop
	 */
	@Override
	public void loop() {
		/* set right and left power to 50%  */
		float leftPower  = 0.5f;
		float rightPower  = 0.5f;
		// Check if current time exceeds pauseTime-if so set power to zero to turn them off
		if (time > pauseTime) {
			leftPower = 0.0f;
			rightPower = 0.0f;
		}
		/* write the values to the motors */
		motorRight.setPower(rightPower);
		motorLeft.setPower(leftPower);

		// Send telemetry data back to driver station.
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", leftPower));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", rightPower));
	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 */
	@Override
	public void stop() {

	}
}
