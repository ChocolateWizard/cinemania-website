/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Mr. Poyo
 */
public final class Util {

    private Util() {
    }

    public static boolean duplicatesExistIgnoreNullAndNonNatural(List<Long> list) {
        Set<Long> set = new HashSet<>();
        for (Long aLong : list) {
            if (aLong != null && aLong > 0 && !set.add(aLong)) {
                return true;
            }
        }
        return false;
    }

    public static boolean duplicatesExist(List<Long> list) {
        Set<Long> set = new HashSet<>();
        for (Long aLong : list) {
            if (!set.add(aLong)) {
                return true;
            }
        }
        return false;
    }

    public static List<Long> sortAsc(List<Long> list) {
        List<Long> pom = new ArrayList<>(list);
        pom.sort((a, b) -> a.compareTo(b));
        return pom;
    }

}
