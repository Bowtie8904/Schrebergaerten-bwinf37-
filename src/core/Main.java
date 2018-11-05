package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import core.gui.Garden;
import core.gui.GardenCanvas;
import core.gui.Window;
import util.Properties;

/**
 * @author &#8904
 *
 */
public class Main
{
    public static boolean FAST_AF = true;
    public static boolean SINGLE = false;

    public static int RUNS;

    public static void main(String[] args)
    {
        try
        {
            Main.RUNS = Integer.parseInt(Properties.getValueOf("runs"));

            if (Main.RUNS < 2)
            {
                SINGLE = true;
                FAST_AF = false;
            }

            List<Garden> baseGardens = new ArrayList<>();
            String fileText = "";
            File file = new File("data.txt");

            try (BufferedReader br = new BufferedReader(new FileReader(file)))
            {
                String st;

                while ((st = br.readLine()) != null)
                {
                    fileText += st;
                }
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }

            String[] parts = fileText.split(",");

            int factor = Integer.parseInt(Properties.getValueOf("sizeFactor"));

            for (String bounds : parts)
            {
                String[] splitBounds = bounds.split("x");

                int width = Integer.parseInt(splitBounds[0].trim()) * factor;
                int height = Integer.parseInt(splitBounds[1].trim()) * factor;

                baseGardens.add(new Garden(width, height));
            }

            Window window = new Window();

            Collections.sort(baseGardens, Comparator.comparing(Garden::getArea).reversed());

            if (SINGLE)
            {
                GardenCanvas canvas = new GardenCanvas();
                canvas.setGardens(baseGardens, true);
                window.setCanvas(canvas);
                window.getCanvas().setFinished(false);
                Mover mover = new Mover(window.getCanvas());
                mover.start();
            }
            else
            {
                Executor executor = Executors.newFixedThreadPool(Integer.parseInt(Properties.getValueOf("threads")));

                List<GardenCanvas> canvases = new ArrayList<>();

                for (int i = 0; i < Main.RUNS; i ++ )
                {
                    GardenCanvas canvas = new GardenCanvas();
                    canvases.add(canvas);

                    List<Garden> gardens = new ArrayList<>();

                    for (Garden garden : baseGardens)
                    {
                        gardens.add(garden.copy());
                    }

                    executor.execute(() ->
                    {
                        canvas.setGardens(gardens, true);
                        Mover mover = new Mover(canvas);
                        mover.start();
                        window.updateProgressbar();
                    });
                }

                boolean finished = false;

                while (!finished)
                {
                    finished = true;

                    for (GardenCanvas canvas : canvases)
                    {
                        if (!canvas.isFinished())
                        {
                            finished = false;
                            break;
                        }
                    }

                    if (finished)
                    {
                        GardenCanvas smallestCanvas = null;
                        int smallestArea = Integer.MAX_VALUE;

                        for (GardenCanvas canvas : canvases)
                        {
                            if (smallestCanvas == null)
                            {
                                smallestCanvas = canvas;
                                smallestArea = (int)(canvas.getFinalRect().getWidth()
                                        * canvas.getFinalRect().getHeight());
                            }
                            else
                            {
                                int newArea = (int)(canvas.getFinalRect().getWidth()
                                        * canvas.getFinalRect().getHeight());

                                if (newArea < smallestArea)
                                {
                                    smallestArea = newArea;
                                    smallestCanvas = canvas;
                                }
                            }
                        }

                        window.setCanvas(smallestCanvas);

                        int width = (int)smallestCanvas.getFinalRect().getWidth();
                        int height = (int)smallestCanvas.getFinalRect().getHeight();

                        window.updateTitle(width + " * " + height + " = " + (width * height));
                    }

                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        }
        catch (Exception ex)
        {
            System.exit(-1);
        }
    }
}