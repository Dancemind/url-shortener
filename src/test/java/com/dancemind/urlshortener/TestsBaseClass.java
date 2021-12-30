package com.dancemind.urlshortener;

import com.dancemind.urlshortener.entity.UrlData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;

public class TestsBaseClass {

    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm";
    private static final int STRING_LENGTH = 10;

    protected String convertObjectToJsonString(Object object) throws JsonProcessingException {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setDateFormat(new SimpleDateFormat(DATE_FORMAT))
                .writeValueAsString(object);
    }

    protected UrlData createUrlDataInstance() {
        return new UrlData(generateString(), generateString(), generateString());
    }

    protected String generateString() {
        return RandomStringUtils.randomAlphabetic(STRING_LENGTH);
    }
}