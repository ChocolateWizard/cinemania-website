/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.helpers;

import com.borak.cwb.backend.InitialTest;
import com.borak.cwb.backend.integration.domain.ConfigPropertiesTest;
import com.borak.cwb.backend.integration.domain.MyImageTest;
import com.borak.cwb.backend.integration.nonsecured.CountryRoutesTest;
import com.borak.cwb.backend.integration.nonsecured.GenreRoutesTest;
import com.borak.cwb.backend.integration.nonsecured.ImageRoutesTest;
import com.borak.cwb.backend.integration.nonsecured.MediaRoutesTest;
import com.borak.cwb.backend.integration.nonsecured.MovieRoutesTest;
import com.borak.cwb.backend.integration.nonsecured.PersonRoutesTest;
import com.borak.cwb.backend.integration.repository.ActingRepositoryJDBCTest;
import com.borak.cwb.backend.integration.repository.ActorRepositoryJDBCTest;
import com.borak.cwb.backend.integration.repository.CritiqueRepositoryJDBCTest;
import com.borak.cwb.backend.integration.repository.DirectorRepositoryJDBCTest;
import com.borak.cwb.backend.integration.repository.FileRepositoryTest;
import com.borak.cwb.backend.integration.repository.GenreRepositoryJDBCTest;
import com.borak.cwb.backend.integration.repository.MediaRepositoryJDBCTest;
import com.borak.cwb.backend.integration.repository.MovieRepositoryJDBCTest;
import com.borak.cwb.backend.integration.repository.PersonRepositoryJDBCTest;
import com.borak.cwb.backend.integration.repository.PersonWrapperRepositoryJDBCTest;
import com.borak.cwb.backend.integration.repository.TVShowRepositoryJDBCTest;
import com.borak.cwb.backend.integration.repository.UserRepositoryJDBCTest;
import com.borak.cwb.backend.integration.repository.WriterRepositoryJDBCTest;
import com.borak.cwb.backend.integration.secured.AuthRoutesTest;
import com.borak.cwb.backend.integration.secured.CritiqueSecuredRoutesTest;
import com.borak.cwb.backend.integration.secured.MovieSecuredRoutesTest;
import com.borak.cwb.backend.integration.secured.PersonSecuredRoutesTest;
import com.borak.cwb.backend.integration.secured.TVShowSecuredRoutesTest;
import com.borak.cwb.backend.integration.secured.UserSecuredRoutesTest;

/**
 *
 * @author Mr. Poyo
 */
public final class TestResultsHelper {

    private TestResultsHelper() {
    }
//=========================================================================================================

    /**
     * Checks if all tests in InitialTest has passed
     *
     * @return Returns false if any test in ContextLoadsTest has not passed.
     * Else returns true.
     */
    public static boolean didInitialTestPass() {
        return InitialTest.didAllTestsPass();
    }

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
     * <li>InitialTest</li>
     * <li>ConfigPropertiesTest</li>
     * <li>MyImageTest</li>
     * <li>FileRepositoryTest</li>
     * </ul>
     *
     * @return Returns false if any of these tests has not passed. Else returns
     * true.
     */
    public static boolean didAllPreControllerTestsPass() {
        boolean[] testsPassed = new boolean[]{
            InitialTest.didAllTestsPass(),
            ConfigPropertiesTest.didAllTestsPass(),
            MyImageTest.didAllTestsPass(),
            FileRepositoryTest.didAllTestsPass(),};
        for (boolean isPassed : testsPassed) {
            if (!isPassed) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if all tests in AuthRoutesTest have passed
     *
     * @return Returns false if any test in AuthRoutesTest has not passed. Else
     * returns true.
     */
    public static boolean didAuthRoutesTestPass() {
        return AuthRoutesTest.didAllTestsPass();
    }

    /**
     * Checks if all necessary tests for MovieSecuredRoutesTest have passed.
     * This includes:
     * <ul>
     * <li>AuthRoutesTest</li>
     * <li>CritiqueSecuredRoutesTest</li>
     * <li>UserSecuredRoutesTest</li>
     * </ul>
     *
     * @return Returns false if any test has not passed. Else returns true.
     */
    public static boolean didMovieSecuredRoutesRequiredTestsPass() {
        boolean[] testsPassed = new boolean[]{
            AuthRoutesTest.didAllTestsPass(),
            UserSecuredRoutesTest.didAllTestsPass()
        };
        for (boolean isPassed : testsPassed) {
            if (!isPassed) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if all necessary tests for TVShowSecuredRoutesTest have passed.
     * This includes:
     * <ul>
     * <li>AuthRoutesTest</li>
     * <li>MovieSecuredRoutesTest</li>
     * </ul>
     *
     * @return Returns false if any test has not passed. Else returns true.
     */
    public static boolean didTVShowSecuredRoutesRequiredTestsPass() {
        boolean[] testsPassed = new boolean[]{
            didAuthRoutesTestPass(),
            MovieSecuredRoutesTest.didAllTestsPass()
        };
        for (boolean isPassed : testsPassed) {
            if (!isPassed) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if all necessary tests for PersonSecuredRoutesTest have passed.
     * This includes:
     * <ul>
     * <li>AuthRoutesTest</li>
     * <li>TVShowSecuredRoutesTest</li>
     * </ul>
     *
     * @return Returns false if any test has not passed. Else returns true.
     */
    public static boolean didPersonSecuredRoutesRequiredTestsPass() {
        boolean[] testsPassed = new boolean[]{
            didTVShowSecuredRoutesRequiredTestsPass(),
            TVShowSecuredRoutesTest.didAllTestsPass()
        };
        for (boolean isPassed : testsPassed) {
            if (!isPassed) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if all tests from all test classes have passed
     *
     * @return Returns false if any test in any test class has not passed. Else
     * returns true.
     */
    public static boolean didAllTestsPass() {
        boolean[] testsPassed = new boolean[]{
            InitialTest.didAllTestsPass(),
            ConfigPropertiesTest.didAllTestsPass(),
            MyImageTest.didAllTestsPass(),
            FileRepositoryTest.didAllTestsPass(),
            CountryRoutesTest.didAllTestsPass(),
            GenreRoutesTest.didAllTestsPass(),
            ImageRoutesTest.didAllTestsPass(),
            MediaRoutesTest.didAllTestsPass(),
            MovieRoutesTest.didAllTestsPass(),
            PersonRoutesTest.didAllTestsPass(),            
            AuthRoutesTest.didAllTestsPass(),           
            MovieSecuredRoutesTest.didAllTestsPass(),
            PersonSecuredRoutesTest.didAllTestsPass(),
            TVShowSecuredRoutesTest.didAllTestsPass(),
            UserSecuredRoutesTest.didAllTestsPass()
        };
        for (boolean isPassed : testsPassed) {
            if (!isPassed) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if all tests from all test classes have failed
     *
     * @return Returns false if any test in any test class has passed. Else
     * returns true.
     */
    public static boolean didAllTestsFail() {
        boolean[] testsPassed = new boolean[]{
            InitialTest.didAllTestsPass(),
            ConfigPropertiesTest.didAllTestsPass(),
            MyImageTest.didAllTestsPass(),
            FileRepositoryTest.didAllTestsPass(),
            CountryRoutesTest.didAllTestsPass(),
            GenreRoutesTest.didAllTestsPass(),
            ImageRoutesTest.didAllTestsPass(),
            MediaRoutesTest.didAllTestsPass(),
            MovieRoutesTest.didAllTestsPass(),
            PersonRoutesTest.didAllTestsPass(),          
            AuthRoutesTest.didAllTestsPass(),           
            MovieSecuredRoutesTest.didAllTestsPass(),
            PersonSecuredRoutesTest.didAllTestsPass(),
            TVShowSecuredRoutesTest.didAllTestsPass(),
            UserSecuredRoutesTest.didAllTestsPass()
        };
        for (boolean isPassed : testsPassed) {
            if (isPassed) {
                return false;
            }
        }
        return true;
    }
}
