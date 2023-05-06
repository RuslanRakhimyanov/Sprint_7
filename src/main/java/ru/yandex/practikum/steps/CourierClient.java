package ru.yandex.practikum.steps;

import ru.yandex.practikum.constant.BaseEndpoint;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practikum.model.Courier;
import ru.yandex.practikum.model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient extends BaseEndpoint {

    @Step("Create courier {courier}")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseReqSpec())
                .body(courier)
                .when()
                .post(BASE_URI + "/api/v1/courier")
                .then();
    }

    @Step("Login as {courierCredentials}")
    public ValidatableResponse login(CourierCredentials courierCredentials) {
        return given()
                .spec(getBaseReqSpec())
                .body(courierCredentials)
                .when()
                .post(BASE_URI + "/api/v1/courier/login/")
                .then();
    }

    @Step("Delete courier {id}")
    public ValidatableResponse delete(int id) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .delete(BASE_URI + "/api/v1/courier" + id)
                .then();
    }
}