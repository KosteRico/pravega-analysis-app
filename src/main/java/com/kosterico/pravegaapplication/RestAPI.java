package com.kosterico.pravegaapplication;

import com.kosterico.twittergenerator.Tweet;
import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.stream.*;
import io.pravega.client.stream.impl.JavaSerializer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

@RestController
public class RestAPI {

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/tweets")
    Map<String, Object> getTweets(@RequestParam(name = "filter", defaultValue = "") String filterQuery, @RequestParam(defaultValue = "100") Integer limit) {
        final String readerGroup = UUID.randomUUID().toString().replace("-", "");

        final ReaderGroupConfig readerGroupConfig = ReaderGroupConfig.builder()
                .stream(Stream.of(Constants.SCOPE_NAME, Constants.STREAM_NAME))
                .build();

        try (ReaderGroupManager readerGroupManager = ReaderGroupManager.withScope(Constants.SCOPE_NAME, Constants.HOST)) {
            readerGroupManager.createReaderGroup(readerGroup, readerGroupConfig);
        }

        int[] stats = new int[5];

        LinkedList<Tweet> tweets = new LinkedList<>();

        try (EventStreamClientFactory clientFactory = EventStreamClientFactory.withScope(Constants.SCOPE_NAME,
                ClientConfig.builder().controllerURI(Constants.HOST).build());
             EventStreamReader<Tweet> reader = clientFactory.createReader("reader",
                     readerGroup,
                     new JavaSerializer<>(),
                     ReaderConfig.builder().build())) {
            Tweet tweet = null;

            int counter = 0;

            do {
                try {
                    tweet = reader.readNextEvent(10000).getEvent();

                    if (tweet != null) {
                        tweet.analyzeType();

                        int type = tweet.getType();

                        stats[type]++;

                        tweets.addFirst(tweet);
                    }
                } catch (ReinitializationRequiredException e) {
                    e.printStackTrace();
                }
            } while (tweet != null && counter++ < limit);
        }

        System.out.println(limit);
        System.out.println(tweets.size());

        Map<String, Object> response = new HashMap<>();

        response.put("stats", stats);
        response.put("count", tweets.size());
        response.put("tweets", tweets);

        return response;
    }
}
