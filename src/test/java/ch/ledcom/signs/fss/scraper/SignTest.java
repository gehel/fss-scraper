package ch.ledcom.signs.fss.scraper;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

public class SignTest {

    @Test
    void parsePage() throws IOException {
        InputStream in = SignTest.class.getClassLoader().getResourceAsStream("ch/ledcom/signs/fss/scraper/Aarau.html");
        Document doc = Jsoup.parse(in, UTF_8.toString(), "https://signsuisse.sgb-fss.ch");
        Sign sign = Sign.from(doc);
        assertThat(sign.word()).isEqualTo("AARAU");
        assertThat(sign.mainVideo()).isEqualTo("https://signsuisse.sgb-fss.ch/fileadmin/signsuisse_ressources/user_upload/206996.mp4");
        assertThat(sign.periphrase()).isEqualTo("Ville, Suisse alémanique.");
        assertThat(sign.definition()).isEqualTo("C'est une commune suisse, capitale du canton d'Argovie, et chef-lieu du district du même nom.");
        assertThat(sign.category()).isEqualTo("Ville");
        assertThat(sign.example()).isEqualTo("Le plus grand employeur de la ville d'Aarau est l'administration cantonale.");
        assertThat(sign.exampleVideo()).isEqualTo("https://signsuisse.sgb-fss.ch/fileadmin/signsuisse_ressources/user_upload/206996_B.mp4");
    }
}
