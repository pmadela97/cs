public class Result {
    String id;
    long time;
    String type;
    String host;
    boolean alert;

    public Result(String id, long time, String type, String host, boolean alert) {
        this.id = id;
        this.time = time;
        this.type = type;
        this.host = host;
        this.alert = alert;
        if(type == null) this.type="";
        if(host == null) this.host="";
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{id: ");
        sb.append(id);
        sb.append(", ");
        sb.append("time: ");
        sb.append(time);
        sb.append(", ");
        sb.append("type: ");
        sb.append(type);
        sb.append(", ");
        sb.append("host: ");
        sb.append(host);
        sb.append(", ");
        sb.append("alert: ");
        sb.append(alert);
        sb.append("}");
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public boolean isAlert() {
        return alert;
    }
}
