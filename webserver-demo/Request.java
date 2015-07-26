package net.mengkang.www;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhoumengkang on 26/7/15.
 */
public class Request {

    private InputStream input;
    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }
    public void parse() {
        // Read a set of characters from the socket
        StringBuffer requestString = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = input.read(buffer);
        }
        catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for(int j=0; j<i; j++){
            requestString.append((char) buffer[j]);
        }
        uri = parseUri(requestString.toString());
    }


    private String parseUri(String requestString) {
        System.out.println("requestString:" + requestString);
        if (requestString == null){
            return null;
        }
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1) {
                return requestString.substring(index1 + 1, index2);
            }
        }
        return null;
    }
    public String getUri() {
        return uri;
    }
}
