package org.ribak.nosql.utils.exceptions;

/**
 * Created by nribak on 12/02/2017.
 */

public class DBDestroyedException extends RuntimeException {

    public DBDestroyedException() {
        super("DB is destroyed!");
    }
}
