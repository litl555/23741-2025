package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@Config
public class Robot {
    public static Telemetry telemetry;

    public static HardwareMap hardwareMap;
    public static SubsystemHandler sys;

    // ==============================================================
    // +                           CONFIG                           +
    // ==============================================================
    // ===                       drivetrain                       ===
    public static DcMotorEx leftFront, leftRear, rightFront, rightRear;

    // ===                  horizontal extension                  ===
    public static DcMotorEx extendo;

    // ===                         intake                         ===
    public static DcMotorEx intake;
    public static RevColorSensorV3 intakeColorSensor;
    public static Servo intakeWristRight, intakeWristLeft;
    public static CRServo flipper;

    // ===                          lift                          ===
    public static DcMotorEx liftFront, liftBack, liftEncoder;
    public static Servo liftHorzExtFront, liftHorzExtBack;

    // ===                        claw/arm                        ===
    public static Servo armPivotFront, armPivotBack;
    public static Servo clawPitch, clawRoll, claw;

    // ==============================================================
    // +                           STATE                            +
    // ==============================================================


    public static void robotInit(HardwareMap hardwareMap, SubsystemHandler subsystems) {
        Robot.hardwareMap = hardwareMap;
        sys = subsystems;

        telemetry = new MultipleTelemetry();

        leftFront = initMotor("lf");
        leftRear = initMotor("lr");
        rightFront = initMotor("rf");
        rightRear = initMotor("rr");

        extendo = hardwareMap.get(DcMotorEx.class,"extendo");
        extendo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extendo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake = hardwareMap.get(DcMotorEx.class,"intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeColorSensor = hardwareMap.get(RevColorSensorV3.class, "intakeColor");

        intakeWristLeft = hardwareMap.servo.get("iw1");
        intakeWristRight = hardwareMap.servo.get("iw2");
        flipper = hardwareMap.crservo.get("flipper");

        // TODO: on old robot, we had to cache lift values when transitioning from auto to teleop
        // as the lift was not guaranteed to be at the zero position at end
        // if lift is off by a constant factor during teleop after auto, this may be the issue
        // see https://github.com/litl555/Team23741-2023/blob/master/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/FTC/Subsystems/Robot.java#L219
        liftBack = initLiftMotor("liftBack");
        liftFront = initLiftMotor("liftFront");
        liftEncoder = hardwareMap.get(DcMotorEx.class, "liftBack");
        liftEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftHorzExtBack = hardwareMap.servo.get("extensionBack");
        liftHorzExtFront = hardwareMap.servo.get("extensionFront");

        armPivotBack = hardwareMap.servo.get("pivotBack");
        armPivotFront = hardwareMap.servo.get("pivotFront");

        clawPitch = hardwareMap.servo.get("clawPitch");
        clawRoll = hardwareMap.servo.get("clawRoll");
        claw = hardwareMap.servo.get("claw");
    }

    private static DcMotorEx initLiftMotor(String name) {
        DcMotorEx m = hardwareMap.get(DcMotorEx.class, name);
        m.setDirection(DcMotorSimple.Direction.REVERSE);
        m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        return m;
    }

    private static DcMotorEx initMotor(String name) {
        return initMotor(name, DcMotor.ZeroPowerBehavior.BRAKE, DcMotorSimple.Direction.FORWARD);
    }

    private static DcMotorEx initMotor(String name, DcMotor.ZeroPowerBehavior zero, DcMotorSimple.Direction dir) {
        DcMotorEx m = hardwareMap.get(DcMotorEx.class, name);
        m.setZeroPowerBehavior(zero);
        m.setDirection(dir);

        return m;
    }
}