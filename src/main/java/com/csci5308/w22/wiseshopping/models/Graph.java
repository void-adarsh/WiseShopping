package com.csci5308.w22.wiseshopping.models;

import com.csci5308.w22.wiseshopping.screens.merchantfunctionality.MerchantMenuScreen;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Elizabeth James
 */
public class Graph {
    private String title;
    private  String xaxisLabel ;
    private String yaxisLabel ;
    private static final float STROKESIZE = 2.0f;
    private static final int CHARTSIZE = 18;

    private static final Logger LOGGER = LoggerFactory.getLogger(Graph.class);


    public  Graph(String title, String xaxisLabel, String yaxisLabel){
        this.title = title;
        this.xaxisLabel = xaxisLabel;
        this.yaxisLabel =  yaxisLabel;
    }

    /**
     * This method creates a chart for a given dataset
     * @param dataset dataset
     * @param filePath path to store the chart
     * @param chartName name of chart
     * @return chart
     */
    public JFreeChart createChart(XYDataset dataset, String filePath, String chartName)  {

        // create a chart
        JFreeChart chart = ChartFactory.createXYLineChart(title,xaxisLabel,yaxisLabel,
                dataset,PlotOrientation.VERTICAL,true,true,false);

        // initialize a XY Plot
        XYPlot plot = chart.getXYPlot();

        // draw chart

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(STROKESIZE));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle(title,new Font("Serif", java.awt.Font.BOLD, CHARTSIZE)));

        try {
            int width = 450;
            int height = 400;
            ChartUtils.saveChartAsPNG(new File(filePath + chartName + ".png"), chart, width,height);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
        }

        return chart;
    }

}
/**
 * reference: https://www.jfree.org/jfreechart/
 * https://zetcode.com/java/jfreechart/
 */
