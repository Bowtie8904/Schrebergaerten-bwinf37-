package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public final class Properties
{
    private static final String DEFAULT_PROPERTY_PATH = "properties.txt";
    static
    {
        try
        {
            File file = new File(DEFAULT_PROPERTY_PATH);
            if (!file.exists())
            {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static List<String> getAllFieldsLike(String field, File file)
    {
        if (!file.exists())
        {
            file = new File(DEFAULT_PROPERTY_PATH);
        }

        List<String> values = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith(field))
                {
                    String[] parts = line.split(":");

                    values.add(parts[0].trim());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return values;
    }

    public static List<String> getAllFieldsLike(String field, String path)
    {
        return getAllFieldsLike(field, new File(path));
    }

    public static List<String> getAllFieldsLike(String field)
    {
        return getAllFieldsLike(field, DEFAULT_PROPERTY_PATH);
    }

    public static List<String> getAllLike(String field, File file)
    {
        if (!file.exists())
        {
            file = new File(DEFAULT_PROPERTY_PATH);
        }

        List<String> values = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith(field))
                {
                    String[] parts = line.split(":");

                    if (parts.length > 1)
                    {
                        line = parts[1].trim();
                    }
                    else
                    {
                        line = "";
                    }

                    values.add(line);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return values;
    }

    public static List<String> getAllLike(String field, String path)
    {
        return getAllLike(field, new File(path));
    }

    public static List<String> getAllLike(String field)
    {
        return getAllLike(field, DEFAULT_PROPERTY_PATH);
    }

    public static String getValueOf(String field, File file)
    {
        if (!file.exists())
        {
            file = new File(DEFAULT_PROPERTY_PATH);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith(field + ":"))
                {
                    return line.replace(field + ":", "").trim();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String getValueOf(String field, String path)
    {
        return getValueOf(field, new File(path));
    }

    public static String getValueOf(String field)
    {
        return getValueOf(field, new File(DEFAULT_PROPERTY_PATH));
    }

    public static boolean setValueOf(String field, String value, File file)
    {
        try
        {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")))
        {
            String line;
            boolean exists = false;
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith(field + ":"))
                {
                    sb.append(field + ": " + value + System.lineSeparator());
                    exists = true;
                }
                else
                {
                    sb.append(line + System.lineSeparator());
                }
            }
            if (!exists)
            {
                sb.append(field + ": " + value + System.lineSeparator());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file))))
        {
            writer.print(sb.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean setValueOf(String field, String value, String path)
    {
        return setValueOf(field, value, new File(path));
    }

    public static boolean setValueOf(String field, String value)
    {
        return setValueOf(field, value, new File(DEFAULT_PROPERTY_PATH));
    }
}