package com.csci5308.w22.wiseshopping.service.sales;

import com.csci5308.w22.wiseshopping.models.Sales;
import com.csci5308.w22.wiseshopping.repository.sales.SalesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

/**
 * @author Pavithra Gunasekaran
 */
@ExtendWith(MockitoExtension.class)
public class SalesServiceTests {
    @Mock
    SalesRepository mockedSalesRepository;

    @InjectMocks
    private SalesService salesService;

    private Sales sales;

    @BeforeEach

    public void setUp() {
        sales = new Sales("Wired Headphones");
    }

    @Test
    public void testGetProductLowestPriceAnalytics() {
        IllegalArgumentException productNullException=Assertions.assertThrows(IllegalArgumentException.class, () -> {
            salesService.getProductLowestPriceAnalytics(null);
        });
        Assertions.assertEquals("product cannot be null or empty or blank",productNullException.getMessage());
        IllegalArgumentException productEmptyException=Assertions.assertThrows(IllegalArgumentException.class, () -> {
            salesService.getProductLowestPriceAnalytics("");
        });
        Assertions.assertEquals("product cannot be null or empty or blank",productEmptyException.getMessage());
        IllegalArgumentException productBlankException=Assertions.assertThrows(IllegalArgumentException.class, () -> {
            salesService.getProductLowestPriceAnalytics(" ");
        });
        Assertions.assertEquals("product cannot be null or empty or blank",productBlankException.getMessage());
        IllegalArgumentException productMissingException=Assertions.assertThrows(IllegalArgumentException.class, () -> {
            salesService.getProductLowestPriceAnalytics("Samsung Mobile");
        });
        Assertions.assertEquals("the selected product does not exists",productMissingException.getMessage());

    }

}
