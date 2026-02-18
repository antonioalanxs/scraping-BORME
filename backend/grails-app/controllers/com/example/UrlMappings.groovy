package com.example

class UrlMappings {

    static mappings = {
        "/"(controller: 'application', action: 'index')
        "/api/constitutions"(resources: "constitution")
    }
}
