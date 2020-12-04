package com.sgu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class TaskTwoController {

    private static final Logger logger = LoggerFactory.getLogger(TaskTwoController.class);

    @GetMapping("/rate/{date}")
    public String getRate(@PathVariable(name = "date") String date, Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        model.addAttribute("localDate", localDate);
        return "rate";
    }

    @GetMapping(value = "/dispersion/{since}")
    public String getDispersion(@PathVariable("since") String since, Model model) {
        logger.info("start " + since);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        LocalDate date = LocalDate.parse(since, formatter);
        logger.info("to model " + date);
        model.addAttribute("localDate", date);
        return "dispersion";
    }

}
