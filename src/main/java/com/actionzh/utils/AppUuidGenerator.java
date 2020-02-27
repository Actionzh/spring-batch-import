package com.actionzh.utils;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

/**
 * Created by wangding on 25/02/2017.
 */
public class AppUuidGenerator {

    private static TimeBasedGenerator timeBasedGenerator;

    static {
        ensureGeneratorInitialized();
    }

    private synchronized static void ensureGeneratorInitialized() {
        if (timeBasedGenerator == null) {
            timeBasedGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
        }
    }

    public static String getNextId() {
        return timeBasedGenerator.generate().toString();
    }

    private AppUuidGenerator() {
    }

}