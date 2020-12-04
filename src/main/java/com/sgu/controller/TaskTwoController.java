package com.sgu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Stack;

import static java.lang.Math.pow;

@Controller
public class TaskTwoController {

    private static final String URL_FIRST_PART = "https://api.exchangeratesapi.io/history?start_at=";
    private static final String URL_SECOND_PART = "&end_at=";
    private static final String URL_THIRD_PART = "&symbols=USD";
    private static final Logger logger = LoggerFactory.getLogger(TaskTwoController.class);

    @GetMapping("/rate/{date}")
    public String getRate(@PathVariable(name = "date") String date, Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        model.addAttribute("localDate", localDate);
        RestTemplate template = new RestTemplate();
        String url = URL_FIRST_PART + date + URL_SECOND_PART + date + URL_THIRD_PART;
        logger.info("total url is " + url);
        String result = template.getForObject(url, String.class);
        model.addAttribute("res", result);
        return "rate";
    }

    @GetMapping(value = "/dispersion/{since}")
    public String getDispersion(@PathVariable("since") String since, Model model) {
        logger.info("start is " + since);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        LocalDate localDate = LocalDate.parse(since, formatter);
        logger.info("to model " + localDate);
        model.addAttribute("localDate", localDate);

        RestTemplate template = new RestTemplate();
        LocalDate currentDate = LocalDate.now();
        String url = URL_FIRST_PART + since + URL_SECOND_PART + currentDate + URL_THIRD_PART;
        String inputLine1 = template.getForObject(url, String.class);

        Stack<Double> tg = new Stack<>();
        int indexM = inputLine1.indexOf("USD");
        int brace = inputLine1.indexOf("}", indexM);

        StringBuilder usd = new StringBuilder();
        for (int i = indexM + 5; i < brace; i++) {
            usd.append(inputLine1.charAt(i));
        }
        double j = 0;
        double middleNum = 0;
        while (indexM > 0){
            StringBuilder num = new StringBuilder();
            for (int i = indexM + 5; i < brace; i++) {
                num.append(inputLine1.charAt(i));
            }
            Double f = Double.parseDouble(num.toString());
            f = pow(Math.tan(f), 7.5);
            tg.push(f);
            middleNum += f;
            j++;
            indexM = inputLine1.indexOf("USD", indexM + 1);
            brace = inputLine1.indexOf("}", brace + 1);
        }
        middleNum = middleNum / j;
        double dispersia = 0;
        while (tg.size() != 0){
            double y = tg.pop();
            y -= middleNum;
            dispersia += pow(y, 2);
        }
        dispersia = (1 / j) * (dispersia / j);
        model.addAttribute("tg", dispersia);
        return "dispersion";
    }

}
