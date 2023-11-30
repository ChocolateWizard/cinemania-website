/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class Util {

    public boolean doesDuplicatesExistIgnoreNonNatural(List<Long> list) {
        Set<Long> set = new HashSet();
        for (Long aLong : list) {
            if (aLong != null && aLong > 0 && !set.add(aLong)) {
                return true;
            }
        }
        return false;
    }

    public List<Long> sortAsc(List<Long> list) {
        List<Long> pom = new ArrayList<>(list);
        pom.sort((a, b) -> a.compareTo(b));
        return pom;
    }

}
