package org.kevoree.api;

/**
 * Created with IntelliJ IDEA.
 * User: duke
 * Date: 19/11/2013
 * Time: 00:17
 */
public interface Port {

    public void send(String payload);

    public void send(String payload, Callback callback);
    
    public String getPath();

    public int getConnectedBindingsSize();

}
