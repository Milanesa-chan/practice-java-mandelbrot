package milanesa.mandelbrot;

import javafx.scene.input.KeyCode;
import jdk.internal.util.xml.impl.Input;

import java.awt.event.*;

public class Core implements MouseWheelListener, MouseListener, MouseMotionListener, KeyListener {
    public static final int P_WIDTH = 800, P_HEIGHT = 600;
    public double OFFSET_X = P_WIDTH / 2, OFFSET_Y = P_HEIGHT / 2;
    public double CAM_OFFSET_X, CAM_OFFSET_Y;
    public static int MAX_ITER = 32;
    public double P_SCALE = 0.005, P_ZOOM_SCALE = 0.1;
    public static final double RES_SCALE = 0.5;

    private static class InputStatus {
        public volatile boolean
                ZOOM_IN = false,
                ZOOM_OUT = false,
                CAM_UP = false,
                CAM_DOWN = false,
                CAM_RIGHT = false,
                CAM_LEFT = false,
                PRECISE = false;

        public synchronized void setState(int keyCode, boolean state) {
            switch (keyCode) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_E:
                    ZOOM_IN = state;
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_Q:
                    ZOOM_OUT = state;
                    break;
                case KeyEvent.VK_W:
                    CAM_UP = state;
                    break;
                case KeyEvent.VK_S:
                    CAM_DOWN = state;
                    break;
                case KeyEvent.VK_A:
                    CAM_LEFT = state;
                    break;
                case KeyEvent.VK_D:
                    CAM_RIGHT = state;
                    break;
                case KeyEvent.VK_SHIFT:
                    PRECISE = state;
                    break;
            }
        }
    }

    private final InputStatus inputStatus = new InputStatus();

    private Frame frameInstance;

    public Core() {
        frameInstance = new Frame(this);
        new Thread(() -> {
            double deltaTimeMillis = 0.0;
            long initTime, finalTime;
            while (true) {
                try {
                    Thread.sleep(10);
                    updateLogic(10);
                } catch (InterruptedException ignored) {
                }
            }
        }).start();
    }

    private void updateLogic(double deltaTimeMillis) {
        checkKeys(deltaTimeMillis);
    }

    private void checkKeys(double deltaTimeMillis) {
        if (inputStatus.ZOOM_IN) {
            screenZoom(-(P_ZOOM_SCALE * deltaTimeMillis / 100));
        }
        if (inputStatus.ZOOM_OUT) {
            screenZoom(P_ZOOM_SCALE * deltaTimeMillis / 100);
        }

        double moveQuantity = inputStatus.PRECISE ? 1 : 10;
        if (inputStatus.CAM_UP) {
            CAM_OFFSET_Y += moveQuantity;
        }
        if (inputStatus.CAM_DOWN) {
            CAM_OFFSET_Y -= moveQuantity;
        }
        if (inputStatus.CAM_RIGHT) {
            CAM_OFFSET_X -= moveQuantity;
        }
        if (inputStatus.CAM_LEFT) {
            CAM_OFFSET_X += moveQuantity;
        }
    }

    private void screenZoom(double multiplier) {
        double oldPScale = P_SCALE;
        P_SCALE += P_SCALE * multiplier;
        double newPScale = P_SCALE;
        CAM_OFFSET_X *= oldPScale / newPScale;
        CAM_OFFSET_Y *= oldPScale / newPScale;
    }

    public static void main(String[] args) {
        new Core();
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        screenZoom(e.getWheelRotation() * P_ZOOM_SCALE);
    }

    private boolean mouseDown = false;
    private int mouseX0, mouseY0;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            mouseDown = true;
            mouseX0 = e.getX();
            mouseY0 = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == 0) {
            mouseDown = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseDown) {
            CAM_OFFSET_X += e.getX() - mouseX0;
            CAM_OFFSET_Y += e.getY() - mouseY0;
            mouseX0 = e.getX();
            mouseY0 = e.getY();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        inputStatus.setState(e.getKeyCode(), true);

        if (e.getKeyCode() == KeyEvent.VK_R) {
            MAX_ITER *= 2;
        }
        if(e.getKeyCode() == KeyEvent.VK_F){
            if(MAX_ITER>1){
                MAX_ITER /= 2;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        inputStatus.setState(e.getKeyCode(), false);
    }
}
