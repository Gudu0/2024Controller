package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.teamcode.bots.FrenzyBot;
import org.firstinspires.ftc.teamcode.odometry.IBaseOdometry;
import org.firstinspires.ftc.teamcode.odometry.VSlamOdometry;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "Frenzy", group = "Robot15173")
public class FrenzyMode extends LinearOpMode {

    // Declare OpMode Members
    FrenzyBot robot = new FrenzyBot();
    IBaseOdometry odometry = null;

    // Timing related variables
    long GAMEPAD_LOCKOUT_TIME_MS = 200;
    Deadline gamepadRateLimit;

    // Intake related variables
    boolean changedIntake = false;
    boolean intakeReverse = false;

    // Rotator related variable
    boolean changedRotator = false;

    @Override
    public void runOpMode() {
        try {
            try{
                gamepadRateLimit = new Deadline(GAMEPAD_LOCKOUT_TIME_MS, TimeUnit.MILLISECONDS);

                robot.init(this, this.hardwareMap, telemetry);

                odometry =  VSlamOdometry.getInstance(this.hardwareMap, 20);
                odometry.setInitPosition(50, 15, 0); // TODO: Remove this

                Thread odometryThread = new Thread(odometry);
                odometryThread.start();
            } catch (Exception ex) {
                telemetry.addData("Init", ex.getMessage());
            }
            telemetry.addData("Bot Info", robot.printInfo());
            telemetry.update();

            // Wait for the game to start (driver presses PLAY)
            waitForStart();

            while (opModeIsActive()) {
                handleDriveTrain();
                handleSpecialActions();

                sendTelemetry();
            }
        } catch (Exception ex) {
            telemetry.addData("Issues with the OpMode", ex.getMessage());
            telemetry.update();
            sleep(1000);
        }
        finally {
            robot.activateIntake(0);
            robot.activateRotator(0);
            robot.activateLift(0);
            if (odometry != null) {
                odometry.stop();
            }
        }
    }

    private void sendTelemetry() {
        telemetry.addData("Left front", robot.getLeftOdometer());
        telemetry.addData("Right front", robot.getRightOdometer());
        telemetry.addData("Left Back", robot.getLeftBackOdometer());
        telemetry.addData("Right Back", robot.getRightBackOdometer());
        telemetry.addData("X", odometry.getCurrentX());
        telemetry.addData("Y", odometry.getCurrentY());
        telemetry.addData("Heading", odometry.getOrientation());
        telemetry.addData("Heading Adjusted", odometry.getAdjustedCurrentHeading());
        telemetry.addData("List position", robot.getLiftPosition());
        telemetry.update();
    }

    private void handleDriveTrain() {
        // DRIVING
        double drive = gamepad1.left_stick_y;
        double turn = 0;
        double ltrigger = gamepad1.left_trigger;
        double rtrigger = gamepad1.right_trigger;
        if (ltrigger > 0) {
            turn = -ltrigger;
        } else if (rtrigger > 0) {
            turn = rtrigger;
        }

        double strafe = gamepad1.right_stick_x;

        if (Math.abs(strafe) > 0) {
            if (strafe < 0) {
                robot.strafeRight(Math.abs(strafe));
            } else {
                robot.strafeLeft(Math.abs(strafe));
            }
        } else {
            robot.move(drive, turn);
        }
    }

    protected void handleSpecialActions() {
        handleLift();
        handleDropper();
        handleIntake();
        handleOuttake();
        handleTurntable();
    }

    protected void handleOuttake() {
        if (isButtonPressable()) {
            if (gamepad2.right_bumper){
                startGamepadLockout();
                intakeReverse = !intakeReverse;

                if (intakeReverse){
                    robot.startIntake();
                } else {
                    robot.stopIntake();
                }
            }
        }
    }

    protected void handleIntake() {
        if (isButtonPressable()) {
            if (gamepad2.left_bumper) {
                startGamepadLockout();
                changedIntake = !changedIntake;

                if (changedIntake) {
                    robot.reverseIntake();
                } else {
                    robot.stopIntake();
                }
            }
        }
    }

    protected void handleTurntable() {
        if (isButtonPressable()) {
            if (gamepad1.y) {
                startGamepadLockout();
                changedRotator = !changedRotator;

                if (changedRotator) {
                    robot.startTurntableBlue();
                } else {
                    robot.stopTurntable();
                }
            }
        }
        if (isButtonPressable()) {
            if (gamepad1.x) {
                startGamepadLockout();
                changedRotator = !changedRotator;

                if (changedRotator) {
                    robot.startTurntableRed();
                } else {
                    robot.stopTurntable();
                }
            }
        }
    }

    protected void handleLift() {
        if (isButtonPressable()) {
            double liftVal = gamepad2.left_stick_y;
            if (liftVal > 0.5) {
                startGamepadLockout();
                robot.liftToLower();
            } else if (liftVal < -0.5) {
                startGamepadLockout();
                robot.liftToUpper();
            }
        }
    }

    protected void handleDropper() {
        if ( gamepad2.dpad_up){
            robot.dropElement();
        }
        else if (gamepad2.dpad_down){
            robot.resetDropper();
        }
    }

    private void startGamepadLockout() {
        gamepadRateLimit.reset();
    }
    private boolean isButtonPressable() {
        return gamepadRateLimit.hasExpired();
    }
}
