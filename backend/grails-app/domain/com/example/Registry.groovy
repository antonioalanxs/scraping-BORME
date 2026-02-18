package com.example

import java.time.LocalDate

class Registry {
    String section
    String page
    String entry
    LocalDate date

    static constraints = {
        section nullable: true
        page nullable: true
        entry nullable: true
        date nullable: true
    }
}
