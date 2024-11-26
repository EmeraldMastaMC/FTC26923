/* Copyright (c) 2021 FIRST. All rights reserved.
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
package org.firstinspires.ftc.team26923;



import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.team26923.GalaxyRunner.Galaxy;
import org.firstinspires.ftc.team26923.GalaxyRunner.Pollable;
import static org.firstinspires.ftc.team26923.GalaxyRunner.Utils.waitUntilStopRequested;
import java.util.ArrayList;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TeleOp")
public class TeleOp extends LinearOpMode {
    @Override
    public void runOpMode() {
        // Create Components
        Claw claw = new Claw(hardwareMap, gamepad2);
        Pivot pivot = new Pivot(hardwareMap, gamepad2);
        Extension extension = new Extension(hardwareMap, gamepad2, pivot);

        // Add them to an array list that GalaxyRunner uses to control our components
        ArrayList<Pollable> components = new ArrayList<>();
        components.add(claw);
        components.add(extension);
        components.add(pivot);

        // Create our galaxy
        Galaxy galaxy = new Galaxy(hardwareMap, gamepad1, telemetry, components);

        // Runs initialize on all components anything
        galaxy.initialize();

        waitForStart();

        // Activate Components
        galaxy.start();
        claw.open();

        // We are able to control our components until we press stop on the driver hub
        waitUntilStopRequested(this);

        galaxy.stop();
    }
}
