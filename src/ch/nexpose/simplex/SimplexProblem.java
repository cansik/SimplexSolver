package ch.nexpose.simplex;

import ch.nexpose.simplex.types.ConstraintType;
import ch.nexpose.simplex.types.OptimisationType;
import com.sun.xml.internal.ws.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cansik on 22/11/15.
 */
public class SimplexProblem {
    private OptimisationType optimisationType;
    private SimplexCoefficient[] coefficients;
    private SimplexConstraint[] constraints;

    public SimplexProblem()
    {
    }

    public void parse(String data)
    {
        int j = 0;
        String[] values = Arrays.stream(data.replace("\n", ";").split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        //read amount
        int amountVariables = Integer.parseInt(values[j++]);
        int amountConstraints = Integer.parseInt(values[j++]);

        //init arrays
        coefficients = new SimplexCoefficient[amountVariables+1];
        constraints = new SimplexConstraint[amountConstraints];

        //read type
        optimisationType = OptimisationType.valueOf(StringUtils.capitalize(values[j++]));

        //read coefficients + d value
        for(int i = 0; i < amountVariables + 1; i++)
        {
            coefficients[i] = new SimplexCoefficient(Double.parseDouble(values[j++]));
        }

        //read notnegative type of coefficients
        for(int i = 0; i < amountVariables; i++)
        {
            coefficients[i].setNotNegative(Boolean.parseBoolean(values[j++]));
        }

        //read constraints
        for(int i = 0; i < amountConstraints; i++)
        {
            SimplexConstraint c = new SimplexConstraint(amountVariables);

            switch (values[j++])
            {
                case "<=":
                    c.setConstraintType(ConstraintType.LessThanEquals);
                    break;

                case "=":
                    c.setConstraintType(ConstraintType.Equals);
                    break;

                case ">=":
                    c.setConstraintType(ConstraintType.GreaterThanEquals);
                    break;
            }

            //read coefficients b & c
            for(int k = 0; k < amountVariables + 1; k++)
            {
                c.getCoefficients()[k] = (Double.parseDouble(values[j++]));
            }

            constraints[i] = c;
        }
    }

    public OptimisationType getOptimisationType() {
        return optimisationType;
    }

    public void setOptimisationType(OptimisationType optimisationType) {
        this.optimisationType = optimisationType;
    }

    public SimplexCoefficient[] getCoefficients() {
        return coefficients;
    }

    public SimplexConstraint[] getConstraints() {
        return constraints;
    }

    public double[] getSlackVariables()
    {
        double[] slackVars = new double[coefficients.length];

        for(int i = 0; i < coefficients.length; i++)
            slackVars[i] = coefficients[i].getValue();

        return slackVars;
    }

    public void convertInequation()
    {
        for(int i = 0; i < coefficients.length - 1; i++)
        {
            coefficients[i].setValue(coefficients[i].getValue()*-1);
        }
    }
}
