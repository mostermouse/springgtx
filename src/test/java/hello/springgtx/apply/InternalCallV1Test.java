package hello.springgtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV1Test {
    @Autowired
    CallService callService;

    @Test
    void printProxy(){
        log.info("callService class={}"  , callService.getClass());

    }

    @Test
    void internalCall(){
        callService.internal();
    }

    @Test
    void externalCall(){
        callService.external();
    }
    @TestConfiguration
    static class InteranlCallV1TestConfig{

        @Bean
        CallService callService(){
            return new CallService();
        }
    }


    @Slf4j
    static class CallService{
        public void external(){
            log.info("call external");
            printTxInfo();

            //프록시로 호출 하는게 아니라 this로 직접 호출이라 트랜잭션 적용이 안됨
            //this.internal(); 하고 같음
            internal();
        }
        @Transactional
        public void internal(){
            log.info("call internal");
            printTxInfo();
        }
        private void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isSynchronizationActive();
            log.info("tx active={}" , txActive);
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly={}" , readOnly);
        }
    }
}
