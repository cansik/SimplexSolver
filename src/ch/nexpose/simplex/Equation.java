package ch.nexpose.simplex;

/**
 * Created by cansik on 22/11/15.
 */
public class Equation {

    public static double[] plugIn(double[] equation, double[] values, int variable)
    {
        assert equation.length == values.length;

        double[] solution = new double[equation.length];

        //solve by var
        for(int i = 0; i < equation.length; i++)
        {
            solution[i] = equation[variable] * values[i];
        }

        //add equation values
        for(int i = 0; i < equation.length; i++)
        {
            if(i != variable)
            {
                solution[i] += equation[i];
            }
        }

        return solution;
    }

    /**
     * Shift equation
     * @param equation Equation without Solution
     * @param variable
     * @return
     */
    public static double[] shift(double[] equation, int variable)
    {
        double value = equation[variable] * -1;

        //set to default because: y1 = x1 + x2 ... + xn | -y1
        equation[variable] = -1;

        //norm values
        for(int i = 0; i < equation.length; i++)
        {
            equation[i] /= value;
        }

        return equation;
    }
}
