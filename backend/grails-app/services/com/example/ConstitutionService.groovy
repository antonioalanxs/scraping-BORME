package com.example

import com.example.helpers.BORMEHelper
import com.example.helpers.DateHelper
import com.example.mappers.ConstitutionMapper

import grails.gorm.transactions.Transactional
import groovy.json.JsonSlurper

import java.time.LocalDate


@Transactional
class ConstitutionService {
    LanguageModelService languageModelService

    Constitution save(Map data) {
        Constitution constitution = ConstitutionMapper.toConstitution(data)
        constitution.entity.save(flush: true, failOnError: true)
        constitution?.appointment?.save(flush: true)
        constitution?.registry?.save(flush: true)
        constitution = constitution.save(flush: true, failOnError: true)
        return constitution
    }

    List<Constitution> findAllByDate(String date) {
        LocalDate parsedDate = DateHelper.parseDate(date, true)

        List<Constitution> results = Constitution.findAllByDate(parsedDate)
        if (!results.isEmpty()) return results

        List<Map<String, String>> constitutionBlocks = BORMEHelper.getConstitutions(parsedDate)
        constitutionBlocks.each { Map<String, String> it ->
            Map response = languageModelService.call(it.value)
            if (!response?.result) return
            Map data = new JsonSlurper().parseText(response.result) as Map
            data["source"] = it.source
            data["date"] = date
            Constitution constitution = save(data)
            results << constitution
        }

        return results
    }

    Constitution findById(Long id) {
        return Constitution.get(id)
    }
}