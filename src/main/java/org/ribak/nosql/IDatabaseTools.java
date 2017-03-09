package org.ribak.nosql;

import java.util.concurrent.ExecutorService;

/**
 * Created by nribak on 12/02/2017.
 */

public interface IDatabaseTools {

    ExecutorService getExecutor();

    String getModule();

    Object getDirectDb();
}
