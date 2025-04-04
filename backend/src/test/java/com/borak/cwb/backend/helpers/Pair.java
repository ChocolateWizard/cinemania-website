/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.helpers;

/**
 * Simple class to store pairs of data. Attributes are final
 *
 * @author Mr. Poyo
 * @param <L> first object of the pair (one on the left)
 * @param <R> second object of the pair (one on the right)
 */
public final class Pair<L, R> {

    private final L l;
    private final R r;

    public Pair(L l, R r) {
        this.l = l;
        this.r = r;
    }

    public L getL() {
        return l;
    }

    public R getR() {
        return r;
    }

}
