import io.restassured.response.ValidatableResponse;
import org.example.Courier;
import org.example.CourierClient;
import org.example.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CourierCreateTest {

    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    // подумать, как сделать так, чтобы удалялся курьер при повторном тесте
    @After
    public void cleanUp() {
        courierClient.delete(courierId);
    }

    @Test
    public void courierCanBeCreated() {
        // Создаем курьера со всеми параметрами в БД
        Courier courier = new Courier("Simon865", "1234567891", "saske321");
        ValidatableResponse createResponse = courierClient.create(courier);
        int createStatusCode = createResponse.extract().statusCode();
        assertEquals(createStatusCode, 201);
        boolean created = createResponse.extract().path("ok");
        assertTrue(created);

        // Проверяем, что можно войти в систему под созданным курьером
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(loginStatusCode, 200);
        courierId = loginResponse.extract().path("id");
        assertNotEquals(courierId, 0);
    }

    @Test
    public void cannotCreateDuplicateCouriers() {

        Courier courier = new Courier("Simon865", "1234567891", "saske321");
        // Создаем первого курьера
        ValidatableResponse createResponse1 = courierClient.createDuplicateCourier(courier, "Simon8658", "1234567891", "saske321");
        int createStatusCode1 = createResponse1.extract().statusCode();
        assertEquals(createStatusCode1, 201);
        boolean created1 = createResponse1.extract().path("ok");
        assertTrue(created1);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(loginStatusCode, 200);
        courierId = loginResponse.extract().path("id");
        assertNotEquals(courierId, 0);

        // Пытаемся создать второго курьера с теми же данными
        ValidatableResponse createResponse2 = courierClient.createDuplicateCourier(courier, "Simon865", "1234567891", "saske321");
        int createStatusCode2 = createResponse2.extract().statusCode();
        assertEquals(createStatusCode2, 409); // Проверяем код ответа 409 (конфликт)
        String errorMessage = createResponse2.extract().path("message");
        assertEquals(errorMessage, "Этот логин уже используется.");// Проверяем сообщение об ошибке
    }

        @Test
    public void courierNotCreatedWithoutLogin() {
        Courier courier = new Courier("", "1234567891", "saske321");
        ValidatableResponse createResponse = courierClient.createWithoutLogin(courier, "1234567891", "saske321");
        int createStatusCode = createResponse.extract().statusCode();
        assertEquals(createStatusCode, 400);
        String errorMessage = createResponse.extract().path("message");
        assertEquals(errorMessage, "Недостаточно данных для создания учетной записи");
    }

    @Test
    public void courierNotCreatedWithoutPassword() {
        Courier courier = new Courier("Simon865", "", "saske321");

        ValidatableResponse createResponse = courierClient.createWithoutPassword(courier, "Simon865", "saske321");
        int createStatusCode = createResponse.extract().statusCode();
        assertEquals(createStatusCode, 400);
        String errorMessage = createResponse.extract().path("message");
        assertEquals(errorMessage, "Недостаточно данных для создания учетной записи");
    }


}

