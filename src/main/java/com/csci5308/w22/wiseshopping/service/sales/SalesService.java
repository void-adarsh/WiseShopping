package com.csci5308.w22.wiseshopping.service.sales;

import com.csci5308.w22.wiseshopping.models.Graph;
import com.csci5308.w22.wiseshopping.models.Sales;
import com.csci5308.w22.wiseshopping.repository.sales.SalesRepository;
import com.csci5308.w22.wiseshopping.utils.Util;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Pavithra Gunasekaran
 */
@Service
public class SalesService {
    @Autowired
    SalesRepository salesRepository;

    private static Multimap<Integer, List<Object>> PriceHistory = ArrayListMultimap.create();
    private static HashMap<Integer, Double> localMinimum = new HashMap<Integer, Double>();

    /**
     * Analytics to find the lowest price of a product over a year using
     * Local minimum concept
     * @param product
     * @return localMinimum
     * @throws SQLException
     * @author: Pavithra Gunasekaran
     */

    @Transactional
    public HashMap<Integer, Double> getProductLowestPriceAnalytics(String product) throws SQLException {
        if (!Util.isValidString(product)) {
            throw new IllegalArgumentException("product cannot be null or empty or blank");
        }
        if(salesRepository.findByProduct(product).isEmpty()){
            throw new IllegalArgumentException("the selected product does not exists");
        }


        List<Sales> sales = salesRepository.findByProduct(product);
        localMinimum = getlocalMinimumByMonth(sales);
        return localMinimum;
    }

    /**
     * This method computes the local minimum value of the price by every month for the products
     * @author Pavithra Gunasekaran
     * @param sales
     * @return localMinimum
     */
    @Transactional
    public HashMap<Integer, Double> getlocalMinimumByMonth(List<Sales> sales){
        int j=0,i=0;
        int prevMonth = 1;
        double  localMin = 1000;
        double currentLocalMin = 0;
        while (j<sales.size()) {
            Date date = sales.get(j).getOrderDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int monthFromResultSet = calendar.get(Calendar.MONTH)+1;

            int weekFromResultSet = calendar.get(Calendar.WEEK_OF_MONTH);
            double averagePriceFromResultSet = sales.get(j).getPriceEach();
            List<Object> priceByWeek = new ArrayList<>();
            priceByWeek.add(weekFromResultSet);
            priceByWeek.add(averagePriceFromResultSet);
            PriceHistory.put(monthFromResultSet, priceByWeek);
            for (Map.Entry<Integer, List<Object>> entry : PriceHistory.entries()) {
                int month = entry.getKey();

                if (prevMonth == month) {
                    if (i == 0) {

                        currentLocalMin = (double) entry.getValue().listIterator(1).next();
                        if (localMin > currentLocalMin) {
                            localMin = currentLocalMin;
                            prevMonth = month;
                        }
                        i++;
                    } else {
                        currentLocalMin = (double) entry.getValue().listIterator(1).next();
                        if (localMin > currentLocalMin) {
                            localMin = currentLocalMin;
                            prevMonth = month;
                        } else {
                            prevMonth = month;
                        }
                        i++;
                    }
                }
                if (prevMonth != month) {
                    //currentLocalMin = entry.getValue().listIterator(1).next();
                    localMinimum.put(prevMonth, localMin);
                    prevMonth = month;
                    i = 0;
                    currentLocalMin = (double) entry.getValue().listIterator(1).next();
                    localMin = currentLocalMin;
                }


            }
            j++;
        }
        localMinimum.put(prevMonth, localMin);
        return localMinimum;
    }

    /**
     * this method creates line chart for the selected product to get the price based on every month
     * @author Pavithra Gunasekaran
     * @return true, if created; else false
     */
    public boolean generateChartForPriceAnalytics(String productName) throws SQLException {
        boolean success = false;
        HashMap<Integer, Double> priceAnalytics = getProductLowestPriceAnalytics(productName);
        success = generatePriceAnalyticsChart(priceAnalytics,productName);
        return success;
    }
    /**
     * this method generates chart for the lowest price of a product in every month
     * @author Pavithra Gunasekaran
     * @param priceAnalytics map of month -> predicted price
     * @param productName name of product
     * @return true, if created; else false
     */
    private boolean generatePriceAnalyticsChart(HashMap<Integer, Double> priceAnalytics, String productName) {
        boolean success = false;
        List<Integer> months = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
        XYSeries series = new XYSeries("price");
        for (Integer month : months) {
            if (priceAnalytics.containsKey(month)) {
                // add 1 here as jan is stored as 0
                series.add(month, priceAnalytics.get(month));
            }
            else {
                //series.add(month+1, 0);
            }

        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        Graph priceAnalyticsGraph = new Graph("Product price analytics","Month","Price");
        JFreeChart chart =  priceAnalyticsGraph.createChart(dataset,"./productPriceAnalyticsCharts/",productName);
        return chart!=null;
    }


    /**
     *  this method gets data to create line chart for all products in the folder productDemandTrend
     * @return true, if created, else false
     * @author : Elizabeth James
     *
     */
    private Map<String, Map<Integer, Integer>> getDataForProductDemandTrend() {

        boolean success = false;
        Iterable<Sales> salesList = salesRepository.findAll();
        Map<String, Map<Integer, Integer>> productDemand = new HashMap<>();

        for (Sales sales : salesList) {
            if (productDemand.containsKey(sales.getProduct())) {
                Map<Integer, Integer> monthlyCount = productDemand.get(sales.getProduct());
                int month = sales.getOrderDate().getMonth();
                if (monthlyCount.containsKey(month)) {
                    monthlyCount.put(month, monthlyCount.get(month) + sales.getQuantityOrdered());
                } else {
                    monthlyCount.put(month, sales.getQuantityOrdered());
                }
            } else {
                Map<Integer, Integer> monthlyCount = new HashMap<>();
                monthlyCount.put(sales.getOrderDate().getMonth(), sales.getQuantityOrdered());
                productDemand.put(sales.getProduct(), monthlyCount);
            }

        }
        return productDemand;

    }

    /**
     * this method creates line chart for all products in the folder productDemandTrend
     * @return true, if created; else false
     * @author : Elizabeth James
     */
    public boolean generateChartForAllProducts() {
        boolean success = false;
        Map<String, Map<Integer, Integer>> productDemand = getDataForProductDemandTrend();
        for (Map.Entry<String, Map<Integer, Integer>> entry : productDemand.entrySet()) {
            success = generateChart(entry.getValue(), entry.getKey());
        }
        return success;
    }

    /**
     * this method generates chart for the productName that is passed
     * @param productCount map of month -> quantity sold
     * @param productName name of product
     * @return true, if created; else false
     * @author : Elizabeth James
     */
    private boolean generateChart(Map<Integer, Integer> productCount, String productName) {
        boolean success = false;
        List<Integer> months = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
        XYSeries series = new XYSeries("items sold");
        for (Integer month : months) {
            if (productCount.containsKey(month)) {
                // add 1 here as jan is stored as 0
                series.add(month+1, productCount.get(month));
            }
            else {
                series.add(month+1, 0);
            }

        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        Graph graph = new Graph("Product demand trend","Month","items sold");
        JFreeChart chart =  graph.createChart(dataset, "./productDemandCharts/", productName);
        return chart!=null;
    }

}
