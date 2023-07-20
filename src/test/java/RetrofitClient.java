import java.util.ArrayList;

import org.aydenadair.calculator.MathProblem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitClient {
    @GET("/calc/add")
    Call<Integer> testAdd(@Query("number1") Object number1, @Query("number2") Object number2);

    @GET("/calc/subtract")
    Call<Integer> testSubtract(@Query("number1") Object number1, @Query("number2") Object number2);

    @GET("/calc/divide")
    Call<Double> testDivide(@Query("number1") Object number1, @Query("number2") Object number2);

    @GET("/calc/multiply")
    Call<Integer> testMultiply(@Query("number1") Object number1, @Query("number2") Object number2);

    @GET("/calc/audit")
    Call<ArrayList<MathProblem>> testAudit();

    @GET("/calc/easy")
    Call<Void> testEasy();
}
