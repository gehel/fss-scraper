package ch.ledcom.signs.fss.scraper.misc;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class Urls {

    private static final String QUERY_URL = "/fr/index.php?eID=signsuisse_search&sword=%s&lang=fr&category=pleaseselect&curlang=fr";
    private final String baseUrl;

    public Urls(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public URL getPage(String url) throws MalformedURLException {
        return new URL(baseUrl + url);
    }

    public URL queryUrl(String query) throws MalformedURLException {
        return getPage(String.format(Locale.ROOT, QUERY_URL, query));
    }
}
