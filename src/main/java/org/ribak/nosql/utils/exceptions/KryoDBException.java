package org.ribak.nosql.utils.exceptions;

/**
 * Created by nribak on 21/02/2017.
 */

abstract class KryoDBException extends RuntimeException {

    KryoDBException(String message) {
        super(message);
    }
}
