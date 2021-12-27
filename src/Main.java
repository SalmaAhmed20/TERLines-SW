import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
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
        OntClass StopArea = model.createClass( baseUri + "StopArea" );
        OntClass StopPoints = model.createClass( baseUri + "StopPoints" );
        model.add(StopArea,RDFS.subClassOf,Stops);
        model.add(StopPoints,RDFS.subClassOf,Stops);
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
        ObjectProperty isParentStation = model.createObjectProperty( baseUri + "isParentStation" );
        isParentStation.addDomain(StopArea);
        isParentStation.addRange(StopPoints);
        var cities = read_csv();
        for (int i = 1; i < cities.size(); i++) {
            System.out.println(cities.get(i)[1]);
            var individual = City.createIndividual(baseUri + cities.get(i)[0].substring(cities.get(i)[0].lastIndexOf('/') + 1));
            individual.addProperty(cityName, cities.get(i)[0]);
            individual.addProperty(latitude, cities.get(i)[1]);
            individual.addProperty(longitude, cities.get(i)[2]);
        }

        //for write on owl file
        FileWriter writer = new FileWriter("TERLines.owl");
        model.write(writer);
        writer.close();
    }

    static public Vector<String[]> read_csv() throws IOException {
        String line = "";
        Vector<String[]> cities = new Vector<String[]>();
        String[] columns;
        BufferedReader br = new BufferedReader(new FileReader("Dataset/cities.csv"));
        while ((line = br.readLine()) != null) {
            columns = line.split(",");
            cities.add(columns);
        }
        return cities;
    }
}