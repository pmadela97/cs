import java.io.Serializable;
import java.sql.Timestamp;

public class Log{
    String id;
    State state;
    long timestamp;
    String type;
    String host;
    public Log(){};

    public Log(String id, State state, long timestamp) {
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
