package ch.ledcom.signs.fss.scraper;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Item(
    String uid,
    String name,
    @JsonProperty("kategorie")
    String category,
    @JsonProperty("sprache")
    String language,
    String link){ }
