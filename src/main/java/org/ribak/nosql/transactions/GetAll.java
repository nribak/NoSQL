package org.ribak.nosql.transactions;

import org.ribak.nosql.db.INoSQLDatabaseTools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by nribak on 24/11/2016.
 */

public class GetAll extends AbstractMultiGetTransaction<Map<String, ?>> {
    public GetAll(INoSQLDatabaseTools databaseTools, String prefix) {
        super(databaseTools, prefix);
    }

    @Override
    protected Map<String, ?> performTransactionWithPrefix(String prefix) {
        Set<String> keys = api().getKeys(prefix);
        Map<String, Object> map = new HashMap<>();
        for (String key : keys) {
            try {
                Object object = api().get(key);
                if (object != null)
                    map.put(key, object);
            } catch (IOException e) {
                log(e);
            }
        }
        return map;
    }
}
