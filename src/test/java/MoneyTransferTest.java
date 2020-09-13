import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import transfer.model.Account;
import transfer.service.MoneyTransferService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MoneyTransferTest {

   @Test
    public void concurrentTest() throws InterruptedException {
        MoneyTransferService moneyTransferService = new MoneyTransferService();
        Account from = new Account(1L,1000L);
        Account to = new Account(2L,1000L);

       ExecutorService executorService = Executors.newFixedThreadPool(20);
       List<MoneyTransferTask> tasks = new ArrayList<>();

       for (int i=0; i<= 5000;i++){
           tasks.add(new MoneyTransferTask(moneyTransferService, from, to, 1L));
           tasks.add(new MoneyTransferTask(moneyTransferService, to, from, 1L));
           }

       List<Future<Long>> futures = executorService.invokeAll(tasks);
       for (Future<Long> future : futures ){
           try {
               future.get();
           } catch (ExecutionException e) {
               e.printStackTrace();
           }
       }

        moneyTransferService.transfer(from, to, 1L);
        moneyTransferService.transfer(to, from, 1L);

        Assertions.assertEquals(from.getBalance(), 1000L);
        Assertions.assertEquals(to.getBalance(), 1000L);


   }
   static class MoneyTransferTask implements Callable<Long>{
       MoneyTransferService moneyTransferService;
       Account from;
       Account to;
       Long amount;

       public MoneyTransferTask(MoneyTransferService moneyTransferService, Account from, Account to, Long amount) {
           this.moneyTransferService = moneyTransferService;
           this.from = from;
           this.to = to;
           this.amount = amount;
       }


       @Override
       public Long call() throws Exception {
           moneyTransferService.transfer(from, to, amount);
           return from.getBalance();
       }
   }
}
