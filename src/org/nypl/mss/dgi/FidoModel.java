package org.nypl.mss.dgi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FidoModel {
    String resultCode;
    long time;
    String puid;
    String formatName;
    String signatureName;
    long fileSize;
    String fileName;
    String meme;
    String match;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = stripParens(fileName);
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = stripParens(formatName);
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = stripParens(match);
    }

    public String getMeme() {
        return meme;
    }

    public void setMeme(String meme) {
        this.meme = stripParens(meme);
    }

    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = stripParens(puid);
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = stripParens(resultCode);
    }

    public String getSignatureName() {
        return signatureName;
    }

    public void setSignatureName(String signatureName) {
        this.signatureName = stripParens(signatureName);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
    
    @Override
    public String toString(){
        if(this.getResultCode().equals("OK")){
        StringBuilder sb = new StringBuilder();
        sb.append("pronomMatch: true\n")
                .append("pronomPuid: ").append(this.getPuid()).append("\n")
                .append("pronomMimeType: ").append(this.getMeme()).append("\n")
                .append("pronomFormatName: ").append(this.getFormatName()).append("\n")
                .append("pronomSignatureName: ").append(this.getSignatureName()).append("\n")
                .append("pronomIdentificationMethod: ").append(this.getMatch());

                //.append("Exec Time\t").append(this.getTime()).append("\n")
                //.append("File Size\t").append(this.getFileSize()).append("\n")
                //append("File Name\t").append(this.getFileName()).append("\n")
                
        return sb.toString();
        } else { return "pronomMatch: false";}
        
    }
    
    private String stripParens(String s){
        Pattern p = Pattern.compile("^\".*\"$");
        Matcher m = p.matcher(s);
        if(m.find()){
            return s.substring(1, s.length() - 1);
        } else {
            return s;
        }
    }
}
