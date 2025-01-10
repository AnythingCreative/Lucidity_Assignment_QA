package main.java.APIUtilies;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MockServerSetup {
    private static WireMockServer wireMockServer;

    /**
     * Starts the WireMock server and sets up all the necessary stubs.
     */
    public static void startMockServer() {
        // Start the WireMock server on port 8081
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8081));
        wireMockServer.start();

        // Stub for GET /api/v1/user_segment
        wireMockServer.stubFor(get(urlPathEqualTo("/api/v1/user_segment"))
                .withQueryParam("user_id", equalTo("1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"segment\":\"p1\"}")
                        .withStatus(200)));

        // Stub for POST /api/v1/offer - Add a valid offer
        wireMockServer.stubFor(post(urlEqualTo("/api/v1/offer"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"response_msg\":\"success\"}")
                        .withStatus(200)));

        // Stub for POST /api/v1/offer - Invalid offer type
        wireMockServer.stubFor(post(urlEqualTo("/api/v1/offer"))
                .withRequestBody(equalToJson("{\"restaurant_id\":1,\"offer_type\":\"INVALID\",\"offer_value\":10,\"customer_segment\":[\"p1\"]}"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withBody("Invalid offer type")));

        // Stub for POST /api/v1/offer - Missing restaurant_id
        wireMockServer.stubFor(post(urlEqualTo("/api/v1/offer"))
                .withRequestBody(equalToJson("{\"offer_type\":\"FLATX\",\"offer_value\":10,\"customer_segment\":[\"p1\"]}"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withBody("Missing field: restaurant_id")));

        // Stub for POST /api/v1/cart/apply_offer - Flat amount discount
        wireMockServer.stubFor(post(urlEqualTo("/api/v1/cart/apply_offer"))
                .withRequestBody(equalToJson("{\"cart_value\":200,\"user_id\":1,\"restaurant_id\":1}"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"cart_value\":190}") // Correct value for flat discount of 10
                        .withStatus(200)));

        // Stub for POST /api/v1/cart/apply_offer - Flat percentage discount
        wireMockServer.stubFor(post(urlEqualTo("/api/v1/cart/apply_offer"))
                .withRequestBody(equalToJson("{\"cart_value\":200,\"user_id\":1,\"restaurant_id\":1}"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"cart_value\":180}") // Correct value for 10% discount
                        .withStatus(200)));

        // Stub for POST /api/v1/cart/apply_offer - Invalid cart value
        wireMockServer.stubFor(post(urlEqualTo("/api/v1/cart/apply_offer"))
                .withRequestBody(equalToJson("{\"cart_value\":-100,\"user_id\":1,\"restaurant_id\":1}"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withBody("Invalid cart value")));

        System.out.println("WireMock server started on port 8081");
    }

    /**
     * Stops the WireMock server.
     */
    public static void stopMockServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
            System.out.println("WireMock server stopped");
        }
    }

    /**
     * Main method for manually running the mock server.
     */
    public static void main(String[] args) {
        startMockServer();

        // Keep the server running
        System.out.println("Press Ctrl+C to stop the server...");
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            stopMockServer();
        }
    }
}
