package ch.rasc.realworld.controller;

import static ch.rasc.realworld.db.tables.Tag.TAG;

import java.util.Map;
import java.util.Set;

import org.jooq.DSLContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagsController {

	private final DSLContext dsl;

	public TagsController(DSLContext dsl) {
		this.dsl = dsl;
	}

	@GetMapping("/tags")
	public Map<String, Set<String>> getTags() {
		return Map.of("tags", this.dsl.select(TAG.NAME).from(TAG).fetchSet(TAG.NAME));
	}

}
