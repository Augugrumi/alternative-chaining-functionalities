import dispatcher.Dispatcher;
import enchainer.Enchainer;
import vfn.testvnfs.AddStringVNF;

public class Main {

    public static void main(String args[]) {
        int chainLength = 4;
        String[] chain = new String[chainLength];
        chain[0] = "http://localhost:55630";
        chain[1] = "http://localhost:55631";
        chain[2] = "http://localhost:55632";
        chain[3] = "http://localhost:55634";

        Enchainer e = new Enchainer(55633, chain);
        final Dispatcher d = new Dispatcher(55630);
        final AddStringVNF v1 = new AddStringVNF("v1", 55631);
        final AddStringVNF v2 = new AddStringVNF("v2", 55632);

        new Thread(d::execute).start();
        new Thread(v1::execute).start();
        new Thread(v2::execute).start();

        e.execute();
    }
}