/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.helpers;

import com.borak.kinweb.backend.ConfigPropertiesTest;
import com.borak.kinweb.backend.integration.domain.MyImageTest;
import com.borak.kinweb.backend.integration.repository.ActingRepositoryJDBCTest;
import com.borak.kinweb.backend.integration.repository.ActorRepositoryJDBCTest;
import com.borak.kinweb.backend.integration.repository.CritiqueRepositoryJDBCTest;
import com.borak.kinweb.backend.integration.repository.DirectorRepositoryJDBCTest;
import com.borak.kinweb.backend.integration.repository.FileRepositoryTest;
import com.borak.kinweb.backend.integration.repository.GenreRepositoryJDBCTest;
import com.borak.kinweb.backend.integration.repository.MediaRepositoryJDBCTest;
import com.borak.kinweb.backend.integration.repository.MovieRepositoryJDBCTest;
import com.borak.kinweb.backend.integration.repository.PersonRepositoryJDBCTest;
import com.borak.kinweb.backend.integration.repository.PersonWrapperRepositoryJDBCTest;
import com.borak.kinweb.backend.integration.repository.TVShowRepositoryJDBCTest;
import com.borak.kinweb.backend.integration.repository.UserRepositoryJDBCTest;
import com.borak.kinweb.backend.integration.repository.WriterRepositoryJDBCTest;

/**
 *
 * @author Mr. Poyo
 */
public final class TestResultsHelper {

    private TestResultsHelper() {
    }
//=========================================================================================================

    /**
     * Checks if all tests in ConfigPropertiesTest have passed
     *
     * @return Returns false if any test in ConfigPropertiesTest has not passed.
     * Else returns true.
     */
    public static boolean didConfigPropertiesTestsPass() {
        return ConfigPropertiesTest.didAllTestsPass();
    }

    /**
     * This method is required for FileRepositoryTest class to check if
     * dependant test classes have passed successfully. Those test classes are:
     * <ul>
     * <li>ConfigPropertiesTest</li>
     * <li>MyImageTest</li>
     * </ul>
     *
     * @return Returns false if any of these tests has not passed. Else returns
     * true.
     */
    public static boolean didFileRepositoryRequiredTestsPass() {
        boolean[] testsPassed = new boolean[]{
            ConfigPropertiesTest.didAllTestsPass(),
            MyImageTest.didAllTestsPass()
        };
        for (boolean isPassed : testsPassed) {
            if (!isPassed) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method is required for testing REST API URL routes. Checks if all
     * tests of classes on whose functionality Controller classes depend on
     * either directly or indirectly. Those test classes are:
     * <ul>
     * <li>ConfigPropertiesTest</li>
     * <li>MyImageTest</li>
     * <li>ActingRepositoryJDBCTest</li>
     * <li>ActorRepositoryJDBCTest</li>
     * <li>DirectorRepositoryJDBCTest</li>
     * <li>FileRepositoryTest</li>
     * <li>GenreRepositoryJDBCTest</li>
     * <li>MediaRepositoryJDBCTest</li>
     * <li>MovieRepositoryJDBCTest</li>
     * <li>PersonRepositoryJDBCTest</li>
     * <li>PersonWrapperRepositoryJDBCTest</li>
     * <li>TVShowRepositoryJDBCTest</li>
     * <li>WriterRepositoryJDBCTest</li>
     * <li>UserRepositoryJDBCTest</li>
     * <li>CritiqueRepositoryJDBCTest</li>
     * </ul>
     *
     * @return Returns false if any of these tests has not passed. Else returns
     * true.
     */
    public static boolean didAllPreControllerTestsPass() {
        boolean[] testsPassed = new boolean[]{
            ConfigPropertiesTest.didAllTestsPass(),
            MyImageTest.didAllTestsPass(),
            ActingRepositoryJDBCTest.didAllTestsPass(),
            ActorRepositoryJDBCTest.didAllTestsPass(),
            DirectorRepositoryJDBCTest.didAllTestsPass(),
            FileRepositoryTest.didAllTestsPass(),
            GenreRepositoryJDBCTest.didAllTestsPass(),
            MediaRepositoryJDBCTest.didAllTestsPass(),
            MovieRepositoryJDBCTest.didAllTestsPass(),
            PersonRepositoryJDBCTest.didAllTestsPass(),
            PersonWrapperRepositoryJDBCTest.didAllTestsPass(),
            TVShowRepositoryJDBCTest.didAllTestsPass(),
            WriterRepositoryJDBCTest.didAllTestsPass(),
            UserRepositoryJDBCTest.didAllTestsPass(),
            CritiqueRepositoryJDBCTest.didAllTestsPass()
        };
        for (boolean isPassed : testsPassed) {
            if (!isPassed) {
                return false;
            }
        }
        return true;
    }

}
