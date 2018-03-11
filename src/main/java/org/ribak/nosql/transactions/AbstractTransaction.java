package org.ribak.nosql.transactions;

import android.os.Handler;
import android.util.Log;

import org.ribak.nosql.db.DatabaseAPI;
import org.ribak.nosql.db.INoSQLDatabaseTools;
import org.ribak.nosql.utils.PriorityCallable;
import org.ribak.nosql.utils.QueuePriority;
import org.ribak.nosql.utils.DBCallback;

import java.util.concurrent.ExecutionException;

/**
 * Created by nribak on 16/11/2016.
 */

abstract class AbstractTransaction <PARAM, RESULT>
{
    private final String mTag;

    private String mKey;
    private PARAM mParam;
    private INoSQLDatabaseTools databaseTools;

    AbstractTransaction(INoSQLDatabaseTools databaseTools, String key, PARAM param)
    {
        this.databaseTools = databaseTools;
        mKey = key;
        mParam = param;
        mTag = getClass().getSimpleName() + ".KRYO-DATABASE";
    }

    PARAM getParam()
    {
        return mParam;
    }

    protected DatabaseAPI api()
    {
        return databaseTools.getApi();
    }

    /**
     * The main method in which the DB transaction is made
     */
    protected abstract RESULT performTransaction(String key);

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

            log(e);
            return null;
        }
    }

    /**
     * Causes this transaction to run asynchronously. This method will not be blocked. the result will be returned through the callback interface
     * @param callback the callback with the result which will be called sometime in the future
     */
    public void async(final DBCallback<RESULT> callback)
    {
        final Handler handler = (callback == null) ? null : new Handler();
        databaseTools.getExecutor().submit(new PriorityCallableTransaction(QueuePriority.low)
        {
            @Override
            protected void onPostCall(final RESULT result)
            {
                if(handler != null)
                    handler.post(() -> callback.onDbCallback(mKey, result));
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
        }

        @Override
        protected RESULT runCall() throws Exception
        {
            return performTransaction(mKey);
        }
    }
}
