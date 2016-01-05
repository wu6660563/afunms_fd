package com.gatherdb;

import java.io.Serializable;

public class ResultToDB implements Serializable{

    /**
	 * serialVersionUID:
	 * <p>
	 *
	 * @since   v1.01
	 */
	private static final long serialVersionUID = 6822727398548434368L;

	private DBAttribute attribute;

    private ResultTosql resultTosql;

    /**
     * @return the attribute
     */
    public DBAttribute getAttribute() {
        return attribute;
    }

    /**
     * @return the resultTosql
     */
    public ResultTosql getResultTosql() {
        return resultTosql;
    }

    /**
     * @param attribute the attribute to set
     */
    public void setAttribute(DBAttribute attribute) {
        this.attribute = attribute;
    }

    /**
     * @param resultTosql the resultTosql to set
     */
    public void setResultTosql(ResultTosql resultTosql) {
        this.resultTosql = resultTosql;
    }

    
}
