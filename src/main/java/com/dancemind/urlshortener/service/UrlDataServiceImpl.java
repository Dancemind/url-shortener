package com.dancemind.urlshortener.service;

import com.dancemind.urlshortener.entity.UrlData;
import com.dancemind.urlshortener.repository.UrlDataRepository;
import com.dancemind.urlshortener.service.exceptions.NoAvailableLettersException;
import com.dancemind.urlshortener.service.exceptions.ShortUrlNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Provides methods for {@link UrlData} management
 */
@Service
public class UrlDataServiceImpl implements UrlDataService {

    private static final String INITIAL_SHORT_URL = "aaaaaa";

    private static final char FIRST_SMALL_LETTER = 'a';
    private static final char LAST_SMALL_LETTER = 'z';
    private static final char FIRST_BIG_LETTER = 'A';
    private static final char LAST_BIG_LETTER = 'Z';

    public UrlDataRepository urlDataRepository;

    public UrlDataServiceImpl(UrlDataRepository urlDataRepository) {
        this.urlDataRepository = urlDataRepository;
    }

    /**
     * Gets data for all urls
     *
     * @return list of UrlData
     */
    @Override
    @Transactional(readOnly = true)
    public List<UrlData> findAllUrlsData() {
        return urlDataRepository.findAllByDeletedFalse();
    }

    /**
     * Creates short url for given long url
     *
     * @param urlData contains long url to remember
     *
     * @return UrlData which contains short url
     */
    @Override
    @Transactional
    public UrlData createUrlData(UrlData urlData) {
        urlData.setShortUrl(generateShortUrl());
        return urlDataRepository.save(urlData);
    }

    /**
     * Gets long url by short url
     *
     * @param shortUrl  short url
     *
     * @return  long url in UrlData object
     */
    @Override
    @Transactional(readOnly = true)
    public UrlData getUrlData(String shortUrl) {
        UrlData urlData = urlDataRepository.findByShortUrlAndDeletedFalse(shortUrl);

        if (urlData == null) {
            throw new ShortUrlNotFoundException("Specified short url is not found.");
        }

        return urlData;
    }

    /**
     * Generates string of 6 letters from 'a' to 'Z'
     *
     * @return generated string
     */
    private String generateShortUrl() {
        UrlData urlData = urlDataRepository.findFirstByDeletedFalseOrderByIdDesc();

        if (urlData == null) {
            return INITIAL_SHORT_URL;
        }

        StringBuilder shortUrl = new StringBuilder(urlData.getShortUrl());

        shortUrl = changeLetter(shortUrl, shortUrl.length() - 1);

        return shortUrl.toString();
    }

    /**
     * Changes letter to the next one.
     * For ex.
     *      aaaaaa to aaaaab,
     *      aazaaa to aaAaaa
     *      aaaaZa to aaabaa
     *      aazZZZ to aaAaaa
     *
     * @param letters   letters
     * @param position  change letter at position
     *
     * @return  fresh letters
     */
    private StringBuilder changeLetter(StringBuilder letters, int position) {
        if (position == -1) {
            throw new NoAvailableLettersException("All short urls are used. The letters are over.");
        }

        char letter = letters.charAt(position);
        if (letter == LAST_BIG_LETTER) {
            letters.setCharAt(position, FIRST_SMALL_LETTER);
            return changeLetter(letters, --position);
        } else if (letter == LAST_SMALL_LETTER) {
            letters.setCharAt(position, FIRST_BIG_LETTER);
            return letters;
        }

        letters.setCharAt(position, (char) ((byte) letters.charAt(position) + 1));
        return letters;
    }
}