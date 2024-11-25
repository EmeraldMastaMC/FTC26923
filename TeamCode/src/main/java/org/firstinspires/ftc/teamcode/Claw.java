package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.GalaxyRunner.TeleOpComponent;

public class Claw extends TeleOpComponent {

    private static final double LEFT_CLOSE_TARGET = 1.0;
    private static final double RIGHT_CLOSE_TARGET = 0.0;
    private static final double LEFT_OPEN_TARGET = 0.5;
    private static final double RIGHT_OPEN_TARGET = 0.5;
    private static final double LEFT_FULL_OPEN_TARGET = 0.0;
    private static final double RIGHT_FULL_OPEN_TARGET = 1.0;
    private static final String LEFT_SERVO_NAME = "left claw";
    private static final String RIGHT_SERVO_NAME = "right claw";
    private final Servo leftServo;
    private final Servo rightServo;
    private boolean openControl;
    private boolean closeControl;
    public Claw(HardwareMap hardwareMap, Gamepad gamepad) {
        rightServo = hardwareMap.get(Servo.class, RIGHT_SERVO_NAME);
        leftServo = hardwareMap.get(Servo.class, LEFT_SERVO_NAME);
        setGamepad(gamepad);
        init();
    }
    public Claw(HardwareMap hardwareMap) {
        rightServo = hardwareMap.get(Servo.class, RIGHT_SERVO_NAME);
        leftServo = hardwareMap.get(Servo.class, LEFT_SERVO_NAME);
        setGamepad(null);
        disableAutoEnableControlsOnStart();
        init();
    }
    @Override
    public void controls() {
        // Preset Controls
        openControl = getGamepad().left_bumper;
        closeControl = getGamepad().right_bumper;
    }

    @Override
    public void nullifyControls() {
        openControl = false;
        closeControl = false;
    }
    public void close() {
        rightServo.setPosition(RIGHT_CLOSE_TARGET);
        leftServo.setPosition(LEFT_CLOSE_TARGET);
    }
    public void open() {
        rightServo.setPosition(RIGHT_OPEN_TARGET);
        leftServo.setPosition(LEFT_OPEN_TARGET);
    }

    public void fullOpen() {
        rightServo.setPosition(RIGHT_FULL_OPEN_TARGET);
        leftServo.setPosition(LEFT_FULL_OPEN_TARGET);
    }
    @Override
    public void poll() {
        if (openControl) {
            open();
        }
        if (closeControl) {
            close();
        }
    }

    @Override
    public void init() {
        fullOpen();
    }
    @Override
    public void preStart() {
        open();
    }
}