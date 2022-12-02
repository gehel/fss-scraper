package ch.ledcom.signs.fss.scraper.query;

import java.util.List;

import ch.ledcom.signs.fss.scraper.Item;

public record QueryResponse(List<Item> items, int count) {
}
