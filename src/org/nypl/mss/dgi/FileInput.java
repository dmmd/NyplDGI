package org.nypl.mss.dgi;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.maven.wagon.CommandExecutionException;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationResult;

public class FileInput {
    private File file;
    
    FileInput(String fileIn) throws FileNotFoundException, IOException, CommandExecutionException{

        this.file = new File(fileIn);
        if(this.file.exists()){
            droid6();
            clamav();
        } else {
            System.out.println("nypldgi: error");
        }
        
    }

    private void droid6() throws FileNotFoundException, IOException, CommandExecutionException {
        BinarySignatureIdentification bin = new BinarySignatureIdentification(file);
        List<IdentificationResult> resultList = bin.getResultList();
        List<IdentificationResult> extResultList = bin.getExtResultList();
        if(resultList.size() == 1){
            System.out.println("droidMatch: true");
            System.out.println("droidPuid: " + resultList.get(0).getPuid());
            System.out.println("droidMimeType: " + resultList.get(0).getMimeType());
            System.out.println("droidMethod: binary signature" );
        }
        if(resultList.isEmpty()){
            if(extResultList.isEmpty()){
                System.out.println("droidMatch: false");
            }
            else{
                System.out.println("droidMatch: true");
                System.out.println("droidPuid: " + extResultList.get(0).getPuid());
                System.out.println("droidMimeType: " + extResultList.get(0).getMimeType());
                System.out.println("droidMethod: extension" );
            }
        }
    }

    private void clamav() throws FileNotFoundException {
        Logger logger = Logger.getLogger("org.nypl.mss.dgi.ClamScan");
        logger.setLevel(Level.OFF);
        BasicConfigurator.configure();
        
        InputStream fis = new FileInputStream(file);
        ClamScan c = new ClamScan("127.0.0.1", 3310, 600000000);
        
        ScanResult result = c.scan(fis);
                Pattern pattern = Pattern.compile("FOUND");
        Matcher matcher = pattern.matcher(result.getResult());
        
        if(matcher.find()){
            System.out.println("virusFound: Found");
            System.out.println("VirusSignature: " + result.getSignature());
        }
        else{
            System.out.println("virusFound: Not Found");
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException, CommandExecutionException{
        String filename = args[0];
        FileInput f = new FileInput(filename);
    }
}
