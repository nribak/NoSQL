package org.ribak.nosql.utils.exceptions;

/**
 * Created by nribak on 12/02/2017.
 */

public class InitializationException extends KryoDBException {

    public InitializationException() {
        super("init was never called!!");
    }
}
