package ru.yandex.practikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import ru.yandex.practikum.TestData;
import ru.yandex.practikum.steps.OrderClient;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import ru.yandex.practikum.model.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderTest extends TestData {
    private OrderClient orderClient;
    private int track;

    private static final List<Order> ORDERS = List
            .of(new Order(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_ADDRESS, TEST_METRO_STATION, TEST_PHONE, TEST_RENT_TIME, TEST_DELIVERY_DATE, TEST_COMMENT, TEST_COLOR_BLACK),
                    new Order(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_ADDRESS, TEST_METRO_STATION, TEST_PHONE, TEST_RENT_TIME, TEST_DELIVERY_DATE, TEST_COMMENT, TEST_COLOR_GREY),
                    new Order(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_ADDRESS, TEST_METRO_STATION, TEST_PHONE, TEST_RENT_TIME, TEST_DELIVERY_DATE, TEST_COMMENT, TEST_COLORS),
                    new Order(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_ADDRESS, TEST_METRO_STATION, TEST_PHONE, TEST_RENT_TIME, TEST_DELIVERY_DATE, TEST_COMMENT));
    private final Order order;


    public OrderTest(Order order) {
        this.order = order;
    }

    @Parameterized.Parameters
    public static List<Order> getOrderCreationTestData() {
        return ORDERS;
    }

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @After
    public void clearData() {
        orderClient.delete(track);
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Позитивный тест создания заказа с параметризацией")
    public void createOrderWithRequiredFields(){
        track = orderClient.create(order)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("track", notNullValue())
                .extract().path("track");

    }
}