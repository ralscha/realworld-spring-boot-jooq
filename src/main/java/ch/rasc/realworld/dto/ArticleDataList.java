package ch.rasc.realworld.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArticleDataList {
	@JsonProperty("articles")
	private final List<Article> articles;
	@JsonProperty("articlesCount")
	private final int count;

	public ArticleDataList(List<Article> articles, int count) {
		this.articles = articles;
		this.count = count;
	}

	public List<Article> getArticles() {
		return this.articles;
	}

	public int getCount() {
		return this.count;
	}

}