package voxpetrae.musicmetadata.services;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.logging.log4j.Logger;
import voxpetrae.musicmetadata.services.interfaces.AlbumInfoService;
public class LinkedAlbumInfoService implements AlbumInfoService {

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(LinkedAlbumInfoService.class);
    public boolean getAlbumInfo(String albumTitle){
        var dbPediaURI = "http://dbpedia.org/sparql";
        var queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
        "SELECT ?album " +
        "WHERE { ?album rdfs:label \"" + albumTitle + "\"@en } ";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecution.service(dbPediaURI).query(queryString).build();
        logger.info("Fetching album: {}...", qexec.getQueryString());
        try {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(System.out, results, query); 
        } finally {
            qexec.close();
        }
        return true; // Should be a model with album information
    }
}
