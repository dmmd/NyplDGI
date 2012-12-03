package org.nypl.mss.dgi;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.BasicConfigurator;
import org.apache.maven.wagon.CommandExecutionException;

public class FileInput {
    private File file;
    private String sigFile = "/Users/dm/.droid6/signature_files/DROID_SignatureFile_V60.xml";
    private String host;
    private int port;
    private int timeout;
    
    FileInput(String fileIn) throws FileNotFoundException, IOException, CommandExecutionException, InterruptedException{
        this.file = new File(fileIn);
        
        if(this.file.exists()){
            //getProps();
            Fido fido = new Fido(this.file);
            clamav();
        } else {
            System.exit(0);
        }
        
    }

    private void clamav() throws FileNotFoundException {
        Logger logger = Logger.getLogger("org.nypl.mss.dgi.ClamScan");
        logger.setLevel(Level.OFF);
        BasicConfigurator.configure();
        
        InputStream fis = new FileInputStream(file);
        ClamScan c = new ClamScan("localhost", 3310, 600000000);
        
        ScanResult result = c.scan(fis);
                Pattern pattern = Pattern.compile("FOUND");
        Matcher matcher = pattern.matcher(result.getResult());
        
        if(matcher.find()){
            System.out.println("virusFound: true");
            System.out.println("VirusSignature: " + result.getSignature());
        }
        else{
            System.out.println("virusFound: false");
        }
    }
    
    private void getProps() throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream("nyplDgiProps.properties"));
        
        sigFile = prop.getProperty("droidSignatureFileLoc");
        host = prop.getProperty("clamavHost");
        port = Integer.parseInt(prop.getProperty("clamavPort"));
        timeout = Integer.parseInt(prop.getProperty("clamavTimeout"));
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException, CommandExecutionException, InterruptedException{
        FileInput f = new FileInput(args[0]);
    }  
}
