package org.ribak.nosql;

import android.os.Parcel;
import android.os.Parcelable;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import org.ribak.nosql.utils.ObjectSerializer;

/**
 * Created by nribak on 12/02/2017.
 */

public class Person implements Parcelable, ObjectSerializer<Person> {
    private String id, name;

    public Person() { }

    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected Person(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + ": " + name;
    }

    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    @Override
    public void write(Person object, Output output) {
        output.writeString(object.id);
        output.writeString(object.name + "_serializer");
    }

    @Override
    public Person read(Class<Person> cls, Input input) {
        String id = input.readString();
        String name = input.readString();
        return new Person(id, name);
    }

    @Override
    public Class<Person> getSerializerClass() {
        return Person.class;
    }

}
