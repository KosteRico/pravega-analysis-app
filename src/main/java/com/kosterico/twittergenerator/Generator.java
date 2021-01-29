package com.kosterico.twittergenerator;

import com.kosterico.pravegaapplication.Constants;
import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.EventStreamWriter;
import io.pravega.client.stream.EventWriterConfig;
import io.pravega.client.stream.StreamConfiguration;
import io.pravega.client.stream.impl.JavaSerializer;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

final class Generator {

    public static void main(String[] args) {

        StreamManager streamManager = StreamManager.create(Constants.HOST);
        final boolean scopeIsNew = streamManager.createScope(Constants.SCOPE_NAME);

        final boolean streamIsNew = streamManager.createStream(Constants.SCOPE_NAME,
                Constants.STREAM_NAME,
                StreamConfiguration.builder().build());

        try (EventStreamClientFactory clientFactory = EventStreamClientFactory.withScope(Constants.SCOPE_NAME,
                ClientConfig.builder().controllerURI(Constants.HOST).build());
             EventStreamWriter<Tweet> writer = clientFactory.createEventWriter(Constants.STREAM_NAME,
                     new JavaSerializer<>(),
                     EventWriterConfig.builder().build());
             BufferedReader br = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:tweets.csv")))
        ) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                Tweet tweet = new Tweet(values[1]);

                Thread.sleep(2000);

                writer.writeEvent("key", tweet);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
