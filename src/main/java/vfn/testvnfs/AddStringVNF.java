package vfn.testvnfs;

import vfn.AbsBaseVNF;

/**
 * Simple VNF that append a string in the end of a packet
 */
public class AddStringVNF extends AbsBaseVNF {

    private String toAdd;

    public AddStringVNF(String toAdd, int port) {
        super(port);
        this.toAdd = toAdd;
    }

    public String functionality(String message) {
        return message.replaceAll("\r\n\r\n", "\r\n") + toAdd;
    }

    public static void main(String[] args) {
        new AddStringVNF(args[1], Integer.parseInt(args[0])).execute();
    }
}