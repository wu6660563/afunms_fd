package com.gatherdb;

import java.io.Serializable;

public interface ResultTosql extends Serializable {

    void executeResultToDB(DBAttribute attribute);

}
