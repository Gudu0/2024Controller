package org.firstinspires.ftc.teamcode.comp.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.shared.GlobalConfig;
import org.firstinspires.ftc.teamcode.shared.GlobalConfig.ALLIANCE_POS;
import org.firstinspires.ftc.teamcode.shared.MotionHardware;
import org.firstinspires.ftc.teamcode.shared.MotionHardware.Direction;
import org.firstinspires.ftc.teamcode.shared.VisionHardware;
import org.firstinspires.ftc.teamcode.shared.VisionHardware.PropPosition;

@Config
@Autonomous(name = "Auto - BA Left", group = "Auto")
public class AutoBL extends LinearOpMode {

    public ALLIANCE_POS alliancePos = ALLIANCE_POS.LEFT;

    public GlobalConfig globalConfig = new GlobalConfig(GlobalConfig.AUTONOMOUS_DELIVERY_MODES.DROPPER);
    MotionHardware robot = new MotionHardware(this, globalConfig);
    VisionHardware vision = new VisionHardware(this, alliancePos);
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        vision.init();

        robot.moveArm(.5, 5, 5);

        waitForStart();

        //TODO In parking code, drop arms to TeleOp pickup position

        // Center Strike Line
        // - Forward: 31.75
        // - Reverse: -44.75
        while(opModeIsActive()) {
            PropPosition propPosition = vision.detectProp();

            switch(propPosition) {
                case MIDDLE:
                    // Dropper Mode
                    robot.moveRobot(.5, -15, 10);
                    robot.dropPixel();
                    sleep(1000);
                    robot.moveRobot(.5, 2, 10);
                    robot.turnRobot(Direction.RIGHT, 14, .5, 10);
                    robot.moveRobot(.5, 21, 10);
                    robot.moveArmMotorToPosition(-1200, 10);
                    robot.moveRobot(.5, -3, 10);
                    robot.strafeWithTime(.5, 270, 2);
                    break;
                case LEFT:
                    //Dropper Mode
                    robot.moveRobot(.5, -13, 10);
                    robot.turnRobot(Direction.LEFT, 7, .5, 10);
                    robot.dropPixel();
                    robot.moveRobot(.5, 3, 10);
                    robot.turnRobot(Direction.RIGHT, 13, .5, 10);
                    robot.moveRobot(.5, 2, 10);
                    robot.turnRobot(Direction.RIGHT, 7.5, .5, 10);
                    robot.moveRobot(.5, 22, 10);
                    robot.moveArmMotorToPosition(-1200, 10);
                    robot.moveRobot(.5, -3, 10);
                    robot.strafeWithTime(.5, 270, 2);
                    break;
                default:
                    //Dropper Mode
                    robot.moveRobot(.5, -13, 10);
                    robot.turnRobot(Direction.RIGHT, 9, .5, 10);
                    robot.moveRobot(.5, -4, 10);
                    robot.dropPixel();
                    robot.moveRobot(.5, 2, 10);
                    robot.turnRobot(Direction.RIGHT, 7, .5, 10);
                    robot.moveRobot(.5, 12, 10);
                    robot.turnRobot(Direction.LEFT, 3, .5, 10);
                    robot.moveRobot(.5, 14, 10);
                    robot.moveArmMotorToPosition(-1200, 10);
                    robot.moveRobot(.5, -3, 10);
                    robot.strafeWithTime(.5, 270, 2);
                    break;
            }
            
            sleep(20);
            break;
        }
    }
}