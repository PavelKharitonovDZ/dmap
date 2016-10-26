package ru.dz.vita2d.data.store;

import java.io.IOException;


/**
 * Represents a supplier of results.
 *
 * <p>It is supposed that supplier calls REST service to get data.
 * 
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #get()}.
 *
 */
@FunctionalInterface
public interface RestSupplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws IOException;
}