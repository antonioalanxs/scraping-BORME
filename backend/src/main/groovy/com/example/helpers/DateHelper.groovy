package com.example.helpers

import java.time.LocalDate

class DateHelper {
    static LocalDate parseDate(String string, boolean emitException = false) {
        /***
         * Parses a date string into a LocalDate object.
         *
         * @param string The date string to parse.
         * @param emitException Whether to throw an exception on failure.
         * @return LocalDate The parsed object, or null if parsing fails and emitException is false.
         */
        try {
            return LocalDate.parse(string)
        } catch (Exception exception) {
            if (emitException) {
                throw exception
            }
            return null
        }
    }
}
