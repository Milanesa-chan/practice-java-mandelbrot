package milanesa.mandelbrot;

import java.awt.event.MouseEvent;

public class Complex {
    public double real, imag;

    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public Complex add(Complex other){
        double nReal = other.real + this.real;
        double nImag = other.imag + this.imag;
        return new Complex(nReal, nImag);
    }

    public static Complex square(Complex comp){
        double nReal = Math.pow(comp.real, 2) - Math.pow(comp.imag, 2);
        double nImag = comp.real*comp.imag*2;
        return new Complex(nReal, nImag);
    }

    @Override
    public String toString() {
        return "Complex{" +
                "real=" + real +
                ", imag=" + imag +
                '}';
    }
}
