package org.firstinspires.ftc.team26923;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.team26923.GalaxyRunner.PID;
import org.firstinspires.ftc.team26923.GalaxyRunner.TeleOpComponent;

public class Pivot extends TeleOpComponent {
    private static final double SENSITIVITY = 1.2;
    private static final double RELATIVE_MAX = -700;
    private static final double HIGH_BASKET_PRESET_DEG = 75.0;
    private static final double LOW_BASKET_PRESET_DEG = 45.0;
    private static final double START_PRESET_DEG = 37.0;
    private static final String PIVOT_MOTOR_LEFT_NAME = "pivot_motor_0";
    private static final String PIVOT_MOTOR_RIGHT_NAME = "pivot_motor_1";
    private static final double SOFT_DOWN_PRESET_DEG = 10.0;
    private double upControl;
    private double downControl;
    private boolean upPresetControl;
    private boolean downPresetControl;
    private double analogStickControl;

    private final DcMotor pivotMotorLeft;
    private final DcMotor pivotMotorRight;

    private final PID normalPID = new PID(0.1275, 0.0, 0.0);
    private final PID slowPID = new PID(0.01, 0.0, 0.0);
    private PID pid = normalPID;

    private double min = 0;
    private double max = 0;


    private double target = 0;
    private double targetDeg = 0;

    public Pivot(HardwareMap hardwareMap, Gamepad gamepad) {
        pivotMotorLeft = hardwareMap.get(DcMotor.class, PIVOT_MOTOR_LEFT_NAME);
        pivotMotorRight = hardwareMap.get(DcMotor.class, PIVOT_MOTOR_RIGHT_NAME);
        setGamepad(gamepad);
        doesRequireThreadToInit();
        initMinMax();
    }

    public Pivot(HardwareMap hardwareMap) {
        pivotMotorLeft = hardwareMap.get(DcMotor.class, PIVOT_MOTOR_LEFT_NAME);
        pivotMotorRight = hardwareMap.get(DcMotor.class, PIVOT_MOTOR_RIGHT_NAME);
        setGamepad(null);
        disableAutoEnableControlsOnStart();
        doesRequireThreadToInit();
        initMinMax();
    }

    @Override
    public void controls() {
        // Manual Override Controls
        upControl = 0.0;
        downControl = 0.0;

        // Preset Controls
        upPresetControl = getGamepad().dpad_up;
        downPresetControl = getGamepad().dpad_down;

        // Analog Stick
        analogStickControl = getGamepad().left_stick_y;

    }

    @Override
    public void nullifyControls() {
        upControl = 0.0;
        downControl = 0.0;
        upPresetControl = false;
        downPresetControl = false;
        analogStickControl = 0.0;
    }

    @Override
    public void poll() {
        double pos = pivotMotorRight.getCurrentPosition();
        if (max > min) {
            if((pos >= (min + 50)) && (pid != normalPID)) {
                pid = normalPID;
            }
        } else if (min >= max) {
            if((pos <= (min - 50)) && (pid != normalPID)) {
                pid = normalPID;
            }
        }
        if ((downControl > 0.0) && (targetDeg >= 0.0)) {
            targetDeg -= downControl * SENSITIVITY;
        }
        if ((upControl > 0.0) && (targetDeg <= 90.0)) {
            targetDeg += upControl * SENSITIVITY;
        }
        if (Math.abs(analogStickControl) > 0.03) {
            analogSticks();
        }
        if (upPresetControl) {
            highBasket();
        }
        if (downPresetControl) {
            down();
        }
        setDegrees();
    }

    private void initMinMax() {
        min = pivotMotorRight.getCurrentPosition();
        max = min + RELATIVE_MAX;
        target = min;
    }
    // deg is between 0 and 90 inclusive
    public void setDegrees() {
        pivotMotorLeft.setPower(pid.control(target, pivotMotorLeft.getCurrentPosition()));
        pivotMotorRight.setPower(pid.control(target, pivotMotorRight.getCurrentPosition()));
    }

    public void setTargetDegrees(double deg) {
        if((0 <= deg) && (deg <= 90)) {
            double targetMultiplier = deg / 90.0;
            double range = 0;
            if (max > min) {
                range = max - min;
            } else if (min >= max) {
                range = min - max;
            }
            targetDeg = deg;
            if (max > min) {
                target = (range * targetMultiplier) + min;
            } else if (min >= max) {
                target = -(range * targetMultiplier) + min;
            }
        }
    }

    public double getTargetDegrees() {
        return targetDeg;
    }

    public void down() {
        pid = slowPID;
        setTargetDegrees(0);
    }

    public void softDown() {
        pid = slowPID;
        setTargetDegrees(SOFT_DOWN_PRESET_DEG);
    }

    public void up() {
        setTargetDegrees(90.0);
    }

    public void highBasket() {
        setTargetDegrees(HIGH_BASKET_PRESET_DEG);
    }

    public void lowBasket() {
        setTargetDegrees(LOW_BASKET_PRESET_DEG);
    }

    public void analogSticks() {
        if ((analogStickControl < 0.0) && (targetDeg <= 90.0 )) {
            setTargetDegrees(targetDeg - analogStickControl * SENSITIVITY);
        }
        if ((analogStickControl > 0.0) && (targetDeg >= 0.0 )) {
            setTargetDegrees(targetDeg - analogStickControl * SENSITIVITY);
        }
    }
    public void startPosition() {
        setTargetDegrees(START_PRESET_DEG);
    }

    @Override
    public void init() {
        startPosition();
    }

}