import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Main {
    public static void main(String[] args) throws IOException {
        String baseUri = "http://www.semanticweb.org/20180122-20180078-20180040/ontologies/2021/11/Project2#";
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        //---------City Class---------
        OntClass City = model.createClass(baseUri + "City");
        //----- Data Property of city ---
        DatatypeProperty cityName = model.createDatatypeProperty(baseUri + "CityName");
        cityName.addDomain(City);
        cityName.addRange(XSD.xstring);
        DatatypeProperty latitude = model.createDatatypeProperty(baseUri + "Latitude");
        latitude.addDomain(City);
        latitude.addRange(XSD.xfloat);
        DatatypeProperty longitude = model.createDatatypeProperty(baseUri + "longitude");
        longitude.addDomain(City);
        longitude.addRange(XSD.xfloat);

        //---------Stops,StopArea,StopPoints Classes---------
        OntClass Stops = model.createClass(baseUri + "Stops");
        OntClass StopArea = model.createClass(baseUri + "StopArea");
        OntClass StopPoints = model.createClass(baseUri + "StopPoints");
        OntClass StopPointsByTrain = model.createClass(baseUri + "StopPointsByTrain");
        OntClass StopPointsByOther = model.createClass(baseUri + "StopPointsByOther");
        model.add(StopPointsByTrain, RDFS.subClassOf, StopPoints);
        model.add(StopPointsByOther, RDFS.subClassOf, StopPoints);
        //----- Data Property of Stops ---
        DatatypeProperty stopId = model.createDatatypeProperty(baseUri + "stopId");
        stopId.addDomain(Stops);
        stopId.addRange(XSD.xstring);
        DatatypeProperty stopsName = model.createDatatypeProperty(baseUri + "stopsName");
        stopsName.addDomain(Stops);
        stopsName.addRange(XSD.xstring);
        DatatypeProperty stopsLat = model.createDatatypeProperty(baseUri + "stopsLat");
        stopsLat.addDomain(Stops);
        stopsLat.addRange(XSD.xfloat);
        DatatypeProperty stopsLong = model.createDatatypeProperty(baseUri + "stopsLong");
        stopsLong.addDomain(Stops);
        stopsLong.addRange(XSD.xfloat);
        //----ObjectProperty-----
        ObjectProperty isParentStation = model.createObjectProperty(baseUri + "isParentStation");
        isParentStation.addDomain(StopArea);
        isParentStation.addRange(StopPoints);
        ObjectProperty CloserTo = model.createObjectProperty(baseUri + "CloserTo");
        CloserTo.addDomain(City);
        CloserTo.addRange(Stops);
        //---------Trips---------
        OntClass Trips = model.createClass(baseUri + "Trips");
        //----- Data Property of Trips ---
        DatatypeProperty service_id = model.createDatatypeProperty(baseUri + "service_id");
        service_id.addDomain(Trips);
        service_id.addRange(XSD.xstring);
        DatatypeProperty tripId = model.createDatatypeProperty(baseUri + "tripId");
        tripId.addDomain(Trips);
        tripId.addRange(XSD.xstring);
        //----ObjectProperty-----
        ObjectProperty has_a = model.createObjectProperty(baseUri + "has_a");
        has_a.addDomain(Trips);
        has_a.addRange(StopPoints);
        //---------Routes---------
        OntClass Routes = model.createClass(baseUri + "Routes");
        //----- Data Property of Routes ---
        DatatypeProperty RouteId = model.createDatatypeProperty(baseUri + "RouteId");
        RouteId.addDomain(Routes);
        RouteId.addRange(XSD.xstring);
        DatatypeProperty agencyId = model.createDatatypeProperty(baseUri + "agencyId");
        agencyId.addDomain(Routes);
        agencyId.addRange(XSD.xstring);
        DatatypeProperty routeShortName = model.createDatatypeProperty(baseUri + "routeShortName");
        routeShortName.addDomain(Routes);
        routeShortName.addRange(XSD.xstring);
        DatatypeProperty routeLongtName = model.createDatatypeProperty(baseUri + "routeLongtName");
        routeLongtName.addDomain(Routes);
        routeLongtName.addRange(XSD.xstring);
        //----ObjectProperty-----
        ObjectProperty hasRoute = model.createObjectProperty(baseUri + "hasRoute");
        hasRoute.addDomain(Trips);
        hasRoute.addRange(Routes);
        //----------individual of city---------
        var cities = readFile("Dataset/cities.csv");

        for (int i = 1; i < cities.size(); i++) {
            var individual = City.createIndividual(baseUri + cities.get(i)[0].substring(cities.get(i)[0].lastIndexOf('/') + 1));
            individual.addProperty(cityName, cities.get(i)[0]);
            model.add(individual, latitude, ResourceFactory.createTypedLiteral(cities.get(i)[1], XSDDatatype.XSDfloat));
            model.add(individual, longitude, ResourceFactory.createTypedLiteral(cities.get(i)[2], XSDDatatype.XSDfloat));
        }
        //----------individual of stops---------
        var stops = readFile("Dataset/stops.txt");
        for (int i = 1; i < stops.size(); i++) {
            if (stops.get(i)[0].contains("StopArea")) {
                var area = StopArea.createIndividual(baseUri + (stops.get(i)[0]).replace(" ", ""));
                area.addProperty(stopId, stops.get(i)[0].substring(stops.get(i)[0].lastIndexOf(":") + 1));
                area.addProperty(stopsName, stops.get(i)[1]);
                int closer = get_Closer(cities, i, stops);
                var individual = City.createIndividual(baseUri + cities.get(closer)[0].substring(cities.get(closer)[0].lastIndexOf('/') + 1));
                area.addProperty(CloserTo, individual);
                if (stops.get(i)[2].equals("")) {
                    model.add(area, stopsLat, ResourceFactory.createTypedLiteral(stops.get(i)[3], XSDDatatype.XSDfloat));
                    model.add(area, stopsLong, ResourceFactory.createTypedLiteral(stops.get(i)[4], XSDDatatype.XSDfloat));
                } else {
                    model.add(area, stopsLat, ResourceFactory.createTypedLiteral(stops.get(i)[2], XSDDatatype.XSDfloat));
                    model.add(area, stopsLong, ResourceFactory.createTypedLiteral(stops.get(i)[3], XSDDatatype.XSDfloat));
                }
                for (int j = i + 1; j < stops.size(); j++) {
                    Individual point;
                    if (stops.get(j)[0].contains("StopPoint")) {
                        if (stops.get(j)[0].contains("Train")) {
                            point = StopPointsByTrain.createIndividual
                                    (baseUri + (stops.get(j)[0]).replace(" ", "-"));
                        } else {
                            point = StopPointsByOther.createIndividual
                                    (baseUri + (stops.get(j)[0]).replace(" ", "-"));
                        }
                        point.addProperty(stopId, stops.get(j)[0].substring(stops.get(j)[0].lastIndexOf(":") + 1));
                        point.addProperty(stopsName, stops.get(j)[1]);
                        if (stops.get(i)[2].equals("")) {
                            model.add(point, stopsLat, ResourceFactory.createTypedLiteral(stops.get(j)[3], XSDDatatype.XSDfloat));
                            model.add(point, stopsLong, ResourceFactory.createTypedLiteral(stops.get(j)[4], XSDDatatype.XSDfloat));
                        } else {
                            model.add(point, stopsLat, ResourceFactory.createTypedLiteral(stops.get(j)[2], XSDDatatype.XSDfloat));
                            model.add(point, stopsLong, ResourceFactory.createTypedLiteral(stops.get(j)[3], XSDDatatype.XSDfloat));
                        }
                        area.addProperty(isParentStation, point);
                    } else {
                        break;
                    }
                }
            }
        }
        // --------------------- individual of routes------------------------
        var routes = readFile("Dataset/routes.txt");
        for (int i = 1; i <= routes.size() - 1; i++) {
            var route = Routes.createIndividual(baseUri + routes.get(i)[0]);
            route.addProperty(RouteId, routes.get(i)[0]);
            route.addProperty(agencyId, routes.get(i)[1]);
            route.addProperty(routeShortName, routes.get(i)[2]);
            route.addProperty(routeLongtName, routes.get(i)[3]);
        }
        // --------------------- individual of trips------------------------
        var trips = readFile("Dataset/trips.txt");
        for (int i = 1; i < trips.size(); i++) {
            var trip = Trips.createIndividual(baseUri + trips.get(i)[2].replace(" ", ""));
            trip.addProperty(service_id, trips.get(i)[1]);
            trip.addProperty(tripId, trips.get(i)[2]);
            var related = Routes.createIndividual(baseUri + trips.get(i)[0].replace(" ", ""));
            trip.addProperty(hasRoute, related);
        }
        // ------------- individual of stops times ---------------------
        var stopsTime = readFile("DataSet/stop_times.txt");
        for (int i = 1; i < stopsTime.size(); i++) {
            var trip = Trips.createIndividual(baseUri+stopsTime.get(i)[0].replace(" ",""));
            if (stopsTime.get(i)[3].contains("Train")) {
                var point = StopPointsByTrain.createIndividual(baseUri+stopsTime.get(i)[3].replace(" ","-"));
                trip.addProperty(has_a,point);
            }
            else
            {
                System.out.println(stopsTime.get(i)[0]);
                var point = StopPointsByOther.createIndividual(baseUri+stopsTime.get(i)[3].replace(" ","-"));
                trip.addProperty(has_a,point);
            }

        }


        //for write on owl file
        FileWriter writer = new FileWriter("TERLines.owl");
        model.write(writer);
        writer.close();
    }

    static public Vector<String[]> readFile(String path) throws IOException {
        String line;
        Vector<String[]> Row = new Vector<String[]>();
        String[] columns;
        BufferedReader br = new BufferedReader(new FileReader(path));
        while ((line = br.readLine()) != null) {
            columns = line.split(",");
            Row.add(columns);
        }
        return Row;
    }

    static public int get_Closer(Vector<String[]> cities, int idx, Vector<String[]> stops) {
        double min = Double.POSITIVE_INFINITY;
        int indexStops = -1;

        double latStops = Math.toRadians(Float.parseFloat(stops.get(idx)[3]));
        double longStops = Math.toRadians(Float.parseFloat(stops.get(idx)[4]));
        for (int j = 1; j < cities.size(); j++) {
            double latCity = Math.toRadians(Float.parseFloat(cities.get(j)[1]));
            double longCity = Math.toRadians(Float.parseFloat(cities.get(j)[2]));
            // Haversine formula
            double dlon = longStops - longCity;
            double dlat = latStops - latCity;
            double a = Math.pow(Math.sin(dlat / 2), 2)
                    + Math.cos(latCity) * Math.cos(latStops)
                    * Math.pow(Math.sin(dlon / 2), 2);
            double c = 2 * Math.asin(Math.sqrt(a));
            // Radius of earth in kilometers. Use 3956
            // for miles
            double r = 6371;
            double distance = c * r;
//                    System.out.println( "distanse:- "+ distance);
            if (distance < min) {
                min = distance;
                indexStops = j;
            }
        }
        return indexStops;
    }
}