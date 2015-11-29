package ch.nexpose;

import ch.nexpose.simplex.Equation;
import ch.nexpose.simplex.SimplexProblem;
import ch.nexpose.simplex.SimplexSolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("please give me a problem to solve!");
            return;
        }

        Path path = FileSystems.getDefault().getPath(args[0]);
        String contents = "Not Data";
        try {
            contents = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //todo: remove equation prove
        double[] eq = new double[] { -40, -120, 2400 };
        double[] x1 = new double[] { -1, -1, 40 };

        double[] solution = Equation.plugIn(eq, x1, 0);

        double[] shifted = Equation.shift(new double[]{7, -5, 32}, 1);

        //create new problem
        SimplexProblem p = new SimplexProblem();
        p.parse(contents);

        //solve problem
        SimplexSolver solver = new SimplexSolver();
        Double res = solver.solve(p);

        if(res == Double.NaN)
        {
            System.out.println("Problem has no solution!");
        }
        else
        {
            System.out.println("Problem solving finished!");
        }
    }
}
