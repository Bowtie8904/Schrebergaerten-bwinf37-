package core.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

/**
 * @author &#8904
 *
 */
public class Garden extends Rectangle
{
    private static int currentID = 0;
    private final int id;
    private boolean active = false;

    public Garden(int width, int height)
    {
        super(0, 0, width, height);
        this.id = currentID ++ ;
    }

    public void right(int move)
    {
        this.x += move;
    }

    public void down(int move)
    {
        this.y += move;
    }

    protected int getID()
    {
        return this.id;
    }

    public boolean isActive()
    {
        return this.active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public boolean isFree(List<Garden> gardens)
    {
        boolean free = true;

        for (Garden garden : gardens)
        {
            if (garden.getID() != this.id && garden.isActive() && intersects(garden))
            {
                free = false;
                break;
            }
        }

        return free;
    }

    public int getArea()
    {
        return this.width * this.height;
    }

    public Garden copy()
    {
        Garden garden = new Garden(this.width, this.height);
        garden.right(this.x);
        garden.down(this.y);
        return garden;
    }

    public void placeMiddle(int canvasMiddleX, int canvasMiddleY)
    {
        this.x = canvasMiddleX - (this.width / 2);
        this.y = canvasMiddleY - (this.height / 2);
    }

    public void draw(Graphics g)
    {
        if (active)
        {
            g.setColor(Color.GREEN);
            g.fillRect(this.x, this.y, this.width, this.height);

            g.setColor(Color.BLACK);
            g.drawRect(this.x, this.y, this.width, this.height);
        }
    }
}