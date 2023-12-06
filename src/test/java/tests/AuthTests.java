package tests;

import client.RestAssuredClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.ApiResponse;
import util.PropertyProvider;

import static org.hamcrest.Matchers.containsString;

@Epic("API")
@Feature("Авторизация")
@Story("Авторизация в сервисе")
public class AuthTests {

    @Severity(SeverityLevel.BLOCKER)
    @Test(description = "Авторизация с валидными данными")
    public void login() {
        String username = PropertyProvider.getEnvProperty("USERNAME");
        String password = PropertyProvider.getEnvProperty("PASSWORD");

        RestAssuredClient.login(username, password)
            .then().statusCode(HttpStatus.SC_OK).body("message", containsString("logged in user session:"));
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Авторизация с невалидными данными")
    public void loginWithInvalidPassword() {
        String username = "User1";
        String password = "invalidPass1234";
        ApiResponse expectedResponse = new ApiResponse(401, "error", "Login or Password not valid");

        ApiResponse actualResponse = RestAssuredClient.login(username, password)
            .then().statusCode(401).extract().as(ApiResponse.class);

        Assert.assertEquals(actualResponse, expectedResponse, "Данные актуального ответа не соответствуют ожидаемым");
    }
}
