package com.dancemind.urlshortener.controller;

import com.dancemind.urlshortener.entity.UrlData;
import com.dancemind.urlshortener.service.UrlDataService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/urls")
public class UrlDataController {

    public UrlDataService urlDataService;

    public UrlDataController(UrlDataService urlDataService) {
        this.urlDataService = urlDataService;
    }

    @ApiOperation(value = "Returns all urls data")
    @GetMapping
    public List<UrlData> getAllExpenseCategories() {
        return urlDataService.findAllUrlsData();
    }
}