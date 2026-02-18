package com.example

import java.time.LocalDate

class Constitution {
    Entity entity
    LocalDate startOfOperations
    String socialObject
    String address
    BigDecimal capital
    Boolean singlePersonDeclaration
    String solePartner
    Appointment appointment
    Registry registry
    String source
    LocalDate date

    static mapping = {
        socialObject column: "social_object", type: "text"
        entity column: 'entity_id', unique: true
        appointment column: 'appointment_id'
        registry column: 'registry_id'
    }

    static constraints = {
        entity nullable: false
        startOfOperations nullable: true
        socialObject nullable: true
        address nullable: true
        capital nullable: true
        singlePersonDeclaration nullable: true
        solePartner nullable: true
        appointment nullable: true
        registry nullable: true
        source nullable: false
        date nullable: false
    }
}
