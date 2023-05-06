package ru.yandex.practikum;

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

import java.util.ArrayList;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderTest {
    private OrderClient orderClient;
    private int track;

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;

    ArrayList<String> colors = new ArrayList<>();

    public OrderTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
    }

    @Parameterized.Parameters(name = "Тестовые данные заказа: {0} {1} {2} {3} {4} {5} {6} {7}")
    public static Object[][] getSumData() {
        return new Object[][]{
                {"Руслан", "Тестович", "Победы дом 37 кв 100", "Сокольники", "9634486817", 2, "2023-05-07", "Тестовый коммент!"},
                {"Анастасия", "Ивановна", "Ленина дом 55 кв 9", "Сокольники", "9634486815", 2, "2023-05-07", "Тестовый коммент!"},
        };
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
    public void createOrderWithRequiredFields(){
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment);

        track = orderClient.create(order)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("track", notNullValue())
                .extract().path("track");
    }

    @Test
    public void createOrderWithSingleColor(){
        colors.add("BLACK");
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colors);

        track = orderClient.create(order)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("track", notNullValue())
                .extract().path("track");
    }

    @Test
    public void createOrderWithMultipleColors(){
        colors.add("BLACK");
        colors.add("GRAY");
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colors);

        track = orderClient.create(order)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("track", notNullValue())
                .extract().path("track");
    }
}