package guru.springframework.msscssm.config;

import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

@SpringBootTest
class StateMachineConfigTest {

    @Autowired
    private StateMachineFactory<PaymentState, PaymentEvent> paymentStateMachineFactory;


    @Test
    public void test() {
        StateMachine<PaymentState,PaymentEvent> paymentStateMachine = paymentStateMachineFactory.getStateMachine(UUID.randomUUID());
        paymentStateMachine.start();
        System.out.println("Initial: " + paymentStateMachine.getInitialState().getId().toString());

        paymentStateMachine.sendEvent(PaymentEvent.PRE_AUTHORIZE);
        System.out.println("S1: "  + paymentStateMachine.getState().getId().toString());
        paymentStateMachine.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);
        System.out.println("S2: "  + paymentStateMachine.getState().getId().toString());

        paymentStateMachine.sendEvent(PaymentEvent.AUTH_APPROVED);
        System.out.println("S3: " + paymentStateMachine.getState().getId().toString());
        System.out.println(paymentStateMachine.getState().toString());

    }

    public void test2() {
        //paymentStateMachine.startReactively().;
    }

}