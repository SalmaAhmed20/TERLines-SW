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
        cityName.addDomain(Stops);
        cityName.addRange(XSD.xstring);
        DatatypeProperty stopsName = model.createDatatypeProperty(baseUri+"stopsName");
        cityName.addDomain(Stops);
        cityName.addRange(XSD.xstring);
        DatatypeProperty stopsLat = model.createDatatypeProperty(baseUri+" stopsLat");
        latitude.addDomain(Stops);
        latitude.addRange(XSD.xfloat);
        DatatypeProperty stopsLong = model.createDatatypeProperty(baseUri+"stopsLong");
        longitude.addDomain(Stops);
        longitude.addRange(XSD.xfloat);
        DatatypeProperty locationType = model.createDatatypeProperty(baseUri+"locationType");
        longitude.addDomain(Stops);
        longitude.addRange(XSD.xfloat);
        DatatypeProperty parentStation = model.createDatatypeProperty(baseUri+"parentStation");
        longitude.addDomain(Stops);
        longitude.addRange(XSD.xstring);
        //for write on owl file
        FileWriter writer = new FileWriter("TERLines.owl");
        model.write(writer);
        writer.close();
    }
}
