/*
 * This file is generated by jOOQ.
 */
package ch.rasc.realworld.db.tables.records;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;

import ch.rasc.realworld.db.tables.AppUser;

/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AppUserRecord extends UpdatableRecordImpl<AppUserRecord>
		implements Record6<Long, String, String, String, String, String> {

	private static final long serialVersionUID = 1L;

	/**
	 * Setter for <code>app_user.id</code>.
	 */
	public void setId(Long value) {
		set(0, value);
	}

	/**
	 * Getter for <code>app_user.id</code>.
	 */
	public Long getId() {
		return (Long) get(0);
	}

	/**
	 * Setter for <code>app_user.username</code>.
	 */
	public void setUsername(String value) {
		set(1, value);
	}

	/**
	 * Getter for <code>app_user.username</code>.
	 */
	public String getUsername() {
		return (String) get(1);
	}

	/**
	 * Setter for <code>app_user.password</code>.
	 */
	public void setPassword(String value) {
		set(2, value);
	}

	/**
	 * Getter for <code>app_user.password</code>.
	 */
	public String getPassword() {
		return (String) get(2);
	}

	/**
	 * Setter for <code>app_user.email</code>.
	 */
	public void setEmail(String value) {
		set(3, value);
	}

	/**
	 * Getter for <code>app_user.email</code>.
	 */
	public String getEmail() {
		return (String) get(3);
	}

	/**
	 * Setter for <code>app_user.bio</code>.
	 */
	public void setBio(String value) {
		set(4, value);
	}

	/**
	 * Getter for <code>app_user.bio</code>.
	 */
	public String getBio() {
		return (String) get(4);
	}

	/**
	 * Setter for <code>app_user.image</code>.
	 */
	public void setImage(String value) {
		set(5, value);
	}

	/**
	 * Getter for <code>app_user.image</code>.
	 */
	public String getImage() {
		return (String) get(5);
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
	public Row6<Long, String, String, String, String, String> fieldsRow() {
		return (Row6) super.fieldsRow();
	}

	@Override
	public Row6<Long, String, String, String, String, String> valuesRow() {
		return (Row6) super.valuesRow();
	}

	@Override
	public Field<Long> field1() {
		return AppUser.APP_USER.ID;
	}

	@Override
	public Field<String> field2() {
		return AppUser.APP_USER.USERNAME;
	}

	@Override
	public Field<String> field3() {
		return AppUser.APP_USER.PASSWORD;
	}

	@Override
	public Field<String> field4() {
		return AppUser.APP_USER.EMAIL;
	}

	@Override
	public Field<String> field5() {
		return AppUser.APP_USER.BIO;
	}

	@Override
	public Field<String> field6() {
		return AppUser.APP_USER.IMAGE;
	}

	@Override
	public Long component1() {
		return getId();
	}

	@Override
	public String component2() {
		return getUsername();
	}

	@Override
	public String component3() {
		return getPassword();
	}

	@Override
	public String component4() {
		return getEmail();
	}

	@Override
	public String component5() {
		return getBio();
	}

	@Override
	public String component6() {
		return getImage();
	}

	@Override
	public Long value1() {
		return getId();
	}

	@Override
	public String value2() {
		return getUsername();
	}

	@Override
	public String value3() {
		return getPassword();
	}

	@Override
	public String value4() {
		return getEmail();
	}

	@Override
	public String value5() {
		return getBio();
	}

	@Override
	public String value6() {
		return getImage();
	}

	@Override
	public AppUserRecord value1(Long value) {
		setId(value);
		return this;
	}

	@Override
	public AppUserRecord value2(String value) {
		setUsername(value);
		return this;
	}

	@Override
	public AppUserRecord value3(String value) {
		setPassword(value);
		return this;
	}

	@Override
	public AppUserRecord value4(String value) {
		setEmail(value);
		return this;
	}

	@Override
	public AppUserRecord value5(String value) {
		setBio(value);
		return this;
	}

	@Override
	public AppUserRecord value6(String value) {
		setImage(value);
		return this;
	}

	@Override
	public AppUserRecord values(Long value1, String value2, String value3, String value4, String value5,
			String value6) {
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
	 * Create a detached AppUserRecord
	 */
	public AppUserRecord() {
		super(AppUser.APP_USER);
	}

	/**
	 * Create a detached, initialised AppUserRecord
	 */
	public AppUserRecord(Long id, String username, String password, String email, String bio, String image) {
		super(AppUser.APP_USER);

		setId(id);
		setUsername(username);
		setPassword(password);
		setEmail(email);
		setBio(bio);
		setImage(image);
	}
}
