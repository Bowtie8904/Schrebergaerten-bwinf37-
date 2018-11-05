package core.gui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * @author &#8904
 *
 */
public class GardenCanvas extends JPanel
{
    private List<Garden> gardens;
    private Rectangle finalRect = null;

    public GardenCanvas()
    {
        this.gardens = new ArrayList<>();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        for (Garden garden : gardens)
        {
            garden.draw(g);
        }

        if (isFinished())
        {
            g.drawRect((int)finalRect.getX(), (int)finalRect.getY(), (int)finalRect.getWidth(),
                    (int)finalRect.getHeight());
        }
    }

    public boolean isFinished()
    {
        return this.finalRect != null;
    }

    public void setFinished(boolean finished)
    {
        if (finished)
        {
            drawFinalRect();
        }
        else
        {
            this.finalRect = null;
        }
    }

    public Rectangle getFinalRect()
    {
        return this.finalRect;
    }

    /**
     * @return the gardens
     */
    public List<Garden> getGardens()
    {
        return this.gardens;
    }

    /**
     * @param gardens
     *            the gardens to set
     */
    public void setGardens(List<Garden> gardens, boolean center)
    {
        this.gardens = gardens;

        if (center)
        {
            for (Garden garden : this.gardens)
            {
                garden.placeMiddle(this.getWidth() / 2, this.getHeight() / 2);
            }
        }
        else
        {
            drawFinalRect();
        }
    }

    public void drawFinalRect()
    {
        int x = Integer.MAX_VALUE;
        int y = Integer.MAX_VALUE;
        int outerX = Integer.MIN_VALUE;
        int outerY = Integer.MIN_VALUE;

        for (Garden garden : gardens)
        {
            if (garden.getX() < x)
            {
                x = (int)garden.getX();
            }

            if (garden.getY() < y)
            {
                y = (int)garden.getY();
            }

            if (garden.getX() + garden.getWidth() > outerX)
            {
                outerX = (int)(garden.getX() + garden.getWidth());
            }

            if (garden.getY() + garden.getHeight() > outerY)
            {
                outerY = (int)(garden.getY() + garden.getHeight());
            }
        }

        this.finalRect = new Rectangle(x, y, outerX - x, outerY - y);
    }
}