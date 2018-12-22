package gateways.hospital;

import gateways.MessageReceiverGateway;
import model.*;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class HospitalAppGateway {

    public static String myRequestQueueCatharina ="BrokerToCatharina Ziekenhuis";

    public static String myRequestQueueMaxima ="BrokerToMáxima Medisch Centrum";

    public static String myRequestQueueUMC ="BrokerToUniversity Medical Center (UMC)";

    public static String myReplyQueueHospital ="HospitalToBroker";

    private final static String CATHARINA = "#{age} >= 10 && startsWith('#{code}','ORT',0)";

    private final static String MAXIMA  = "#{age} >= 18";

    private final static String UMC = "#{age} >= 0";

    MessageReceiverGateway messageReceiverGatewayHospital = new MessageReceiverGateway(myReplyQueueHospital);

    List<HospitalSender>  hospitalSenders = new ArrayList<>();

    HashMap<String, HospitalCostsRequest> hmap = new HashMap<>();

    HashMap<Integer, HospitalReplyAggregate> hmapAggregation = new HashMap<>();

    CostsSerializer costsSerializer = new CostsSerializer();
    private int counter;

    public HospitalAppGateway() {

        hospitalSenders.add(new HospitalSender(myRequestQueueCatharina,CATHARINA));
        hospitalSenders.add(new HospitalSender(myRequestQueueMaxima,MAXIMA));
        hospitalSenders.add(new HospitalSender(myRequestQueueUMC,UMC));

        try {
            messageReceiverGatewayHospital.setListener(new MessageListener() {
                @Override
                public void onMessage(Message msg) {
                    try {
                        int aggregationID = msg.getIntProperty("aggregationID");
                        System.out.println("message received from hospital:\n"+msg);
                        HospitalCostsReply bankInterestReply = costsSerializer.replyFromString(((TextMessage) msg).getText());
                        HospitalReplyAggregate hospitalReplyAggregate = hmapAggregation.get(aggregationID);
                        hospitalReplyAggregate.addReply(bankInterestReply);
                        if(hospitalReplyAggregate.isFinished() == true){
                            HospitalCostsRequest bankInterestRequest = hmap.get(msg.getJMSCorrelationID());
                            System.out.println("request :"+bankInterestRequest+" reply:"+bankInterestReply);
                            onHospitalCostsReplyArrived(hospitalReplyAggregate.getBest(),bankInterestRequest);
                        }


                    } catch (JMSException e) {
                        e.printStackTrace();

                    }
                }});
            messageReceiverGatewayHospital.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    public void sendTreatmentRequest(HospitalCostsRequest hospitalCostsRequest) throws JMSException, EvaluationException {
        String jsonString = costsSerializer.RequestToString(hospitalCostsRequest);
        HospitalReplyAggregate hospitalReplyAggregate = new HospitalReplyAggregate();
        int messagesSent=0;
        counter+=1;

        Evaluator evaluator = new Evaluator();
        evaluator.putVariable("age",String.valueOf(hospitalCostsRequest.getAge()));
        evaluator.putVariable("code",hospitalCostsRequest.getTreatmentCode());

        for (HospitalSender hospitalSender : hospitalSenders){
            if (hospitalSender.evaluate(hospitalCostsRequest)) {
                Message clientMessage1 = hospitalSender.sender.createTextMessage(jsonString);
                clientMessage1.setIntProperty("aggregationID", counter);
                hospitalSender.sender.send(clientMessage1);
                hmap.put(clientMessage1.getJMSMessageID(), hospitalCostsRequest);
                messagesSent += 1;
            }
        }

        hospitalReplyAggregate.setNumberRequestSent(messagesSent);
        hmapAggregation.put(counter, hospitalReplyAggregate);
    }

    public abstract void onHospitalCostsReplyArrived(HospitalCostsReply bankInterestReply, HospitalCostsRequest bankInterestRequest);
}
