package com.dancemind.urlshortener.integration;

import com.dancemind.urlshortener.TestsBaseClass;
import com.dancemind.urlshortener.UrlShortenerApplication;
import com.dancemind.urlshortener.entity.UrlData;
import com.dancemind.urlshortener.repository.UrlDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {UrlShortenerApplication.class})
@AutoConfigureMockMvc
@Transactional
public class UrlDataTest extends TestsBaseClass {

    private static final String CREATE_URL = "/urls";
    private static final String GET_ALL_URLS_DATA_URL = "/urls";
    private static final String GET_URL_DATA_URL = "/{shortUrl}";

    private static final String SHORT_URL = "aaaaaa";
    private static final String SHORT_URL_FOR_CREATE = "aaaaab";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UrlDataRepository urlDataRepository;

    private UrlData urlData;
    private long nextExpectedId;

    @BeforeEach
    public void init() {
        urlDataRepository.deleteAll();
        urlDataRepository.findAll();

        urlData = createUrlDataInstance();
        urlData.setShortUrl(SHORT_URL);
        urlDataRepository.save(urlData);

        nextExpectedId = urlData.getId() + 1;
    }

    @Test
    public void createUrlData() throws Exception {
        UrlData urlDataToCreate = createUrlDataInstance();
        urlDataToCreate.setShortUrl(SHORT_URL_FOR_CREATE);

        mockMvc.perform(post(CREATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(urlDataToCreate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(convertObjectToJsonString(urlDataRepository.getById(nextExpectedId))));
    }

    @Test
    public void findAllUrlsData() throws Exception {
        UrlData urlData2 = createUrlDataInstance();
        urlDataRepository.save(urlData2);

        mockMvc.perform(get(GET_ALL_URLS_DATA_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(convertObjectToJsonString(
                        urlDataRepository.findAllById(List.of(urlData.getId(), urlData2.getId())))));
    }

    @Test
    public void getUrlData() throws Exception {
        mockMvc.perform(get(GET_URL_DATA_URL, SHORT_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(convertObjectToJsonString(urlDataRepository.getById(urlData.getId()))));
    }
}