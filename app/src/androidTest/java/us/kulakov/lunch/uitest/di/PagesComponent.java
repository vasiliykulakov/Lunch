package us.kulakov.lunch.uitest.di;


import javax.inject.Singleton;

import dagger.Component;
import us.kulakov.lunch.di.FindLunchPagesComponentInterface;

/**
 * Provides page (page-object pattern) dependencies to running tests.
 */
@Component
@Singleton
public interface PagesComponent extends FindLunchPagesComponentInterface {
}
