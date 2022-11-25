/*
 * This file is generated by jOOQ.
 */
package ch.rasc.realworld.db;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
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
 * A class modelling foreign key relationships and constraints of tables in the default
 * schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

	// -------------------------------------------------------------------------
	// UNIQUE and PRIMARY KEY definitions
	// -------------------------------------------------------------------------

	public static final UniqueKey<AppUserRecord> CONSTRAINT_7 = Internal.createUniqueKey(
			AppUser.APP_USER, DSL.name("CONSTRAINT_7"),
			new TableField[] { AppUser.APP_USER.ID }, true);
	public static final UniqueKey<AppUserRecord> CONSTRAINT_76 = Internal.createUniqueKey(
			AppUser.APP_USER, DSL.name("CONSTRAINT_76"),
			new TableField[] { AppUser.APP_USER.USERNAME }, true);
	public static final UniqueKey<AppUserRecord> CONSTRAINT_760 = Internal
			.createUniqueKey(AppUser.APP_USER, DSL.name("CONSTRAINT_760"),
					new TableField[] { AppUser.APP_USER.EMAIL }, true);
	public static final UniqueKey<ArticleRecord> CONSTRAINT_F = Internal.createUniqueKey(
			Article.ARTICLE, DSL.name("CONSTRAINT_F"),
			new TableField[] { Article.ARTICLE.ID }, true);
	public static final UniqueKey<ArticleRecord> CONSTRAINT_FF = Internal.createUniqueKey(
			Article.ARTICLE, DSL.name("CONSTRAINT_FF"),
			new TableField[] { Article.ARTICLE.SLUG }, true);
	public static final UniqueKey<ArticleFavoriteRecord> CONSTRAINT_B = Internal
			.createUniqueKey(ArticleFavorite.ARTICLE_FAVORITE, DSL.name("CONSTRAINT_B"),
					new TableField[] { ArticleFavorite.ARTICLE_FAVORITE.ARTICLE_ID,
							ArticleFavorite.ARTICLE_FAVORITE.USER_ID },
					true);
	public static final UniqueKey<ArticleTagRecord> CONSTRAINT_B1 = Internal
			.createUniqueKey(ArticleTag.ARTICLE_TAG, DSL.name("CONSTRAINT_B1"),
					new TableField[] { ArticleTag.ARTICLE_TAG.ARTICLE_ID,
							ArticleTag.ARTICLE_TAG.TAG_ID },
					true);
	public static final UniqueKey<CommentRecord> CONSTRAINT_6 = Internal.createUniqueKey(
			Comment.COMMENT, DSL.name("CONSTRAINT_6"),
			new TableField[] { Comment.COMMENT.ID }, true);
	public static final UniqueKey<FollowRecord> CONSTRAINT_7B = Internal.createUniqueKey(
			Follow.FOLLOW, DSL.name("CONSTRAINT_7B"),
			new TableField[] { Follow.FOLLOW.USER_ID, Follow.FOLLOW.FOLLOW_ID }, true);
	public static final UniqueKey<TagRecord> CONSTRAINT_1 = Internal.createUniqueKey(
			Tag.TAG, DSL.name("CONSTRAINT_1"), new TableField[] { Tag.TAG.ID }, true);

	// -------------------------------------------------------------------------
	// FOREIGN KEY definitions
	// -------------------------------------------------------------------------

	public static final ForeignKey<ArticleRecord, AppUserRecord> CONSTRAINT_FF2 = Internal
			.createForeignKey(Article.ARTICLE, DSL.name("CONSTRAINT_FF2"),
					new TableField[] { Article.ARTICLE.USER_ID }, Keys.CONSTRAINT_7,
					new TableField[] { AppUser.APP_USER.ID }, true);
	public static final ForeignKey<ArticleFavoriteRecord, ArticleRecord> CONSTRAINT_B0 = Internal
			.createForeignKey(ArticleFavorite.ARTICLE_FAVORITE, DSL.name("CONSTRAINT_B0"),
					new TableField[] { ArticleFavorite.ARTICLE_FAVORITE.ARTICLE_ID },
					Keys.CONSTRAINT_F, new TableField[] { Article.ARTICLE.ID }, true);
	public static final ForeignKey<ArticleFavoriteRecord, AppUserRecord> CONSTRAINT_B05 = Internal
			.createForeignKey(ArticleFavorite.ARTICLE_FAVORITE,
					DSL.name("CONSTRAINT_B05"),
					new TableField[] { ArticleFavorite.ARTICLE_FAVORITE.USER_ID },
					Keys.CONSTRAINT_7, new TableField[] { AppUser.APP_USER.ID }, true);
	public static final ForeignKey<ArticleTagRecord, ArticleRecord> CONSTRAINT_B12 = Internal
			.createForeignKey(ArticleTag.ARTICLE_TAG, DSL.name("CONSTRAINT_B12"),
					new TableField[] { ArticleTag.ARTICLE_TAG.ARTICLE_ID },
					Keys.CONSTRAINT_F, new TableField[] { Article.ARTICLE.ID }, true);
	public static final ForeignKey<ArticleTagRecord, TagRecord> CONSTRAINT_B12C = Internal
			.createForeignKey(ArticleTag.ARTICLE_TAG, DSL.name("CONSTRAINT_B12C"),
					new TableField[] { ArticleTag.ARTICLE_TAG.TAG_ID }, Keys.CONSTRAINT_1,
					new TableField[] { Tag.TAG.ID }, true);
	public static final ForeignKey<CommentRecord, AppUserRecord> CONSTRAINT_63 = Internal
			.createForeignKey(Comment.COMMENT, DSL.name("CONSTRAINT_63"),
					new TableField[] { Comment.COMMENT.USER_ID }, Keys.CONSTRAINT_7,
					new TableField[] { AppUser.APP_USER.ID }, true);
	public static final ForeignKey<CommentRecord, ArticleRecord> CONSTRAINT_637 = Internal
			.createForeignKey(Comment.COMMENT, DSL.name("CONSTRAINT_637"),
					new TableField[] { Comment.COMMENT.ARTICLE_ID }, Keys.CONSTRAINT_F,
					new TableField[] { Article.ARTICLE.ID }, true);
	public static final ForeignKey<FollowRecord, AppUserRecord> CONSTRAINT_7BF = Internal
			.createForeignKey(Follow.FOLLOW, DSL.name("CONSTRAINT_7BF"),
					new TableField[] { Follow.FOLLOW.USER_ID }, Keys.CONSTRAINT_7,
					new TableField[] { AppUser.APP_USER.ID }, true);
	public static final ForeignKey<FollowRecord, AppUserRecord> CONSTRAINT_7BF0 = Internal
			.createForeignKey(Follow.FOLLOW, DSL.name("CONSTRAINT_7BF0"),
					new TableField[] { Follow.FOLLOW.FOLLOW_ID }, Keys.CONSTRAINT_7,
					new TableField[] { AppUser.APP_USER.ID }, true);
}
