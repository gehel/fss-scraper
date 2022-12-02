package ch.ledcom.signs.fss.scraper.io;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jsoup.nodes.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Dumper {

    private final OkHttpClient httpClient;
    private final ObjectMapper mapper;

    public Dumper(OkHttpClient httpClient, ObjectMapper mapper) {
        this.httpClient = httpClient;
        this.mapper = mapper;
    }

    public void dump(InputStream in, Path path) throws IOException {
        if (Files.exists(path)) return;
        Files.createFile(path);
        OutputStream out = Files.newOutputStream(path);
        ByteStreams.copy(in, out);
    }

    public void dump(URL url, Path path) throws IOException {
        if (Files.exists(path)) return;
        Response response = httpClient.newCall(
                new Request.Builder()
                        .url(url)
                        .build()
        ).execute();

        if (!response.isSuccessful()) throw new IOException("Error retrieving " + url + ": HTTP/" + response.code());

        ResponseBody body = response.body();
        if (body == null) throw new IOException("Null body on " + url);

        dump(body.byteStream(), path);
    }

    public void dump(Document document, Path path) throws IOException {
        if (Files.exists(path)) return;
        com.google.common.io.Files.write(document.outerHtml().getBytes(UTF_8), path.toFile());
    }

    public void dump(Object obj, Path path) throws IOException {
        if (Files.exists(path)) return;
        Files.createFile(path);
        mapper.writeValue(path.toFile(), obj);
    }
}
