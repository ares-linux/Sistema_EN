package animaciones;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;

public class LoadingSpinner extends JComponent {
    public enum State {
        LOADING, SUCCESS, ERROR
    }

    private State currentState = State.LOADING;
    private Timer timer;
    private double angle = 0;
    private double progress = 0; 
    
    private Color loadingColor = new Color(50, 150, 250); // Azul
    private Color successColor = new Color(40, 200, 80);  // Verde
    private Color errorColor = new Color(220, 50, 50);    // Rojo
    private int strokeWidth = 5;

    public LoadingSpinner() {
        setPreferredSize(new Dimension(100, 100));
        timer = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAnimation();
                repaint();
            }
        });
        timer.start();
    }

    public void startLoading() {
        currentState = State.LOADING;
        progress = 0;
        angle = 0;
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    public void showSuccess() {
        currentState = State.SUCCESS;
        progress = 0;
    }

    public void showError() {
        currentState = State.ERROR;
        progress = 0;
    }

    // Métodos para cambiar los colores si lo necesitas
    public void setLoadingColor(Color color) { this.loadingColor = color; }
    public void setSuccessColor(Color color) { this.successColor = color; }
    public void setErrorColor(Color color) { this.errorColor = color; }
    public void setStrokeWidth(int width) { this.strokeWidth = width; }

    private void updateAnimation() {
        if (currentState == State.LOADING) {
            angle += 5;
            if (angle >= 360) {
                angle -= 360;
            }
        } else {
            if (progress < 1.0) {
                progress += 0.05;
                if (progress >= 1.0) {
                    progress = 1.0;
                    // Opcional: Detener el timer si ya no hay más animación
                    // timer.stop();
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height) - strokeWidth * 2;
        int x = (width - size) / 2;
        int y = (height - size) / 2;

        g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        if (currentState == State.LOADING) {
            g2.setColor(loadingColor);
            // Dibujar un arco giratorio (spinner)
            g2.draw(new Arc2D.Double(x, y, size, size, -angle, 270, Arc2D.OPEN));
        } else if (currentState == State.SUCCESS) {
            g2.setColor(successColor);
            // Círculo completo
            g2.draw(new Arc2D.Double(x, y, size, size, 0, 360, Arc2D.OPEN));
            
            // Dibujar la palomita (checkmark) animada
            Path2D.Double path = new Path2D.Double();
            double startX = x + size * 0.25;
            double startY = y + size * 0.5;
            double midX = x + size * 0.45;
            double midY = y + size * 0.7;
            double endX = x + size * 0.75;
            double endY = y + size * 0.3;

            path.moveTo(startX, startY);
            if (progress < 0.5) {
                double p = progress * 2;
                path.lineTo(startX + (midX - startX) * p, startY + (midY - startY) * p);
            } else {
                path.lineTo(midX, midY);
                double p = (progress - 0.5) * 2;
                path.lineTo(midX + (endX - midX) * p, midY + (endY - midY) * p);
            }
            g2.draw(path);

        } else if (currentState == State.ERROR) {
            g2.setColor(errorColor);
            // Círculo completo
            g2.draw(new Arc2D.Double(x, y, size, size, 0, 360, Arc2D.OPEN));

            // Dibujar la cruz (X) animada
            double p1 = Math.min(progress * 2, 1.0);
            double p2 = Math.max((progress - 0.5) * 2, 0.0);

            Path2D.Double path = new Path2D.Double();
            double startX1 = x + size * 0.3;
            double startY1 = y + size * 0.3;
            double endX1 = x + size * 0.7;
            double endY1 = y + size * 0.7;

            path.moveTo(startX1, startY1);
            path.lineTo(startX1 + (endX1 - startX1) * p1, startY1 + (endY1 - startY1) * p1);

            if (p2 > 0) {
                double startX2 = x + size * 0.7;
                double startY2 = y + size * 0.3;
                double endX2 = x + size * 0.3;
                double endY2 = y + size * 0.7;

                path.moveTo(startX2, startY2);
                path.lineTo(startX2 + (endX2 - startX2) * p2, startY2 + (endY2 - startY2) * p2);
            }
            g2.draw(path);
        }

        g2.dispose();
    }
}
