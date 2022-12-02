package ch.ledcom.signs.fss.scraper;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public record Sign(
        String word,
        String mainVideo,
        String periphrase,
        String definition,
        String category,
        String example,
        String exampleVideo,
        Optional<String> image,
        Optional<String> photo) {

    public static Sign from(Document doc) {
        String word = doc.select("h1").get(0).text();
        String mainVideo = doc.select("#video-main source").get(0).attr("abs:src");
        String periphrase = doc.select("h2:contains(Périphrase / Synonyme)+p").get(0).text();
        String definition = doc.select("h2:contains(Définition)+p").get(0).text();
        String category = doc.select(".maincategories").get(0).text();
        String example = doc.select("h2:contains(Exemple)+p").get(0).text();
        String exampleVideo = doc.select("#video-example source").get(0).attr("abs:src");

        Elements imageElement = doc.select("h2:contains(Illustration)+img");
        Optional<String> image = optionalAttr(imageElement);

        Elements photoElement = doc.select("h2:contains(Photo Signe)+img");
        Optional<String> photo = optionalAttr(photoElement);

        return new Sign(word, mainVideo, periphrase, definition,  category, example, exampleVideo, image, photo);
    }

    @NotNull
    private static Optional<String> optionalAttr(Elements imageElement) {
        if (imageElement.size() > 0) {
            return Optional.of(imageElement.get(0).attr("abs:src"));
        } else {
            return Optional.empty();
        }
    }
}
