package main.java.base;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import main.java.APIUtilies.MockServerSetup;

/**
 * Base class to handle setup and teardown for tests.
 */
public class BaseTest {

    /**
     * Start the mock server before running the test cases.
     */
    @BeforeClass
    public void setup() {
        MockServerSetup.startMockServer();
    }

    /**
     * Stop the mock server after completing the test cases.
     */
    @AfterClass
    public void teardown() {
        MockServerSetup.stopMockServer();
    }
}
