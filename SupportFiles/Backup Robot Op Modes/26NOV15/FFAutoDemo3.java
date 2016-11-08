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
 * Opmode Name:	FFAutoDemo3.java
 * Author: 		FRC 503-FrogForce
 * Description:	Demonstrates sample autonomous mode of driving robot straight
 * 				for 2 seconds, turning robot 180 degrees, drive back to starting
 * 				point for 2 seconds. This will introduce an action variable to
 * 			 	control what action we waht to take in our loop function.
 * 			 	Action = 1 -- drive straight for 2 seconds
 * 			 	Action = 2 -- initialize 180 degree turn
 * 			 	Action = 3 -- continue turn until timer pops
 * 			    Action = 4 -- drive back to starting point for 2 seconds
 * 			    Action = 5 -- Done stop robot
 *
 ******************************************************************************/
public class FFAutoDemo3 extends OpMode {
    //Define objects for motors
	DcMotor motorRight;
	DcMotor motorLeft;
	//define pause time variable
	double pauseTime;
	//Define integer action variable to control what step we are at
	int action;
	/**
	 * Constructor
	 */
	public FFAutoDemo3() {

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
		motorLeft = hardwareMap.dcMotor.get( "left_drive");
		motorLeft.setDirection(DcMotor.Direction.REVERSE);

		// Get the current time and add 2 seconds - this is our stop time
		pauseTime = time + 2.0;

		//Initialize action variable to 1
		action = 1;
	}

	/*
	 * This method will be called repeatedly in a loop
	 */
	@Override
	public void loop() {
		//Define variables
		float leftPower  = 0.0f;
		float rightPower  = 0.0f;

		// test action
		if (action == 1 ) {
			/***********************************************
			 if action = 1 drive straight for two seconds
			 ***********************************************/
			/* set right and left power to 50%  */
			leftPower  = 0.5f;
			rightPower  = 0.5f;
			/* Check if current time exceeds pauseTime-if so, set motor power to zero
			   and set action to 2 for next time through */
			if (time > pauseTime) {
				leftPower = 0;
				rightPower = 0;
				action = 2;
			}
		} else if (action == 2) {
			/***********************************************
			 if action = 2 initialize turn 180 degrees
			 ***********************************************/
			/* set right and left power to 50%  */
			leftPower  = 0.5f;
			rightPower  = 0.5f;
			//Reset left motor to forward which will pivot robot
			motorLeft.setDirection(DcMotor.Direction.FORWARD);
			// Get the current time and add 1.55 seconds - time to complete pivot action
			pauseTime = time + 1.75;
			//set action to 3 for next time through
			action = 3;
		} else if (action == 3) {
			/***********************************************
			 if action = 3 continue turn until timer pops
			 ***********************************************/
			/* set right and left power to 50%  */
			leftPower  = 0.5f;
			rightPower  = 0.5f;
			if (time > pauseTime) {
				leftPower = 0;
				rightPower = 0;
				// Get the current time and add 2 seconds - time to return to starting point
				pauseTime = time + 2.0;
				action = 4;

			}
		} else if (action == 4) {
			/***********************************************
			 if action = 4 drive straight for 2 seconds to return to start
			 ***********************************************/
			/* set right and left power to 50%  */
			leftPower  = 0.5f;
			rightPower  = 0.5f;
			//Reset left motor to reverse to drive forward
			motorLeft.setDirection(DcMotor.Direction.REVERSE);
			/* Check if current time exceeds pauseTime-if so, set motor power to zero
			   and set action to 5 for next time through */
			if (time > pauseTime) {
				leftPower = 0;
				rightPower = 0;
				action = 5;
			}
		} else {
			/***********************************************
			 if action = 4 drive straight for 2 seconds to return to start
 			***********************************************/
			/* set right and left power to 0% -- stop motors */
			leftPower  = 0.0f;
			rightPower  = 0.0f;
		}

		/* write the values to the motors */
		motorRight.setPower(rightPower);
		motorLeft.setPower(leftPower);

		// Send telemetry data back to driver station.
        telemetry.addData("Text", "*** Robot Data***");
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
}
