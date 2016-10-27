package ru.dz.vita2d.data;

/**
 * USed to supply lambda with field name/data info.
 * @author dz
 *
 */

@FunctionalInterface
public interface FieldConsumer {

    /**
     * Consume field information.
     *
     * @param rawName Field name as in JSON.
     * @param rawData Field data as in JSON.
     * @param readableData Field data in human readable form.
     */
    void accept(String rawName, String rawData, String readableData);
}