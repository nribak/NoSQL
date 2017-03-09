package org.ribak.nosql.utils.exceptions;

/**
 * Created by nribak on 12/02/2017.
 */

public class DBDestroyedException extends KryoDBException {

    public DBDestroyedException() {
        super("DB is destroyed!");
    }
}
