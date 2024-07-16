/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.helpers;

/**
 * Simple class to store triplets of data. Attributes are final
 *
 * @author Mr. Poyo
 * @param <L> first object of the triple (one on the left)
 * @param <M> second object of the triple (one in the middle)
 * @param <R> third object of the triple (one on the right)
 */
public final class Triple<L, M, R> {

    private final L l;
    private final M m;
    private final R r;

    public Triple(L l, M m, R r) {
        this.l = l;
        this.m = m;
        this.r = r;
    }

    public L getL() {
        return l;
    }

    public M getM() {
        return m;
    }

    public R getR() {
        return r;
    }

}
