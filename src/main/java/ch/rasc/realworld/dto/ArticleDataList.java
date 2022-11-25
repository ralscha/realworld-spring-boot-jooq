package ch.rasc.realworld.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ArticleDataList(@JsonProperty("articles") List<Article> articles,
		@JsonProperty("articlesCount") int count) {
}