/*
 * This file is generated by jOOQ.
 */
package ch.rasc.realworld.db.tables;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import ch.rasc.realworld.db.DefaultSchema;
import ch.rasc.realworld.db.Indexes;
import ch.rasc.realworld.db.Keys;
import ch.rasc.realworld.db.tables.records.ArticleRecord;

/**
 * This class is generated by jOOQ.
 */
@Generated(value = { "http://www.jooq.org", "jOOQ version:3.11.11" },
		comments = "This class is generated by jOOQ")
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Article extends TableImpl<ArticleRecord> {

	private static final long serialVersionUID = -1408122621;

	/**
	 * The reference instance of <code>ARTICLE</code>
	 */
	public static final Article ARTICLE = new Article();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<ArticleRecord> getRecordType() {
		return ArticleRecord.class;
	}

	/**
	 * The column <code>ARTICLE.ID</code>.
	 */
	public final TableField<ArticleRecord, Long> ID = createField("ID",
			org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "");

	/**
	 * The column <code>ARTICLE.USER_ID</code>.
	 */
	public final TableField<ArticleRecord, Long> USER_ID = createField("USER_ID",
			org.jooq.impl.SQLDataType.BIGINT, this, "");

	/**
	 * The column <code>ARTICLE.SLUG</code>.
	 */
	public final TableField<ArticleRecord, String> SLUG = createField("SLUG",
			org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

	/**
	 * The column <code>ARTICLE.TITLE</code>.
	 */
	public final TableField<ArticleRecord, String> TITLE = createField("TITLE",
			org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

	/**
	 * The column <code>ARTICLE.DESCRIPTION</code>.
	 */
	public final TableField<ArticleRecord, String> DESCRIPTION = createField(
			"DESCRIPTION", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>ARTICLE.BODY</code>.
	 */
	public final TableField<ArticleRecord, String> BODY = createField("BODY",
			org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>ARTICLE.CREATED_AT</code>.
	 */
	public final TableField<ArticleRecord, LocalDateTime> CREATED_AT = createField(
			"CREATED_AT", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this,
			"");

	/**
	 * The column <code>ARTICLE.UPDATED_AT</code>.
	 */
	public final TableField<ArticleRecord, LocalDateTime> UPDATED_AT = createField(
			"UPDATED_AT",
			org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false)
					.defaultValue(org.jooq.impl.DSL.field("CURRENT_TIMESTAMP()",
							org.jooq.impl.SQLDataType.LOCALDATETIME)),
			this, "");

	/**
	 * Create a <code>ARTICLE</code> table reference
	 */
	public Article() {
		this(DSL.name("ARTICLE"), null);
	}

	/**
	 * Create an aliased <code>ARTICLE</code> table reference
	 */
	public Article(String alias) {
		this(DSL.name(alias), ARTICLE);
	}

	/**
	 * Create an aliased <code>ARTICLE</code> table reference
	 */
	public Article(Name alias) {
		this(alias, ARTICLE);
	}

	private Article(Name alias, Table<ArticleRecord> aliased) {
		this(alias, aliased, null);
	}

	private Article(Name alias, Table<ArticleRecord> aliased, Field<?>[] parameters) {
		super(alias, null, aliased, parameters, DSL.comment(""));
	}

	public <O extends Record> Article(Table<O> child, ForeignKey<O, ArticleRecord> key) {
		super(child, key, ARTICLE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Schema getSchema() {
		return DefaultSchema.DEFAULT_SCHEMA;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Index> getIndexes() {
		return Arrays.<Index>asList(Indexes.CONSTRAINT_INDEX_F,
				Indexes.CONSTRAINT_INDEX_FF, Indexes.PRIMARY_KEY_F);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<ArticleRecord, Long> getIdentity() {
		return Keys.IDENTITY_ARTICLE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<ArticleRecord> getPrimaryKey() {
		return Keys.CONSTRAINT_F;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<ArticleRecord>> getKeys() {
		return Arrays.<UniqueKey<ArticleRecord>>asList(Keys.CONSTRAINT_F,
				Keys.CONSTRAINT_FF);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<ArticleRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<ArticleRecord, ?>>asList(Keys.CONSTRAINT_FF2);
	}

	public AppUser appUser() {
		return new AppUser(this, Keys.CONSTRAINT_FF2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Article as(String alias) {
		return new Article(DSL.name(alias), this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Article as(Name alias) {
		return new Article(alias, this);
	}

	/**
	 * Rename this table
	 */
	@Override
	public Article rename(String name) {
		return new Article(DSL.name(name), null);
	}

	/**
	 * Rename this table
	 */
	@Override
	public Article rename(Name name) {
		return new Article(name, null);
	}
}
