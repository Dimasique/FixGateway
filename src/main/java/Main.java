import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FieldConvertError;
import quickfix.FileStoreFactory;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.RuntimeError;
import quickfix.ScreenLogFactory;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;

import java.io.InputStream;
import java.sql.SQLException;

public class Main {
    private final static Logger log = LoggerFactory.getLogger(Main.class);
    private final SocketAcceptor acceptor;

    public Main(SessionSettings settings) throws ConfigError, FieldConvertError, SQLException {
        Application application = new Application(settings);
        MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new ScreenLogFactory(true, true, true);
        MessageFactory messageFactory = new DefaultMessageFactory();

        acceptor = new SocketAcceptor(application, messageStoreFactory, settings, logFactory,
                messageFactory);
    }

    private void start() throws RuntimeError, ConfigError {
        acceptor.start();
    }

    private void stop() {

        acceptor.stop();
    }

    public static void main(String[] args) throws Exception {
        try {
            InputStream inputStream = Main.class.getResourceAsStream("executor.cfg");

            SessionSettings settings = new SessionSettings(inputStream);
            inputStream.close();

            Main executor = new Main(settings);
            executor.start();

            System.out.println("press <enter> to quit");
            System.in.read();

            //log.info("Stopped after entering smth");
            executor.stop();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}