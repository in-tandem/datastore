package com.org.somak.datastore.entity;

import java.util.ArrayList;
import java.util.List;

public class CurrentAwareData<T> {

    private T current;
    private List<T> past;

    public CurrentAwareData( T item){
        this.current = item;
        this.past = new ArrayList<>();
    }

    public T getCurrent() {
        return current;
    }

    public void setCurrent(T current) {
        this.current = current;
    }

    public List<T> getPast() {
        return past;
    }

    public void setPast(List<T> past) {
        this.past = past;
    }

    public void moveToPast(){
        this.past.add(this.current);
        this.current = null;
    }
}
