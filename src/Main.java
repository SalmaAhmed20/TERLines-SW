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
        model.add(StopArea, RDFS.subClassOf, Stops);
        model.add(StopPoints, RDFS.subClassOf, Stops);
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
        var cities = readFile("Dataset/cities.csv");
        for (int i = 1; i < cities.size(); i++) {
            var individual = City.createIndividual(baseUri + cities.get(i)[0].substring(cities.get(i)[0].lastIndexOf('/') + 1));
            individual.addProperty(cityName, cities.get(i)[0]);
            model.add(individual, latitude, ResourceFactory.createTypedLiteral(cities.get(i)[1], XSDDatatype.XSDfloat));
            model.add(individual, longitude, ResourceFactory.createTypedLiteral(cities.get(i)[2], XSDDatatype.XSDfloat));
        }
        var stops = readFile("Dataset/stops.txt");
        for (int i = 1; i < stops.size(); i++) {
            if (stops.get(i)[0].contains("StopArea")) {
                var area = StopArea.createIndividual
                        (baseUri + (stops.get(i)[0].substring(0, stops.get(i)[0].lastIndexOf(":")) + "_" + stops.get(i)[1]).replace(" ",""));
                area.addProperty(stopId, stops.get(i)[0].substring(stops.get(i)[0].lastIndexOf(":") + 1));
                area.addProperty(stopsName, stops.get(i)[1]);
                if (stops.get(i)[2].equals("")) {
                    model.add(area, stopsLat, ResourceFactory.createTypedLiteral(stops.get(i)[3], XSDDatatype.XSDfloat));
                    model.add(area, stopsLong, ResourceFactory.createTypedLiteral(stops.get(i)[4], XSDDatatype.XSDfloat));
                } else {
                    model.add(area, stopsLat, ResourceFactory.createTypedLiteral(stops.get(i)[2], XSDDatatype.XSDfloat));
                    model.add(area, stopsLong, ResourceFactory.createTypedLiteral(stops.get(i)[3], XSDDatatype.XSDfloat));
                }
                for (int j = i + 1; j < stops.size(); j++) {
                    if (stops.get(j)[0].contains("StopPoint")) {
                        var point = StopPoints.createIndividual
                                (baseUri + (stops.get(j)[0].substring(0, stops.get(j)[0].lastIndexOf(":")) +"_" + stops.get(j)[1]).replace(" ",""));
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
}