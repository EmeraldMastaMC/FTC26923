package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.GalaxyRunner.PID;
import org.firstinspires.ftc.teamcode.GalaxyRunner.TeleOpComponent;

public class Extension extends TeleOpComponent {
    private static final String EXTENSION_MOTOR_NAME = "extension_motor";
    private static final double SENSITIVITY = 30;
    private static final double UNLOCK_PIVOT_ANGLE = 55.0;
    private static final double FULL_RELATIVE_MAX = -4000.0;
    private static final double RELATIVE_MAX_MULTIPLIER = 0.60;
    private static final double RELATIVE_MAX = FULL_RELATIVE_MAX * RELATIVE_MAX_MULTIPLIER;
    private static final double ANALOG_STICK_DEADZONE = 0.03;
    private boolean upControl;
    private boolean downControl;
    private boolean upPresetControl;
    private boolean downPresetControl;
    private double analogStickControl;
    private final DcMotor extensionMotor;
    private double target = 0;
    private double max = 0;
    private double fullMax = 0;
    private double min = 0;
    private final PID pid = new PID(0.1, 0.0, 0.0);
    private final Pivot pivot;

    public Extension(HardwareMap hardwareMap, Gamepad gamepad, Pivot pivot) {
        extensionMotor = hardwareMap.get(DcMotorEx.class, EXTENSION_MOTOR_NAME);
        this.pivot = pivot;
        setGamepad(gamepad);
        doesRequireThreadToInit();
        initMinMax();
    }
    public Extension(HardwareMap hardwareMap, Pivot pivot) {
        extensionMotor = hardwareMap.get(DcMotorEx.class, EXTENSION_MOTOR_NAME);
        this.pivot = pivot;
        setGamepad(null);
        disableAutoEnableControlsOnStart();
        doesRequireThreadToInit();
        initMinMax();
    }
    private void initMinMax() {
        min = extensionMotor.getCurrentPosition();
        fullMax = min + FULL_RELATIVE_MAX;
        max = min + RELATIVE_MAX;
        target = min;
    }

    public void fullExtend() {
        target = max;
    }

    public void highBasket() {
        target = fullMax;
    }

    public void fullContract() {
        target = min;
    }


    public void extend() {
        target -= SENSITIVITY;
    }

    public void contract() {
        target += SENSITIVITY;
    }

    public void analogSticks() {
        double pos = extensionMotor.getCurrentPosition();
        if (pivot.getTargetDegrees() < UNLOCK_PIVOT_ANGLE) {
            if (max > min) {
                if (target > max) {
                    target = max - 1.0;
                }
                if (Math.abs(analogStickControl) > ANALOG_STICK_DEADZONE) {
                    if ((analogStickControl < 0.0) && (pos < max)) {
                        target -= analogStickControl * SENSITIVITY;
                    }
                    if ((analogStickControl > 0.0) && (pos > min)) {
                        target -= analogStickControl * SENSITIVITY;
                    }
                }
            } else if (min >= max){
                if (target < max) {
                    target = max + 1.0;
                }
                if (Math.abs(analogStickControl) > ANALOG_STICK_DEADZONE) {
                    if ((analogStickControl < 0.0) && (pos > max)) {
                        target += analogStickControl * SENSITIVITY;
                    }
                    if ((analogStickControl > 0.0) && (pos < min)) {
                        target += analogStickControl * SENSITIVITY;
                    }
                }
            }
        } else {
            if (Math.abs(analogStickControl) > ANALOG_STICK_DEADZONE) {
                if (max > min) {
                    target -= analogStickControl * SENSITIVITY;
                } else if (min >= max) {
                    target += analogStickControl * SENSITIVITY;
                }

            }
        }
    }
    @Override
    public void controls() {
        // Manual Override Controls
        upControl = false;
        downControl = false;

        // Preset Controls
        upPresetControl = getGamepad().dpad_right;
        downPresetControl = getGamepad().dpad_left;

        // Analog Controls
        analogStickControl = getGamepad().right_stick_y;
    }

    @Override
    public void nullifyControls() {
        upControl = false;
        downControl = false;
        upPresetControl = false;
        downPresetControl = false;
        analogStickControl = 0.0;
    }

    @Override
    public void poll() {
        double pos = extensionMotor.getCurrentPosition();
        if(max > min) {
            if (upControl && (pos < max)) {
                extend();
            }
            if (downControl && (pos > min)) {
                contract();
            }

        } else if (min >= max){
            if (upControl && (pos > max)) {
                extend();
            }
            if (downControl && (pos < min)) {
                contract();
            }
        }
        if (downPresetControl) {
            fullContract();
        }
        if (upPresetControl) {
            fullExtend();
        }
        analogSticks();
        extensionMotor.setPower(pid.control(target, extensionMotor.getCurrentPosition()));
    }

    @Override
    public void init() {
        fullContract();
    }

    @Override
    public void deinit() {
        fullContract();
    }
}
