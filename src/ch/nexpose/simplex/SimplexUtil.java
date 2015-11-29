package ch.nexpose.simplex;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by cansik on 29/11/15.
 */
public class SimplexUtil {

    public static double[] addHelperVar(double[] a, double defaultValue)
    {
        double[] n = new double[a.length + 1];

        for(int i = 0; i < a.length; i++)
        {
            n[i+1] = a[i];
        }

        n[0] = defaultValue;

        return n;
    }

    public static <T> T[] insertAt(T[] a, int pos, T value)
    {
        T[] n = Arrays.copyOf(a, a.length+1);

        int s = 0;
        for(int i = 0; i < a.length; i++)
        {
            if(i == pos)
            {
                n[pos] = value;
                s = 1;
            }
            else
            {
               n[i+s] = a[i];
            }
        }

        return n;
    }

    public static double[] insertAt(double[] a, int pos, double value)
    {
        double[] n = Arrays.copyOf(a, a.length+1);

        int s = 0;
        for(int i = 0; i < a.length; i++)
        {
            if(i == pos && s == 0)
            {
                n[pos] = value;
                s = 1;
                i--;
            }
            else
            {
                n[i+s] = a[i];
            }
        }

        return n;
    }

    public static String[] insertAt(String[] a, int pos, String value)
    {
        String[] n = Arrays.copyOf(a, a.length+1);

        int s = 0;
        for(int i = 0; i < a.length; i++)
        {
            if(i == pos && s == 0)
            {
                n[pos] = value;
                s = 1;
                i--;
            }
            else
            {
                n[i+s] = a[i];
            }
        }

        return n;
    }

    public static double[] removeAt(double[] a, int pos)
    {
        double[] n = Arrays.copyOf(a, a.length-1);

        int s = 0;
        for(int i = 0; i < n.length; i++)
        {
            if(i == pos)
                s++;

            n[i] = a[i+s];
        }

        return n;
    }

    public static String[] removeAt(String[] a, int pos)
    {
        String[] n = Arrays.copyOf(a, a.length-1);

        int s = 0;
        for(int i = 0; i < n.length; i++)
        {
            if(i == pos)
                s++;

            n[i] = a[i+s];
        }

        return n;
    }
}
