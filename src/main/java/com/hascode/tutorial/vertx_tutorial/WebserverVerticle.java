package com.hascode.tutorial.vertx_tutorial;

import java.io.File;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.platform.Verticle;

public class WebserverVerticle extends Verticle {

	@Override
	public void start() {
		vertx.createHttpServer()
				.requestHandler(new Handler<HttpServerRequest>() {
					@Override
					public void handle(final HttpServerRequest request) {
						File file = null;
						if (request.path().equals("/")) {
							file = new File("chat.html");
						} else {
							file = new File(request.path());
						}
						request.response().sendFile("web/" + file);
					}
				}).listen(8080, "localhost");

		vertx.createHttpServer()
				.websocketHandler(new Handler<ServerWebSocket>() {
					@Override
					public void handle(final ServerWebSocket ws) {
						if (ws.path().startsWith("/chat")) {
							ws.dataHandler(new Handler<Buffer>() {
								@Override
								public void handle(final Buffer data) {
									ws.writeTextFrame(data.toString());
								}
							});
						} else {
							ws.reject();
						}
					}
				}).listen(8090);
	}
}
