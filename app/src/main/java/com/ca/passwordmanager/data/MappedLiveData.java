package com.ca.passwordmanager.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class MappedLiveData<S, T> extends MediatorLiveData<T> {

    public interface Mapper<S, T> { T map(S source); }

    public MappedLiveData(@NonNull LiveData<S> source, @NonNull Mapper<S, T> mapper) {
        addSource(source, s -> setValue(mapper.map(s)));
    }
}
