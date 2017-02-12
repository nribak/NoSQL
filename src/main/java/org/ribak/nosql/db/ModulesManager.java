package org.ribak.nosql.db;


import org.ribak.nosql.IDatabaseIO;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nribak on 21/11/2016.
 * Singleton, holds all the SnappyDB modules currently active
 */

public class ModulesManager
{
    private static ModulesManager instance = null;

    public static ModulesManager getInstance()
    {
        if(instance == null)
            instance = new ModulesManager();
        return instance;
    }

    private Map<String, IDatabaseIO> mDatabases;
    private String mOpenedModule;
    private ModulesManager()
    {
        this.mDatabases = new HashMap<>();
    }

    /**
     * get a reference to a DB
     * @param module name of the module
     * @return the instance to the newly created DB
     */
    public IDatabaseIO getDB(String module) {
        if(mDatabases.containsKey(module))
            return mDatabases.get(module);
        IDatabaseIO databaseIO = new SnappyDatabase(module);
        mDatabases.put(module, databaseIO);
        return databaseIO;
    }

    /**
     * opens and closes the necessary DB modules. call this before any DB transaction
     * @param module the module to open
     */
    public void setModule(String module)
    {
        IDatabaseIO newDatabase = mDatabases.get(module);
        IDatabaseIO oldDatabase = (mOpenedModule == null) ? null : mDatabases.get(mOpenedModule);

        if(oldDatabase != null && oldDatabase != newDatabase && oldDatabase.isOpen())
            oldDatabase.close();

        if(!newDatabase.isOpen())
            newDatabase.open();

        this.mOpenedModule = module;
    }
}
