package ch.nexpose.simplex;

import ch.nexpose.simplex.types.ConstraintType;

import java.util.DoubleSummaryStatistics;

/**
 * Created by cansik on 22/11/15.
 */
public class SimplexSolver {

    private double[][] schema;
    private int aimIndex;
    private int cIndex;

    public void solve(SimplexProblem problem)
    {
        //switch ineqations to pattern: <=
        for(SimplexConstraint c : problem.getConstraints())
        {
            //todo: cover equals!
            if(c.getConstraintType() != ConstraintType.LessThanEquals)
                c.convertInequation();
        }

        //create schema
        schema = new double[problem.getConstraints().length+1][problem.getCoefficients().length];

        aimIndex = schema.length-1;
        cIndex = schema[aimIndex].length-1;

        //fill initial schema
        for(int y = 0; y < aimIndex; y++)
            schema[y] = problem.getConstraints()[y].getSlackVariables();

        schema[aimIndex] = problem.getSlackVariables();

        printSchema("Initial Schema");

        //run algorithm
        int stepCount = 0;
        while (nextStep(stepCount++)){}

        //solve head functions

        //show result
    }

    private boolean nextStep(int stepCount)
    {
        //get position of not null aim function component
        MatrixPos aimFunctionPos = getPositiveAimFunctionComponent();

        //finished if aimFunctionPos is Null
        if(aimFunctionPos == null)
            return false;

        System.out.println("Aim Column (" + aimFunctionPos.x + "): " + schema[aimIndex][aimFunctionPos.x]);

        //get position of smallest quotient (ci / aiq)
        MatrixPos pivotIndex = getPositionOfSmallestQuotient(aimFunctionPos.x);

        System.out.println("Pivot (" + pivotIndex.y + "|" + pivotIndex.x + "): " + schema[pivotIndex.y][pivotIndex.x]);

        //shift with pivot
        schema[pivotIndex.y] = Equation.shift(schema[pivotIndex.y], pivotIndex.x);

        //solve every function except the one with the pivot element
        double[] values = schema[pivotIndex.y];
        for(int y = 0; y < schema.length; y++)
        {
            if(y != pivotIndex.y) {
                double[] eq = schema[y];
                schema[y] = Equation.plugIn(eq, values, pivotIndex.x);
            }
        }

        printSchema("Step " + stepCount);
        return true;
    }

    private MatrixPos getPositionOfSmallestQuotient(int x)
    {
        double min = Double.MAX_VALUE;
        int bestY = -1;

        for(int y = 0; y < aimIndex; y++)
        {
            double q = Math.abs(schema[y][cIndex] / schema[y][x]);
            if(q < min)
            {
                min = q;
                bestY = y;
            }
        }

        assert bestY != -1;

        //return pivot element
        return new MatrixPos(bestY, x);
    }

    private MatrixPos getPositiveAimFunctionComponent()
    {
        for(int x = 0; x < cIndex; x++)
        {
            if(schema[aimIndex][x] > 0)
                return new MatrixPos(aimIndex, x);
        }

        //nothing found
        //todo: do something here! are we finished?
        System.out.println("no new element found to switch!");
        return null;
    }

    private void printSchema()
    {
        printSchema("Schema");
    }

    private void printSchema(String message)
    {
        System.out.println(message + ":");
        for(int y = 0; y < schema.length; y++)
        {
            if(y == schema.length-1)
                System.out.print("z  | ");
            else
                System.out.print("y" + y + " | ");

            for(int x = 0; x < schema[y].length; x++)
            {
                System.out.format("%12.2f", schema[y][x]);
                //System.out.print(schema[x][y] + " ");
            }
            System.out.println();
        }

        System.out.println();
    }

}
