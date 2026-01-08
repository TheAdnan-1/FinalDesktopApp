package org.example.theadnan.services;

import java.util.HashMap;
import java.util.Map;

public class JsonParser {

    public static Map<String, Double> parseRates(String json) {
        Map<String, Double> rates = new HashMap<>();

        String ratesPart = json.split("\"rates\":\\{")[1]
                .split("}")[0];

        String[] entries = ratesPart.split(",");

        for (String entry : entries) {
            String[] pair = entry.split(":");
            String currency = pair[0].replace("\"", "");
            double value = Double.parseDouble(pair[1]);
            rates.put(currency, value);
        }

        return rates;
    }
}
