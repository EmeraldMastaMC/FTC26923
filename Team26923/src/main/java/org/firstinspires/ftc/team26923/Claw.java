package org.firstinspires.ftc.team26923;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.team26923.GalaxyRunner.TeleOpComponent;

public class Claw extends TeleOpComponent {

    private static final String LEFT_SERVO_NAME = "left claw";
    private static final String RIGHT_SERVO_NAME = "right claw";
    private final CRServo leftServo;
    private final CRServo rightServo;
    private boolean openControl;
    private boolean closeControl;

    public Claw(HardwareMap hardwareMap, Gamepad gamepad) {
        rightServo = hardwareMap.get(CRServo.class, RIGHT_SERVO_NAME);
        leftServo = hardwareMap.get(CRServo.class, LEFT_SERVO_NAME);
        setGamepad(gamepad);
        init();
    }
    public Claw(HardwareMap hardwareMap) {
        rightServo = hardwareMap.get(CRServo.class, RIGHT_SERVO_NAME);
        leftServo = hardwareMap.get(CRServo.class, LEFT_SERVO_NAME);
        setGamepad(null);
        disableAutoEnableControlsOnStart();
        init();
    }

    public void close() {
        rightServo.setDirection(DcMotorSimple.Direction.REVERSE);
        rightServo.setPower(1.0);

        leftServo.setDirection(DcMotorSimple.Direction.FORWARD);
        leftServo.setPower(1.0);
    }
    public void open() {
        rightServo.setDirection(DcMotorSimple.Direction.FORWARD);
        rightServo.setPower(1.0);

        leftServo.setDirection(DcMotorSimple.Direction.REVERSE);
        leftServo.setPower(1.0);
    }

    public void stopServos() {
        rightServo.setDirection(DcMotorSimple.Direction.FORWARD);
        rightServo.setPower(0.0);

        leftServo.setDirection(DcMotorSimple.Direction.REVERSE);
        leftServo.setPower(0.0);
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
    @Override
    public void poll() {
        if (openControl) {
            open();
        }
        if (closeControl) {
            close();
        }
        if (!closeControl && !openControl) {
            stopServos();
        }
    }
}