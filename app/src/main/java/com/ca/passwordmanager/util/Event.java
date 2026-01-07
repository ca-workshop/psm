package com.ca.passwordmanager.util;

import androidx.annotation.Nullable;

public class Event<T> {
    private final T content;
    private boolean handled = false;

    public Event(T content) { this.content = content; }

    @Nullable
    public T getContentIfNotHandled() {
        if (handled) return null;
        handled = true;
        return content;
    }

    public T peek() { return content; }
}
