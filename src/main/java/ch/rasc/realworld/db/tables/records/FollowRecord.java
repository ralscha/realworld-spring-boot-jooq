/*
 * This file is generated by jOOQ.
 */
package ch.rasc.realworld.db.tables.records;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

import ch.rasc.realworld.db.tables.Follow;

/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FollowRecord extends UpdatableRecordImpl<FollowRecord>
		implements Record2<Long, Long> {

	private static final long serialVersionUID = 1L;

	/**
	 * Setter for <code>FOLLOW.USER_ID</code>.
	 */
	public void setUserId(Long value) {
		set(0, value);
	}

	/**
	 * Getter for <code>FOLLOW.USER_ID</code>.
	 */
	public Long getUserId() {
		return (Long) get(0);
	}

	/**
	 * Setter for <code>FOLLOW.FOLLOW_ID</code>.
	 */
	public void setFollowId(Long value) {
		set(1, value);
	}

	/**
	 * Getter for <code>FOLLOW.FOLLOW_ID</code>.
	 */
	public Long getFollowId() {
		return (Long) get(1);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	@Override
	public Record2<Long, Long> key() {
		return (Record2) super.key();
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	@Override
	public Row2<Long, Long> fieldsRow() {
		return (Row2) super.fieldsRow();
	}

	@Override
	public Row2<Long, Long> valuesRow() {
		return (Row2) super.valuesRow();
	}

	@Override
	public Field<Long> field1() {
		return Follow.FOLLOW.USER_ID;
	}

	@Override
	public Field<Long> field2() {
		return Follow.FOLLOW.FOLLOW_ID;
	}

	@Override
	public Long component1() {
		return getUserId();
	}

	@Override
	public Long component2() {
		return getFollowId();
	}

	@Override
	public Long value1() {
		return getUserId();
	}

	@Override
	public Long value2() {
		return getFollowId();
	}

	@Override
	public FollowRecord value1(Long value) {
		setUserId(value);
		return this;
	}

	@Override
	public FollowRecord value2(Long value) {
		setFollowId(value);
		return this;
	}

	@Override
	public FollowRecord values(Long value1, Long value2) {
		value1(value1);
		value2(value2);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached FollowRecord
	 */
	public FollowRecord() {
		super(Follow.FOLLOW);
	}

	/**
	 * Create a detached, initialised FollowRecord
	 */
	public FollowRecord(Long userId, Long followId) {
		super(Follow.FOLLOW);

		setUserId(userId);
		setFollowId(followId);
	}
}
