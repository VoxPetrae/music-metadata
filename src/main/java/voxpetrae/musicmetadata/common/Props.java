package voxpetrae.musicmetadata.common;

import java.io.InputStream;
import java.util.Properties;

public class Props {
    static String propertyValue;
    static String separator = System.getProperty( "file.separator" );
    static String fileName = "musicmetadata.properties";
    //static InputStream inputStream;
    public static String prop(String key){
        System.out.println("prop " + fileName);
        Properties properties = new Properties();
        //Thread currentThread = Thread.currentThread();
        
        try(InputStream propertiesStream = Props.class.getResource(fileName).openStream()){
            if (propertiesStream != null){
                properties.load(propertiesStream);
                propertyValue = properties.getProperty(key);
                //System.out.println("OK " + propertyValue);
                return propertyValue.trim();
            }
            else{
                System.out.println("Exception in " + Props.class.getName() + ": " + fileName);
                throw new NullPointerException("Input stream for file " + fileName + " was null.");
            }
        }
        catch (NullPointerException npe){
            StackTraceElement[] stes = npe.getStackTrace();
            for (StackTraceElement ste : stes) {
                System.out.println("Npe Exception in " + fileName + ": " + ste.toString());    
            }
            
        }
        catch (Exception eee){
            System.out.println("Exception in " + fileName + ": " + eee.getMessage());
        }
        return propertyValue;
    }
}
