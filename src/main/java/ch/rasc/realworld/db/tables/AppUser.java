/*
 * This file is generated by jOOQ.
 */
package ch.rasc.realworld.db.tables;


import ch.rasc.realworld.db.DefaultSchema;
import ch.rasc.realworld.db.Keys;
import ch.rasc.realworld.db.tables.records.AppUserRecord;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function6;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row6;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AppUser extends TableImpl<AppUserRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>app_user</code>
     */
    public static final AppUser APP_USER = new AppUser();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AppUserRecord> getRecordType() {
        return AppUserRecord.class;
    }

    /**
     * The column <code>app_user.id</code>.
     */
    public final TableField<AppUserRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>app_user.username</code>.
     */
    public final TableField<AppUserRecord, String> USERNAME = createField(DSL.name("username"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>app_user.password</code>.
     */
    public final TableField<AppUserRecord, String> PASSWORD = createField(DSL.name("password"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>app_user.email</code>.
     */
    public final TableField<AppUserRecord, String> EMAIL = createField(DSL.name("email"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>app_user.bio</code>.
     */
    public final TableField<AppUserRecord, String> BIO = createField(DSL.name("bio"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>app_user.image</code>.
     */
    public final TableField<AppUserRecord, String> IMAGE = createField(DSL.name("image"), SQLDataType.VARCHAR(511), this, "");

    private AppUser(Name alias, Table<AppUserRecord> aliased) {
        this(alias, aliased, null);
    }

    private AppUser(Name alias, Table<AppUserRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>app_user</code> table reference
     */
    public AppUser(String alias) {
        this(DSL.name(alias), APP_USER);
    }

    /**
     * Create an aliased <code>app_user</code> table reference
     */
    public AppUser(Name alias) {
        this(alias, APP_USER);
    }

    /**
     * Create a <code>app_user</code> table reference
     */
    public AppUser() {
        this(DSL.name("app_user"), null);
    }

    public <O extends Record> AppUser(Table<O> child, ForeignKey<O, AppUserRecord> key) {
        super(child, key, APP_USER);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public Identity<AppUserRecord, Long> getIdentity() {
        return (Identity<AppUserRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<AppUserRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_4;
    }

    @Override
    public List<UniqueKey<AppUserRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.CONSTRAINT_45, Keys.CONSTRAINT_459);
    }

    @Override
    public AppUser as(String alias) {
        return new AppUser(DSL.name(alias), this);
    }

    @Override
    public AppUser as(Name alias) {
        return new AppUser(alias, this);
    }

    @Override
    public AppUser as(Table<?> alias) {
        return new AppUser(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public AppUser rename(String name) {
        return new AppUser(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public AppUser rename(Name name) {
        return new AppUser(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public AppUser rename(Table<?> name) {
        return new AppUser(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<Long, String, String, String, String, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function6<? super Long, ? super String, ? super String, ? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function6<? super Long, ? super String, ? super String, ? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
