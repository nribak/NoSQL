package org.ribak.nosql.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nribak on 12/02/2017.
 */

public class DbKey implements Comparable<DbKey> {
//    public static final String GLOBAL_PREFIX = "DB";
    private static final String COLON = ":";

    private List<String> groups;
    private String key;

    private DbKey() {
        groups = new LinkedList<>();
    }

    public DbKey(String singleGroup, String key) {
        if(singleGroup != null)
            this.groups = Collections.singletonList(singleGroup);
        else
            this.groups = new LinkedList<>();
        this.key = key;
    }

    public DbKey(String key) {
        groups = new LinkedList<>();
        this.key = key;
    }

    public List<String> getGroups() {
        return groups;
    }

    public String getKey() {
        return key;
    }

    public String getQualifiedGroups() {
        StringBuilder builder = new StringBuilder();
        for (String group : groups) {
            if(builder.length() > 0)
                builder.append(COLON);
            builder.append(group);
        }
        return builder.toString();
    }

    public String getQualifiedKey() {
        String group = getQualifiedGroups();
        return group + COLON + key;
    }

    @Override
    public String toString() {
        return getQualifiedKey();
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;
        if(this.getClass() != o.getClass())
            return false;
        DbKey dbKey = (DbKey) o;
        return this.getQualifiedKey().equals(dbKey.getQualifiedKey());
    }

    @Override
    public int hashCode()
    {
        return getQualifiedKey().hashCode();
    }

    @Override
    public int compareTo(@NonNull DbKey another)
    {
        return this.getQualifiedKey().compareTo(another.getQualifiedKey());
    }

    @Nullable
    public static DbKey create(String key) {
        if(key == null || key.isEmpty())
            return null;
        String[] parts = key.split(COLON);
        Builder builder = new Builder();
        for (int i = 1; i < parts.length - 1; i++)
            builder.addGroup(parts[i]);
        builder.setKey(parts[parts.length - 1]);
        return builder.build();
    }

    public static class Builder {
        private List<String> groups;
        private String key;

        public Builder() {
            groups = new ArrayList<>();
//            dbKey = new DbKey();
//            dbKey.groups = new LinkedList<>();
        }

        public Builder addGroup(String group) {
            groups.add(group);
            return this;
        }
        public Builder setKey(String key) {
            this.key = key;
            return this;
        }
        public DbKey build() {
            DbKey dbKey = new DbKey();
            dbKey.key = key;
            dbKey.groups = groups;
            return dbKey;
        }
    }

}
