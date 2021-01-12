# WebSocket Based Trade OHLC Server

* <B>Java Version Used:</B> Java 8
* <B>Web Server:</B> Spring Boot 2 embedded tomcat on port 8080 (change the port here if needed: trade-server/src/main/resources/application.properties)
* <B>Build Tool:</B> Apache Maven 3.5.2
* <B>Main Class:</B> trade-server/src/main/java/com.upstock.trade.server.Application

##### Assumptions and testing instructions:
* Upon receiving first valid subscription request from any client, server will start workers. It will wait until then.
* Server will stop all the workers once all of the trading packets have been processed.
* Once all of the packets have been received by client, it can resubmit the subscription request. Server will restart the workers in this case.
* Performance tracker will print logs only when workers are up. When workers restarts, perf tracker restart as well.

Example to test:

Client:
```
wscat -c ws://localhost:8080
Connected (press CTRL+C to quit)
> {"event": "subscribe", "symbol": "XETHZUSD", "interval": 15}
```

Server:
```
< {"volume":0.02,"event":"ohlc_notify","o":226.85,"h":226.85,"l":226.85,"c":0,"symbol":"XETHZUSD","bar_num":1}
< {"volume":5,"event":"ohlc_notify","o":226.85,"h":226.85,"l":226.85,"c":0,"symbol":"XETHZUSD","bar_num":1}
< {"volume":5,"event":"ohlc_notify","o":226.85,"h":226.85,"l":226.85,"c":226.85,"symbol":"XETHZUSD","bar_num":1}
< {"event":"ohlc_notify","symbol":"XETHZUSD","bar_num":2}
< {"volume":2.15891,"event":"ohlc_notify","o":226.61,"h":226.61,"l":226.61,"c":0,"symbol":"XETHZUSD","bar_num":2}
< {"volume":2.15891,"event":"ohlc_notify","o":226.61,"h":226.61,"l":226.61,"c":226.61,"symbol":"XETHZUSD","bar_num":2}
```

## Instructions to setup and start server on Linux
1. Make sure to have Java 8 and Maven already installed. Maven version used is 3.5.2.
2. From the project's root directory execute below
```
sh run.sh (Linux/MAC) 
run.bat (Windows)
```

### General Guidelines

* See application.properties file under trade-server module for the various configurations.
* Server will print the packets in the console that are being sent to clients. So the program output can also be verified there.
* To check performance stats, see perf.log file under the project's root directory.

## Architecture Diagram

[See Architecture Diagram Here](https://viewer.diagrams.net/?highlight=0000ff&edit=_blank&layers=1&nav=1#R5Vxbc6M2FP41nmkfkgEJMDwm2Vs76TYdp5Nt37BRbLoYuUJ24v31FUYySAIbm4tJ6n1YOBwEnMt3LpIygnfL18%2FEXy1%2BwwGKRsAIXkfwwwgA03Qd9l9K2WYUx7UywpyEAWfKCZPwB%2BJEg1PXYYASiZFiHNFwJRNnOI7RjEo0nxD8IrM940h%2B6sqfI40wmfmRTn0KA7rIqK5t5PQvKJwvxJNNg19Z%2BoKZE5KFH%2BCXAgl%2BHME7gjHNjpavdyhKhSfkkt33qeLq%2FsUIimmdGz7%2F6uPF16fJxP%2BFEhr%2FnXzdPF5ZXjbMxo%2FW%2FIv529KtEAHB6zhA6SjGCN6%2BLEKKJit%2Fll59YUpntAVdRuzMZIf7r0xPnsMousMRJuw8xjFKr1OCvyOFqH8K%2F7oNIhS9Fkj80z4jvESUbBkLtyubm9VLQUkWl%2FyioCB7zIk%2BN4z5fqhcduyAi%2B8EUZp9SXIE4Kf0ZzeUHL8KxItySbqGJknb9UokaXYlSXhckihgXspPMaELPMexH33MqbeyrHOee4xXXKj%2FIEq3HHL8NcWy%2FJlEyfZbev%2B1B8T5X7tzOLYF4cMrf0J2ti2ePSASMokgwomVyqI%2BmSN6QCJuxpd%2B9UGVEhT5NNzI2FWmH37rAw7Zq%2BxNwXLHkikAqKg4wWsyQ%2FyuItaoA1mWNJBpKQNlX6wNdEOIvy2wrVKGpP4Li%2Bfk1peNmNviXljnm6c11uxztZ5GYbIYASdiarwNwg07nKeHeBHNBJk9rXClhJlhwXeUfm85f4kP3PtTFnAlu%2FWjcB6z4xmzstT0blMoCFlEu%2BEXlmEQZC6CkvCHP92Nl9onlzYb3L4d2R9KLfagw6qYsw%2FL%2FCFS5CvDoivj2nABl%2B%2B5xixY8PNzguhIRaM2LEAzgAeCZyhJGD5z3U2J0NtPL5h8RwT83CwcKBHg2U7%2FaQGVXXF2v%2FQOHNMCPfu1EzMs4c8qUBSChglKwq%2FpdBU07GEFDVOOGMfiRaVOuoZ8x7PKNXkq5Du2Avl2N5CvvrB4TreQ72jWRYmfFibDBWXhEG2AMpRELvB%2BuACtq4sBdLCeoWp8Nt8TPjvDw2c9aboEPlfK9mgKDrx%2BABl6suq01LkuIEPTlW2gZg7elg%2B6msJ%2F%2F3LPhjIedlmu7oh%2FrBHjVI2ClcGr9DDwqT%2BhmOz6NW%2FKGaGtFClmiTMaJc7oduWLNZo%2Bl%2FTFrl3MdhQXs93zXMwGCswCZaCOXczSk97BpyXC9hqnJSwrMTxTkv%2Fg8xLh%2BQWFPXKF%2FV%2FQUHO%2Bi6OheZlK8TWk3wrHWZ0IbH6a14npidRW%2FFY8Kd52Tn15NO%2BBVj%2BgbIoES2DpuaAMrCMDdQzKpt6%2BTtbTZEbCqe7PzGmobBfHgbY%2BaEcp4t%2BydGu%2BM9ciBux%2B9VFb%2BEg7LT7TUfrMzXBbjAzlUa9EI7EHXNc7gn8mabFpmJrKT6oqWwBcR3GHklLQKYFbtX3THtzqlSCXlY7DF5YVLIlN%2FcrKHU5oOi8yHZ0pq9RahvcHhCOS78GEMDXyWNC59hwj%2F1n9BiK9zHsHgUi0FVoJRLbs7u2EoXHZoN3HIKDXFhxX4dBw1RLpxKVwdf8FuayyiVpjxZtTl0NdgZkceM2zYLdD1LVroq5ZF3VzfzTh2DrHB0%2BdQ4JQbo1a3D4rW6nQaMQvFh1VliyN2IFtKw5RmAHrs7sh8vhhelVzp%2Boyl6ntVeNOcpmTPciUQ6flgcMeYZqN%2BG37sEso810nskPLrfag1rxDD8O92j84xwHyQLK%2F67L2L7DvwvYPPHlK%2FJg9A%2B80%2B1f5bXjYoD3vEHtHBq03PQ7D%2FVupMUBr69lYiWEouYZYJ9y0yhDBtoewrs8yiSlc45H4cbIMkyTEsT6H8bggyA8Y2wPGkWYOb3d5BRgr%2BaCYiCvUM15JPSPKoPbrGX3By14b6nrTVC6SIpx%2F11hcuEp2geSGMZjW6jW%2FKEZ5QmxAY4K5%2FieIbPJFNezds9HlJzLytHIVqw4KpVsESrYS1IeGMtuSrW%2B%2F2P5gGDthlstWLUTfn1A6y6XOILdnInrb9QGRZ0yWfszkwlRJ%2FTTiGfd4Pk9V%2Bm681QJqW07v6pZ6a2f7GwxNum%2Bsq9thIeTVTQQHUggpjq7twjqyvOtUfsvpY7mrntmV7FZYrwKfpsihB%2F7aOx6SDHTe2n4H0WptZW2tsnAaNMsPxT7LvrJD6FwEzPIK1XHGMjgZ1hF0Om1zVm3I6nxlqNITAZYyRP2Voea1Idev%2B3qzp7kpWLKHSmx%2BGq7biyy7nX1Ozfy8h7pPnz982i2VbzifNKjkUC3l9hvWixuJy5JDUK2pZsnhRaf8z2uTy11C4wj8no%2B0YiL%2FeHJotw3JjXRqle1ortj9uXupGQo3YTxvlluhDYrp28ut2pzgB4arbF1q2H3re1If6pn4RKzheE81ugrD%2B4L8SI2%2B76G077P6yr2hrgqulH%2Ffa3YhdK%2Ft83JS1QAgVHqlLW0f1ab%2B4OGp%2FCP8HdXfQPP66pVbw4Fuq8rtz4Jur2mGvC294RysZqf53%2BnJ2PO%2FdgQ%2F%2Fgc%3D)

![Alt text](Architecture_Diagram.png?raw=true)

### Brief description about each component in the architecture

###### Producer
Reads trades json file and pushes the individual trades to the trades queue.
* Module -> trade-producer
* Java Classes -> Worker1, DefaultTradeReader

###### Processor

1. Reads trade from trades queue and process it to generate one or more OHLC packets. 
2. Publishes the generated packets to the local queue ohlc packet publisher.
3. Without affecting rest of the application a publisher can be implemented that publishes packets to a message broker topic.

* Module -> trade-processor
* Java Classes -> Worker2, DefaultTradeProcessor, OHLCPacketHelper, TradeProcessorHelper, LocalMQPublisher

For the detailed processing logic on generating ohlc packets for a trade, See Java classes and documentations in DefaultTradeProcessor, OHLCPacketHelper, TradeProcessorHelper.

###### Worker3

1. Reads ohlc packets from ohlc packet queue and hands over them to subscription service by invoking OHLCPacketSubscriptionService.handleNewPacket() method.
2. Without affecting rest of the application this can be enhanced to subscribe to a topic from a packet publishing message broker.

* Module -> trade-subscription
* Java Classes -> Worker3

###### Subscriber
Provides OHLC packet subscription service implementation for
 1. Registering and managing new listeners for the client's trades packet subscription requests.
 2. Generating Packet Receiving Event and handing over new events to a packet transmission thread pool so that worker3 don't block upon listener's action completion.
 3. Feeds latest data to performance tracker to update it's stats.
 
 * Module -> trade-subscription
 * Java Classes -> OHLCPacketSubscriptionService

###### Packet Transmission Thread Pool
A multi thread pool that transmits ohlc packet to WebSocket clients by invoking each of the listener's SendToWebSocketOHLCPacketListener.onEvent() method.
Since it's a pool of threads, it operates independently without blocking worker3 and subscription service.
Hence other components will be unaffected due to any packet transmission problems.

 * Module -> trade-subscription
 * Java Classes -> SendToWebSocketOHLCPacketListener, OHLCPacketListener

###### Performance Stat Logger
* Receives latest data from Subscriber, update it's stats and adds the updated stats to a logging queue.
* A separate logger thread reads stats from the logging queue and prints them.
* Check the generated log file perf.log under the root project directory.
* It starts logging server performance stats only after receiving first valid subscription from any client.
* It stops logging after the last trade is processed.
* Performance Stats are logged at a configured interval. It's configured as "perf.tracker.capture.interval.millis" under trade-server module application.properties file.
* It restarts logging when workers are restarted

Sample perf log stat:

{totalActiveSubscriptions=1, totalActiveSessions=1, totalTradesProcessed=178014, totalTimeTakenInSeconds=10, tradesProcessedPerSecond=17790.0}

* Module -> performance-tracker
* Java Classes -> ServerPerformanceTracker

###### Queues
Wrapper classes to ohlc queue and trade queue. The underlying concrete implementation of queue can be replaced with any messaging queue without affecting rest of the application component e.g. RabbitMQ, ActiveMQ, Kafka etc..
* Module -> trade-queue
* Java Classes -> OHLCPacketQueue, TradeRecordQueue

### Other Modules
* <B>trade-server:</B> 
   
   * Spring Boot based WebSocket Server Application Module.
   * See SocketHandler Class for WebSocket implementation.
   * See application.properties file for the various configurations.
   
### Concurrency Control
Since it's a multi worker, multi threaded application, Concurrency control has been taken care using ConcurrentHashMap, ReentrantLock, synchronized blocks at various places in application code.
