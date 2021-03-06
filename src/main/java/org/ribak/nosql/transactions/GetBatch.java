package org.ribak.nosql.transactions;

import org.ribak.nosql.db.INoSQLDatabaseTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nribak on 21/02/2017.
 */

public class GetBatch <RESULT> extends AbstractTransaction<Void, List<RESULT>> {
    public GetBatch(INoSQLDatabaseTools databaseTools, String key) {
        super(databaseTools, key, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<RESULT> performTransaction(String key) {
        if(!api().supportsBatching())
            return new ArrayList<>();
        List<RESULT> items = new ArrayList<>();
        try {
            List<Object> objects = api().getBatch(key);
            for (Object object : objects) {
                if(object != null)
                    items.add((RESULT) object);
            }
        } catch (IOException e) {
            log(e);
        }
        return items;
    }
}
