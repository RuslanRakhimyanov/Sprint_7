package ru.yandex.practikum;

import ru.yandex.practikum.steps.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practikum.model.Courier;
import ru.yandex.practikum.model.CourierCredentials;
import ru.yandex.practikum.model.CourierGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierTest {
    private CourierClient courierClient;
    private int courierId;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void clearData() {
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Создание курьера с валидными данными")
    public void courierCanBeCreatedWithValidData() {
        Courier courier = CourierGenerator.getRandom();

        ValidatableResponse createResponse = courierClient.create(courier);
        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");

        createResponse.assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));
    }
    @Test
    @DisplayName("Авторизация под успешно созданным курьером")
    public void courierCanBeCreatedAndLogInWithValidData() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier);

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");

    }
    @Test
    @DisplayName("Создание курьера с уже существующими данными")
    public void courierCantBeCreatedWithSimilarData() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier);
        courierClient.create(courier)
                .assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .assertThat()
                .body("message", is("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void courierCantBeCreatedWithoutLogin() {
        Courier courier = CourierGenerator.getRandom();
        courier.setLogin("");

        courierClient.create(courier)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void courierCantBeCreatedWithoutPassword() {
        Courier courier = CourierGenerator.getRandom();
        courier.setPassword("");

        courierClient.create(courier)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Авторизация курьера без логина")
    public void courierCantLogInWithoutLogin() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier);
        courier.setLogin("");

        courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    public void courierCantLogInWithoutPassword() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier);
        courier.setPassword("");

        courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Авторизация с несуществующим логином")
    public void courierCantLogInWithNonExistentLogin() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier);
        courier.setLogin(courier.getLogin() + "Test");

        courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", is("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Авторизация курьера с неправильным паролем")
    public void courierCantLogInWithWrongPassword() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier);
        courier.setPassword(courier.getPassword() + "Test");

        courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", is("Учетная запись не найдена"));

    }
}