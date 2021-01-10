# WebSocket Based Trade OHLC Server

* <B>Java Version Used:</B> Java 8
* <B>Web Server:</B> Spring Boot 2 Embedded tomcat on port 8080 (change port here if needed: trade-server/src/main/resources/application.properties)
* <B>Build Tool:</B> Maven

#### Assumption: Upon receiving first request from any client, server will start workers. It will wait until then


## Data Model

There is only one table Bank_Account designed in order to stick to basic requirement i.e. transfer money without any user authentication. Hence, i have prepopulated this table with 5 accounts as below
```
INSERT INTO BANK_ACCOUNT (BANK_ACCOUNT_ID,ACCOUNT_NUMBER,ACCOUNT_NAME,BALANCE) VALUES ('1','093801051','account-1',1000.00);
INSERT INTO BANK_ACCOUNT (BANK_ACCOUNT_ID,ACCOUNT_NUMBER,ACCOUNT_NAME,BALANCE) VALUES ('2','093801052','account-2',1500.00);
INSERT INTO BANK_ACCOUNT (BANK_ACCOUNT_ID,ACCOUNT_NUMBER,ACCOUNT_NAME,BALANCE) VALUES ('3','093801053','account-3',1300.00);
INSERT INTO BANK_ACCOUNT (BANK_ACCOUNT_ID,ACCOUNT_NUMBER,ACCOUNT_NAME,BALANCE) VALUES ('4','093801054','account-4',7000.00);
INSERT INTO BANK_ACCOUNT (BANK_ACCOUNT_ID,ACCOUNT_NUMBER,ACCOUNT_NAME,BALANCE) VALUES ('5','093801054','account-5',8000.00);
```


## maven command to build the application
```
mvn clean package
```
## command to start server on Linux
From the project's root directory execute below
```
sh run.sh (Linux/MAC) or run.bat (Windows)
```

## command to run tests
All the integration test cases have been written inside ft-engine module. From project root directory, run below
```
mvn clean install

Test Cases have been written for AccountService and FundsTransferService.
There are test cases cases written testing concurrency with 1000 threads.
```

## Architecture Diagram

[See Architecture Here](https://viewer.diagrams.net/?highlight=0000ff&edit=_blank&layers=1&nav=1#R7Vtbl5s2EP41Pqd92D2AuPlx7d1NTk%2FaunV6sn2UQcZ0seUI%2BZZfH2EkQMj4go2hTfOyaJDGYma%2BT6OR0gPD%2BfYDgcvZr9hHUc%2FQ%2FG0PPPcMQ9ddm%2F1JJLtUYjtWKghI6PNOuWAcfkNcqHHpKvRRLHWkGEc0XMpCDy8WyKOSDBKCN3K3KY7kX13CACmCsQcjVfol9OkslbqGk8s%2FojCYiV%2FW7X76Zg5FZ%2F4l8Qz6eFMQgZceGBKMafo03w5RlBhP2CUd91rxNpsYQQt6zoAPwzftl%2FXwzflKnen6lUz7f74%2BcC1rGK34B%2FPJ0p2wAMGrhY8SJVoPDDazkKLxEnrJ2w3zOZPN6DxiLZ09Zh%2BZ9J2GUTTEESZ7RWC6%2F8fkAYF%2ByGYt3i3wgmkb8KkgQtG28hv1zHIs5BCeI0p2rAsfYGggHcKjzeS23%2BSus1zuoFnBbabLO0IeLkGmOrcoe%2BBGvcDAxo0NfAsjAU0ykt5XraSLqC1aybCbshI4bSXkM1zyJiZ0hgO8gNFLLh3Idsz7fMJ4ya33D6J0x0kGriiWbcvsR3ZvxcbfibJHSzSft1x52trxVjrXZIKXeoSgCNJwLY87ZF0%2BdIRDpjHzpOkYhz0pVMR4RTzERxW5oazI6MuKtJIiCkmAqKLoiRC4K3RbJh3iCyasaaXYSTXmkZQZq35wmUpwjQj2UBwz8jHsiHloMEmeguTppw0m74gYP7cOU1MMuRSmemMwtf6HaR2YZjnPtTC1QUmR3gxMlQnrd4CpfQim%2FspD1SjV20ep3T2UOt1CKQOmC4pIfdAeNc0%2BAdZ9a4RIyGyCyIUIZnuEPRBO5WU3RzpwStFQXkfPRTrQKsLqBNJvBUZXiaHfP35iqrQR9N6ZYRVE%2FrFCrGc5zth2YJk8%2BpDCMcVkv9m6AyoBcCXzOQdAqR0AZWPbgH4nMNnaCmiZpXDW3Xq4sMq4cEuKGsaFWHALjvzMdrMo7joiyh5oHRG6WnoYryaxR8LJkRUfqCs%2BswktFSEowe%2BoVF0oFiO4CEZhsGBNj9kwWWMGiYVDD0ZP%2FMU89P2oyj8yGm%2FgIsOSN0dZDazgI8dVXQQac1E7icM2pG%2BF5zS5NyzezPOFpNF6bp%2Bl4JnTajJbuSqjKGqa2dQaTCzweAbmCIrDb3Cy75A4hG8xWG9r0LOeL0JbBCcoGrBEI9gH06ESYiXceB2Yz6SXVV%2BLkXAk2CvBmSStuu3ILkpbdSNIdMHTaYya8ala%2BvgrTthVrKItbpys00VI%2B8B6ZDVGdta%2FhuzyrZVcAAGW2%2BSuqnnu7Ns34s6yoqa5U61d%2FFjcaZ3izlLW3nniVGsonDjVlKhl4jyUyd%2BXON3uEGc93nT6DVejUh47YkOBtMbLVtd5Wq1i%2FFg0555MEeW6Oeg6zxlqOYPzHOgazwFxdNgWzxlqwQLPIm%2F%2FTWlNtD0WZBxmFYhQr0WDtzo%2FO4fvrDP5Lt3BHM0sgGPWgdmlZ2JAl3MYSz66VnNS7ZruBt%2BOHDxxuytDGF2O%2BuuD3ulk1DtXLvO1Itzoy%2FVHcSmq%2Bozqmu5Ac6sj%2FGbRqy5jd41Po06A5kScjepYfBbuL94zPkuXh0z7eMC52rHuDQWcWnE7Tpf%2FwTQ5BV3lmp2c%2FpeOZx%2BuLAiIbEAvlX0aXBbVq1A8cR6jOA7xgj39hmk43alHWZ9nBEGfdRhhHCkBcffboKVTJyCuAhZPnQ5cmVWuNtwu0VaraZkRhQ2FYIr3AZDbz%2F66wuLFQ7xn6CfWQTeX2%2Fyl0PIFTRKP4RSZzHVknZ89srmn2uVfZOLCLE6iuY2zSOkS9g1CxDSdR3lja4qjwVPHx4Z7cZCwZn4pPsVr%2Fl8LwMt3)

## Architecture of Application
This is a multi layered application with clear separation between individual layers. Following are the layers

1. Database : In-Memory H2 DB
2. Data Access (Persistence) layer
3. Core Business Layer
4. Service Gateway 
5. Application Layer (Rest Services)

Each and every layer can independently be modified and enhanced without affecting any other layer. 

The application flow goes as below:

Rest Client ==> Rest Service ==> Service Gateway ==> Service Locator ==> Core Business Service ==> Data Access Layer ==> DB

### Concurrency Control (support for multiple systems and services invoking our application)
Concurrency control has been handled at both the levels i.e. at database layer using DB table row level locks and at application layer. Ideally for this test application locking using DB table row is sufficient. However for demonstration purpose i have added locking at java layer too. In a real world scenario with application clustering we may not choose to put locking at application layer based on our priorities such as load on DB should be minimum or can afford DB load over heavy locking at java layer. But locking at DB level will be a must. We can also make use of JPA to perform DB locking in a more standard way which avoids writing any DB vendor specifc sql queries.

### Brief Description about each layer

## Basic API validations
* 'Get Account By Id' : Account existance
* Deposit Money : Account existance and deposit amount validation
* Withdraw Money : Account existance, withdraw amount validation and balance check
* Transfer Money : Source & Destination Account existance, transfer amount validation, source account balance check and credit back to source account in case of deposit failure to destination account.

