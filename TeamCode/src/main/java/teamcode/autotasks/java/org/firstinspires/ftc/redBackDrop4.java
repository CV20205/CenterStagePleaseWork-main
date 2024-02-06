package teamcode.autotasks.java.org.firstinspires.ftc;
//package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import java.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.TimerTask;


@Autonomous(name="AutoRedTrussNessie1", group="Linear Opmode")
public class redBackDrop4 extends LinearOpMode {
    //impporting thing code
    private ElapsedTime runtime = new ElapsedTime();//
    private Timer timer = new Timer();
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
    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //blah blah blah
        fL  = hardwareMap.get(DcMotor.class, "fL");
        fR = hardwareMap.get(DcMotor.class, "fR");
        bL  = hardwareMap.get(DcMotor.class, "bL");
        bR = hardwareMap.get(DcMotor.class, "bR");
        Rslide = hardwareMap.get(DcMotor.class, "Rslide");
        Lslide = hardwareMap.get(DcMotor.class, "Lslide");
        l1 = hardwareMap.get(Servo.class, "l1");
        l2 = hardwareMap.get(Servo.class, "l2");
        c1 = hardwareMap.get(Servo.class, "c1");
        c2 = hardwareMap.get(Servo.class, "c2");
        cR  = hardwareMap.get(DcMotor.class, "cR");
        iW = hardwareMap.get(DcMotor.class, "iW");
        i1 = hardwareMap.get(Servo.class, "i1");
        i2 = hardwareMap.get(Servo.class, "i2");
        PurplePixelDropper = hardwareMap.get(Servo.class, "PurplePixelDropper");
        drone = hardwareMap.get(Servo.class, "drone");
        //direction set
        fL.setDirection(DcMotor.Direction.REVERSE);
        bL.setDirection(DcMotor.Direction.REVERSE);
        fR.setDirection(DcMotor.Direction.FORWARD);
        bR.setDirection(DcMotor.Direction.FORWARD);
        Rslide.setDirection(DcMotor.Direction.REVERSE);
        Lslide.setDirection(DcMotor.Direction.FORWARD);
        waitForStart();
        runtime.reset();//13

        /*might want to look into the side strafing for time sake :) NEEDS TESTING !!!
         */

        while(opModeIsActive() && runtime.seconds() < 1.05){//forward purple pixel
            moveForward();

            microClose();
            keepTheArmUp();
            flippyKeep();
        }
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1.05){// back purple
            moveBackward();

            keepTheArmUp();
            microClose();
            flippyKeep();
        }
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 2){//spini tini
            SpinL();//

            keepTheArmUp();
            microClose();
            flippyKeep();
        }
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 6){//to the backdrop
            moveBackward();

            keepTheArmUp();
            microClose();
            flippyKeep();
        }
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 2){//get up to the right
            moveLeft();

            keepTheArmUp();
            microClose();
            flippyKeep();
        }
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1){//more close to the backdrop
            moveForward();

            keepTheArmUp();
            microClose();
            flippyKeep();
        }
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1){//more close to the backdrop
            moveRight();

            keepTheArmUp();
            microClose();
            flippyKeep();
        }
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1){//place YAY
            LinSlideUp();  //lin extension

            keepTheArmUp();
            microClose();
            flippyKeep();
        }
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1){//place YAY
            flippyFlip();//flip

            keepTheArmUp();
            microClose();
            flippyKeep();
        }
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1){//place YAY
            moveBackward();//closer to backdrop

            keepTheArmUp();
            microClose();
            flippyKeep();
        }
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1){//place YAY
            microOpen();//open the micro

            keepTheArmUp();
            microClose();
            flippyKeep();
        }
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1){//place YAY
            flippyUnFlip();//flip back

            keepTheArmUp();
            microClose();
            flippyKeep();
        }
        while(opModeIsActive() && runtime.seconds() < 1){//place YAY
            LinSlideDown();//retract

            keepTheArmUp();
            microClose();
            flippyKeep();
        }

    }
    public void keepTheArmUp(){//the intake cuz Lucas can't drive other-ways
        i1.setPosition(0.7);
        i2.setPosition(0);
    }
    public void moveForward(){//self explanatory
        fL.setPower(.5);
        fR.setPower(.5);
        bL.setPower(.5);
        bR.setPower(.5);
    }
    public void moveRight(){//strafe and position
        fL.setPower(.5);
        fR.setPower(-.5);
        bL.setPower(-.5);
        bR.setPower(.5);
    }
    public void moveLeft(){//strafe and position
        fL.setPower(-.5);
        fR.setPower(.5);
        bL.setPower(.5);
        bR.setPower(-.5);
    }
    public void moveBackward(){//self explanatory
        fL.setPower(-.5);
        fR.setPower(-.5);
        bL.setPower(-.5);
        bR.setPower(-.5);
    }
    public void SpinR(){// spin to the right
        fL.setPower(-.5);
        fR.setPower(.5);
        bL.setPower(-.5);
        bR.setPower(.5);
    }
    public void SpinL(){//spin to the left
        fL.setPower(.5);
        fR.setPower(-.5);
        bL.setPower(.5);
        bR.setPower(-.5);
    }
    public void LinSlideUp(){//slide up
        Lslide.setPower(0.6);
        Lslide.setPower(-1);
        Lslide.setPower(0.1);
    }
    public void LinSlideDown(){//slides down
        Lslide.setPower(0.6);
        Lslide.setPower(-1);
        Lslide.setPower(0.1);
    }
    public void flippyKeep(){//keeps the flippy arm in place
        l1.setPosition(0.1);
        l2.setPosition(0.5);
        c1.setPosition(0.7);
    }
    public void microOpen(){//micro opens
        c2.setPosition(0.4);
    }
    public void microClose(){//micro closes
        c2.setPosition(0.0);
    }
    public void intakeSpinIn(){//intake for stacks
        iW.setPower(-1);
    }
    public void flippyUnFlip(){//the bucket un-flips back
        l1.setPosition(0.08);
        l2.setPosition(0.52);
        c1.setPosition(0.7);
    }
    public void flippyFlip(){//the bucket flips back
        l1.setPosition(0.6);
        l2.setPosition(0);
        c1.setPosition(0);
    }


}








