package org.example;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class CourierClient extends RestClient {

    public static final String COURIER_PATH = "api/v1/courier";
    public static final String COURIER_LOGIN = "/api/v1/courier/login";
    public static final String COURIER_DELETE = "/api/v1/courier/{id}";

    @Step("Создание курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Создание курьера с пустой строкой в поле логин")
    public ValidatableResponse createWithoutLogin(Courier courier, String password, String firstName) {
        return given()
                .spec(getBaseSpec())
                .queryParam("login", courier.getLogin())
                .queryParam("password", password)
                .queryParam("firstName", firstName)
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Создание курьера с пустой строкой в поле пароль")
    public ValidatableResponse createWithoutPassword(Courier courier, String login, String firstName) {
        return given()
                .spec(getBaseSpec())
                .queryParam("login", login)
                .queryParam("password", courier.getPassword())
                .queryParam("firstName", firstName)
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Авторизация курьера и получение ID курьера при авторизации")
    public ValidatableResponse login(CourierCredentials courierCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(courierCredentials)
                .when()
                .post(COURIER_LOGIN)
                .then();

    }

    public void delete(int courierId) {
        given()
                .spec(getBaseSpec())
                .when()
                .delete(COURIER_DELETE, courierId)
                .then();
    }

    @Step("Проверка создания двух одинаковых курьеров")
    public ValidatableResponse createDuplicateCourier(Courier courier, String login, String password, String firstName)  {
        return given()
                .spec(getBaseSpec())
                .queryParam("login", login)
                .queryParam("password", password)
                .queryParam("firstName", firstName)
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

}










