package com.demi.module.charts;

import com.demi.service.ChartService;
import com.demi.service.impl.ChartServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart.Data;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author admin
 */
public class PieChart implements Initializable {

    @FXML
    private javafx.scene.chart.PieChart pieChart;

    private ChartService chartService = new ChartServiceImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        Map<String, Integer> map = chartService.bookTypeCount();
        Data[] datas = new Data[map.size()];
        int index = 0;
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> next = iterator.next();
            datas[index++] = new Data(next.getKey(), next.getValue());
        }

        ObservableList<javafx.scene.chart.PieChart.Data> pieChartData = FXCollections.observableArrayList(datas);
        pieChart.setData(pieChartData);
        pieChart.setClockwise(false);
    }
}
