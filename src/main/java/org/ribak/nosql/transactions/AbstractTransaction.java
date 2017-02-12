package org.ribak.nosql.transactions;

import android.os.Handler;
import android.util.Log;

import com.snappydb.DB;

import org.ribak.nosql.IDatabaseTools;
import org.ribak.nosql.db.ModulesManager;
import org.ribak.nosql.utils.DbKey;
import org.ribak.nosql.utils.PriorityCallable;
import org.ribak.nosql.utils.QueuePriority;
import org.ribak.nosql.utils.SnappyCallback;
import org.ribak.nosql.utils.exceptions.DBDestroyedException;

import java.util.concurrent.ExecutionException;

/**
 * Created by nribak on 16/11/2016.
 */

abstract class AbstractTransaction <PARAM, RESULT>
{
    private final String mTag;

    private DbKey mKey;
    private PARAM mParam;
    private IDatabaseTools databaseTools;

    AbstractTransaction(IDatabaseTools databaseTools, DbKey key, PARAM param)
    {
        this.databaseTools = databaseTools;
        mKey = key;
        mParam = param;
        mTag = getClass().getSimpleName() + ".SNAPPY-DATABASE";
    }

    PARAM getParam()
    {
        return mParam;
    }

    protected DB getDB()
    {
        return (DB) databaseTools.getDirectDb();
    }

    protected void setDatabaseDead() {
        databaseTools.markDead();
    }
    /**
     * The main method in which the DB transaction is made
     */
    protected abstract RESULT performTransaction(DbKey dbKey);

    protected void log(Exception e) {
        Log.w(mTag, e.getMessage());
    }

    /**
     * Causes this transaction to run synchronously. Will be blocked until getting a result back
     */
    public RESULT sync()
    {
        try
        {
            return databaseTools.getExecutor().submit(new PriorityCallableTransaction(QueuePriority.medium)).get();
        } catch (InterruptedException | ExecutionException e)
        {
            Log.w(mTag, e.getMessage());
            return null;
        }
    }

    /**
     * Causes this transaction to run asynchronously. This method will not be blocked. the result will be returned through the callback interface
     * @param callback the callback with the result which will be called sometime in the future
     */
    public void async(final SnappyCallback<RESULT> callback)
    {
        final Handler handler = (callback == null) ? null : new Handler();
        databaseTools.getExecutor().submit(new PriorityCallableTransaction(QueuePriority.low)
        {
            @Override
            protected void onPostCall(final RESULT result)
            {
                if(handler != null)
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onDbCallback(mKey, result);
                        }
                    });
            }


        });
    }

    /**
     * Causes this transaction to run asynchronously. This method will not be blocked and the result will not be reported back
     */
    public void async()
    {
        this.async(null);
    }


    private class PriorityCallableTransaction extends PriorityCallable<RESULT>
    {
        private PriorityCallableTransaction(QueuePriority queuePriority)
        {
            super(databaseTools.getModule(), queuePriority);
            if(databaseTools.isDead())
                throw new DBDestroyedException();
        }

        @Override
        protected void onPreCall()
        {
            ModulesManager.getInstance().setModule(databaseTools.getModule());
        }

        @Override
        protected RESULT runCall() throws Exception
        {
            return performTransaction(mKey);
        }

    }
}
