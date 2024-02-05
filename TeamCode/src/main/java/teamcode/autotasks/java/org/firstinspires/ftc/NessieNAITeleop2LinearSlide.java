package teamcode.autotasks.java.org.firstinspires.ftc;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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


@TeleOp(name="NESSIE NAI TELEOP", group="Linear Opmode")
public class NessieNAITeleop2LinearSlide extends LinearOpMode {
    //impporting thing code
    private ElapsedTime runtime = new ElapsedTime();
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

    class setTimesAPressed extends TimerTask {
        private int a;
        public setTimesAPressed(int a) {
            this.a = a;
        }

        public void run() {
            timesAPressed = a;
        }
    }

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

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        boolean lastAPressed = false;
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // telemetry.addData("Status", "Running");
            // telemetry.update();
            //POV mode thing idk what this means lol
            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;
            double strafe = gamepad1.left_stick_x;
            boolean strafeL = gamepad1.x;
            boolean strafeR = gamepad1.y;
            double LY2 = gamepad2.left_stick_y;
            boolean RB = gamepad2.right_bumper;
            boolean LB = gamepad2.left_bumper;

            //moew
            double frontleftPower = 1 * Range.clip(drive + turn + strafe, -1.0, 1.0);
            double frontrightPower = 1 * Range.clip(drive - turn - strafe, -1.0, 1.0);
            double backleftPower = 1 * Range.clip(drive + turn - strafe, -1.0, 1.0);
            double backrightPower = 1 * Range.clip(drive - turn + strafe, -1.0, 1.0);
            fL.setPower(frontleftPower);
            bL.setPower(backleftPower);
            fR.setPower(frontrightPower);
            bR.setPower(backrightPower);
            //START OF ACTUAL CODE YAYYYY!!!!! meow :3

            //arm CODE AAAA
            if (gamepad2.dpad_up){
                Rslide.setPower(0.6);
            } else if (gamepad2.dpad_down){
                Rslide.setPower(-1);
            } else {
                Rslide.setPower(0.1);
            }
            //OTHER ARM CODE
            if (gamepad2.dpad_up){
                Lslide.setPower(0.6);
            } else if (gamepad2.dpad_down){
                Lslide.setPower(-1);
            } else {
                Lslide.setPower(0.1);
            }
            //flippy arm code

                /*if(gamepad2.x){
                l1.setPosition(1);
                l2.setPosition(0);

            }
            if(gamepad2.b){
                l1.setPosition(0);
               l2.setPosition(1);
            }
            */
            if(gamepad2.right_stick_y < 0 ){
                l1.setPosition(0.08);
                l2.setPosition(0.52);
                c1.setPosition(0.7);
                telemetry.addData("Status", "y < 0");

            }else if(gamepad2.right_stick_y > 0 ){
                l1.setPosition(0.6);
                l2.setPosition(0);
                c1.setPosition(0);
                telemetry.addData("Status", "y > 0");
            }else{
                l1.setPosition(0.1);
                l2.setPosition(0.5);
                c1.setPosition(0.7);
                // telemetry.addData("Status", "y == 0");
            }

            //CLAW CODE
            /*if (gamepad2.left_bumper){
                c2.setPosition(0.25);
            }
             if (gamepad2.right_bumper){
                c2.setPosition(0.1);{
                }
                */
            if(gamepad2.right_bumper){
                c2.setPosition(0.2);
                telemetry.addData("Status", "y < 0");

            }else if(gamepad2.left_bumper){
                c2.setPosition(0.4);
                //   telemetry.addData("Status", "y > 0");
            }else{
                c2.setPosition(0);

                // telemetry.addData("Status", "x is pressed");
            }
            if(gamepad1.right_bumper){
                //c1.setPosition(0);

            }
            //flippy arm code
            /*if(gamepad2.y){
                c1.setPosition(0.3);


            }
            if(gamepad2.a){
                c1.setPosition(0.75);

            }
            //intake system (counter rollers)

            */

           /* if (gamepad2.right_trigger){
                cR.setPower(-1);
            } else if (gamepad2.left_trigger){
                cR.setPower(1);
            } else {
                cR.setPower(0);

            }
            if (gamepad2.right_trigger){
                iW.setPower(-0.6);
            } else if (gamepad2.left_trigger){
                iW.setPower(0.6);
            } else {
                iW.setPower(0);

            }
            */
            //intake flipping thing RAHH!! CODEEE
            if(gamepad1.x){
                i1.setPosition(0.7);
                i2.setPosition(0);

            }
            if(gamepad1.b){
                i1.setPosition(0.065);
                i2.setPosition(0.635);
            }
            //testing intake
            boolean APressed = gamepad1.a;
            if(APressed && !lastAPressed) {
                timer.purge();
                timesAPressed++;
                timer.schedule(new setTimesAPressed(0), 300);
            }

            telemetry.addData("TimesAPressed", timesAPressed);
            telemetry.update();

            if(timesAPressed >= 2){
                i1.setPosition(0.08);
                i2.setPosition(0.62);
            }
            if(timesAPressed == 1){
                i1.setPosition(0.11);
                i2.setPosition(0.59);
            }

            //test
            //intake
            if(gamepad1.right_trigger > 0){
                iW.setPower(-1);
            }else if(gamepad1.left_trigger > 0){
                iW.setPower(0.6);
            }else{
                iW.setPower(0);
                //counter rollers
            }
            if (gamepad1.right_trigger > 0){
                cR.setPower(-1);
            } else if (gamepad1.left_trigger> 0 ){
                cR.setPower(1);
            } else {
                cR.setPower(0);
            }
            lastAPressed = APressed;
            //drone launcher
            if (gamepad1.right_stick_button){
                drone.setPosition(0);
            }else {
                drone.setPosition(0.4);

            }

            if (gamepad2.right_stick_button){
                PurplePixelDropper.setPosition(0);
            }else {
                PurplePixelDropper.setPosition(0.4);
            }
        }
    }
}

