import io.restassured.response.ValidatableResponse;
import org.example.Courier;
import org.example.CourierClient;
import org.example.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CourierLoginTest {
    private CourierClient courierClient;
    private int courierId;
    Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new Courier("Simon865", "1234567891", "saske321");
        courierClient.create(courier);

    }

    // Удаление курьера по ID
    @After
    public void cleanUp() {
        courierClient.login(CourierCredentials.from(courier));
        courierClient.delete(courierId);
    }

    // Тест на авторизацию курьера без логина
    @Test
    public void TestCourierNotAuthorizationWithoutLogin() {
        courier.setLogin("");
        courier.setPassword("1234567891");

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));

        // Проверка статуса кода ответа
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(loginStatusCode, 400);

        // Проверка сообщения об ошибке
        String errorMessage = loginResponse.extract().path("message");
        assertEquals(errorMessage, "Недостаточно данных для входа");
    }

    // Тест на авторизацию курьера без пароля
    @Test
    public void TestCourierNotAuthorizationWithoutPassword() {
        courier.setLogin("Simon865");
        courier.setPassword("");

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));

        // Проверка статуса кода ответа
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(loginStatusCode, 400);

        // Проверка сообщения об ошибке
        String errorMessage = loginResponse.extract().path("message");
        assertEquals(errorMessage, "Недостаточно данных для входа");
    }

    // Тест на авторизацию курьера без логина и пароля
    @Test
    public void TestCourierNotAuthorizationWithoutLoginAndPassword() {
        courier.setLogin("");
        courier.setPassword("");

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));

        // Проверка статуса кода ответа
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(loginStatusCode, 400);

        // Проверка сообщения об ошибке
        String errorMessage = loginResponse.extract().path("message");
        assertEquals(errorMessage, "Недостаточно данных для входа");
    }

    // Тест на авторизацию курьера с несуществующим логином
    @Test
    public void TestCourierNotAuthorizationWithMistakeLogin() {
        courier.setLogin("Simon86");
        courier.setPassword("1234567891");

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));

        // Проверка статуса кода ответа
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(loginStatusCode, 404);

        // Проверка сообщения об ошибке
        String errorMessage = loginResponse.extract().path("message");
        assertEquals(errorMessage, "Учетная запись не найдена");
    }

    // Тест на авторизацию курьера с несуществующим паролем
    @Test
    public void TestCourierNotAuthorizationWithMistakePassword() {
        courier.setLogin("Simon865");
        courier.setPassword("123456789");

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));

        // Проверка статуса кода ответа
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(loginStatusCode, 404);

        // Проверка сообщения об ошибке
        String errorMessage = loginResponse.extract().path("message");
        assertEquals(errorMessage, "Учетная запись не найдена");
    }
}

