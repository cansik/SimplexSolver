package ch.nexpose.simplex;

import ch.nexpose.simplex.types.ConstraintType;

/**
 * Created by cansik on 22/11/15.
 */
public class SimplexSolver {

    private double[][] schema;
    private int aimIndex;
    private int cIndex;

    //equation identifier
    private String[] head;
    private String[] side;

    public void solve(SimplexProblem problem)
    {
        //switch ineqations to pattern: <=
        for(SimplexConstraint c : problem.getConstraints())
        {
            //todo: cover equals!
            if(c.getConstraintType() != ConstraintType.LessThanEquals)
                c.convertInequation();
        }

        //create head & side variables
        head = new String[problem.getCoefficients().length - 1];

        for (int i = 0; i < head.length; i++)
            head[i] = "x" + (i + 1);

        side = new String[problem.getConstraints().length];

        for (int i = 0; i < side.length; i++)
            side[i] = "y" + (i + 1);

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

        System.out.println();

        //solve head functions
        for (int i = 0; i < head.length; i++) {
            String xName = "x" + (i + 1);
            int index = getIndexOf(xName);
            System.out.println(xName + "\t= " + schema[index][cIndex]);
        }

        //show result
        System.out.println("Result: " + schema[aimIndex][cIndex]);
        System.out.println();
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

        //swap variable name
        swapVariableName(pivotIndex.y, pivotIndex.x);

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
            double bq = schema[y][x];

            //bq has to be bigger than or equal 0
            if (bq >= 0)
                continue;

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

    private void swapVariableName(int s, int h) {
        String tmp = side[s];
        side[s] = head[h];
        head[h] = tmp;
    }

    private int getIndexOf(String varName) {
        for (int i = 0; i < aimIndex; i++)
            if (side[i].equals(varName))
                return i;

        return 0;
    }

    private void printSchema(String message)
    {
        System.out.println(message + ":");

        //print header
        System.out.print("\t");
        for (int i = 0; i < head.length; i++)
            System.out.format("%12s", head[i]);
        System.out.println();

        //print line
        System.out.print("\t");
        for (int i = 0; i < head.length; i++)
            System.out.format("%12s", "--");
        System.out.println();

        //print data
        for(int y = 0; y < schema.length; y++)
        {
            if(y == schema.length-1)
                System.out.print("z  | ");
            else
                System.out.print(side[y] + " | ");

            for(int x = 0; x < schema[y].length; x++)
            {
                System.out.format("%12.2f", schema[y][x]);
            }
            System.out.println();
        }

        System.out.println();
    }

}
