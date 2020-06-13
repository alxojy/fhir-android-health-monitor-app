package edu.monash.kmhc.chart;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.model.observation.ObservationType;

public class BarChartKMHC {

    private com.github.mikephil.charting.charts.BarChart barChart;
    private View view;

    public BarChartKMHC(View view, int id) {
        this.barChart = view.findViewById(id);
        this.view = view;
    }

    public void plotBarChart(HashMap<String, PatientModel> patientObservationHashMap) {
        ArrayList<String> patientName = new ArrayList<>();
        List<IBarDataSet> dataBars = new ArrayList<IBarDataSet>();
        AtomicInteger i = new AtomicInteger();
        ArrayList<Integer> colours = new ArrayList<Integer>(
                Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                        Color.CYAN, Color.MAGENTA, Color.GRAY, Color.BLACK
                ));

        patientObservationHashMap.forEach((patientId, patientModel) -> {
            if (patientModel.isObservationMonitored(ObservationType.CHOLESTEROL)) {
                float patientCholVal = Float.parseFloat(patientModel.getObservationReading(ObservationType.CHOLESTEROL).getValue());
                patientName.add(patientModel.getName());
                ArrayList<BarEntry> barEntries = new ArrayList<>();
                barEntries.add(new BarEntry(patientCholVal,0));
                BarDataSet dataset = new BarDataSet(barEntries,patientModel.getName());
                dataset.setColor(colours.get(i.intValue() % colours.size()));
                dataBars.add(dataset);
                i.getAndIncrement();
            }
        });

        BarData data = new BarData(Collections.singletonList("Total Cholesterol mg/dL"), dataBars);
        barChart.setData(data);
        barChart.setDragEnabled(true); // on by default
        barChart.setVisibleXRangeMinimum(patientName.size()%6);
        barChart.setVisibleXRangeMaximum(5);
        barChart.getLegend().setWordWrapEnabled(true);
        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }
}
