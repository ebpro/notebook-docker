package fr.univtln.bruno.demos.docker;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.java.Log;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class.
 *
 */
@Log
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://0.0.0.0:8080/restjpa/";

    public final static EntityManagerFactory emf;
    static {
        Map<String, String> env = System.getenv();
        Map<String, Object> configOverrides = new HashMap<>();
        for (String envName : env.keySet()) {
            if (envName.contains("DATASOURCE_URL")) {
                log.info("Override with env. var. :"+envName);
                configOverrides.put("jakarta.persistence.jdbc.url", env.get(envName));
            }
            if (envName.contains("DATASOURCE_USERNAME")) {
                log.info("Override with env. var. :"+envName);
                configOverrides.put("jakarta.persistence.jdbc.user", env.get(envName));
            }
            if (envName.contains("DATASOURCE_PASSWORD")) {
                log.info("Override with env. var. :"+envName);
                configOverrides.put("jakarta.persistence.jdbc.password", env.get(envName));
            }
        }
        emf =
                Persistence.createEntityManagerFactory("restjpa-pu", configOverrides);
    }

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in test package
        final ResourceConfig rc = new ResourceConfig()
                .packages("fr.univtln.bruno.demos.docker")
                .register(
                new LoggingFeature(
                        Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME),
                        Level.INFO,
                        LoggingFeature.Verbosity.PAYLOAD_ANY,
                        1000
                )
        );

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
        
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("Stopping server..");
                server.shutdown();//.stop() has been deprecated
            }
        }, "shutdownHook"));

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return server;
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        try {
            startServer();
            log.info("Press CTRL^C to exit..");
            Thread.currentThread().join();
        } catch (Exception e) {
            log.severe("There was an error while starting Grizzly HTTP server."+ e);
        }

    }
}

