# TERLines-Semantic web
The final goal is to integrate information coming from different sources, deploy a SPARQL
Endpoint to query the data, and obtain answers and information related to their
Needs. Specifically, you have to combine data from TER lines and schedules
(https://ressources.data.sncf.com/explore/dataset/sncf-ter-gtfs/) with data coming from 
Wikidata.
A user should be able to do the following:

 1- Search for a combination of trains that allow him/her to travel from one city to 
another.

 2- Have the ability to get information about relevant places and events near the stops 
along the travel extracted from Wikidata or DBpedia.

Do the following-:

a. Choose a namespace for the IRIs

b. Decide what data needs to be transformed into either classes, resources, 
 Properties or literals
 
c. Process the data to create the triples

Deploy RDF Files to apache Jena FUSEKI.
Define the necessary SPARQL queries to extract the necessary data, such as:

1. Find the city that that is closer to a stop, according to their latitude and longitude
Values

2. Find the possible trains to go from one city to another, and the stops
3. Find all stop areas with their stop points.

Note that city include stop areas and stop areas include stop points
+ Cities data included
1. Transform data from TER lines and schedules to RDF using the Jena framework. In 
In order to do that, it is necessary to:
a. Choose a namespace for the IRIs
b. Decide what data needs to be transformed into either classes, resources, 
 Properties or literals
c. Process the data to create the triples
2. Deploy RDF Files to apache Jena FUSEKI.
3. Define the necessary SPARQL queries to extract the necessary data, such as:
a. Find the city that that is closer to a stop, according to their latitude and longitude
Values
b. Find the possible trains to go from one city to another, and the stops
c. Find all stop areas with their stop points.
Note that city include stop areas and stop areas include stop points
+ Cities data included
