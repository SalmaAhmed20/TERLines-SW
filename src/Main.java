import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.XSD;


import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String baseUri = "http://www.semanticweb.org/20180122-20180078-20180040/ontologies/2021/11/Project2#";
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM );
        //---------City Class---------
        OntClass City = model.createClass(baseUri+"City" );
        //----- Data Property of city ---
        DatatypeProperty cityName = model.createDatatypeProperty(baseUri+"CityName");
        cityName.addDomain(City);
        cityName.addRange(XSD.xstring);
        DatatypeProperty latitude = model.createDatatypeProperty(baseUri+"Latitude");
        latitude.addDomain(City);
        latitude.addRange(XSD.xfloat);
        DatatypeProperty longitude = model.createDatatypeProperty(baseUri+"longitude");
        longitude.addDomain(City);
        longitude.addRange(XSD.xfloat);
        //---------Stops Class---------
        OntClass Stops = model.createClass(baseUri+"Stops" );
        //----- Data Property of Stops ---
        DatatypeProperty stopId = model.createDatatypeProperty(baseUri+"stopId");
        stopId.addDomain(Stops);
        stopId.addRange(XSD.xstring);
        DatatypeProperty stopsName = model.createDatatypeProperty(baseUri+"stopsName");
        stopsName.addDomain(Stops);
        stopsName.addRange(XSD.xstring);
        DatatypeProperty stopsLat = model.createDatatypeProperty(baseUri+"stopsLat");
        stopsLat.addDomain(Stops);
        stopsLat.addRange(XSD.xfloat);
        DatatypeProperty stopsLong = model.createDatatypeProperty(baseUri+"stopsLong");
        stopsLong.addDomain(Stops);
        stopsLong.addRange(XSD.xfloat);
        DatatypeProperty locationType = model.createDatatypeProperty(baseUri+"locationType");
        locationType.addDomain(Stops);
        locationType.addRange(XSD.xfloat);
        DatatypeProperty parentStation = model.createDatatypeProperty(baseUri+"parentStation");
        parentStation.addDomain(Stops);
        parentStation.addRange(XSD.xstring);
        //for write on owl file
        FileWriter writer = new FileWriter("TERLines.owl");
        model.write(writer);
        writer.close();
    }
}