package org.ribak.nosql;

/**
 * Created by nribak on 12/02/2017.
 */

public interface IDatabaseIO {

    void open();

    boolean isOpen();

    void close();
}
