package com.mufan.models;

/**
 * Memory class
 * Created by mufan on 2016/5/22.
 */
public class Memory {

    long free;
    long total;
    long used;
    public Memory(long f,long t,long u){
        free = f;
        total = t;
        used = u;
    }
    public Memory(long f,long t) {
        free = f;
        total = t;
        used = total - free;
    }
    public long getFreeMemory(){
        return free;
    }
    public long getTotalMemory(){
        return total;
    }
    public long getUsedMemory(){
        return used;
    }
    public String getFreeOfTotal(){
        return String.valueOf(free) + " free out of " + String.valueOf(total);
    }

    @Override
    public String toString() {
        return "Memory{" +
                "free=" + free +
                ", total=" + total +
                ", used=" + used +
                '}';
    }
}
