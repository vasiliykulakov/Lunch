package us.kulakov.lunch.config;

/**
 * Provides a client configuration. The abstraction allows us to import the config at runtime (e.g.
 * from a configuration backend), obfuscate the config, etc., and allows for mocking.
 */
public interface ClientConfigProvider {
    ClientConfig getConfig();
}
