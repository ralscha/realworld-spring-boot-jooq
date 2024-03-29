/*
 * This file is generated by jOOQ.
 */
package ch.rasc.realworld.db.tables.records;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;

import ch.rasc.realworld.db.tables.Comment;

/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CommentRecord extends UpdatableRecordImpl<CommentRecord>
		implements Record6<Long, String, Long, Long, LocalDateTime, LocalDateTime> {

	private static final long serialVersionUID = 1L;

	/**
	 * Setter for <code>COMMENT.ID</code>.
	 */
	public void setId(Long value) {
		set(0, value);
	}

	/**
	 * Getter for <code>COMMENT.ID</code>.
	 */
	public Long getId() {
		return (Long) get(0);
	}

	/**
	 * Setter for <code>COMMENT.BODY</code>.
	 */
	public void setBody(String value) {
		set(1, value);
	}

	/**
	 * Getter for <code>COMMENT.BODY</code>.
	 */
	public String getBody() {
		return (String) get(1);
	}

	/**
	 * Setter for <code>COMMENT.ARTICLE_ID</code>.
	 */
	public void setArticleId(Long value) {
		set(2, value);
	}

	/**
	 * Getter for <code>COMMENT.ARTICLE_ID</code>.
	 */
	public Long getArticleId() {
		return (Long) get(2);
	}

	/**
	 * Setter for <code>COMMENT.USER_ID</code>.
	 */
	public void setUserId(Long value) {
		set(3, value);
	}

	/**
	 * Getter for <code>COMMENT.USER_ID</code>.
	 */
	public Long getUserId() {
		return (Long) get(3);
	}

	/**
	 * Setter for <code>COMMENT.CREATED_AT</code>.
	 */
	public void setCreatedAt(LocalDateTime value) {
		set(4, value);
	}

	/**
	 * Getter for <code>COMMENT.CREATED_AT</code>.
	 */
	public LocalDateTime getCreatedAt() {
		return (LocalDateTime) get(4);
	}

	/**
	 * Setter for <code>COMMENT.UPDATED_AT</code>.
	 */
	public void setUpdatedAt(LocalDateTime value) {
		set(5, value);
	}

	/**
	 * Getter for <code>COMMENT.UPDATED_AT</code>.
	 */
	public LocalDateTime getUpdatedAt() {
		return (LocalDateTime) get(5);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	@Override
	public Record1<Long> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record6 type implementation
	// -------------------------------------------------------------------------

	@Override
	public Row6<Long, String, Long, Long, LocalDateTime, LocalDateTime> fieldsRow() {
		return (Row6) super.fieldsRow();
	}

	@Override
	public Row6<Long, String, Long, Long, LocalDateTime, LocalDateTime> valuesRow() {
		return (Row6) super.valuesRow();
	}

	@Override
	public Field<Long> field1() {
		return Comment.COMMENT.ID;
	}

	@Override
	public Field<String> field2() {
		return Comment.COMMENT.BODY;
	}

	@Override
	public Field<Long> field3() {
		return Comment.COMMENT.ARTICLE_ID;
	}

	@Override
	public Field<Long> field4() {
		return Comment.COMMENT.USER_ID;
	}

	@Override
	public Field<LocalDateTime> field5() {
		return Comment.COMMENT.CREATED_AT;
	}

	@Override
	public Field<LocalDateTime> field6() {
		return Comment.COMMENT.UPDATED_AT;
	}

	@Override
	public Long component1() {
		return getId();
	}

	@Override
	public String component2() {
		return getBody();
	}

	@Override
	public Long component3() {
		return getArticleId();
	}

	@Override
	public Long component4() {
		return getUserId();
	}

	@Override
	public LocalDateTime component5() {
		return getCreatedAt();
	}

	@Override
	public LocalDateTime component6() {
		return getUpdatedAt();
	}

	@Override
	public Long value1() {
		return getId();
	}

	@Override
	public String value2() {
		return getBody();
	}

	@Override
	public Long value3() {
		return getArticleId();
	}

	@Override
	public Long value4() {
		return getUserId();
	}

	@Override
	public LocalDateTime value5() {
		return getCreatedAt();
	}

	@Override
	public LocalDateTime value6() {
		return getUpdatedAt();
	}

	@Override
	public CommentRecord value1(Long value) {
		setId(value);
		return this;
	}

	@Override
	public CommentRecord value2(String value) {
		setBody(value);
		return this;
	}

	@Override
	public CommentRecord value3(Long value) {
		setArticleId(value);
		return this;
	}

	@Override
	public CommentRecord value4(Long value) {
		setUserId(value);
		return this;
	}

	@Override
	public CommentRecord value5(LocalDateTime value) {
		setCreatedAt(value);
		return this;
	}

	@Override
	public CommentRecord value6(LocalDateTime value) {
		setUpdatedAt(value);
		return this;
	}

	@Override
	public CommentRecord values(Long value1, String value2, Long value3, Long value4, LocalDateTime value5,
			LocalDateTime value6) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		value6(value6);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached CommentRecord
	 */
	public CommentRecord() {
		super(Comment.COMMENT);
	}

	/**
	 * Create a detached, initialised CommentRecord
	 */
	public CommentRecord(Long id, String body, Long articleId, Long userId, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		super(Comment.COMMENT);

		setId(id);
		setBody(body);
		setArticleId(articleId);
		setUserId(userId);
		setCreatedAt(createdAt);
		setUpdatedAt(updatedAt);
	}
}
