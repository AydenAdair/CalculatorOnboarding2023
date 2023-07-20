package org.aydenadair.calculator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path(value = "/calc")
@Produces(MediaType.APPLICATION_JSON)
public class CalculatorResources {
    private final List<MathProblem> auditLog = new ArrayList<>();

    // This endpoint is for testing purposes, and will always return true.
    @GET
    @Path("easy")
    public boolean easy() {
        return true;
    }

    @GET
    @Path("add")
    public int add(@QueryParam("number1") int number1, @QueryParam("number2") int number2) {
        auditLog.add(new MathProblem("add", number1, number2, (number1 + number2)));
        return number1 + number2;
    }

    @GET
    @Path("subtract")
    public int subtract(@QueryParam("number1") int number1, @QueryParam("number2") int number2) {
        auditLog.add(new MathProblem("subtract", number1, number2, (number1 - number2)));
        return number1 - number2;
    }

    @GET
    @Path("divide")
    public double divide(@QueryParam("number1") int number1, @QueryParam("number2") int number2) throws Exception {
        if (number2 == 0) {
            throw new Exception("Dividing by zero");
        }
        auditLog.add(new MathProblem("divide", number1, number2, ((double) number1) / number2));
        return ((double) number1) / number2;
    }

    @GET
    @Path("multiply")
    public int multiply(@QueryParam("number1") int number1, @QueryParam("number2") int number2) {
        auditLog.add(new MathProblem("subtract", number1, number2, (number1 * number2)));
        return number1 * number2;
    }

    @GET
    @Path("audit")
    public List<MathProblem> audit() {
        return auditLog;
    }
}
