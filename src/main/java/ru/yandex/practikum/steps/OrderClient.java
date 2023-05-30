package ru.yandex.practikum.steps;

import ru.yandex.practikum.constant.BaseEndpoint;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practikum.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseEndpoint {

    @Step("Create order {order}")
    public ValidatableResponse create(Order order){
        return given()
                .spec(getBaseReqSpec())
                .body(order)
                .when()
                .post(BASE_URI + "/api/v1/orders/")
                .then();
    }

    @Step("Delete order {track}")
    public ValidatableResponse delete(int track) {
        return given()
                .spec(getBaseReqSpec())
                .queryParam("track", track)
                .when()
                .put(BASE_URI + "/api/v1/orders/cancel")
                .then();
    }

    @Step("Get list of orders")
    public ValidatableResponse getOrdersList(){
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(BASE_URI + "/api/v1/orders/")
                .then();
    }
}