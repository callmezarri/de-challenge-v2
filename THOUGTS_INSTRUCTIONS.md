
# Architecture Case

Lest start we the assumptions:
- There is five minutes between each information dispatch to outside EPL
- information is sent to external costumers, this means they ar not subscribed and there is a way for the dispatcher to know them wich is not part of the scope.
    - if it was part of the scope, then using the same architecure i will add a topic which will be populated by another k8s service. Then the dispatcher(s) will get targets from it
- The database will be consumed by other services like the ETL of the challenge to generate statistcs, etc. so thats why the information is being stored
    - Assuming a scoreboard update is needed it should be done after match and was not part of the scope
    - If you need a score board update and is was part of the scope, adapting a service similar to the challenge can be added and trigger after each match with a new topic that is populated after geting the last message from the device (Assuming there is an end message)
    - ETL and services related to seasonsonal information are not included, because the awards are yearly.
        - If needed another ETL job can be added which is triggered after each Scoreboard update, and the Web Page / Api will read from its result
- Data sended by the divices is incremetal IE, if someone is changed mid game and enter again his time counter will not start from 0
- The resposability of sendind player data to the teams resides on the EPL
    - It is not crossed with the match before sending
        - A new topic should be added and Player Static Writer should publish to it too
        - A new Service Should be added, it should get a whole team partial statistics and update the redis cache, after this it should publish to the data ready topic
        - Dispatcher should read the data ready topic and send to the respective team analytics center
    - If it is crossed with the match before sending then the actual model should suffice
- If speed is not enough, an apache ignite intermediate layer can be put between services (Player Statistic Writer, Match Statistic Writer, Partial Result Reader) and Database, in this way queries will run faster

# ETL Job

There are two etl jobs, one that writes to a h2 database (Spring Batch) and one that writes files made with python (Pandas).
Assumptions:
- Spring Batch
    - There are no time constraint in which it should run
        - If needed, changing the intial Json reader to one that can read from spark or beam would speed up things (in my pc it still runs under 360 ms, even the docker version) 
    - This ETL is meant to run each end of season so the volume each season should be similar, thats why no extra in memory-caching is used (spark, beam, etc.)
    - This ETL was writting with persistance in mind. The only need things you need to change to add new seasons data is: 
        - change the sql schema so it doesn't drop the tables
        - delete the datasets from the input folde after each run and add new ones following the same naming convention
        - changing the database to a persistent one
- Python
    - It seemed fun
    - I was working with DaskMS and wanted to test Dask, but unfortunally there is no json_normalize in Dask DataFrames. Json normalization could be done on Dask but it also required to load all data in memory so it was pretty much the same as pandas, the only visible advantage was chunk processing on the DataFrames, but if you are ram contraint it was not a real speed
    - Data structures are similar to Java code, so if you change the writters to connect to a db, you can use pandas to_sql() and use the same model, but you need to add goal difference and played matches to the scoreboard DataFrame

# Deployment
To deploy the code you need docker. To build the image go to the deploy folder and run `docker build -t challenge -f Dockerfile ..`

After the image is created (slow because of mvn dependcies, i didn't want to add the jar so dependecies are installed in a layer), then run `docker run -p 8080:8080 challenge` the first 8080 can be changed based on which port do you want to use to access spring boot.

The spring boot deployment comes with 2 ways of checking the results one are two APIs `/api/v1/{scoreboard, statistics}` both of them have `/` for all and `/{season}/` to filter by season additionally there is `/api/v1/{scoreboard}/{season}/{team}` for the results of an specific team, also you can login on `/h2-console` with `user: sa` and `password: password`, be sure to change the connection string to `jdbc:h2:mem:test`

To check python results you can either run `docker run -p 8080:8080 challenge -v` (this process is slower cause terminal I/O :P) and they will be shown in the console before the spring boot compilation.

You can access the docker with `docker exec -it {id} /bin/bash` where `id` is the id of the container you get when running `docker ps -a | grep /tcp | awk '{print $1}'` while non other container is binded to a tcp port, if not, you need to identify it manually and put the id then run `cd srcPython/output ; cat *`.

If you want you can try this oneliner that will do everything for you `docker stop $(docker ps -aq) ; docker run -p 8080:8080 challenge ; docker exec -it $(docker ps -a | grep /tcp | awk '{print $1}') /bin/bash -c "cd srcPython/output ; cat *"`, or if you preffer the `-v` option run `docker stop $(docker ps -aq) ; docker run -p 8080:8080 challenge -v` **this will stop all your other containers**. 
