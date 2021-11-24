import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String args[]) throws InterruptedException, SQLException {
        ServiceImp serviceImp = new ServiceImp(args[0], "Result.txt");
       Thread t1 = new Thread(new Runnable() {
           @Override
           public void run() {
               serviceImp.readFile();
           }
       });
    Thread t2 = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                serviceImp.saveFile();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    t1.start();
   t2.start();
    t1.join();
   t2.join();
    }
}
