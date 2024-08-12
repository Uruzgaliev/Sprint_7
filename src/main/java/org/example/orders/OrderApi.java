package org.example.orders;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;


public class OrderApi extends RestOrder {

    public static final String ORDER_PATH = "api/v1/orders";
    public static final String CANSEL_ORDER_PATH = "/api/v1/orders/cancel";



// Создание заказа
    public ValidatableResponse createOrder(Order order) {
        return given()
                .spec(getBaseSpecification())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    // Отмена заказа
    public void cancelOrder(OrderCancel orderCancel) {
        given()
                .spec(getBaseSpecification())
                .body(orderCancel)
                .when()
                .put(CANSEL_ORDER_PATH)
                .then();
    }

    // Получение списка заказов
    public ValidatableResponse getOrdersList() {
        return given()
                .spec(getBaseSpecification())
                .when()
                .get(ORDER_PATH)
                .then();



    }







}
