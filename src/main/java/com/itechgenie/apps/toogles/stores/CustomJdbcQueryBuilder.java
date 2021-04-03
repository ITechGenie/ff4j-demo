package com.itechgenie.apps.toogles.stores;

import org.ff4j.store.JdbcQueryBuilder;

public class CustomJdbcQueryBuilder extends JdbcQueryBuilder {
	
	public CustomJdbcQueryBuilder(String prefix, String suffix) {
		this.tablePrefix = prefix;
		this.tableSuffix = suffix;
	}
	
	/**
	 * Table name for audit.
	 *
	 * @return
	 *     Table name for audit
	 */
	public String getTableNameAudit() {
        return getTableName("FEATURE_AUDITS");
    }
	
	/**
     * Table name for features.
     *
     * @return
     *     Table name for features
     */
	public String getTableNameFeatures() {
        return getTableName("FEATURE_TOGGLES");
    }

	 /**
     * Table name for properties.
     *
     * @return
     *     Table name for properties.
     */
    public String getTableNameProperties() {
        return getTableName("FEATURE_PROPERTIES");
    }
    
    /**
     * Table name for custom properties.
     *
     * @return
     *     Table name for custom properties.
     */
    public String getTableNameCustomProperties() {
        return getTableName("FEATURE_PROPERTIES_MAP");
    }
    
    /**
     * Table name for roles.
     *
     * @return
     *     Table name for roles
     */
	public String getTableNameRoles() {
        return getTableName("FEATURE_ROLES");
    }
}
