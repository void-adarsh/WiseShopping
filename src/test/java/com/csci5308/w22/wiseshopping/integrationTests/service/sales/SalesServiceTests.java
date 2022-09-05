package com.csci5308.w22.wiseshopping.integrationTests.service.sales;

import com.csci5308.w22.wiseshopping.models.Sales;
import com.csci5308.w22.wiseshopping.service.sales.SalesService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.SQLException;

/**
 * @author Pavithra Gunasekaran
 */
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SalesServiceTests {
    @Autowired
    private SalesService salesService;

    private Sales sales;

    @BeforeEach
    public void setUp(){
        sales = new Sales("Wired Headphones");
    }


    /**
     * Integration test for lowest price analytics for a product
     * @author Pavithra Gunasekaran
     * @throws SQLException
     */

    @Test
    public void testGetProductLowestPriceAnalytics() throws SQLException {
       // Multimap<Integer, List<Double>> actualSalesData = salesService.getProductLowestPriceAnalytics("Wired Headphones");
        boolean success = salesService.generateChartForPriceAnalytics("Wired Headphones");
        Assertions.assertTrue(success);

    }

    /**
     * Integration test for product demand trend analytics
     * @author Pavithra Gunasekaran
     */
    @Test
    public void testProductDemandTrend(){
        Assertions.assertTrue(salesService.generateChartForAllProducts());
    }
}
