package us.kulakov.lunch.results.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Scope annotation for all things related to the screen displaying results
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ResultsScope {
}
