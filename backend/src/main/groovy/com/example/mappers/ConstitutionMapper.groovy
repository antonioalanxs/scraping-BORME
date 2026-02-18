package com.example.mappers

import com.example.Constitution
import com.example.dto.ConstitutionDTO
import com.example.Entity
import com.example.Appointment
import com.example.Registry
import com.example.helpers.DateHelper

class ConstitutionMapper {

    static Constitution toConstitution(Map data) {
        if (!data) return null

        Entity entity = new Entity(
                code: data.entity?.code,
                name: data.entity?.name
        )

        Appointment appointment = new Appointment(
                soleAdministrator: data.appointment?.sole_administrator
        )

        Registry registry = new Registry(
                section: data.registry?.section,
                page: data.registry?.page,
                entry: data.registry?.entry,
                date: DateHelper.parseDate(data.registry?.date)
        )

        return new Constitution(
                entity: entity,
                startOfOperations: DateHelper.parseDate(data?.start_of_operations),
                socialObject: data?.social_object,
                address: data?.address,
                capital: data?.capital as BigDecimal,
                singlePersonDeclaration: data?.single_person_declaration,
                solePartner: data?.sole_partner,
                appointment: appointment,
                registry: registry,
                source: data.source,
                date: DateHelper.parseDate(data.date)
        )
    }

    static ConstitutionDTO toDTO(Constitution data) {
        if (!data) return null

        return new ConstitutionDTO(
                id: data.id,
                singlePersonDeclaration: data?.singlePersonDeclaration,
                date: data?.date?.toString(),
                capital: data?.capital,
                source: data?.source,
                address: data?.address,
                solePartner: data?.solePartner,
                startOfOperations: data?.startOfOperations?.toString(),
                socialObject: data?.socialObject,
                entity: [
                        code: data.entity.code,
                        name: data.entity.name
                ],
                appointment: [
                        soleAdministrator: data?.appointment?.soleAdministrator
                ],
                registry: [
                        section: data?.registry?.section,
                        page   : data?.registry?.page,
                        entry  : data?.registry?.entry,
                        date   : data?.registry?.date?.toString()
                ]
        )
    }

    static List<ConstitutionDTO> toDTOList(List<Constitution> data) {
        data?.collect { toDTO(it) } ?: []
    }
}
