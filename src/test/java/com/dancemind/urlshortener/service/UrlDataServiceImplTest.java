package com.dancemind.urlshortener.service;

import com.dancemind.urlshortener.TestsBaseClass;
import com.dancemind.urlshortener.entity.UrlData;
import com.dancemind.urlshortener.repository.UrlDataRepository;
import com.dancemind.urlshortener.service.exceptions.BusyShortUrlException;
import com.dancemind.urlshortener.service.exceptions.NoAvailableLettersException;
import com.dancemind.urlshortener.service.exceptions.ShortUrlNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlDataServiceImplTest extends TestsBaseClass {

    private static final String INITIAL_SHORT_URL = "aaaaaa";

    private static final String SHORT_URL = "aazZZZ";
    private static final String NEXT_SHORT_URL = "aaAaaa";
    private static final String LAST_SHORT_URL = "ZZZZZZ";
    private static final String EMPTY_SHORT_URL = "";

    private static final int NO_INVOCATIONS = 0;

    private static final String NON_EXISTENT_SHORT_URL = "aaa+";

    @Mock
    private UrlDataRepository urlDataRepository;

    private UrlDataService urlDataService;

    private final List<UrlData> urlDataList = new ArrayList<>();
    private UrlData urlData;

    @BeforeEach
    public void init() {
        urlDataService = new UrlDataServiceImpl(urlDataRepository);
        urlData = createUrlDataInstance();
        Collections.addAll(urlDataList, urlData, createUrlDataInstance());
    }

    @Test
    public void findAllUrlsData_Success() {
        when(urlDataRepository.findAllByDeletedFalseOrderByIdDesc()).thenReturn(urlDataList);

        List<UrlData> gotUrlDataList = urlDataService.findAllUrlsData();

        verify(urlDataRepository).findAllByDeletedFalseOrderByIdDesc();
        assertEquals(gotUrlDataList, urlDataList);
    }

    @Test
    public void createUrlData_Success() {
        urlData.setShortUrl(EMPTY_SHORT_URL);
        urlDataService.createUrlData(urlData);
        verify(urlDataRepository).save(urlData);
    }

    @Test
    public void createUrlData_CustomShortUrl_Success() {
        urlDataService.createUrlData(urlData);
        verify(urlDataRepository).save(urlData);
    }

    @Test
    public void createUrlData_Capture() {
        ArgumentCaptor<UrlData> captor = ArgumentCaptor.forClass(UrlData.class);

        urlDataService.createUrlData(urlData);

        verify(urlDataRepository).save(captor.capture());

        UrlData gotUrlData = captor.getValue();
        assertEquals(urlData.getComment(), gotUrlData.getComment());
    }

    @Test
    public void createUrlData_null_aaaaaa() {
        urlData.setShortUrl(EMPTY_SHORT_URL);
        urlDataService.createUrlData(urlData);
        assertEquals(INITIAL_SHORT_URL, urlData.getShortUrl());
    }

    @Test
    public void createUrlData_aazZZZ_aaAaaa() {
        urlData.setShortUrl(SHORT_URL);
        UrlData urlData2 = createUrlDataInstance();
        urlData2.setShortUrl(EMPTY_SHORT_URL);

        when(urlDataRepository.findFirstByDeletedFalseAndIsCustomFalseOrderByIdDesc()).thenReturn(urlData);

        urlDataService.createUrlData(urlData2);

        assertEquals(NEXT_SHORT_URL, urlData2.getShortUrl());
    }

    @Test
    public void createUrlData_LastShortUrl_Failed() {
        urlData.setShortUrl(LAST_SHORT_URL);
        UrlData urlData2 = createUrlDataInstance();
        urlData2.setShortUrl(EMPTY_SHORT_URL);

        when(urlDataRepository.findFirstByDeletedFalseAndIsCustomFalseOrderByIdDesc()).thenReturn(urlData);

        assertThatThrownBy(() -> urlDataService.createUrlData(urlData2))
                .isInstanceOf(NoAvailableLettersException.class);
        verify(urlDataRepository, times(NO_INVOCATIONS)).save(any());
    }

    @Test
    public void createUrlData_DuplicatedShortUrl_Failed() {
        UrlData urlData2 = createUrlDataInstance();
        urlData2.setShortUrl(urlData.getShortUrl());

        when(urlDataRepository.findByShortUrlAndDeletedFalse(urlData2.getShortUrl())).thenReturn(urlData);

        assertThatThrownBy(() -> urlDataService.createUrlData(urlData2))
                .isInstanceOf(BusyShortUrlException.class);
        verify(urlDataRepository, times(NO_INVOCATIONS)).save(any());
    }

    @Test
    public void getUrlData_Success() {
        when(urlDataRepository.findByShortUrlAndDeletedFalse(urlData.getShortUrl())).thenReturn(urlData);
        assertEquals(urlDataService.getUrlData(urlData.getShortUrl()), urlData);
    }

    @Test
    public void getUrlData_WrongShortUrl_Failed() {
        assertThatThrownBy(() -> urlDataService.getUrlData(NON_EXISTENT_SHORT_URL))
                .isInstanceOf(ShortUrlNotFoundException.class);
    }

    @Test
    public void getUrlData_DeletedUrlData_Failed() {
        urlData.setDeleted(true);

        when(urlDataRepository.findByShortUrlAndDeletedFalse(urlData.getShortUrl()))
                .thenThrow(ShortUrlNotFoundException.class);
        assertThatThrownBy(() -> urlDataService.getUrlData(urlData.getShortUrl()))
                .isInstanceOf(ShortUrlNotFoundException.class);
        verify(urlDataRepository).findByShortUrlAndDeletedFalse(urlData.getShortUrl());
    }
}