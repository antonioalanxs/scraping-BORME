package com.example

class Entity {
    String code
    String name

    static constraints = {
        code nullable: false, blank: false
        name nullable: false, blank: false
    }
}
