package com.sgu.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.tan;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Page {

    private static final Logger logger = LoggerFactory.getLogger(Page.class);

    private HashMap<String, HashMap<String, Double>> rates;

    public HashMap<String, HashMap<String, Double>> getRates() {
        return rates;
    }

    public void setRates(HashMap<String, HashMap<String, Double>> rates) {
        this.rates = rates;
    }

    public List<Double> getCots(){
        List<Double> result = new ArrayList<>();
        for (Map.Entry<String, HashMap<String, Double>> entry : rates.entrySet()) {
            for (Map.Entry<String, Double> cots : entry.getValue().entrySet()) {
                result.add(cots.getValue());
            }
        }
        return result;
    }

    public double getTg(){
        Set<Double> cotsSet = new LinkedHashSet<>();
        int c = 0;
        for (Map.Entry<String, HashMap<String, Double>> entry : rates.entrySet()) {
            for (Map.Entry<String, Double> cots : entry.getValue().entrySet()) {
                cotsSet.add((double) round(pow(tan(cots.getValue()), 7.5)));
                c++;
            }
        }

        logger.info("Set " + cotsSet);
        double mathExp = 0;
        double squareMathExp = 0;
        Double[] vars = new Double[cotsSet.size()];
        int iterator = 0;
        for (Double num : cotsSet) {
            vars[iterator] = num;
            ++iterator;
        }
        for(int i = 0; i < cotsSet.size(); ++i){
            int elementCount = 0;
            for (Map.Entry<String, HashMap<String, Double>> entry : rates.entrySet()) {
                for (Map.Entry<String, Double> cots : entry.getValue().entrySet()) {
                    if (pow(tan(cots.getValue()), 7.5) - vars[i] < 0.1){
                        elementCount++;
                    }
                }
                //logger.info("element " + elementCount);
                double p = elementCount * 1.0 / c;
                mathExp += vars[i] * vars[i] * p;
                squareMathExp += vars[i] * p;
            }
        }
        return mathExp - squareMathExp * squareMathExp;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, HashMap<String, Double>> entry : rates.entrySet()){
            stringBuilder.append("date").append(' ').append(entry.getKey()).append(' ').append('\n');
            for (Map.Entry<String, Double> cots: entry.getValue().entrySet()) {
                stringBuilder.append("Cots").append(' ').
                            append(cots.getKey()).append(' ').
                            append(cots.getValue()).append(' ').append('\n');
            }
        }
        return stringBuilder.toString();
    }

}
