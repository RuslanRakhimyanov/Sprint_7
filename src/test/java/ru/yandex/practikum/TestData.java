package ru.yandex.practikum;
import ru.yandex.practikum.steps.CourierClient;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TestData {
    protected CourierClient courierClient;
    protected int courierId;
    protected static final String TEST_FIRST_NAME = "Руслан";
    protected static final String TEST_LAST_NAME = "Тестов";
    protected static final String TEST_ADDRESS = "Победы дом 37 кв 100";
    protected static final String TEST_METRO_STATION = "Сокольники";
    protected static final String TEST_PHONE = "9634486817";
    protected static final int TEST_RENT_TIME = 2;
    protected static final String TEST_DELIVERY_DATE = LocalDate.now().plusDays(1).toString();
    protected static final String TEST_COMMENT = "Тестовый коммент";
    protected static final List<String> TEST_COLOR_BLACK = List.of("BLACK");
    protected static final List<String> TEST_COLOR_GREY = List.of("GREY");
    protected static final List<String> TEST_COLORS = Arrays.asList("BLACK", "GREY");
}
