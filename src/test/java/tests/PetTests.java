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
import pojo.Pet;

import java.util.HashSet;
import java.util.Set;

@Epic("API")
@Feature("Питомцы")
@Story("Добавление, получение и удаление питомца")
public class PetTests {

    private Set<Long> addedPetIds = new HashSet<>();

    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Добавление нового питомца")
    public void addNewPet() {
        Pet newPet = new Pet()
            .setCategory(new Pet.PetCategory(50, "First"))
            .setName("Google")
            .setPhotoUrls(new String[]{"link1"})
            .setStatus("available")
            .setTags(new Pet.Tag[]{new Pet.Tag(4, "tagName")});

        Pet addedPet = RestAssuredClient.addPet(newPet).then().statusCode(HttpStatus.SC_OK).extract().as(Pet.class);
        addedPetIds.add(addedPet.getId());

        Assert.assertEquals(addedPet, newPet, "Данные добавленного питомца не соответствуют ожидаемым");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Получение питомца по id")
    public void getPetById() {
        Pet newPet = new Pet()
            .setCategory(new Pet.PetCategory(50, "First"))
            .setName("Tury")
            .setPhotoUrls(new String[]{"link1"})
            .setStatus("available")
            .setTags(new Pet.Tag[]{new Pet.Tag(4, "tagName")});

        Pet expectedPet = RestAssuredClient.addPet(newPet)
            .then().statusCode(HttpStatus.SC_OK)
            .extract().as(Pet.class);
        addedPetIds.add(expectedPet.getId());

        Pet obtainedPet = RestAssuredClient.getPetById(expectedPet.getId())
            .then().statusCode(200)
            .extract().as(Pet.class);

        Assert.assertEquals(obtainedPet, expectedPet, "Данные полученного питомца не соответствуют ожидаемым");
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Получение питомца по несуществующему id")
    public void getPetWithAbsentId() {
        int absentPetId = 500;

        RestAssuredClient.getPetById(absentPetId).then().statusCode(404);
    }

    @AfterClass
    public void deleteTestData() {
        addedPetIds.forEach(addedOrderId -> RestAssuredClient.deletePetById(addedOrderId));
    }
}
