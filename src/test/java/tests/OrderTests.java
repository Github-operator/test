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
import pojo.Order;
import pojo.Pet;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Epic("API")
@Feature("Питомцы")
@Story("Добавление, получение и удаление питомца")
public class OrderTests {

    private Set<Long> addedOrderIds = new HashSet<>();
    private Set<Long> addedPetIds = new HashSet<>();

    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Добавление заказа")
    public void addOrder() {
        Pet newPet = new Pet()
            .setCategory(new Pet.PetCategory(50, "First"))
            .setName("Mike")
            .setPhotoUrls(new String[] { "link1" })
            .setStatus("available")
            .setTags(new Pet.Tag[] { new Pet.Tag(4, "tagName") });

        Long petId = RestAssuredClient.addPet(newPet).then().statusCode(HttpStatus.SC_OK).extract().as(Pet.class).getId();
        addedPetIds.add(petId);

        Order newOrder = new Order()
            .setPetId(petId)
            .setQuantity(1)
            .setShipDate(LocalDateTime.now().toString())
            .setStatus("placed")
            .setComplete(true);

        Order addedOrder = RestAssuredClient.addOrder(newOrder)
            .then().statusCode(HttpStatus.SC_OK)
            .extract().as(Order.class);

        addedOrderIds.add(addedOrder.getId());

        Assert.assertEquals(addedOrder, newOrder, "Данные добавленного заказа не соответствуют ожидаемым");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Получение заказа")
    public void getOrder() {
        Pet newPet = new Pet()
            .setCategory(new Pet.PetCategory(50, "First"))
            .setName("Jack")
            .setPhotoUrls(new String[] { "link1" })
            .setStatus("available")
            .setTags(new Pet.Tag[] { new Pet.Tag(4, "tagName") });

        Long petId = RestAssuredClient.addPet(newPet)
            .then().statusCode(HttpStatus.SC_OK).extract().as(Pet.class).getId();
        addedPetIds.add(petId);

        Order newOrder = new Order()
            .setPetId(petId)
            .setQuantity(1)
            .setShipDate(LocalDateTime.now().toString())
            .setStatus("placed")
            .setComplete(true);

        Order expectedOrder = RestAssuredClient.addOrder(newOrder)
            .then().statusCode(HttpStatus.SC_OK).extract().as(Order.class);
        addedOrderIds.add(expectedOrder.getId());

        Order obtainedOrder = RestAssuredClient.getOrderById(expectedOrder.getId())
            .then().statusCode(200).extract().as(Order.class);

        Assert.assertEquals(obtainedOrder, expectedOrder, "Данные полученного заказа не соответствуют ожидаемым");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Удаление заказа")
    public void deleteOrder() {
        Pet newPet = new Pet()
            .setCategory(new Pet.PetCategory(50, "First"))
            .setName("Timka")
            .setPhotoUrls(new String[] { "link1" })
            .setStatus("available")
            .setTags(new Pet.Tag[] { new Pet.Tag(4, "tagName") });

        Long petId = RestAssuredClient.addPet(newPet)
            .then().statusCode(HttpStatus.SC_OK).extract().as(Pet.class).getId();
        addedPetIds.add(petId);

        Order newOrder = new Order()
            .setPetId(petId)
            .setQuantity(1)
            .setShipDate(LocalDateTime.now().toString())
            .setStatus("placed")
            .setComplete(true);

        Order expectedOrder = RestAssuredClient.addOrder(newOrder)
            .then().statusCode(HttpStatus.SC_OK).extract().as(Order.class);
        addedOrderIds.add(expectedOrder.getId());

        RestAssuredClient.deleteOrderById(expectedOrder.getId()).then().statusCode(200);
    }

    @AfterClass
    public void deleteTestData() {
        addedOrderIds.forEach(addedOrderId -> RestAssuredClient.deleteOrderById(addedOrderId));
        addedPetIds.forEach(addedOrderId -> RestAssuredClient.deletePetById(addedOrderId));
    }
}
