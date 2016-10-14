package br.leosilvadev.proxy.readers;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import br.leosilvadev.proxy.server.ProxyServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

@Component
public class RoutesReader {

	private static final Logger logger = LoggerFactory.getLogger(ProxyServer.class);

	private final Vertx vertx;

	public RoutesReader(Vertx vertx) {
		this.vertx = vertx;
	}

	public void read(String path, Function<JsonObject, Object> callback) {
		vertx.fileSystem().readFile(path, (fileResult) -> {
			Buffer buffer = fileResult.result();
			if (buffer == null) {
				logger.error(String.format("Routes File %s not found!", path), path);

			} else {
				callback.apply(buffer.toJsonObject());
			}
		});
	}

}