package vfn;

import httputils.MyHttpConstants;
import org.rapidoid.http.MediaType;
import org.rapidoid.http.ReqRespHandler;
import org.rapidoid.setup.Setup;

import java.util.logging.Logger;

/**
 * Abstract class that implements VNF interface. Use TEMPLATE METHOD pattern to make new VNF implementation works with
 * the other classes
 */
public abstract class AbsBaseVNF implements VNF {

    /**
     * Logging utility field
     */
    private static final Logger LOGGER = Logger.getLogger(AbsBaseVNF.class.getName());

    /**
     * Port on which the VNF waits for messages
     */
    private final int port;

    /**
     * Constructor of the class
     * @param port Port on which the VNF waits for messages, must be in range 0-65535
     */
    public AbsBaseVNF(int port) {
        // TODO think a better check
        if (!(port >= 0 && port <= 65535))
            throw new RuntimeException("Port number must be in range 0-65535");
        this.port = port;
    }

    /**
     * Method to run the object
     */
    @Override
    public void execute() {
        Setup setup = Setup.create(AbsBaseVNF.class.getName() + String.valueOf(port));
        setup.address(MyHttpConstants.LOCAL_ADDRESS).port(port);
        setup.post("/").serve((ReqRespHandler) (req, resp) ->
                resp.code(200).contentType(MediaType.ANY).body(functionality(new String(req.body())).getBytes()));
    }
}