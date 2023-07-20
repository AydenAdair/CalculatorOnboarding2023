import okhttp3.*;
import org.aydenadair.calculator.CalculatorApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import java.io.IOException;
import java.net.URL;


import static org.junit.jupiter.api.Assertions.*;

public class TestCalculator {

    private RetrofitClient client;
    private final int SUCCESS_CODE = 200;
    private final int EXCEPTION_CODE = 400;
    private final int NOT_FOUND_CODE = 404;

    @BeforeEach
    public void createThread() throws Exception {
        Thread thread = new Thread(() -> {
            try {
                CalculatorApp.main(new String[]{});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();

        String authorizationToken = Credentials.basic("admin", "password");

        Interceptor headerAuthorizationInspector = new Interceptor() {

            @Override
            public Response intercept(final Chain chain) throws IOException {
                okhttp3.Request request = chain.request();
                Headers headers = request.headers().newBuilder().add("Authorization", authorizationToken).build();
                request = request.newBuilder().headers(headers).build();
                return chain.proceed(request);
            }
        };

        final URL baseURL = new URL("http://localhost:8080");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(new OkHttpClient().newBuilder().addInterceptor(headerAuthorizationInspector).build())
                .build();

        client = retrofit.create(RetrofitClient.class);

        long startTime = System.currentTimeMillis();
        boolean serverRunning = false;

        final double TIMEOUT_DURATION_MILLISECONDS = 5000;

        while (System.currentTimeMillis() - TIMEOUT_DURATION_MILLISECONDS < startTime) {
            try {
                if (client.testEasy().execute().code() == SUCCESS_CODE) {
                    serverRunning = true;
                    break;
                }
            } catch (Exception e) {
                // do nothing
            }
        }

        if (!serverRunning) {
            throw new Exception("Server timed out");
        }
    }

    // Addition Tests
    @Test
    public void testAddEqual() throws IOException {
        assertEquals(3, client.testAdd(1,2).execute().body());
        assertEquals(-1, client.testAdd(4, -5).execute().body());
    }

    @Test
    public void testAddNotEqual() throws IOException {
        assertNotEquals(3, client.testAdd(1,1).execute().body());
        assertNotEquals(-1, client.testAdd(-3, -2).execute().body());
    }

    @Test
    public void testAddSuccessCode() throws IOException {
        assertEquals(SUCCESS_CODE, client.testAdd(8000,-90).execute().code());
    }

    @Test
    public void testAddFailCode() throws IOException {
        assertEquals(NOT_FOUND_CODE, client.testAdd("NaN", 0).execute().code());
    }

    @Test
    @SuppressWarnings("NumericOverflow")
    public void testAddIntOverflow() throws IOException {
        assertEquals(0, client.testAdd(Integer.MAX_VALUE + 1, Integer.MAX_VALUE + 1).execute().body());
    }

    // Subtraction Tests
    @Test
    public void testSubtractEqual() throws IOException {
        assertEquals(0, client.testSubtract(5,5).execute().body());
        assertEquals(-10, client.testSubtract(2, 12).execute().body());
    }

    @Test
    public void testSubtractNotEqual() throws IOException {
        assertNotEquals(3, client.testSubtract(1,1).execute().body());
        assertNotEquals(-1, client.testSubtract(-1,-1).execute().body());
    }

    @Test
    public void testSubtractSuccessCode() throws IOException {
        assertEquals(SUCCESS_CODE, client.testSubtract(-20, 90).execute().code());
    }

    @Test
    public void testSubtractFailCode() throws IOException {
        assertEquals(NOT_FOUND_CODE, client.testSubtract(-20, "NaN").execute().code());
    }

    @Test
    @SuppressWarnings("NumericOverflow")
    public void testSubtractIntOverflow() throws IOException {
        assertEquals(0, client.testSubtract(Integer.MAX_VALUE + 1, Integer.MAX_VALUE + 1).execute().body());
    }

    // Division Tests
    @Test
    public void testDivideEqual() throws IOException {
        assertEquals(1, client.testDivide(5,5).execute().body());
        assertEquals(-1, client.testDivide(12, -12).execute().body());
    }

    @Test
    public void testDivideNotEqual() throws IOException {
        assertNotEquals(3, client.testDivide(1,1).execute().body());
        assertNotEquals(-1, client.testDivide(-1,-1).execute().body());
    }

    @Test
    public void testDivideSuccessCode() throws IOException {
        assertEquals(SUCCESS_CODE, client.testDivide(-20, 2).execute().code());
    }

    @Test
    public void testDivideFailCode() throws IOException {
        assertEquals(NOT_FOUND_CODE, client.testDivide(-20, "NaN").execute().code());
    }

    @Test
    @SuppressWarnings("NumericOverflow")
    public void testDivideIntOverflow() throws IOException {
        assertEquals(1, client.testDivide(Integer.MAX_VALUE + 1, Integer.MIN_VALUE).execute().body());
    }

    @Test
    public void testDivideByZero() throws IOException {
        assertEquals(EXCEPTION_CODE, client.testDivide(-20, 0).execute().code());
    }


    // Multiplication Tests
    @Test
    public void testMultiplyEqual() throws IOException {
        assertEquals(25, client.testMultiply(5,5).execute().body());
        assertEquals(-24, client.testMultiply(2, -12).execute().body());
    }

    @Test
    public void testMultiplyNotEqual() throws IOException {
        assertNotEquals(3, client.testMultiply(1,1).execute().body());
        assertNotEquals(-1, client.testMultiply(-1,-1).execute().body());
    }

    @Test
    public void testMultiplySuccessCode() throws IOException {
        assertEquals(SUCCESS_CODE, client.testMultiply(-20, 90).execute().code());
    }

    @Test
    public void testMultiplyFailCode() throws IOException {
        assertEquals(NOT_FOUND_CODE, client.testMultiply(-20, "NaN").execute().code());
    }

    @Test
    public void testMultiplyIntOverflow() throws IOException {
        assertEquals(Integer.MIN_VALUE, client.testMultiply((Integer.MAX_VALUE / 2) + 1, 2).execute().body());
    }




}
