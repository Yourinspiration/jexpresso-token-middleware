package de.yourinspiration.jexpresso.token

import de.yourinspiration.jexpresso.core.Next
import de.yourinspiration.jexpresso.core.Request
import de.yourinspiration.jexpresso.core.Response
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class TokenMiddlewareTest extends Specification {

    def tokenMiddleware
    def req
    def res
    def next
    def tokenResolver

    def token = '223gb2jh3gv2jhv'

    def setup() {
        req = Mock(Request)
        res = Mock(Response)
        next = Mock(Next)
        tokenResolver = Mock(TokenResolver)
        tokenMiddleware = new TokenMiddleware(tokenResolver, '/test/path*', '/other/path')
    }

    def "should ignore request with not matching path"() {
        setup:
        req.path() >> '/some/path'

        when:
        tokenMiddleware.handle(req, res, next)

        then:
        1 * next.next()
    }

    def "should handle existing query token param"() {
        setup:
        req.path() >> '/test/path'
        req.query(tokenMiddleware.tokenParamName) >> token

        when:
        tokenMiddleware.handle(req, res, next)

        then:
        1 * tokenResolver.resolve(token)
    }

    def "should cancel the request if the token could not be resolved"() {
        setup:
        req.query(tokenMiddleware.tokenParamName) >> token
        req.path() >> '/test/path'
        tokenResolver.resolve(token) >> false

        when:
        tokenMiddleware.handle(req, res, next)

        then:
        1 * next.cancel()
    }

    def "should call next for a valid token"() {
        setup:
        req.path() >> '/other/path'
        req.query(tokenMiddleware.tokenParamName) >> token
        tokenResolver.resolve(token) >> true

        when:
        tokenMiddleware.handle(req, res, next)

        then:
        1  * next.next()
    }

}
