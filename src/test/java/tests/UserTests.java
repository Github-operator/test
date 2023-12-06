package tests;

import client.RestAssuredClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import pojo.ApiResponse;
import pojo.User;

import java.util.HashSet;
import java.util.Set;

@Epic("API")
@Feature("Пользователи")
@Story("Создание, изменение, получение и удаление пользователя")
public class UserTests {

    private Set<String> addedUserNames = new HashSet<>();

    @Severity(SeverityLevel.BLOCKER)
    @Test(description = "Добавление пользователя")
    public void addUser() {
        User newUser = new User()
            .setUsername("Aron")
            .setFirstName("Got")
            .setLastName("Shon")
            .setEmail("cobra@mail.com")
            .setPassword("pas12345")
            .setPhone("89099099090")
            .setUserStatus(0);
        ApiResponse expectedResponse = new ApiResponse()
            .setCode(200)
            .setType("unknown");

        ApiResponse actualResponse = RestAssuredClient.addUser(newUser)
            .then().statusCode(200)
            .extract().as(ApiResponse.class);
        addedUserNames.add(newUser.getUsername());

        Assert.assertEquals(actualResponse, expectedResponse, "Данные ответа не соответствуют ожидаемым");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Получение пользователя")
    public void getUserByName() {
        User testUser = new User()
            .setUsername("Derek")
            .setFirstName("Robin")
            .setLastName("Good")
            .setEmail("cobra@mail.com")
            .setPassword("pas12345")
            .setPhone("89099099090")
            .setUserStatus(0);

        Long addedUserId = Long.parseLong(RestAssuredClient.addUser(testUser)
            .then().statusCode(200)
            .extract().jsonPath().getString("message"));
        testUser.setId(addedUserId);
        addedUserNames.add(testUser.getUsername());

        User obtainedUser = RestAssuredClient.getUserByName(testUser.getUsername())
            .then().statusCode(200)
            .extract().as(User.class);

        Assert.assertEquals(obtainedUser, testUser, "Данные полученного пользователя не соответствуют ожидаемым");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Изменение данных пользователя пользователя")
    public void editUser() {
        User newUserData = new User()
            .setUsername("Forest")
            .setFirstName("Gump")
            .setLastName("Forest")
            .setEmail("ForestGunp@mail.com")
            .setPassword("pas54321")
            .setPhone("89099099111")
            .setUserStatus(0);
        User testUser = new User()
            .setUsername("Frank")
            .setFirstName("Ford")
            .setLastName("Good")
            .setEmail("cobra@mail.com")
            .setPassword("pas12345")
            .setPhone("89099099090")
            .setUserStatus(0);
        ApiResponse expectedResponse = new ApiResponse()
            .setCode(200)
            .setType("unknown");

        Long addedUserId = Long.parseLong(RestAssuredClient.addUser(testUser)
            .then().statusCode(200)
            .extract().jsonPath().getString("message"));

        newUserData.setId(addedUserId);
        expectedResponse.setMessage(Long.toString(addedUserId));
        addedUserNames.add(testUser.getUsername());

        ApiResponse actualResponse = RestAssuredClient.changeUserByName(testUser.getUsername(), newUserData)
            .then().statusCode(HttpStatus.SC_OK)
            .extract().as(ApiResponse.class);

        Assert.assertEquals(actualResponse, expectedResponse, "Данные ответа не соответствуют ожидаемым");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Удаления пользователя")
    public void deleteUser() {
        User testUser = new User()
            .setUsername("Delete")
            .setFirstName("Response")
            .setLastName("Good")
            .setEmail("cobra@mail.com")
            .setPassword("pas12345")
            .setPhone("89099099090")
            .setUserStatus(0);
        ApiResponse expectedResponse = new ApiResponse()
            .setCode(200)
            .setType("unknown")
            .setMessage(testUser.getUsername());

        Long addedUserId = Long.parseLong(RestAssuredClient.addUser(testUser)
            .then().statusCode(200)
            .extract().jsonPath().getString("message"));
        testUser.setId(addedUserId);
        addedUserNames.add(testUser.getUsername());

        ApiResponse actualResponse = RestAssuredClient.deleteUserByName(testUser.getUsername())
            .then().statusCode(200)
            .extract().as(ApiResponse.class);

        Assert.assertEquals(actualResponse, expectedResponse, "Данные ответа не соответствуют ожидаемым");
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Изменение не существующего пользователя")
    public void editAbsentUser() {
        User testUser = new User()
            .setUsername("Verdoom")
            .setFirstName("Gump")
            .setLastName("Forest")
            .setEmail("ForestGunp@mail.com")
            .setPassword("pas54321")
            .setPhone("89099099111")
            .setUserStatus(0);
        ApiResponse expectedResponse = new ApiResponse()
            .setCode(404)
            .setType("error")
            .setMessage("User not found");

        ApiResponse actualResponse = RestAssuredClient.changeUserByName("AbsentUser", testUser)
            .then().statusCode(404)
            .extract().as(ApiResponse.class);

        Assert.assertEquals(actualResponse, expectedResponse, "Данные ответа не соответствуют ожидаемым");
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Удаление не существующего пользователя")
    public void deleteAbsentUser() {
        RestAssuredClient.deleteUserByName("Absent").then().statusCode(404);
    }

    @AfterClass
    public void deleteTestData() {
        addedUserNames.forEach(addedUserName -> RestAssuredClient.deleteUserByName(addedUserName));
    }
}
