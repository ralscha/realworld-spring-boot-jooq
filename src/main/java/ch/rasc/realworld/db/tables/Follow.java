/*
 * This file is generated by jOOQ.
 */
package ch.rasc.realworld.db.tables;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function2;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import ch.rasc.realworld.db.DefaultSchema;
import ch.rasc.realworld.db.Keys;
import ch.rasc.realworld.db.tables.records.FollowRecord;

/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Follow extends TableImpl<FollowRecord> {

	private static final long serialVersionUID = 1L;

	/**
	 * The reference instance of <code>FOLLOW</code>
	 */
	public static final Follow FOLLOW = new Follow();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<FollowRecord> getRecordType() {
		return FollowRecord.class;
	}

	/**
	 * The column <code>FOLLOW.USER_ID</code>.
	 */
	public final TableField<FollowRecord, Long> USER_ID = createField(DSL.name("USER_ID"),
			SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>FOLLOW.FOLLOW_ID</code>.
	 */
	public final TableField<FollowRecord, Long> FOLLOW_ID = createField(
			DSL.name("FOLLOW_ID"), SQLDataType.BIGINT.nullable(false), this, "");

	private Follow(Name alias, Table<FollowRecord> aliased) {
		this(alias, aliased, null);
	}

	private Follow(Name alias, Table<FollowRecord> aliased, Field<?>[] parameters) {
		super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
	}

	/**
	 * Create an aliased <code>FOLLOW</code> table reference
	 */
	public Follow(String alias) {
		this(DSL.name(alias), FOLLOW);
	}

	/**
	 * Create an aliased <code>FOLLOW</code> table reference
	 */
	public Follow(Name alias) {
		this(alias, FOLLOW);
	}

	/**
	 * Create a <code>FOLLOW</code> table reference
	 */
	public Follow() {
		this(DSL.name("FOLLOW"), null);
	}

	public <O extends Record> Follow(Table<O> child, ForeignKey<O, FollowRecord> key) {
		super(child, key, FOLLOW);
	}

	@Override
	public Schema getSchema() {
		return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
	}

	@Override
	public UniqueKey<FollowRecord> getPrimaryKey() {
		return Keys.CONSTRAINT_7B;
	}

	@Override
	public List<ForeignKey<FollowRecord, ?>> getReferences() {
		return Arrays.asList(Keys.CONSTRAINT_7BF, Keys.CONSTRAINT_7BF0);
	}

	private transient AppUser _constraint_7bf;
	private transient AppUser _constraint_7bf0;

	/**
	 * Get the implicit join path to the <code>PUBLIC.APP_USER</code> table, via the
	 * <code>CONSTRAINT_7BF</code> key.
	 */
	public AppUser constraint_7bf() {
		if (this._constraint_7bf == null) {
			this._constraint_7bf = new AppUser(this, Keys.CONSTRAINT_7BF);
		}

		return this._constraint_7bf;
	}

	/**
	 * Get the implicit join path to the <code>PUBLIC.APP_USER</code> table, via the
	 * <code>CONSTRAINT_7BF0</code> key.
	 */
	public AppUser constraint_7bf0() {
		if (this._constraint_7bf0 == null) {
			this._constraint_7bf0 = new AppUser(this, Keys.CONSTRAINT_7BF0);
		}

		return this._constraint_7bf0;
	}

	@Override
	public Follow as(String alias) {
		return new Follow(DSL.name(alias), this);
	}

	@Override
	public Follow as(Name alias) {
		return new Follow(alias, this);
	}

	@Override
	public Follow as(Table<?> alias) {
		return new Follow(alias.getQualifiedName(), this);
	}

	/**
	 * Rename this table
	 */
	@Override
	public Follow rename(String name) {
		return new Follow(DSL.name(name), null);
	}

	/**
	 * Rename this table
	 */
	@Override
	public Follow rename(Name name) {
		return new Follow(name, null);
	}

	/**
	 * Rename this table
	 */
	@Override
	public Follow rename(Table<?> name) {
		return new Follow(name.getQualifiedName(), null);
	}

	// -------------------------------------------------------------------------
	// Row2 type methods
	// -------------------------------------------------------------------------

	@Override
	public Row2<Long, Long> fieldsRow() {
		return (Row2) super.fieldsRow();
	}

	/**
	 * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
	 */
	public <U> SelectField<U> mapping(
			Function2<? super Long, ? super Long, ? extends U> from) {
		return convertFrom(Records.mapping(from));
	}

	/**
	 * Convenience mapping calling {@link SelectField#convertFrom(Class, Function)}.
	 */
	public <U> SelectField<U> mapping(Class<U> toType,
			Function2<? super Long, ? super Long, ? extends U> from) {
		return convertFrom(toType, Records.mapping(from));
	}
}
