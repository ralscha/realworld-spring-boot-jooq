/*
 * This file is generated by jOOQ.
 */
package ch.rasc.realworld.db.tables.records;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

import ch.rasc.realworld.db.tables.Tag;

/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TagRecord extends UpdatableRecordImpl<TagRecord>
		implements Record2<Long, String> {

	private static final long serialVersionUID = 1L;

	/**
	 * Setter for <code>TAG.ID</code>.
	 */
	public void setId(Long value) {
		set(0, value);
	}

	/**
	 * Getter for <code>TAG.ID</code>.
	 */
	public Long getId() {
		return (Long) get(0);
	}

	/**
	 * Setter for <code>TAG.NAME</code>.
	 */
	public void setName(String value) {
		set(1, value);
	}

	/**
	 * Getter for <code>TAG.NAME</code>.
	 */
	public String getName() {
		return (String) get(1);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	@Override
	public Record1<Long> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	@Override
	public Row2<Long, String> fieldsRow() {
		return (Row2) super.fieldsRow();
	}

	@Override
	public Row2<Long, String> valuesRow() {
		return (Row2) super.valuesRow();
	}

	@Override
	public Field<Long> field1() {
		return Tag.TAG.ID;
	}

	@Override
	public Field<String> field2() {
		return Tag.TAG.NAME;
	}

	@Override
	public Long component1() {
		return getId();
	}

	@Override
	public String component2() {
		return getName();
	}

	@Override
	public Long value1() {
		return getId();
	}

	@Override
	public String value2() {
		return getName();
	}

	@Override
	public TagRecord value1(Long value) {
		setId(value);
		return this;
	}

	@Override
	public TagRecord value2(String value) {
		setName(value);
		return this;
	}

	@Override
	public TagRecord values(Long value1, String value2) {
		value1(value1);
		value2(value2);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached TagRecord
	 */
	public TagRecord() {
		super(Tag.TAG);
	}

	/**
	 * Create a detached, initialised TagRecord
	 */
	public TagRecord(Long id, String name) {
		super(Tag.TAG);

		setId(id);
		setName(name);
	}
}
