package guru.springframework.msscssm.services;

import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import guru.springframework.msscssm.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    static private Map<String,Integer> counter = new HashMap<>();

    Payment payment;
    @BeforeEach
    void setUp() {
        payment = Payment.builder().amount(new BigDecimal(41)).build();
    }

    @AfterAll
    static void summarize(){
        System.out.println("Summarize: "+ counter);
    }

    @Transactional
    @RepeatedTest(10)
    @Test
    void preAuth() {
        Payment savedPayment = paymentService.newPayment(payment);
        System.out.println("savedPayement: "+savedPayment);
        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());
        System.out.println("state: "+sm.getState().getId());

        Payment preAuthedPayment = paymentRepository.getReferenceById(savedPayment.getId());
        System.out.println("preauthedPayment: "+preAuthedPayment);
        count(sm.getState().getId().toString());
        if("PRE_AUTH".equals(sm.getState().getId().toString())) {
            System.out.println("Authorizing!");
            sm = paymentService.authorizePayment(savedPayment.getId());
            System.out.println("state: "+sm.getState().getId());

            Payment authedPayment = paymentRepository.getReferenceById(savedPayment.getId());
            System.out.println("preauthedPayment: "+preAuthedPayment);
            count(sm.getState().getId().toString());
        } else {
            System.out.println("Not continuing with authorizing!");
        }
    }
    private static void count(String state) {
        if(!counter.containsKey(state)) {
            counter.put(state, 1);
        } else{
            counter.put(state, counter.get(state)+1);
        }
    }
}