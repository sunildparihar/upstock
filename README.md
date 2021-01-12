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

[See Architecture Diagram Here](https://viewer.diagrams.net/?highlight=0000ff&edit=_blank&layers=1&nav=1#R5Vxbc6M2FP41nmkfkgEEGB6TbPbSSdN0nZ1s%2B4aNYtPFyBWyE%2B%2BvrzASF0kYbAMmqfdhkTgIdC7fuUjKCNwsXz9hb7X4HfkwHBma%2FzoCH0aGoeuOTf9LerZpj%2B2YacccBz4jyjsmwU%2FIOjXWuw58GJcICUIhCVblzhmKIjgjpT4PY%2FRSJntGYfmtK28OpY7JzAvl3qfAJ4u017G0vP8zDOYL%2FmZdY3eWHidmHfHC89FLoQvcjsANRoikV8vXGxgmzON8SZ%2F7WHE3%2BzAMI9LkgU%2B%2FeWhx%2FzSZeF8IJtHf8f3m8cJ002E2XrhmM2ZfS7acBRitIx8mo2gjcP2yCAicrLxZcveFCp32LcgypC2dXmazTBrPQRjeoBBh2o5QBJP7BKMfUOiUp8Jmt4GYwNdCF5vaJ4iWkOAtJWF6ZTG1eikIyWScXxQEZI1Zp8cUY54NlfOOXjD2HcBKvS9OjgzwMflZJ3KO3TX4hzJOOprESctxFZzUu%2BIkqOck9KmVsibCZIHmKPLC27z3uszrnOYOoRVj6j%2BQkC2DHG9NUJn%2FlKN4%2Bz15%2FtI1ePuvXRuMLd7x4ZW9IW1ti60HiAPKEYhZZ6WwiIfnkOzhiJPSJbPeK1IMQ48EmzJ2qeTDHn1AAf2UTBVMZ1xSBQMIIo7RGs8ge6qINeJAplkaSDeFgdIZSwNdYextC2SrhCBu%2FsH8Pbn2pSPmupgx63j1NMcK%2FbRDKr9rP9jQy3lyuVpPwyBeUDq0CGecgL6wQKN6zJv9gMmU0ztTLNKKIygM486bUi9cUmYvDOYRvZ5R1Uv08TrBh4C6uSt2Yxn4fmo3MA5%2BetPdeInSMhHQwa3rkfVBqcZ7rVgEosxXs5eU3KEKoC60S80xGNOP1XBOgp6fY0hGIkS1oRaSVjxgNINxjLAszV9eEP4BsfHraT5CcAvPVvJP8rL0jr37JU%2BgiBT60187jsTkRi6iR8GT6IbCJ%2Bt2V57EGpYnyRqpG6lzIpUy6doP2K6pluShfsC2BD9gdeMHxA%2Fm7%2BnWD9iSdhHsJdnKcEGZG0QboAxKLOd4P1yAlsVFAdpfz2A1PuvvCZ%2Ft4eGzKpLqH58reVsblxtuP4AM3LLopHi6KSAD3SnrQMPAvC0bdCSB%2F%2FH5jg6lPeziXtkQ%2F1xDSikqBc2NV8ml7xFvQhDeFXHelDECS8hcdIUxagpjdLqyxQaVoHPaYtcmZtmCiVnOcSZmAQFmDWGglmIe8T38gw%2F9rm5jJDkCH3yMxA3h5BiJhkiaq5eYPvggicNQQWCPTGD%2FF2iWkODs0KyfJ219Dcj3wnWatBoWa%2BZJa9IoFT6%2FFxvFx45JdmuDMGD24yF0Hu1xAD3WQxhmzUAdB2G6XGCP19N4hoOpbM%2FUaEhZL%2BqBtjlohwniX9PYb75T1yIG7H7NUZvbSDv1Rt0WKuGn4TYfGZRHveBVzR5wXS5PfouTzFfTJZEflOK2ALi2YA6KvNRWwK1YS2oPbuW0lPFKxuEz8woofFO%2FvHKG45qO80y1a3mVUkvxfg9zeDFlMC5M9DwmsC9dW8t%2FZr%2BOSM4534Ej4jWOVhyRVTb3dtzQWDVo9z7IkHMLhqtgaLhq8nDiXLiazSDnVbqOrK1Ypex8qMsxkwGvfhTsdoi6VkPU1Zuibm6POhibx9jgocUdAMp1WpPpZ2VdVyjuHErPt0VVpiwnkRuWJRhEodTUZ3WDx%2FHDtKrTjarLWKaxVY07iWUOtiC97DpN19hvEbp%2BEr1l7TcJYfHtQHJg9lCsNWQ33Kv%2BG8cYQO5IsqfOq%2F8c%2B86s%2F4ZbXp%2Bv02fDPUz%2FRXq%2BvFBF77r7yDtSaLnosR%2Fu30qOYbS2uY6mGJoQa%2FCdzKdmGdzZ9uDW5VUmvp6sPWIvipdBHAcoktcwHhcYej4le0AolNTh7e71MMZCPMgX4gr5jKvIZ3ga1H4%2BI%2B%2B%2ByaQhbnFN%2BFIShP3vGvEbF%2FHOkVxRAt1cveY3%2BShPkA6oTRCT%2FwTiTb7Dh357Orq0qXZauaVWBgXlIQbFYYfm0KDSrbL2ZccB9rqxA1a5LFFD5BMUylUucTm7PRWRy64PED8jvPQiyhcqSuIlHk%2B7Q%2FN5ItJ3Y62mIZbl5Kqu0lo7O4FhnyUSzIM62x6XMxvNrInsDjtxUR%2Fu9bWzS0gjDFMYovnOLv1SK4d8WYjWUzkXyAacHWdQaNMwdntwVW%2FpnMJpoVMPoZJccn%2FabXU9sQQ7KDwVo5%2FsFGrxdKAKT41qSZ2Gp2ddJTuuslROrLUa%2BD0eafnaV31ibbUNySfJ1FQdU5TOcx0a07qqkPbgwNhQjcJzXsqfGQw2QTSvj4oruvefYRvQnOEGRqRmPsozdk0mPlyf1uYSpKE5wkmPE%2BsDHS47Xt0GUTS9B5Ovprv6ApZb%2F8u98mC2pLPfVr5HYGMV56nIAarxhrZENFtFEHC4a%2FQfnxPsgVxJnPCdCu8pExUjpyztrMlEs0pB%2B25W3p821L2vlfzve2cqAM6lVR6k8d5UQQEAECqCLZ1ekBa4wP4F6xr6jk4vGJLVV%2B9PGo77N6vM%2Fij3756a1G6VDxzj72kz%2F3s5KXn%2BV4fA7X8%3D)

![Alt text](Architecture.png?raw=true)

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
