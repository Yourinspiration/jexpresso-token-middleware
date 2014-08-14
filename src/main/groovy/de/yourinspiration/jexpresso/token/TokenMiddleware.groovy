package de.yourinspiration.jexpresso.token

import de.yourinspiration.jexpresso.core.MiddlewareHandler
import de.yourinspiration.jexpresso.core.Next
import de.yourinspiration.jexpresso.core.Request
import de.yourinspiration.jexpresso.core.Response

class TokenMiddleware implements MiddlewareHandler {

    private final String tokenParamName
    private final TokenResolver tokenResolver
    private final String[] routes

    TokenMiddleware(final TokenResolver tokenResolver, final String tokenParamName, final String... routes) {
        this.tokenResolver = tokenResolver
        this.tokenParamName = tokenParamName
        this.routes = routes
    }


    @Override
    void handle(final Request request, final Response response, final Next next) {
        if (matchesPath(request.path())) {
            if (hasTokenParam(request)) {
                if (tokenResolver.resolve(request.query(tokenParamName))) {
                    next.next()
                } else {
                    next.cancel()
                }
            } else {
                next.next()
            }
        }
    }


    boolean matchesPath(String pathToMatch) {
        routes.each { path ->
            if (pathToMatch.matches(path.replaceAll('\\*', '.*'))) {
                return true
            }
        }
    }

    private hasTokenParam(Request request) {
        return request.query(tokenParamName) != null
    }

}
