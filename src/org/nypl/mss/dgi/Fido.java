/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nypl.mss.dgi;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author dm
 */
public class Fido {
    Fido(File file) throws IOException, InterruptedException{
        String cmd = "python /usr/local/fido/fido.py ";
        Process p = Runtime.getRuntime().exec(cmd + file.getAbsolutePath());
        p.waitFor();
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String[] result = br.readLine().split(",");
        FidoModel fm = new FidoModel();
        int i = 0;
        for(String field: result){
            switch(i){
                case 0: 
                        fm.setResultCode(field);
                        break;
                case 1: fm.setTime(Long.parseLong(field));
                        break;
                case 2: fm.setPuid(field);
                        break;
                case 3: fm.setFormatName(field);
                        break;
                case 4: fm.setSignatureName(field);
                        break;
                case 5: fm.setFileSize(Long.parseLong(field));
                        break;
                case 6: fm.setFileName(field);
                        break;
                case 7: fm.setMeme(field);
                        break;
                case 8: fm.setMatch(field);
                        break;            
            }
            ++i;
        }
        System.out.println(fm);
    }
}

