import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.XSD;


import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String baseUri = "http://www.semanticweb.org/20180122-20180078-20180040/ontologies/2021/11/Project2#";
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM );
        //NameSpace for base uri
        //model.setNsPrefix("TER",baseUri);
        //---------City Class---------
        OntClass City = model.createClass(baseUri+"City" );
        //----- Data Property of city
        DatatypeProperty cityName = model.createDatatypeProperty("CityName");
        cityName.addDomain(City);
        cityName.addRange(XSD.xstring);
        DatatypeProperty latitude = model.createDatatypeProperty("Latitude");
        cityName.addDomain(City);
        cityName.addRange(XSD.xfloat);
        //for write on owl file
        FileWriter writer = new FileWriter("TERLines.owl");
        model.write(writer);
        writer.close();
    }
}
