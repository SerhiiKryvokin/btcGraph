#### btcGraph
draws a chart based on data retrieved from blockchain api  
on startup loads 100+ newest blocks and then tries to load new blocks each minute  
it is assumed that height of blocks is always unique  

##### how to run  
execute command from root directory : *mvn spring-boot:run*  
open localhost:8080/ to see the graph  
