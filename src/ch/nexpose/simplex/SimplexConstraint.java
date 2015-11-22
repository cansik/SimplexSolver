package ch.nexpose.simplex;

import ch.nexpose.simplex.types.ConstraintType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cansik on 22/11/15.
 */
public class SimplexConstraint {
    private ConstraintType constraintType;
    private double[] coefficients;

    public SimplexConstraint(int coefficientAmount)
    {
        coefficients = new double[coefficientAmount+1];
    }

    public ConstraintType getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(ConstraintType constraintType) {
        this.constraintType = constraintType;
    }

    public double[] getCoefficients() {
        return coefficients;
    }


    public void convertInequation()
    {
        for(int i = 0; i < coefficients.length; i++)
        {
            coefficients[i] *= -1;
        }
    }

    public double[] getSlackVariables()
    {
        double[] slackVars = new double[coefficients.length];
        for(int i = 0; i < coefficients.length - 1; i++) {
            slackVars[i] = coefficients[i] * -1;
        }

        //copy non-var amount
        slackVars[slackVars.length-1] = coefficients[coefficients.length-1];

        return slackVars;
    }
}
