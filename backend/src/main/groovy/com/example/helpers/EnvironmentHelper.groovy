package com.example.helpers

import grails.util.Environment

import org.yaml.snakeyaml.Yaml

import io.github.cdimascio.dotenv.Dotenv

class EnvironmentHelper {
    static void loadEnvironmentFile() {
        /***
         * Loads environment variables from a .env file specified in application.yml based on the current Grails
         * environment.
         */
        EnvironmentHelper.class.getResourceAsStream("/application.yml").withStream { InputStream stream ->
            Map configuration = new Yaml().load(stream) as Map
            String path = configuration?.properties?.paths?.environment

            Dotenv environment = Dotenv.configure()
                    .filename(path)
                    .load()

            environment.entries().each { entry ->
                System.setProperty(entry.key, entry.value)
            }
        }
    }
}
