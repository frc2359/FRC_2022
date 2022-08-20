package frc.robot;
import math;


// some other stuff Siven gotta import

public class SwerveDrive{
    
    // the distance between each wheel axle on the length and width.
    public final double L = LENGTH_YOU_WROTE;
    public final double W = WIDTH_YOU_WROTE; 

    public void drive (double x1, double y1, double x2) {
        double r = Math.sqrt ((L * L) + (W * W));
        y1 *= -1;

        double a = x1 - x2 * (L / r);
        double b = x1 + x2 * (L / r);
        double c = y1 - x2 * (W / r);
        double d = y1 + x2 * (W / r);


        // speeds are between 0 and 1, which may require necessary transformations depending on the speed range your motor requires.

        double backRightSpeed = Math.sqrt ((a * a) + (d * d));
        double backLeftSpeed = Math.sqrt ((a * a) + (c * c));
        double frontRightSpeed = Math.sqrt ((b * b) + (d * d));
        double frontLeftSpeed = Math.sqrt ((b * b) + (c * c));

        // These will be in a range of -1 to 1, if you wish to turn this range into real degrees then simply multiply by 180

        double backRightAngle = Math.atan2 (a, d) / Math.pi;
        double backLeftAngle = Math.atan2 (a, c) / Math.pi;
        double frontRightAngle = Math.atan2 (b, d) / Math.pi;
        double frontLeftAngle = Math.atan2 (b, c) / Math.pi;
    }

    private WheelDrive backRight;
    private WheelDrive backLeft;
    private WheelDrive frontRight;
    private WheelDrive frontLeft;

    public SwerveDrive (WheelDrive backRight, WheelDrive backLeft, WheelDrive frontRight, WheelDrive frontLeft) {
        this.backRight = backRight;
        this.backLeft = backLeft;
        this.frontRight = frontRight;
        this.frontLeft = frontLeft;
    }

    backRight.drive (backRightSpeed, backRightAngle);
    backLeft.drive (backLeftSpeed, backLeftAngle);
    frontRight.drive (frontRightSpeed, frontRightAngle);
    frontLeft.drive (frontLeftSpeed, frontLeftAngle);


}