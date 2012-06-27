package org.nypl.mss.dgi;

import java.io.*;
import java.net.URI;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.maven.wagon.CommandExecutionException;
import uk.gov.nationalarchives.droid.core.BinarySignatureIdentifier;
import uk.gov.nationalarchives.droid.core.interfaces.*;
import uk.gov.nationalarchives.droid.core.interfaces.resource.FileSystemIdentificationRequest;
import uk.gov.nationalarchives.droid.core.interfaces.resource.RequestMetaData;


public class BinarySignatureIdentification {
    
    private BinarySignatureIdentifier binarySignatureIdentifier;
    private String signatureFile;
    private Logger logger, logger2;
    private List<IdentificationResult> resultList;
    private List<IdentificationResult> extResultList;
    private File fileIn;
    
    public BinarySignatureIdentification(File fileIn, String signatureFile) throws FileNotFoundException, IOException, CommandExecutionException {
        this.fileIn = fileIn;
        this.signatureFile = signatureFile;
        initLogger();
        initBin();
        matchBinary();
    }
    
    private void initBin() throws CommandExecutionException{
        binarySignatureIdentifier = new BinarySignatureIdentifier();
        
        File sigFile = new File(signatureFile);
        
        if (!sigFile.exists())
            throw new CommandExecutionException("Signature file not found");
        
        binarySignatureIdentifier.setSignatureFile(signatureFile);
      
        try {
            binarySignatureIdentifier.init();
        } catch (Exception e) {
            throw new CommandExecutionException ("Can't parse signature file");
        }
        
        binarySignatureIdentifier.setMaxBytesToScan(-1);
    }
    
    private void initLogger(){
        logger = Logger.getLogger("uk.gov.nationalarchives.droid.core.signature.xml.SAXModelBuilder");
        logger.setLevel(Level.OFF);
        
        logger2 = Logger.getLogger("uk.gov.nationalarchives.droid.core.signature.droid6.InternalSignature");
        logger2.setLevel(Level.OFF);
        
        BasicConfigurator.configure();
    }

    public List<IdentificationResult> getResultList() {
        return resultList;
    }

    public List<IdentificationResult> getExtResultList() {
        return extResultList;
    }
    
    

    private void matchBinary() {
        URI resourceUri = fileIn.toURI();
        
        RequestMetaData metaData = new RequestMetaData(fileIn.length(),
                    fileIn.lastModified(), fileIn.getName());
        RequestIdentifier identifier = new RequestIdentifier(resourceUri);
        identifier.setParentId(1L);
        IdentificationRequest request = null;
        InputStream in = null;

        try {
                in = new FileInputStream(fileIn);
                request = new FileSystemIdentificationRequest(metaData, identifier);

                request.open(in);
                
                IdentificationResultCollection results =
                    binarySignatureIdentifier.matchBinarySignatures(request);
                binarySignatureIdentifier.checkForExtensionsMismatches(results, signatureFile);
                binarySignatureIdentifier.removeLowerPriorityHits(results);
                
                resultList = results.getResults();
                
                
                IdentificationResultCollection extResults = binarySignatureIdentifier.matchExtensions(request, true);
                
                extResultList = extResults.getResults();
                
        } catch (Exception ex){}        
    }
}
