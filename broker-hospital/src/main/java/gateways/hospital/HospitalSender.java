package gateways.hospital;

import gateways.MessageSenderGateway;
import model.HospitalCostsRequest;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import javax.jms.Message;

public class HospitalSender {
    MessageSenderGateway sender;
    private String rule;

    HospitalSender(String queueName, String rule){
        this.sender = new MessageSenderGateway(queueName);
        this.rule = rule;
    }

    public boolean evaluate(HospitalCostsRequest hospitalCostsRequest){
        Evaluator evaluator = new Evaluator();
        evaluator.putVariable("age",String.valueOf(hospitalCostsRequest.getAge()));
        evaluator.putVariable("code",hospitalCostsRequest.getTreatmentCode());


        try {
            return evaluator.evaluate(rule).equals("1.0");
        } catch (EvaluationException e) {
            e.printStackTrace();
            return false;
        }
    }

    public MessageSenderGateway messageSender(){
        return sender;
    }
}
