package dispatcher;

import executeonmain.ExecuteOnMain;
import httputils.*;
import org.rapidoid.http.MediaType;
import org.rapidoid.http.ReqHandler;
import org.rapidoid.setup.Setup;
import vfn.AbsBaseVNF;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Logger;

import static httputils.MessageWrapper.unwrapChain;
import static httputils.MessageWrapper.unwrapMessage;

public class Dispatcher implements ExecuteOnMain {

    /**
     * Logging utility field
     */
    private static final Logger LOGGER = Logger.getLogger(AbsBaseVNF.class.getName());

    /**
     * Port on which the VNF waits for messages
     */
    private final int port;

    /**
     * Server to receive messages
     */
    private AbsBaseServer server;

    /**
     * Constructor of the class
     * @param port Port on which the VNF waits for messages, must be in range 0-65535
     */
    public Dispatcher(int port) {
        // TODO think a better check
        if (!(port >= 0 && port <= 65535))
            throw new RuntimeException("Port number must be in range 0-65535");
        this.port = port;
        server = new RapidoidServer();
    }

    /**
     * Method to run the object
     */
    @Override
    public void execute() {
        server.receive(port, message -> {
            try {
                String originalMessage = unwrapMessage(message);
                String[] chain = unwrapChain(message);
                String destination = chain[chain.length - 1];
                String[] vnfChain = Arrays.copyOfRange(chain, 1, chain.length - 1);

                for (String link : vnfChain) {
                    originalMessage = server.sendPOST(originalMessage, link, MyHttpConstants.TYPE_APPLICATION_XWWWFORMURLENCODED);
                }

                server.send(originalMessage, destination);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
