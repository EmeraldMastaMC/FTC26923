package org.firstinspires.ftc.team26923;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.team26923.GalaxyRunner.Galaxy;
import org.firstinspires.ftc.team26923.GalaxyRunner.Pollable;
import org.firstinspires.ftc.team26923.GalaxyRunner.Utils.Pose2D;

import java.util.ArrayList;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Auto")
public class Auton extends LinearOpMode {
    @Override
    public void runOpMode() {
        // Create Components
        Claw claw = new Claw(hardwareMap);
        Pivot pivot = new Pivot(hardwareMap);
        Extension extension = new Extension(hardwareMap, pivot);

        // Add them to an array list that GalaxyRunner uses to control our components
        ArrayList<Pollable> components = new ArrayList<>();
        components.add(claw);
        components.add(extension);
        components.add(pivot);

        // Create our galaxy
        Galaxy galaxy = new Galaxy(hardwareMap, telemetry, components);
        galaxy.initialize();

        waitForStart();
        if(isStopRequested()) return;
        galaxy.start();

        galaxy.moveTo(new Pose2D(30, 30, Math.PI / 2.0));
        galaxy.strafeLeft(5);
        galaxy.strafeRight(5);
        galaxy.backward(5);
        galaxy.forward(5);
        galaxy.rotateClockwise(Math.PI);
        galaxy.rotateCounterClockwise(Math.PI);

        galaxy.stop();
    }
}