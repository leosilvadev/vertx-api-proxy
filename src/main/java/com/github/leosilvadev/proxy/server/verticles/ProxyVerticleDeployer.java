package com.github.leosilvadev.proxy.server.verticles;

import java.util.List;

import com.github.leosilvadev.proxy.config.ApplicationConfig;
import com.github.leosilvadev.proxy.middlewares.Middleware;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class ProxyVerticleDeployer {

	public void deploy(Vertx vertx, ApplicationConfig applicationConfig, List<Middleware> middlewares) {
		JsonObject json = new JsonObject()
				.put(ApplicationConfig.PORT_JSON, applicationConfig.getPort())
				.put(ApplicationConfig.ROUTES_PATH_JSON, applicationConfig.getRoutesPath());
		vertx.deployVerticle(new ProxyVerticle(middlewares), new DeploymentOptions().setConfig(json));
	}

}