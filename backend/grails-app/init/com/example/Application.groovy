package com.example

import com.example.helpers.EnvironmentHelper

import groovy.transform.CompileStatic

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration

@CompileStatic
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        EnvironmentHelper.loadEnvironmentFile()
        GrailsApp.run(Application, args)
    }
}
