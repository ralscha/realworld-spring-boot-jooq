/*
 * This file is generated by jOOQ.
 */
package ch.rasc.realworld.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Schema;
import org.jooq.impl.CatalogImpl;

/**
 * This class is generated by jOOQ.
 */
@Generated(value = { "http://www.jooq.org", "jOOQ version:3.11.11" },
		comments = "This class is generated by jOOQ")
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DefaultCatalog extends CatalogImpl {

	private static final long serialVersionUID = 1593384614;

	/**
	 * The reference instance of <code></code>
	 */
	public static final DefaultCatalog DEFAULT_CATALOG = new DefaultCatalog();

	/**
	 * The schema <code></code>.
	 */
	public final DefaultSchema DEFAULT_SCHEMA = ch.rasc.realworld.db.DefaultSchema.DEFAULT_SCHEMA;

	/**
	 * No further instances allowed
	 */
	private DefaultCatalog() {
		super("");
	}

	@Override
	public final List<Schema> getSchemas() {
		List result = new ArrayList();
		result.addAll(getSchemas0());
		return result;
	}

	private final List<Schema> getSchemas0() {
		return Arrays.<Schema>asList(DefaultSchema.DEFAULT_SCHEMA);
	}
}
