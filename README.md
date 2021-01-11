# WebSocket Based Trade OHLC Server

* <B>Java Version Used:</B> Java 8
* <B>Web Server:</B> Spring Boot 2 embedded tomcat on port 8080 (change the port here if needed: trade-server/src/main/resources/application.properties)
* <B>Build Tool:</B> Apache Maven 3.5.2
* <B>Main Class:</B> trade-server/src/main/java/com.upstock.trade.server.Application

##### Assumption:
Upon receiving first valid subscription request from any client, server will start workers. It will wait until then.

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

[See Architecture Diagram Here](https://viewer.diagrams.net/?highlight=0000ff&edit=_blank&layers=1&nav=1#R5Vxbd6M2EP41Pqd9SA5IgOFxk721J92mx%2BnJtm%2FEKDZdbLlCTuz99SuMBEjiZoOx4%2FU%2BLAzDAHP5ZkaXjODtYvOJ%2BKv5HzhA0QgYwWYE348AME3XYf8llG1KcVwrJcxIGHCmnDAJvyNONDh1HQYolhgpxhENVzJxipdLNKUSzScEv8pszziSn7ryZ0gjTKZ%2BpFMfw4DOU6prGzn9Mwpnc%2FFk0%2BBXFr5g5oR47gf4tUCCH0bwlmBM06PF5hZFifKEXtL7PlZczV6MoCVtc8On3308%2F%2FI4mfi%2FUUKX%2F8ZfXh6uuJQXP1rzD%2BYvS7dCAwSvlwFKhBgjePM6DymarPxpcvWV2ZzR5nQRsTOTHWYfmZw8h1F0iyNMdoLgx%2BRnMzp%2FJiIUbSo%2FxsxUxHwL4QWiZMtY%2BA1AvCh3K5cr%2BTW3ke16KW1esI8t%2FMrnfjHLROeqYwdce3toEjZrEgXMtfgpJnSOZ3jpRx9y6o2s65znDuMVV%2Bp%2FiNItjxN%2FTbGsf6Y%2Bsv2a3H%2FtAXH%2Bz%2B4cjm1BeL%2FhT0jPtsWze0RCphFEODH9iuTV603F4tInM0RrVOSWm5SgyKfhiyy%2FzD781nscsidnrmC5Y8kVAFRMHOM1mSJ%2BVzFAVEGWJQkyLUVQ%2BoGaoHeE%2BNsC2yphiNu%2FsHhO7n2pxNwXM2Ud7p7WuMQ%2FnYiZ6yYIX9jhLDnE82gqyOwxhSslzAwEvqHkQ8v5S5z%2Fzn9i6UFyWD8KZ0t2PGXelPjcTQILIcPfd%2FzCIgyCNDZQHH73n3byEsfkambC7ZuR%2Fb4OV3hy4DfnkFx04uqgrgShK%2BPacAFX7KFeLFjw83OM6EiFoT5Mr1n%2BnuApimMGzNx2T0TY7ZdXTL4hAn7tlgcU6H%2B2k39JfqAEf0OFK87ul9yBl7RAT3%2F9JAtLBLKKEIVsYQqfKGYL0zlWtrDPK1uYcqpoShRtc0LvWO94Vrkl98V6x1aw3j4O1qsvLJ5zXKx3NO%2BixE%2FK6IsAZbsJlKGkclGZnS9A6%2BZiAB2sp6gan81Lwmfn%2FPC5rFoaHp%2Fb4myL2ht4RwFk6Mmm02rmtoAMTVf2gZbFd18x6GoG%2F%2FPzHRNl3O%2BqXD0Q%2F1ojxqk6Bet%2FV8lh4FN%2FQjHZjS68qWCEttKdmCXBaJQEo3usWPTeVCz2HmK2o4SY7R4WYjZQYBYogo4cYpZe9F5SWVIBsFw6q0oMz5T0f%2FZ1iYj8gsEeuMF%2BFjTUgu%2FkaGieplPchPRr4TjtE4HNT%2FM%2BMTmRxhO%2FFk%2BKt%2FXQX7aoe6B1FFA2gSdj6aGgDKwGQUcGZVMft47XT%2FGUhE96PLM4orJfNANte9COEsS%2FYeXWbOeuRQzY%2FY6C2mk41Q7xmY4ywNwNt4VkKEu9EgOJA%2BC6PiL4d5w0m4apmXyvrrIHwHWUcChpBZ0SuFWHb%2FqDW70T5LrScfjEuoIluWlYXbnnk5oOy0yNU2TtM1OaAGq0JSDqVClMzTwWdK49x8h%2F1rCJSG%2Fzfq5EVDE7WkhEthzu%2FaShcZnQ4%2BcgoPcWHFfhueGqJUruU%2BGq%2BIKCrtKJWmPFB6dOh7oCMznwmgfB7pCoa7dEXbMCdfN4NOHYOiQG951DglAeGrW4f1YOpUKjE789Nmr5QSd2YNtKQBRmwIYc3RB1%2FHlGVfegGrSWaR1V4z5qmb0jyJRTp%2BWB%2BogwzU78tl0fEsp8157s0HKrI6i36NDT8KD%2BDw4JgDyRZHedmf8XVn4O6P%2FAk6fEm%2FwZePv5v8pvw3qH9rw69iM5tD7oUQ%2F3F9hjgPr1bKzFMJRaQywQ7tpliGQ7QFrXZ5nEFK7xQPxlvAjjOMRLfQ7jYU6QHzC2e4wjzR3e7vIKMFbqQTERV%2BhnvJJ%2BRrRB%2Ffcz%2BoKXzBrqetNEL5IhnP%2FXWFy4ineJ5B1jMK3VJr8opDwiJtCYYG7%2FCSIv%2BaIa9u6pdPmJjPxUuYpVBwXZpEu8RIr9Oak9NJT5lux92Sp7ox8PEQVI7iGW5iGls1zqDHJ%2FLqIPu94j8ozJwl8yvTBTUj%2FJeMYdns0Sk15MtFpAHZbTR3VLo%2FVoGxsMTbtvbFR3yEbIa1sInqYRUgJd2zPUsLxrX37LGWK5q17ZlexWWK8CnybIoSf%2B1jse4hR0Lni%2FQ0V7UlhbqyycBt3qQ7ErcKjqEDonAbO8Q3WcsQxOhtWATh13ZbWErN6nl9QxEWApItqvDDWvDbl%2FzfrNgeamoF6NZJufLiLsnaawN9xucT5A36fPHz7ulsp3nE86q%2BJQbeWy7dWF4tAtKw5BtaW6FYcnnfI%2FbJhcHiU0GuC3R6SFVtvisGKLS%2Bvo62RTS18Sdllo1zjTDgxX2UPUcRhs6Nl1qJfEE7GY4pKaZRUPs864oVnOBjP6Dx59Cd25Ls9ti2y9l4bq4lkI3Wv7sOJQdQAIlUHLnvZxanNwsH5OvYH%2FSI0w0KK%2BegnVm4Ruqx4OEuj2upaq29IbDsFqdpr%2FeZeUPf8jOfDDDw%3D%3D)

![Alt text](Architecture_Diagram.png?raw=true)

### Brief description about each component in the architecture

###### Producer
Reads trades json file and pushes the individual trades to the trades queue.
* Module -> trade-producer
* Java Classes -> Worker1, DefaultTradeReader

###### Processor
Reads trade from trades queue and process it to generate one or more OHLC packets. Pushes the generated packets to the ohlc queue.
* Module -> trade-processor
* Java Classes -> Worker2, DefaultTradeProcessor, OHLCPacketHelper, TradeProcessorHelper

For the detailed processing logic on generating ohlc packets for a trade, See Java classes and documentations in DefaultTradeProcessor, OHLCPacketHelper, TradeProcessorHelper.

###### Worker3
1. Reads ohlc packets from ohlc packet queue and hands over them to subscription service by invoking OHLCPacketSubscriptionService.notifyAllListenerAsync() method.
2. Feeds latest data to performance tracker to update it's stats.
* Module -> trade-subscription
* Java Classes -> Worker3

###### Subscriber
Provides OHLC packet subscription service implementation for
 1. Registering and managing new listeners for the client's trades packet subscription requests.
 2. It maintains a mapping of listeners per subscription type (trading symbol in our case).
 3. Handing over new ohlc packets to a packet transmission thread pool so that worker3 don't block upon listener's action completion.
 
 * Module -> trade-subscription
 * Java Classes -> OHLCPacketSubscriptionService

###### Packet Transmission Thread Pool
A multi thread pool that transmits ohlc packet to WebSocket clients by invoking each of the mapped listener's SendToWebSocketOHLCPacketListener.onPacketReceived() method.
Since it's a pool of threads, it operates independently without blocking worker3 and subscription service.
Hence other components will be unaffected due to any packet transmission problems.

 * Module -> trade-subscription
 * Java Classes -> SendToWebSocketOHLCPacketListener, OHLCPacketListener

###### Performance Stat Logger
* Receives latest data from worker3, update it's stats and adds the updated stats to a logging queue.
* A separate logger thread reads stats from the logging queue and prints them.
* Check the generated log file perf.log under the root project directory.
* It starts logging server performance stats only after receiving first valid subscription from any client.
* It stops logging after the last trade is processed.
* Performance Stats are logged at a configured interval. It's configured as "perf.tracker.capture.interval.millis" under trade-server module application.properties file.

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
