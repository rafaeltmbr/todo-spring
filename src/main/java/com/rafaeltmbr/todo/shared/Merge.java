package com.rafaeltmbr.todo.shared;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;

import java.util.ArrayList;
import java.util.Collections;

public final class Merge {

    /**
     * Merge to objects. Copy the non-null properties of the source object into the destination object.
     *
     * @param source      source object with nullable properties.
     * @param destination destination object to be updated.
     */
    public static void mergeNonNullProperties(Object source, Object destination) {
        BeanUtils.copyProperties(source, destination, getNonNullProperties(source, new String[0]));
    }

    /**
     * Merge to objects. Copy the non-null properties of the source object into the destination object. Also, the
     * merging process supports an array of property names that must be ignored.
     *
     * @param source           source object with nullable properties.
     * @param destination      destination object to be updated.
     * @param ignoreProperties array of property names to be ignored.
     */
    public static void mergeNonNullProperties(Object source, Object destination, String[] ignoreProperties) {
        BeanUtils.copyProperties(source, destination, getNonNullProperties(source, ignoreProperties));
    }

    /**
     * Get an array of all property names that have null value.
     *
     * @param source       object to be scanned.
     * @param initialValue initial array of property names to be included.
     * @return array of properties that have null values.
     */

    private static String[] getNonNullProperties(Object source, String[] initialValue) {
        var beanWrapper = new BeanWrapperImpl(source);
        var descriptors = beanWrapper.getPropertyDescriptors();
        var emptyPropertyNames = new ArrayList<String>();
        Collections.addAll(emptyPropertyNames, initialValue);

        for (var descriptor : descriptors) {
            if (beanWrapper.getPropertyValue(descriptor.getName()) == null) {
                emptyPropertyNames.add(descriptor.getName());
            }
        }

        var emptyPropertyNamesArray = new String[emptyPropertyNames.size()];
        return emptyPropertyNames.toArray(emptyPropertyNamesArray);
    }
}
