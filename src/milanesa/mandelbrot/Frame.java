package milanesa.mandelbrot;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private JPanel panel;
    private Core coreInstance;
    private int accumFrames;
    private int fps;

    public Frame(Core core){
        coreInstance = core;
        SwingUtilities.invokeLater(() -> {
            this.setTitle("Mandelbrot");

            panel = new JPanel(){
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    updateGraphics(g);
                }
            };

            panel.setPreferredSize(new Dimension(Core.P_WIDTH, Core.P_HEIGHT));
            panel.addMouseMotionListener(coreInstance);
            panel.addMouseListener(coreInstance);
            this.addKeyListener(coreInstance);
            this.add(panel);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.pack();
            this.setVisible(true);

            new Thread(() -> {
                while(true){
                    panel.repaint();
                }
            }).start();

            new Thread(() -> {
                while(true){
                    try {
                        Thread.sleep(1000);
                        fps = accumFrames;
                        accumFrames = 0;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            this.addMouseWheelListener(coreInstance);
        });
    }

    public void updateGraphics(Graphics g){
        updateMandelbrot(g);

        accumFrames++;
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(""+fps, Core.P_WIDTH-g.getFontMetrics().stringWidth("0000"), g.getFontMetrics().getHeight()+5);
    }

    private void updateMandelbrot(Graphics g){
        double scale = coreInstance.P_SCALE;
        double offsetX = coreInstance.OFFSET_X + coreInstance.CAM_OFFSET_X;
        double offsetY = coreInstance.OFFSET_Y + coreInstance.CAM_OFFSET_Y;
        int pixelStep = (int) Math.round(1/Core.RES_SCALE);
        for (int i = 0; i < Core.P_HEIGHT; i+=pixelStep) {
            for (int j = 0; j < Core.P_WIDTH; j+=pixelStep) {
                int iter = computeIterations(new Complex((j-offsetX)*scale, (i-offsetY)*scale));
                /*
                int r = (iter/Core.MAX_ITER)*255;
                int gr = (iter/Core.MAX_ITER)*255;
                int b = (iter/Core.MAX_ITER)*255;
                Color col = new Color(((r&0xFF)<<16) + ((gr&0xFF)<<8) + ((b&0xFF)));
                 */

                Color col = new Color((int) (0xFFFFFF*Math.pow((double)iter/Core.MAX_ITER, 4)));
                g.setColor(col);
                g.fillRect(j, i, pixelStep, pixelStep);
            }
        }
    }

    private int computeIterations(Complex c){
        int iter = 0;
        Complex z = new Complex(0, 0);
        while(iter < Core.MAX_ITER){
            z = Complex.square(z).add(c);
            iter++;
            if(Math.pow(z.real, 2) + Math.pow(z.imag, 2) >= 256) break;
        }
        return iter;
    }
}
