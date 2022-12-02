package ch.ledcom.signs.fss.scraper;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import ch.ledcom.signs.fss.scraper.io.Dumper;
import ch.ledcom.signs.fss.scraper.misc.Urls;
import ch.ledcom.signs.fss.scraper.query.Query;
import ch.ledcom.signs.fss.scraper.query.QueryResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
public class Main {

    private static final String BASE_URL = "https://signsuisse.sgb-fss.ch";
    private final Path basePath;

    private final Query query;

    private final Urls urls;

    private final Dumper dumper;

    public Main() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        OkHttpClient httpClient = new OkHttpClient();
        basePath = Paths.get("target/downloads");
        urls = new Urls(BASE_URL);
        query = new Query(urls, httpClient, mapper);
        dumper = new Dumper(httpClient, mapper);
    }

    public static void main(String[] args) throws IOException {
        new Main().scrape();
    }

    private void scrape() throws IOException {
        log.debug("Creating download dir: {}", basePath.toAbsolutePath());
        Files.createDirectories(basePath);

        String q = "bonj";
        QueryResponse queryResponse = query.query(q);

        dumper.dump(queryResponse, basePath.resolve("query-" + q + ".json"));

        for (Item item : queryResponse.items()) {
            process(item);
        }
    }

    private void process(Item item) throws IOException {
        log.debug("Downloading sign {}.", item.name());
        Document document = Jsoup.parse(urls.getPage(item.link()), (int) Duration.of(10, SECONDS).toMillis());
        Sign sign = Sign.from(document);

        Path signDir = basePath.resolve(sign.word());
        Files.createDirectories(signDir);

        dumper.dump(document, signDir.resolve(sign.word() + ".html"));

        dumper.dump(sign, signDir.resolve(sign.word() + ".json"));
        dumper.dump(new URL(sign.mainVideo()), signDir.resolve(sign.word() + "-main-video.mp4"));
        dumper.dump(new URL(sign.exampleVideo()), signDir.resolve(sign.word() + "-example-video.mp4"));
        if (sign.image().isPresent())
            dumper.dump(new URL(sign.image().get()), signDir.resolve(sign.word() + "-image.jpg"));
        if (sign.photo().isPresent())
            dumper.dump(new URL(sign.photo().get()), signDir.resolve(sign.word() + "-photo.jpg"));
    }

}
