package core.gui;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

import core.Main;

/**
 * @author &#8904
 *
 */
public class Window extends JFrame
{
    private GardenCanvas canvas;
    private int titleNumber = 0;
    private JProgressBar progress;

    public Window()
    {
        this.canvas = new GardenCanvas();

        setSize(400, 500);
        setTitle("Schrebergärten");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        add(this.canvas, BorderLayout.CENTER);

        this.progress = new JProgressBar(0, 100);
        this.progress.setStringPainted(true);

        add(this.progress, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void updateProgressbar()
    {
        titleNumber ++ ;

        double percent = (double)titleNumber / Main.RUNS * 100.0;
        progress.setValue((int)percent);
        progress.setString(String.format("%.2f", percent) + "%");
    }

    public void updateTitle(String title)
    {
        setTitle(title);
    }

    public GardenCanvas getCanvas()
    {
        return this.canvas;
    }

    public void setCanvas(GardenCanvas canvas)
    {
        this.canvas.setGardens(canvas.getGardens(), false);

        Rectangle finalRect = this.canvas.getFinalRect();

        int x = (int)((this.canvas.getWidth() / 2) - (finalRect.getWidth() / 2));
        int y = (int)((this.canvas.getHeight() / 2) - (finalRect.getHeight() / 2));

        int diffX = (int)(x - finalRect.getX());
        int diffY = (int)(y - finalRect.getY());

        for (Garden garden : this.canvas.getGardens())
        {
            garden.right(diffX);
            garden.down(diffY);
        }

        progress.setValue(100);
        progress.setString(100 + "%");

        this.canvas.drawFinalRect();

        this.canvas.repaint();
    }
}