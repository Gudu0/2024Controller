package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.PerpetualCommand;
import com.arcrobotics.ftclib.command.button.Button;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.TriggerReader;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.commands.arm.NudgeArm;
import org.firstinspires.ftc.teamcode.commands.arm.SetArmLevel;
import org.firstinspires.ftc.teamcode.commands.carousel.MoveCarousel;
import org.firstinspires.ftc.teamcode.commands.carousel.StopCarousel;
import org.firstinspires.ftc.teamcode.commands.drive.bc4h.DefaultDrive;
import org.firstinspires.ftc.teamcode.commands.intake.MoveIntake;
import org.firstinspires.ftc.teamcode.commands.intake.StopIntake;
import org.firstinspires.ftc.teamcode.commands.leds.blinkin.ShowAllianceColor;
import org.firstinspires.ftc.teamcode.commands.leds.blinkin.ShowTeamColors;
import org.firstinspires.ftc.teamcode.commands.webcam.DetectTSEPosition;
import org.firstinspires.ftc.teamcode.commands.webcam.StreamToDashboard;
import org.firstinspires.ftc.teamcode.cv.OpenCvShippingElementDetector;
import org.firstinspires.ftc.teamcode.opmodes.createmechanism.CreateLEDs;
import org.firstinspires.ftc.teamcode.subsystems.arm.ArmSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.carousel.CarouselSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.drive.bc4h.BC4HDriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.leds.blinkin.LEDSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.webcam.WebCamSubsystem;

import java.util.HashMap;
import java.util.Map;


@TeleOp(name="Telop: FTCLib BlinkinWebCamArmCarouselDT Example", group="FTCLib")
public class FTCLibBlinkinWebCamArmCarouselDTTeleop extends CommandOpMode {

    private LEDSubsystem m_leds;
    private WebCamSubsystem m_webCam;
    private ArmSubsystem m_arm;
    private IntakeSubsystem m_intake;
    private CarouselSubsystem m_carousel;
    private BC4HDriveSubsystem m_bc4h_drive;





    private NudgeArm m_nudgeArmUp;
    private NudgeArm m_nudgeArmDown;

    private SetArmLevel m_moveToLevel0;
    private SetArmLevel m_moveToLevel1;
    private SetArmLevel m_moveToLevel2;
    private SetArmLevel m_moveToLevel3;

    private MoveIntake m_seGrabber;
    private MoveIntake m_seReleaser;
    private StopIntake m_stopIntake;

    private MoveCarousel m_moveCarouselRight;
    private MoveCarousel m_moveCarouselLeft;
    private StopCarousel m_stopCarousel;

    private DefaultDrive m_driveRobot;

    @Override
    public void initialize() {

        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = dashboard.getTelemetry();

        //set up Game pad 1
        GamepadEx driveOp = new GamepadEx(gamepad1);

        //set up Game pad 2
        GamepadEx toolOp2 = new GamepadEx(gamepad2);

        //create LED SubSystem
        CreateLEDs createLEDs = new CreateLEDs(hardwareMap, "blinkin", driveOp, true);

        //m_leds = new LEDSubsystem(hardwareMap,"blinkin", 10);

        //create webcam subsystem
        //m_webCam = new WebCamSubsystem(hardwareMap,"Webcam 1",new OpenCvShippingElementDetector(640,480,telemetry));



        Map<Integer, Integer> armLevels = new HashMap<>();
        armLevels.put(0,0);
        armLevels.put(1,200);
        armLevels.put(2,500);
        armLevels.put(3,850);

        m_arm = new ArmSubsystem(hardwareMap,"arm", DcMotorEx.RunMode.STOP_AND_RESET_ENCODER, (HashMap) armLevels);
        m_arm.setArmTargetPosition(0);
        m_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        m_nudgeArmUp = new NudgeArm(m_arm,5, telemetry);
        m_nudgeArmDown = new NudgeArm(m_arm, -5, telemetry);

        m_moveToLevel0 = new SetArmLevel(m_arm,0, telemetry);
        m_moveToLevel1 = new SetArmLevel(m_arm,1, telemetry);
        m_moveToLevel2 = new SetArmLevel(m_arm,2, telemetry);
        m_moveToLevel3 = new SetArmLevel(m_arm,3, telemetry);


        Button armNudger = new GamepadButton(toolOp2, GamepadKeys.Button.RIGHT_STICK_BUTTON);

        //A Level 0
        Button armLevel0 = new GamepadButton(toolOp2, GamepadKeys.Button.A);
        //X Level 1
        Button armLevel1 = new GamepadButton(toolOp2, GamepadKeys.Button.X);
        //Y Level 2
        Button armLevel2 = new GamepadButton(toolOp2, GamepadKeys.Button.Y);
        //B Level 3
        Button armLevel3 = new GamepadButton(toolOp2, GamepadKeys.Button.B);


        armNudger.whenPressed(new InstantCommand(() -> {
            if(toolOp2.getRightY() == 1){
                m_nudgeArmUp.schedule();
            }
            else if(toolOp2.getRightY() == -1){
                m_nudgeArmDown.schedule();
            }
        }));

        armLevel0.whenPressed(m_moveToLevel0);
        armLevel1.whenPressed(m_moveToLevel1);
        armLevel2.whenPressed(m_moveToLevel2);
        armLevel3.whenPressed(m_moveToLevel3);

        m_intake = new IntakeSubsystem(hardwareMap,"intake", DcMotorSimple.Direction.REVERSE, 0.0);

        m_seGrabber = new MoveIntake(m_intake, -0.75, () -> toolOp2.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER), telemetry);
        m_seReleaser = new MoveIntake(m_intake, 0.6, () -> toolOp2.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER), telemetry);
        m_stopIntake = new StopIntake(m_intake);

        m_intake.setDefaultCommand(new PerpetualCommand(m_stopIntake));


        m_carousel = new CarouselSubsystem(hardwareMap,"carousel");

        m_moveCarouselRight = new MoveCarousel(m_carousel,0.5, telemetry);
        m_moveCarouselLeft = new MoveCarousel(m_carousel, -0.5, telemetry);
        m_stopCarousel = new StopCarousel(m_carousel, telemetry);

        Button carouselRight = new GamepadButton(toolOp2, GamepadKeys.Button.RIGHT_BUMPER);
        Button carouselLeft = new GamepadButton(toolOp2, GamepadKeys.Button.LEFT_BUMPER);


        carouselRight.whileHeld(m_moveCarouselRight);
        carouselLeft.whileHeld(m_moveCarouselLeft);
        m_carousel.setDefaultCommand(new PerpetualCommand(m_stopCarousel));

        m_bc4h_drive = new BC4HDriveSubsystem(hardwareMap, "frontLeft", "backLeft", "frontRight", "backRight",
                                                       560, 7.5, DcMotor.Direction.FORWARD, DcMotor.Direction.FORWARD,
                                                        DcMotor.Direction.REVERSE, DcMotor.Direction.REVERSE, DcMotor.RunMode.RUN_USING_ENCODER );


        m_driveRobot = new DefaultDrive(m_bc4h_drive, () -> driveOp.getLeftY(), () -> driveOp.getLeftX(), () -> driveOp.getRightX());
        m_bc4h_drive.setDefaultCommand(m_driveRobot);


        m_seGrabber.schedule();
        m_seReleaser.schedule();

        CommandScheduler.getInstance().run();


    }
}
