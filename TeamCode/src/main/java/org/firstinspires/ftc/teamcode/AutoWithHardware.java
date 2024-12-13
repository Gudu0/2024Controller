/* Copyright (c) 2022 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

/*
 * This OpMode illustrates how to use an external "hardware" class to modularize all the robot's sensors and actuators.
 * This approach is very efficient because the same hardware class can be used by all of your teleop and autonomous OpModes
 * without requiring many copy & paste operations.  Once you have defined and tested the hardware class with one OpMode,
 * it is instantly available to other OpModes.
 *
 * The real benefit of this approach is that as you tweak your robot hardware, you only need to make changes in ONE place (the Hardware Class).
 * So, to be effective you should put as much or your hardware setup and access code as possible in the hardware class.
 * Essentially anything you do with hardware in BOTH Teleop and Auto should likely go in the hardware class.
 *
 * The Hardware Class is created in a separate file, and then an "instance" of this class is created in each OpMode.
 * In order for the class to do typical OpMode things (like send telemetry data) it must be passed a reference to the
 * OpMode object when it's created, so it can access all core OpMode functions.  This is illustrated below.
 *
 * In this concept sample, the hardware class file is called RobotHardware.java and it must accompany this sample OpMode.
 * So, if you copy ConceptExternalHardwareClass.java into TeamCode (using Android Studio or OnBotJava) then RobotHardware.java
 * must also be copied to the same location (maintaining its name).
 *
 * For comparison purposes, this sample and its accompanying hardware class duplicates the functionality of the
 * RobotTelopPOV_Linear OpMode.  It assumes three motors (left_drive, right_drive and arm) and two servos (left_hand and right_hand)
 *
 * View the RobotHardware.java class file for more details
 *
 *  Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 *  Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 *
 *  In OnBot Java, add a new OpMode, select this sample, and select TeleOp.
 *  Also add another new file named RobotHardware.java, select the sample with that name, and select Not an OpMode.
 */

@TeleOp(name="Concept: Robot Hardware Class", group="Robot")
@Disabled
public class AutoWithHardware extends LinearOpMode {

    // Create a RobotHardware object to be used to access robot hardware.
    // Prefix any hardware functions with "robot." to access this class.
    RobotHardware   robot       = new RobotHardware(this);

    @Override
    public void runOpMode() {
        double drive        = 0;
        double turn         = 0;
        double arm          = 0;
        double handOffset   = 0;

        double INCHES_2_TICKS = 90;
        double TICKS_2_INCHES = 1/INCHES_2_TICKS;

        // initialize all the hardware, using the hardware class. See how clean and simple this is?
        robot.init();

        // Send telemetry message to signify robot waiting;
        // Wait for the game to start (driver presses START)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //my numbers are just turning the feet from my sheet into numbers on here, so I'll have to adjust/account for it later, or change all the values. 
            //assuming left and back is negative from the red side facing the submersible.
            //added a bit forward so we dont scrape the wall, left 7ft to get to the net zone and score the preloaded sample.
            robot.driveRobot(0,0.2,-7);
            sleep(10);
            //moving back to line up to get a neutral sample
            robot.driveRobot(0,0,1);
            sleep(10);
            //repeats this step 3 times, gettin all 3 neutral samples (hopefully)
            for(int pass = 0; pass<3; pass++){
                //getting befind the neutral samples
                robot.driveRobot(5,0,0);
                 sleep(10);
                //lining up
                robot.driveRobot(0,0,-1);
                 sleep(10);
                //pusing it back to net zone
                robot.driveRobot(-5,0,0);
                 sleep(10);
            }
            //back to observation zone to park.
            robot.driveRobot(0,0,8);
             sleep(10);
            
            
            // Send telemetry messages to explain controls and show robot status
            telemetry.addData("Drive", "Left Stick");
            telemetry.addData("Turn", "Right Stick");
            telemetry.addData("Arm Up/Down", "Y & A Buttons");
            telemetry.addData("Hand Open/Closed", "Left and Right Bumpers");
            telemetry.addData("-", "-------");

            telemetry.addData("Drive Power", "%.2f", drive);
            telemetry.addData("Turn Power",  "%.2f", turn);
            telemetry.addData("Arm Power",  "%.2f", arm);
            telemetry.addData("Hand Position",  "Offset = %.2f", handOffset);
            telemetry.update();

            // Pace this loop so hands move at a reasonable speed.
            sleep(50);
        }
    }
}
/*
 _   _            _   _                
| \ | | ___  _ __| |_| |__             
|  \| |/ _ \| '__| __| '_ \            
| |\  | (_) | |  | |_| | | |           
|_|_\_|\___/|_|   \__|_| |_|           
| ____|   _  __ _  ___ _ __   ___      
|  _|| | | |/ _` |/ _ \ '_ \ / _ \     
| |__| |_| | (_| |  __/ | | |  __/     
|_____\__,_|\__, |\___|_| |_|\___|     
 ____       |___/       _   _          
|  _ \ ___ | |__   ___ | |_(_) ___ ___ 
| |_) / _ \| '_ \ / _ \| __| |/ __/ __|
|  _ < (_) | |_) | (_) | |_| | (__\__ \
|_|_\_\___/|_.__/ \___/ \__|_|\___|___/
|  _ \(_)_   _(_)___(_) ___  _ __      
| | | | \ \ / / / __| |/ _ \| '_ \     
| |_| | |\ V /| \__ \ | (_) | | | |    
|____/|_| \_/ |_|___/_|\___/|_| |_|    
*/
