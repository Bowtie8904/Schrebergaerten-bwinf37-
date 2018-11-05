package core;

import java.util.List;
import java.util.SplittableRandom;

import core.gui.Garden;
import core.gui.GardenCanvas;

/**
 * @author &#8904
 *
 */
public class Mover
{
    private List<Garden> gardens;
    private GardenCanvas canvas;
    private SplittableRandom r;

    private static final int UP = 1;
    private static final int DOWN = 2;
    private static final int RIGHT = 3;
    private static final int LEFT = 4;

    public Mover(GardenCanvas canvas)
    {
        this.canvas = canvas;
        this.gardens = canvas.getGardens();
        this.r = new SplittableRandom();
    }

    public void start()
    {
        for (Garden garden : this.gardens)
        {
            garden.setActive(true);
            while (!garden.isFree(this.gardens))
            {
                if (!Main.FAST_AF)
                {
                    try
                    {
                        Thread.sleep(1);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

                move(garden);
            }
        }

        canvas.drawFinalRect();
    }

    private void move(Garden garden)
    {
        int direction = r.nextInt(4) + 1;

        switch (direction)
        {
        case UP:
            garden.down(-1);
            break;
        case DOWN:
            garden.down(1);
            break;
        case RIGHT:
            garden.right(1);
            break;
        case LEFT:
            garden.right(-1);
            break;
        }

        canvas.repaint();
    }
}