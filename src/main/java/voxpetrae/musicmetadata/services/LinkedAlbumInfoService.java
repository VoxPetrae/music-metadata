package voxpetrae.musicmetadata.services;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

import voxpetrae.musicmetadata.services.interfaces.AlbumInfoService;
public class LinkedAlbumInfoService implements AlbumInfoService {

    public boolean getAlbumInfo(){
        String serviceURI = "http://dbpedia.org/sparql";
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                             "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                             "SELECT * " +
                             "WHERE { " +
                             "album rdfs:label \"Thriller\" @en ." +
                             "}" + 
                             "LIMIT 10";
        /*
 PREFIX dbo: <http://dbpedia.org/ontology/>
PREFIX dbr: <http://dbpedia.org/resource/>

SELECT ?album ?artist ?releaseDate
WHERE {
  ?album a dbo:Album ;
         dbo:musicalArtist ?artist ;
         dbo:releaseDate ?releaseDate ;
         rdfs:label "Thriller"@en .  
}
         */
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecution.service(serviceURI).query(query).build();

        try {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(System.out, results, query); 
        } finally {
            qexec.close();
        }
        return true;
    }
}
