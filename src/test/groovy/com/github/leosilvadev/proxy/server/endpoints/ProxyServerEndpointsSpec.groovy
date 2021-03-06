package com.github.leosilvadev.proxy.server.endpoints

import static io.restassured.RestAssured.*
import static io.restassured.matcher.RestAssuredMatchers.*
import static org.hamcrest.Matchers.*
import groovy.json.JsonOutput
import io.restassured.http.ContentType
import io.vertx.core.Future
import io.vertx.core.json.Json
import spock.util.concurrent.AsyncConditions

import com.github.leosilvadev.proxy.IntegrationSpec
import com.github.leosilvadev.proxy.server.ProxyServerFixture

class ProxyServerEndpointsSpec extends IntegrationSpec {
	
	def setupSpec() {
		deployProxyVerticle(8002, 'routes-endpoints.json')
		
		def conds = new AsyncConditions()
		def server = ProxyServerFixture.buildServer vertx
		server.listen(9002) { Future res ->
			conds.evaluate { assert res.succeeded() }
		}
		conds.await 5
	}

	def 'Should forward a GET request to /users'() {
		given:
		def request = given().accept(ContentType.JSON)

		when:
		def response = request.get('http://localhost:8002/users')

		then:
		response.statusCode() == 200

		and:
		response.contentType() == 'application/json'

		and:
		response.header('application') == 'vertx-proxy'
	}

	def 'Should forward a GET request to /users/params with query parameters'() {
		given:
		def request = given().accept(ContentType.JSON)

		when:
		def response = request.get('http://localhost:8002/users/params?param1=1&param2=2')

		then:
		response.statusCode() == 200

		and:
		response.contentType() == 'application/json'

		and:
		def json = Json.decodeValue(response.body().asString(), Map)
		json.param1 == '1'
		json.param2 == '2'
	}

	def 'Should forward a PUT request to /users'() {
		given:
		def user = [name: 'leonardo']
		def request = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(JsonOutput.toJson(user))

		when:
		def response = request.put('http://localhost:8002/users')

		then:
		response.statusCode() == 201

		and:
		response.contentType() == 'application/json'

		and:
		response.header('application') == 'vertx-proxy'
	}

	def 'Should forward a POST request to /users'() {
		given:
		def user = [name: 'leonardo']
		def request = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(JsonOutput.toJson(user))

		when:
		def response = request.post('http://localhost:8002/users')

		then:
		response.statusCode() == 201

		and:
		response.contentType() == 'application/json'

		and:
		response.header('application') == 'vertx-proxy'
	}
}
