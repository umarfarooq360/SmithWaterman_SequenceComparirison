import java.util.*;
import java.util.Properties;
import java.io.*;

/**
 * This class is responsible for reading the config file and storing things as properties.
 * 
 * @author (Omar Farooq) 
 * @version (20th Feb 2014)
 * 
 */
public class Config
{
    Properties configFile;   //Properties stored in config file
    /**
     * Constructor to make a new config instance
     * @param filename The name of the file
     */
    public Config(String filename)  
    {
        configFile = new java.util.Properties();
        try {
            FileInputStream file = new FileInputStream(filename);  //get input stream
            configFile.load(file);  
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Return the value for the property searched using a key
     * @param key This is the key of the property you want
     */
    public String getProperty(String key)
    {
        String value = this.configFile.getProperty(key);  //Simple 
        return value;
    }
}