package voxpetrae.musicmetadata.common;

import java.io.InputStream;
import java.util.Properties;

public class Props {
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Props.class);
    static String propertyValue;
    static String separator = System.getProperty( "file.separator" );
    static String fileName = "musicmetadata.properties";
    private Props(){
        throw new IllegalStateException("Utility class");
    }
    public static String prop(String key){
        Properties properties = new Properties();
        //System.out.println("PROPPATH " + Props.class.getResource(fileName).toExternalForm());
        try(InputStream propertiesStream = Props.class.getResource(fileName).openStream()){
            if (propertiesStream != null){
                properties.load(propertiesStream);
                propertyValue = properties.getProperty(key);
                return propertyValue.trim();
            }
            else{
                logger.error("Exception in %s: %s", Props.class.getName(), fileName);
                throw new NullPointerException("Input stream for file " + fileName + " was null.");
            }
        }
        catch (NullPointerException npe){
            StackTraceElement[] stes = npe.getStackTrace();
            for (StackTraceElement ste : stes) {
                logger.error("Npe Exception in " + fileName + ": " + ste.toString());    
            }
            
        }
        catch (Exception eee){
            logger.error("Exception in " + fileName + ": " + eee.getMessage());
        }
        return propertyValue;
    }
}
