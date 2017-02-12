package org.ribak.nosql.transactions;

import com.snappydb.SnappydbException;

import org.ribak.nosql.IDatabaseTools;
import org.ribak.nosql.utils.DbKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nribak on 16/11/2016.
 */

public class GetKeys extends AbstractTransaction<Void, List<DbKey>>
{
    private boolean getAll;
    public GetKeys(IDatabaseTools tools, DbKey key)
    {
        super(tools, key, null);
        this.getAll = (key == null);
    }

    @Override
    protected List<DbKey> performTransaction(DbKey dbKey)
    {
        List<DbKey> keys = new ArrayList<>();
        String prefix = (getAll) ? DbKey.GLOBAL_PREFIX : dbKey.getQualifiedGroups(true);
        try {
            String[] k = getDB().findKeys(prefix);
            for (String kk : k) {
                DbKey dbk = DbKey.create(kk);
                if(dbk != null)
                    keys.add(dbk);
            }
        } catch (SnappydbException e) {
            log(e);
        }
        return keys;
    }
}
