/*
 * Copyright (c) 2020 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package teamcode;

import androidx.annotation.NonNull;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.teamcode.RedTeamElementDeterminationPipeline;
//import org.firstinspires.ftc.teamcode.SpikeMarkPosition;
//import org.firstinspires.ftc.teamcode.constants.AutoServoConstants;
//import org.firstinspires.ftc.teamcode.rr.MecanumDrive;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.Timer;
import java.util.TimerTask;


/*
 * This sample demonstrates a basic (but battle-tested and essentially
 * 100% accurate) method of detecting the TeamElement when lined up with
 * the sample regions over the first 3 stones.
 */
@Autonomous(name = "4 NessieRedStacks :)")
//@Disabled
public class NessieCanSeeRed extends LinearOpMode {
    //  telemetry.addData("Status", "sInitialized");
    //     telemetry.update();

    // Wait for the game to start (driver presses PLAY)
    private ElapsedTime runtime = new ElapsedTime();
    // private Timer timer = new Timer();
    public DcMotor fL = null;
    public DcMotor fR = null;
    public DcMotor bL = null;
    public DcMotor bR = null;
    private DcMotor Rslide = null;
    private DcMotor Lslide = null;
    private Servo l1 = null;
    private Servo l2 = null;
    private Servo c1 = null;
    private Servo c2 = null;
    public DcMotor cR = null;
    public DcMotor iW = null;
    private Servo i1 = null;
    private Servo i2 = null;
    private Servo drone = null;
    private int timesAPressed = 0;
    private Servo PurplePixelDropper = null;
    OpenCvWebcam webcam;
    RedTeamElementDeterminationPipeline pipeline;

    @Override
    public void runOpMode() {
        /**
         * NOTE: Many comments have been omitted from this sample for the
         * sake of conciseness. If you're just starting out with EasyOpenCv,
         * you should take a look at {@link InternalCamera1Example} or its
         * webcam counterpart, {@link WebcamExample} first.
         */


        telemetry.addData("Status", "sInitialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        // Wait for the game to start (driver presses PLAY)
        fL = hardwareMap.get(DcMotor.class, "fL");
        fR = hardwareMap.get(DcMotor.class, "fR");
        bL = hardwareMap.get(DcMotor.class, "bL");
        bR = hardwareMap.get(DcMotor.class, "bR");
        Rslide = hardwareMap.get(DcMotor.class, "Rslide");
        Lslide = hardwareMap.get(DcMotor.class, "Lslide");
        l1 = hardwareMap.get(Servo.class, "l1");
        l2 = hardwareMap.get(Servo.class, "l2");
        c1 = hardwareMap.get(Servo.class, "c1");
        c2 = hardwareMap.get(Servo.class, "c2");
        cR = hardwareMap.get(DcMotor.class, "cR");
        iW = hardwareMap.get(DcMotor.class, "iW");
        i1 = hardwareMap.get(Servo.class, "i1");
        i2 = hardwareMap.get(Servo.class, "i2");
        PurplePixelDropper = hardwareMap.get(Servo.class, "PurplePixelDropper");
        drone = hardwareMap.get(Servo.class, "drone");


        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        fL.setDirection(DcMotor.Direction.REVERSE);
        bL.setDirection(DcMotor.Direction.REVERSE);
        fR.setDirection(DcMotor.Direction.FORWARD);
        bR.setDirection(DcMotor.Direction.FORWARD);
        Rslide.setDirection(DcMotor.Direction.REVERSE);
        Lslide.setDirection(DcMotor.Direction.FORWARD);
        // Wait for the game to start (driver presses PLAY)
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        pipeline = new RedTeamElementDeterminationPipeline();
        webcam.setPipeline(pipeline);

        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though.
//        webcam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
        webcam.setMillisecondsPermissionTimeout(2500);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                /*
                 * This will be called if the camera could not be opened
                 */
                telemetry.addData("erroCode", errorCode);
            }
        });

        //MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(12, -63, Math.PI / 2));
//        timer.schedule(new PutGrabberToCertainPosition(0), 3000);

        waitForStart();

        while (opModeIsActive()) {
//            sleep(1000);
            telemetry.addData("Analysis", pipeline.getAnalysis());
            telemetry.update();
            if (pipeline.getAnalysis() == SpikeMarkPosition.UNO) {


//          code
                //code
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 1) {// forward
                    fL.setPower(0.5);
                    bL.setPower(0.5);
                    fR.setPower(0.5);
                    bR.setPower(0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// turn left
                    fL.setPower(-0.5);
                    bL.setPower(-0.5);
                    fR.setPower(0.5);
                    bR.setPower(0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// forward
                    fL.setPower(0.5);
                    bL.setPower(0.5);
                    fR.setPower(0.5);
                    bR.setPower(0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 1.05) {// rollers
                    cR.setPower(-0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// back up
                    fL.setPower(-0.5);
                    bL.setPower(-0.5);
                    fR.setPower(-0.5);
                    bR.setPower(-0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// strafe left
                    fL.setPower(-0.5);
                    bL.setPower(0.5);
                    fR.setPower(0.5);
                    bR.setPower(-0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// forward
                    fL.setPower(0.5);
                    bL.setPower(0.5);
                    fR.setPower(0.5);
                    bR.setPower(0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.8) {// turn around
                    fL.setPower(0.5);
                    bL.setPower(0.5);
                    fR.setPower(-0.5);
                    bR.setPower(-0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// strafe left
                    fL.setPower(-0.5);
                    bL.setPower(0.5);
                    fR.setPower(0.5);
                    bR.setPower(-0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// arm up
                    Rslide.setPower(0.5);
                    Lslide.setPower(0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// claw flip
                    l1.setPosition(0.6);
                    l2.setPosition(0);
                    c1.setPosition(0);

                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// forward
                        fL.setPower(0.3);
                        bL.setPower(0.3);
                        fR.setPower(0.3);
                        bR.setPower(0.3);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// open claw
                        c2.setPosition(0.4);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// close claw
                        c2.setPosition(0.0);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// backwards
                        fL.setPower(-0.3);
                        bL.setPower(-0.3);
                        fR.setPower(-0.3);
                        bR.setPower(-0.3);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// left
                        fL.setPower(-0.3);
                        bL.setPower(-0.3);
                        fR.setPower(-0.3);
                        bR.setPower(-0.3);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// claw flip
                        l1.setPosition(0.6);
                        l2.setPosition(0);
                        c1.setPosition(0);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// left / back?
                        fL.setPower(-0.3);
                        bL.setPower(-0.3);
                        fR.setPower(-0.3);
                        bR.setPower(-0.3);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// left / back?
                        c2.setPosition(0.4);
                    }




                }




            } else if (pipeline.getAnalysis() == SpikeMarkPosition.DOS) {


                //code
                //code
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 1) {// forward
                    fL.setPower(0.5);
                    bL.setPower(0.5);
                    fR.setPower(0.5);
                    bR.setPower(0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// turn left
                    fL.setPower(-0.5);
                    bL.setPower(-0.5);
                    fR.setPower(0.5);
                    bR.setPower(0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// forward
                    fL.setPower(0.5);
                    bL.setPower(0.5);
                    fR.setPower(0.5);
                    bR.setPower(0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 1.05) {// rollers
                    cR.setPower(-0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// back up
                    fL.setPower(-0.5);
                    bL.setPower(-0.5);
                    fR.setPower(-0.5);
                    bR.setPower(-0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// strafe left
                    fL.setPower(-0.5);
                    bL.setPower(0.5);
                    fR.setPower(0.5);
                    bR.setPower(-0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// forward
                    fL.setPower(0.5);
                    bL.setPower(0.5);
                    fR.setPower(0.5);
                    bR.setPower(0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.8) {// turn around
                    fL.setPower(0.5);
                    bL.setPower(0.5);
                    fR.setPower(-0.5);
                    bR.setPower(-0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// strafe left
                    fL.setPower(-0.5);
                    bL.setPower(0.5);
                    fR.setPower(0.5);
                    bR.setPower(-0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// arm up
                    Rslide.setPower(0.5);
                    Lslide.setPower(0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// claw flip
                    l1.setPosition(0.6);
                    l2.setPosition(0);
                    c1.setPosition(0);

                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// forward
                        fL.setPower(0.3);
                        bL.setPower(0.3);
                        fR.setPower(0.3);
                        bR.setPower(0.3);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// open claw
                        c2.setPosition(0.4);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// close claw
                        c2.setPosition(0.0);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// backwards
                        fL.setPower(-0.3);
                        bL.setPower(-0.3);
                        fR.setPower(-0.3);
                        bR.setPower(-0.3);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// left
                        fL.setPower(-0.3);
                        bL.setPower(-0.3);
                        fR.setPower(-0.3);
                        bR.setPower(-0.3);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// claw flip
                        l1.setPosition(0.6);
                        l2.setPosition(0);
                        c1.setPosition(0);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// left / back?
                        fL.setPower(-0.3);
                        bL.setPower(-0.3);
                        fR.setPower(-0.3);
                        bR.setPower(-0.3);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// left / back?
                        c2.setPosition(0.4);
                    }
                }
            } else if(pipeline.getAnalysis() == SpikeMarkPosition.TRES) {

                //code
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 1) {// forward
                    fL.setPower(0.5);
                    bL.setPower(0.5);
                    fR.setPower(0.5);
                    bR.setPower(0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// turn left
                    fL.setPower(-0.5);
                    bL.setPower(-0.5);
                    fR.setPower(0.5);
                    bR.setPower(0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// forward
                    fL.setPower(0.5);
                    bL.setPower(0.5);
                    fR.setPower(0.5);
                    bR.setPower(0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 1.05) {// rollers
                    cR.setPower(-0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// back up
                    fL.setPower(-0.5);
                    bL.setPower(-0.5);
                    fR.setPower(-0.5);
                    bR.setPower(-0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// strafe left
                    fL.setPower(-0.5);
                    bL.setPower(0.5);
                    fR.setPower(0.5);
                    bR.setPower(-0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// forward
                    fL.setPower(0.5);
                    bL.setPower(0.5);
                    fR.setPower(0.5);
                    bR.setPower(0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.8) {// turn around
                    fL.setPower(0.5);
                    bL.setPower(0.5);
                    fR.setPower(-0.5);
                    bR.setPower(-0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// strafe left
                    fL.setPower(-0.5);
                    bL.setPower(0.5);
                    fR.setPower(0.5);
                    bR.setPower(-0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// arm up
                    Rslide.setPower(0.5);
                    Lslide.setPower(0.5);
                }
                runtime.reset();
                while (opModeIsActive() && runtime.seconds() < 0.5) {// claw flip
                    l1.setPosition(0.6);
                    l2.setPosition(0);
                    c1.setPosition(0);

                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// forward
                        fL.setPower(0.3);
                        bL.setPower(0.3);
                        fR.setPower(0.3);
                        bR.setPower(0.3);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// open claw
                        c2.setPosition(0.4);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// close claw
                        c2.setPosition(0.0);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// backwards
                        fL.setPower(-0.3);
                        bL.setPower(-0.3);
                        fR.setPower(-0.3);
                        bR.setPower(-0.3);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// left
                        fL.setPower(-0.3);
                        bL.setPower(-0.3);
                        fR.setPower(-0.3);
                        bR.setPower(-0.3);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// claw flip
                        l1.setPosition(0.6);
                        l2.setPosition(0);
                        c1.setPosition(0);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// left / back?
                        fL.setPower(-0.3);
                        bL.setPower(-0.3);
                        fR.setPower(-0.3);
                        bR.setPower(-0.3);
                    }
                    runtime.reset();
                    while (opModeIsActive() && runtime.seconds() < 0.5) {// left / back?
                        c2.setPosition(0.4);
                    }



                }
            }
        }


    }}
//}