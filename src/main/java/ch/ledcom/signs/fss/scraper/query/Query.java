package ch.ledcom.signs.fss.scraper.query;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.ledcom.signs.fss.scraper.misc.Urls;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Slf4j
public class Query {

    private final OkHttpClient httpClient;
    private final Urls urls;
    private final ObjectMapper mapper;

    public Query(Urls urls, OkHttpClient httpClient, ObjectMapper mapper) {
        this.httpClient = httpClient;
        this.urls = urls;
        this.mapper = mapper;
    }

    public QueryResponse query(String query) throws IOException {
        log.debug("Querying: {}.", query);
        URL url = urls.queryUrl(query);

        Response response = httpClient.newCall(
                new Request.Builder()
                        .url(url)
                        .build()
        ).execute();

        if (!response.isSuccessful()) throw new IOException("Error retrieving " + url + ": HTTP/" + response.code());

        ResponseBody body = response.body();
        if (body == null) throw new IOException("Null body on " + url);

        QueryResponse queryResponse = mapper.readValue(body.byteStream(), QueryResponse.class);
        log.debug("Found {} entries.", queryResponse.count());
        return queryResponse;
    }
}
