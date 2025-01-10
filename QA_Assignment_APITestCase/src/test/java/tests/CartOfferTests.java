package test.java.tests;

import io.restassured.response.Response;
import main.java.base.BaseTest;

import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * Test class for validating the cart offer functionality.
 */
public class CartOfferTests extends BaseTest {

    private static final String BASE_URL = "http://localhost:8081";

    /**
     * Test flat amount offer (e.g., Flat 10 Rs. off).
     */
    @Test
    public void testFlatAmountOffer() {
        Response addOfferResponse = given()
                .contentType("application/json")
                .body("{\"restaurant_id\":1,\"offer_type\":\"FLATX\",\"offer_value\":10,\"customer_segment\":[\"p1\"]}")
                .post(BASE_URL + "/api/v1/offer");

        Assert.assertEquals(addOfferResponse.getStatusCode(), 200);
        Assert.assertEquals(addOfferResponse.jsonPath().getString("response_msg"), "success");

        Response applyOfferResponse = given()
                .contentType("application/json")
                .body("{\"cart_value\":200,\"user_id\":1,\"restaurant_id\":1}")
                .post(BASE_URL + "/api/v1/cart/apply_offer");

        Assert.assertEquals(applyOfferResponse.getStatusCode(), 200);
        Assert.assertEquals(applyOfferResponse.jsonPath().getInt("cart_value"), 190);
    }

    /**
     * Test flat percentage offer (e.g., 10% off).
     */
    @Test
    public void testFlatPercentageOffer() {
        Response addOfferResponse = given()
                .contentType("application/json")
                .body("{\"restaurant_id\":1,\"offer_type\":\"FLATX%\",\"offer_value\":10,\"customer_segment\":[\"p1\"]}")
                .post(BASE_URL + "/api/v1/offer");

        Assert.assertEquals(addOfferResponse.getStatusCode(), 200);
        Assert.assertEquals(addOfferResponse.jsonPath().getString("response_msg"), "success");

        Response applyOfferResponse = given()
                .contentType("application/json")
                .body("{\"cart_value\":200,\"user_id\":1,\"restaurant_id\":1}")
                .post(BASE_URL + "/api/v1/cart/apply_offer");

        Assert.assertEquals(applyOfferResponse.getStatusCode(), 200);
        Assert.assertEquals(applyOfferResponse.jsonPath().getInt("cart_value"), 180);
    }

    /**
     * Test invalid offer type.
     */
    @Test
    public void testInvalidOfferType() {
        Response addOfferResponse = given()
                .contentType("application/json")
                .body("{\"restaurant_id\":1,\"offer_type\":\"INVALID\",\"offer_value\":10,\"customer_segment\":[\"p1\"]}")
                .post(BASE_URL + "/api/v1/offer");

        Assert.assertEquals(addOfferResponse.getStatusCode(), 400);
        Assert.assertTrue(addOfferResponse.getBody().asString().contains("Invalid offer type"));
    }

    /**
     * Test adding an offer without the restaurant_id.
     */
    @Test
    public void testMissingRestaurantId() {
        Response addOfferResponse = given()
                .contentType("application/json")
                .body("{\"offer_type\":\"FLATX\",\"offer_value\":10,\"customer_segment\":[\"p1\"]}")
                .post(BASE_URL + "/api/v1/offer");

        Assert.assertEquals(addOfferResponse.getStatusCode(), 400);
        Assert.assertTrue(addOfferResponse.getBody().asString().contains("Missing field: restaurant_id"));
    }

    /**
     * Test invalid cart value (negative value).
     */
    @Test
    public void testInvalidCartValue() {
        Response applyOfferResponse = given()
                .contentType("application/json")
                .body("{\"cart_value\":-100,\"user_id\":1,\"restaurant_id\":1}")
                .post(BASE_URL + "/api/v1/cart/apply_offer");

        Assert.assertEquals(applyOfferResponse.getStatusCode(), 400);
        Assert.assertTrue(applyOfferResponse.getBody().asString().contains("Invalid cart value"));
    }
}
