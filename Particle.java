import javax.swing.*;
import java.awt.*;

public class Particle implements Constants2{
    private double x;
    private double y;
    private double vr;
    private double vt;
    private int size;
    private double mass;
    private int identity;
    Particle(double x, double y, int i) {
        identity = i;
        mass = MASS;
        this.x = x;
        this.y = y;
        vr = 50;
        //vr = Math.exp(8*Math.random());
        vt = Math.random()*2*Math.PI;
        size = SIZE;}
    Particle(double x, double y, double vr, double vt, int i) {
        identity = i;
        mass = MASS;
        size = SIZE;
        this.x = x;
        this.y = y;
        this.vr= vr;
        this.vt = vt;}
    public void drawSelf(Graphics graphics) {
        switch (identity) {
            case 0:
                graphics.setColor(Color.RED);
                break;
            case 1:
                graphics.setColor(Color.BLUE);
                break;
            case 2:
                graphics.setColor(Color.GREEN);
                break;
            case 3:
                graphics.setColor(Color.ORANGE);}
        graphics.fillOval((int)(x),(int)(y),2*SIZE,2*SIZE);}
    public void polarize(double x1, double y1) {
        vr = Math.sqrt(x1*x1+y1*y1);
        vt = Math.atan2(y1,x1);}
    public void notch() {
        modX(NOTCH*this.vr*Math.cos(this.vt));
        modY(NOTCH*this.vr*Math.sin(this.vt));}
    public double getX() {return x;}
    public void setX(double x) {this.x = x; }
    public void modX(double x) {this.x+=x;}
    public double getY() {return y;}
    public void setY(double y) {this.y = y;}
    public void modY(double y) {this.y+=y;}
    public double getVr() {return vr;}
    public void setVr(double vr) {this.vr = vr;}
    public double getVt() {return vt;}
    public void setVt(double vt) {this.vt = vt;}
    public double getKE() {return mass*vr*vr;}
    public int getSize() {return size;}
    public void setSize(int size) {this.size = size;}
    public double getMass() {return mass; }
    public void setMass(double mass) {this.mass = mass;}
    public int getIdentity() {return  identity;}
    public void setIdentity(int i) {identity = i;}
    public void modKE(double deltaE) {vr = Math.sqrt((mass*vr*vr+deltaE)/mass);} //modifies kinetic energy for bond formation release
}
