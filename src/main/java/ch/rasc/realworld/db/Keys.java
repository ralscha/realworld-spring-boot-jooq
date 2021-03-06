/*
 * This file is generated by jOOQ.
 */
package ch.rasc.realworld.db;

import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;

import ch.rasc.realworld.db.tables.AppUser;
import ch.rasc.realworld.db.tables.Article;
import ch.rasc.realworld.db.tables.ArticleFavorite;
import ch.rasc.realworld.db.tables.ArticleTag;
import ch.rasc.realworld.db.tables.Comment;
import ch.rasc.realworld.db.tables.Follow;
import ch.rasc.realworld.db.tables.Tag;
import ch.rasc.realworld.db.tables.records.AppUserRecord;
import ch.rasc.realworld.db.tables.records.ArticleFavoriteRecord;
import ch.rasc.realworld.db.tables.records.ArticleRecord;
import ch.rasc.realworld.db.tables.records.ArticleTagRecord;
import ch.rasc.realworld.db.tables.records.CommentRecord;
import ch.rasc.realworld.db.tables.records.FollowRecord;
import ch.rasc.realworld.db.tables.records.TagRecord;

/**
 * A class modelling foreign key relationships and constraints of tables of the
 * <code></code> schema.
 */
@Generated(value = { "http://www.jooq.org", "jOOQ version:3.11.11" },
		comments = "This class is generated by jOOQ")
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

	// -------------------------------------------------------------------------
	// IDENTITY definitions
	// -------------------------------------------------------------------------

	public static final Identity<AppUserRecord, Long> IDENTITY_APP_USER = Identities0.IDENTITY_APP_USER;
	public static final Identity<ArticleRecord, Long> IDENTITY_ARTICLE = Identities0.IDENTITY_ARTICLE;
	public static final Identity<CommentRecord, Long> IDENTITY_COMMENT = Identities0.IDENTITY_COMMENT;
	public static final Identity<TagRecord, Long> IDENTITY_TAG = Identities0.IDENTITY_TAG;

	// -------------------------------------------------------------------------
	// UNIQUE and PRIMARY KEY definitions
	// -------------------------------------------------------------------------

	public static final UniqueKey<AppUserRecord> CONSTRAINT_7 = UniqueKeys0.CONSTRAINT_7;
	public static final UniqueKey<AppUserRecord> CONSTRAINT_76 = UniqueKeys0.CONSTRAINT_76;
	public static final UniqueKey<AppUserRecord> CONSTRAINT_760 = UniqueKeys0.CONSTRAINT_760;
	public static final UniqueKey<ArticleRecord> CONSTRAINT_F = UniqueKeys0.CONSTRAINT_F;
	public static final UniqueKey<ArticleRecord> CONSTRAINT_FF = UniqueKeys0.CONSTRAINT_FF;
	public static final UniqueKey<ArticleFavoriteRecord> CONSTRAINT_B = UniqueKeys0.CONSTRAINT_B;
	public static final UniqueKey<ArticleTagRecord> CONSTRAINT_B1 = UniqueKeys0.CONSTRAINT_B1;
	public static final UniqueKey<CommentRecord> CONSTRAINT_6 = UniqueKeys0.CONSTRAINT_6;
	public static final UniqueKey<FollowRecord> CONSTRAINT_7B = UniqueKeys0.CONSTRAINT_7B;
	public static final UniqueKey<TagRecord> CONSTRAINT_1 = UniqueKeys0.CONSTRAINT_1;

	// -------------------------------------------------------------------------
	// FOREIGN KEY definitions
	// -------------------------------------------------------------------------

	public static final ForeignKey<ArticleRecord, AppUserRecord> CONSTRAINT_FF2 = ForeignKeys0.CONSTRAINT_FF2;
	public static final ForeignKey<ArticleFavoriteRecord, ArticleRecord> CONSTRAINT_B0 = ForeignKeys0.CONSTRAINT_B0;
	public static final ForeignKey<ArticleFavoriteRecord, AppUserRecord> CONSTRAINT_B05 = ForeignKeys0.CONSTRAINT_B05;
	public static final ForeignKey<ArticleTagRecord, ArticleRecord> CONSTRAINT_B12 = ForeignKeys0.CONSTRAINT_B12;
	public static final ForeignKey<ArticleTagRecord, TagRecord> CONSTRAINT_B12C = ForeignKeys0.CONSTRAINT_B12C;
	public static final ForeignKey<CommentRecord, ArticleRecord> CONSTRAINT_637 = ForeignKeys0.CONSTRAINT_637;
	public static final ForeignKey<CommentRecord, AppUserRecord> CONSTRAINT_63 = ForeignKeys0.CONSTRAINT_63;
	public static final ForeignKey<FollowRecord, AppUserRecord> CONSTRAINT_7BF = ForeignKeys0.CONSTRAINT_7BF;
	public static final ForeignKey<FollowRecord, AppUserRecord> CONSTRAINT_7BF0 = ForeignKeys0.CONSTRAINT_7BF0;

	// -------------------------------------------------------------------------
	// [#1459] distribute members to avoid static initialisers > 64kb
	// -------------------------------------------------------------------------

	private static class Identities0 {
		public static Identity<AppUserRecord, Long> IDENTITY_APP_USER = Internal
				.createIdentity(AppUser.APP_USER, AppUser.APP_USER.ID);
		public static Identity<ArticleRecord, Long> IDENTITY_ARTICLE = Internal
				.createIdentity(Article.ARTICLE, Article.ARTICLE.ID);
		public static Identity<CommentRecord, Long> IDENTITY_COMMENT = Internal
				.createIdentity(Comment.COMMENT, Comment.COMMENT.ID);
		public static Identity<TagRecord, Long> IDENTITY_TAG = Internal
				.createIdentity(Tag.TAG, Tag.TAG.ID);
	}

	private static class UniqueKeys0 {
		public static final UniqueKey<AppUserRecord> CONSTRAINT_7 = Internal
				.createUniqueKey(AppUser.APP_USER, "CONSTRAINT_7", AppUser.APP_USER.ID);
		public static final UniqueKey<AppUserRecord> CONSTRAINT_76 = Internal
				.createUniqueKey(AppUser.APP_USER, "CONSTRAINT_76",
						AppUser.APP_USER.USERNAME);
		public static final UniqueKey<AppUserRecord> CONSTRAINT_760 = Internal
				.createUniqueKey(AppUser.APP_USER, "CONSTRAINT_760",
						AppUser.APP_USER.EMAIL);
		public static final UniqueKey<ArticleRecord> CONSTRAINT_F = Internal
				.createUniqueKey(Article.ARTICLE, "CONSTRAINT_F", Article.ARTICLE.ID);
		public static final UniqueKey<ArticleRecord> CONSTRAINT_FF = Internal
				.createUniqueKey(Article.ARTICLE, "CONSTRAINT_FF", Article.ARTICLE.SLUG);
		public static final UniqueKey<ArticleFavoriteRecord> CONSTRAINT_B = Internal
				.createUniqueKey(ArticleFavorite.ARTICLE_FAVORITE, "CONSTRAINT_B",
						ArticleFavorite.ARTICLE_FAVORITE.ARTICLE_ID,
						ArticleFavorite.ARTICLE_FAVORITE.USER_ID);
		public static final UniqueKey<ArticleTagRecord> CONSTRAINT_B1 = Internal
				.createUniqueKey(ArticleTag.ARTICLE_TAG, "CONSTRAINT_B1",
						ArticleTag.ARTICLE_TAG.ARTICLE_ID, ArticleTag.ARTICLE_TAG.TAG_ID);
		public static final UniqueKey<CommentRecord> CONSTRAINT_6 = Internal
				.createUniqueKey(Comment.COMMENT, "CONSTRAINT_6", Comment.COMMENT.ID);
		public static final UniqueKey<FollowRecord> CONSTRAINT_7B = Internal
				.createUniqueKey(Follow.FOLLOW, "CONSTRAINT_7B", Follow.FOLLOW.USER_ID,
						Follow.FOLLOW.FOLLOW_ID);
		public static final UniqueKey<TagRecord> CONSTRAINT_1 = Internal
				.createUniqueKey(Tag.TAG, "CONSTRAINT_1", Tag.TAG.ID);
	}

	private static class ForeignKeys0 {
		public static final ForeignKey<ArticleRecord, AppUserRecord> CONSTRAINT_FF2 = Internal
				.createForeignKey(ch.rasc.realworld.db.Keys.CONSTRAINT_7, Article.ARTICLE,
						"CONSTRAINT_FF2", Article.ARTICLE.USER_ID);
		public static final ForeignKey<ArticleFavoriteRecord, ArticleRecord> CONSTRAINT_B0 = Internal
				.createForeignKey(ch.rasc.realworld.db.Keys.CONSTRAINT_F,
						ArticleFavorite.ARTICLE_FAVORITE, "CONSTRAINT_B0",
						ArticleFavorite.ARTICLE_FAVORITE.ARTICLE_ID);
		public static final ForeignKey<ArticleFavoriteRecord, AppUserRecord> CONSTRAINT_B05 = Internal
				.createForeignKey(ch.rasc.realworld.db.Keys.CONSTRAINT_7,
						ArticleFavorite.ARTICLE_FAVORITE, "CONSTRAINT_B05",
						ArticleFavorite.ARTICLE_FAVORITE.USER_ID);
		public static final ForeignKey<ArticleTagRecord, ArticleRecord> CONSTRAINT_B12 = Internal
				.createForeignKey(ch.rasc.realworld.db.Keys.CONSTRAINT_F,
						ArticleTag.ARTICLE_TAG, "CONSTRAINT_B12",
						ArticleTag.ARTICLE_TAG.ARTICLE_ID);
		public static final ForeignKey<ArticleTagRecord, TagRecord> CONSTRAINT_B12C = Internal
				.createForeignKey(ch.rasc.realworld.db.Keys.CONSTRAINT_1,
						ArticleTag.ARTICLE_TAG, "CONSTRAINT_B12C",
						ArticleTag.ARTICLE_TAG.TAG_ID);
		public static final ForeignKey<CommentRecord, ArticleRecord> CONSTRAINT_637 = Internal
				.createForeignKey(ch.rasc.realworld.db.Keys.CONSTRAINT_F, Comment.COMMENT,
						"CONSTRAINT_637", Comment.COMMENT.ARTICLE_ID);
		public static final ForeignKey<CommentRecord, AppUserRecord> CONSTRAINT_63 = Internal
				.createForeignKey(ch.rasc.realworld.db.Keys.CONSTRAINT_7, Comment.COMMENT,
						"CONSTRAINT_63", Comment.COMMENT.USER_ID);
		public static final ForeignKey<FollowRecord, AppUserRecord> CONSTRAINT_7BF = Internal
				.createForeignKey(ch.rasc.realworld.db.Keys.CONSTRAINT_7, Follow.FOLLOW,
						"CONSTRAINT_7BF", Follow.FOLLOW.USER_ID);
		public static final ForeignKey<FollowRecord, AppUserRecord> CONSTRAINT_7BF0 = Internal
				.createForeignKey(ch.rasc.realworld.db.Keys.CONSTRAINT_7, Follow.FOLLOW,
						"CONSTRAINT_7BF0", Follow.FOLLOW.FOLLOW_ID);
	}
}
