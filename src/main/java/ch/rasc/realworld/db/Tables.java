/*
 * This file is generated by jOOQ.
 */
package ch.rasc.realworld.db;

import ch.rasc.realworld.db.tables.AppUser;
import ch.rasc.realworld.db.tables.Article;
import ch.rasc.realworld.db.tables.ArticleFavorite;
import ch.rasc.realworld.db.tables.ArticleTag;
import ch.rasc.realworld.db.tables.Comment;
import ch.rasc.realworld.db.tables.Follow;
import ch.rasc.realworld.db.tables.Tag;

/**
 * Convenience access to all tables in the default schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

	/**
	 * The table <code>APP_USER</code>.
	 */
	public static final AppUser APP_USER = AppUser.APP_USER;

	/**
	 * The table <code>ARTICLE</code>.
	 */
	public static final Article ARTICLE = Article.ARTICLE;

	/**
	 * The table <code>ARTICLE_FAVORITE</code>.
	 */
	public static final ArticleFavorite ARTICLE_FAVORITE = ArticleFavorite.ARTICLE_FAVORITE;

	/**
	 * The table <code>ARTICLE_TAG</code>.
	 */
	public static final ArticleTag ARTICLE_TAG = ArticleTag.ARTICLE_TAG;

	/**
	 * The table <code>COMMENT</code>.
	 */
	public static final Comment COMMENT = Comment.COMMENT;

	/**
	 * The table <code>FOLLOW</code>.
	 */
	public static final Follow FOLLOW = Follow.FOLLOW;

	/**
	 * The table <code>TAG</code>.
	 */
	public static final Tag TAG = Tag.TAG;
}
