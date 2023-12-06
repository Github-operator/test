package client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.DataClass;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import pojo.Order;
import pojo.Pet;
import pojo.User;
import util.PropertyProvider;

import java.util.logging.Level;
import java.util.logging.Logger;

import static util.PropertyProvider.getEnvProperty;

public final class RestAssuredClient {

    private static final Logger LOGGER = Logger.getLogger(RestAssuredClient.class.getName());

    static String sessionToken = "undefined";
    private static final RequestSpecification authorizedResponseSpecification;

    static {
        String baseUrl = getEnvProperty("BASE_URL");
        if (baseUrl == null) {
            LOGGER.log(Level.SEVERE, "Не смог получить BASE_URL");
            throw new RuntimeException("Не смог получить BASE_URL");
        }
        RestAssured.baseURI = baseUrl;

        RestAssured.filters(new AllureRestAssured());

        RestAssured.requestSpecification = new RequestSpecBuilder()
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .build();

        RestAssured.config = RestAssuredConfig
            .config()
            .objectMapperConfig(new ObjectMapperConfig()
                .jackson2ObjectMapperFactory(
                    (cls, charset) -> new ObjectMapper().findAndRegisterModules()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                ));

        String username = PropertyProvider.getEnvProperty("USERNAME");
        String password = PropertyProvider.getEnvProperty("PASSWORD");
        Response response = RestAssuredClient.login(username, password);
        if (response.statusCode() == HttpStatus.SC_OK) {
            sessionToken = response.then().extract().jsonPath()
                .getString("message")
                .replace("logged in user session:", "");
        } else {
            LOGGER.log(Level.SEVERE, "Не удалось поучить токен сессии. Код ответа запроса: "
                + response.statusCode());
        }

        authorizedResponseSpecification = RestAssured
            .given()
            .header("sessionToken", sessionToken);
    }

    @Step("Отправка запроса на авторизацию")
    public static Response login(String username, String password) {
        return RestAssured
            .given().log().ifValidationFails()
            .queryParam("username", username)
            .queryParam("password", password)
            .get(DataClass.USER_LOGIN_URL);
    }

    @Step("Отправка запроса на получение пользователя по имени")
    public static Response getUserByName(String username) {
        return RestAssured.given()
            .spec(authorizedResponseSpecification)
            .log().ifValidationFails()
            .get(DataClass.USER_URL + "/{username}", username);
    }

    @Step("Отправка запроса на добавление пользователя")
    public static Response addUser(User user) {
        return RestAssured.given()
            .spec(authorizedResponseSpecification)
            .log().ifValidationFails()
            .body(user).post(DataClass.USER_URL);
    }

    @Step("Отправка запроса на изменение данных пользователя по имени")
    public static Response changeUserByName(String username, User user) {
        return RestAssured.given()
            .spec(authorizedResponseSpecification)
            .log().ifValidationFails()
            .body(user)
            .put(DataClass.USER_URL + "/{username}", username);
    }

    @Step("Отправка запроса на удаление пользователя по имени")
    public static Response deleteUserByName(String username) {
        return RestAssured.given()
            .spec(authorizedResponseSpecification)
            .log().ifValidationFails()
            .delete(DataClass.USER_URL + "/{username}", username);
    }

    @Step("Отправка запроса на добавление питомца")
    public static Response addPet(Pet pet) {
        return RestAssured.given()
            .spec(authorizedResponseSpecification)
            .log().ifValidationFails()
            .body(pet).post(DataClass.PET_URL);
    }

    @Step("Отправка запроса на получение питомца по id")
    public static Response getPetById(long id) {
        return RestAssured.given().given()
            .spec(authorizedResponseSpecification)
            .log().ifValidationFails()
            .get(DataClass.PET_URL + "/{id}", id);
    }

    @Step("Отправка запроса на удаление питомца по id")
    public static Response deletePetById(long id) {
        return RestAssured.given().given()
            .spec(authorizedResponseSpecification)
            .log().ifValidationFails()
            .delete(DataClass.PET_URL + "/{id}", id);
    }

    @Step("Отправка запроса на добавление заказа на питомца")
    public static Response addOrder(Order order) {
        return RestAssured.given()
            .spec(authorizedResponseSpecification)
            .log().ifValidationFails()
            .body(order).post(DataClass.ORDER_URL);
    }

    @Step("Отправка запроса на удаление заказа на питомца по id")
    public static Response deleteOrderById(long orderId) {
        return RestAssured.given()
            .spec(authorizedResponseSpecification)
            .log().ifValidationFails()
            .delete(DataClass.ORDER_URL + "/{orderId}", orderId);
    }

    @Step("Отправка запроса на получение заказа на питомца по id")
    public static Response getOrderById(long orderId) {
        return RestAssured.given()
            .spec(authorizedResponseSpecification)
            .log().ifValidationFails()
            .get(DataClass.ORDER_URL + "/{orderId}", orderId);
    }
}
