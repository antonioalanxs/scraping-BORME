package com.example

import javax.servlet.*
import javax.servlet.http.HttpServletRequest

class LanguageModelServiceRequestCleanerFilter implements Filter {

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response)
        } finally {
            LanguageModelService.clearThreadLocal()
            println "[${this.class.name}] Cleared ThreadLocal for request '${((HttpServletRequest) request).requestURI}'"
        }
    }
}
