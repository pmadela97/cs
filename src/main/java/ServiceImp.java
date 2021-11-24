import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Semaphore;

public class ServiceImp  {

private String filePath;
private String resultPath;
ObjectMapper objectMapper;
 Semaphore semProd,semCon;
 boolean b = false;
 Map<String,Long> stMap;
 Map<String,Long> fMap;
 LinkedList<Result> resultsList;
 LinkedList<Result> resultBufor;
 DatabaseConnection databaseConnection;


    public ServiceImp(String filePath, String resultPath) {
        this.filePath = filePath;
        this.resultPath = resultPath;
        objectMapper = new ObjectMapper();
        stMap = new HashMap<>();
        fMap = new HashMap<>();
        semProd = new Semaphore(1);
        semCon = new Semaphore(0);
        resultsList = new LinkedList<>();
        resultBufor = new LinkedList<>();
        databaseConnection = new DatabaseConnection();
    }
     void readFile (){
        try {
            FileInputStream fstream = new FileInputStream(filePath);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while (true) {
                    if ((strLine = br.readLine()) == null){
                        System.out.println("Potrzebny dostęp do czytania");
                        semProd.acquire();
                        System.out.println("Uzyskany dostęp do czytania");
                        resultBufor = resultsList;
                        resultsList = null;
                        b=true;
                        semCon.release();
                        System.out.println("Zwolniony dostęp do pisania");
                        break;
                    }
                        Log log = objectMapper.readValue(strLine, Log.class);
                        System.out.println(log);

                        if(log.getState().equals(State.STARTED)){
                            long f = Optional.ofNullable(fMap.get(log.getId())).orElse(0L);
                            if(f!=0){
                            resultsList.add(new Result(log.getId(),f-log.getTimestamp(),log.getType(),log.getHost(),f-log.getTimestamp()>4));
                            fMap.remove(log.getId());
                            }else{
                                stMap.put(log.getId(),log.getTimestamp());
                            }
                        }
                        else if(log.getState().equals(State.FINISHED)){
                            long s = Optional.ofNullable(stMap.get(log.getId())).orElse(0L);
                            if(s!=0){
                                resultsList.add(new Result(log.getId(),log.getTimestamp()-s,log.getType(),log.getHost(),log.getTimestamp()-s>4));
                                stMap.remove(log.getId());

                            }
                            fMap.put(log.getId(),log.getTimestamp());
                        }
                        if(resultsList.size()==2){
                            semProd.acquire();
                            resultBufor = resultsList;
                            resultsList = new LinkedList<>();
                            semCon.release();

                        }
                }


        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

     }
      String saveFile() throws SQLException, InterruptedException {
          Connection con = databaseConnection.getCon();
          Statement st = con.createStatement();
        String ct = "CREATE TABLE IF NOT EXISTS results(id varchar(255),time int,type varchar(64),host varchar(64),alert boolean, PRIMARY Key(ID))";
        while(true) {
            semCon.acquire();
            while(resultBufor.size()>0) {
                StringBuilder sb = new StringBuilder("INSERT INTO results(id,time,type,host,alert)VALUES(");
                Result r = resultBufor.removeFirst();
                sb.append("'" + r.getId() + "',");
                sb.append("'" + r.getTime() + "',");
                sb.append("'" + r.getType() + "',");
                sb.append("'" + r.getHost() + "',");
                sb.append("'" + r.isAlert() + "')");
                st.executeQuery(sb.toString());
                System.out.println(sb);
            }
            semProd.release();
            if (b==true){
                con.close();
                break;
            }
        }

        return ct;
    }

}
