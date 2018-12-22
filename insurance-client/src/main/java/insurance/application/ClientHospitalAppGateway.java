package insurance.application;

import insurance.model.TreatmentCostsReply;
import insurance.model.TreatmentCostsRequest;
import insurance.model.TreatmentSerializer;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.HashMap;

public abstract class ClientHospitalAppGateway {

    MessageSenderGateway messageSenderGatewayBroker = new MessageSenderGateway(myRequestQueue);
    MessageReceiverGateway messageReceiverGatewayBroker = new MessageReceiverGateway(myReplyQueue);
    public static String myReplyQueue="BrokerToClient";
    public static String myRequestQueue="ClientToBroker";
    HashMap<String, TreatmentCostsRequest> hmap = new HashMap<>();

    TreatmentSerializer treatmentSerializer = new TreatmentSerializer();

    public ClientHospitalAppGateway() {
        try {

            messageReceiverGatewayBroker.setListener(new MessageListener() {
                @Override
                public void onMessage(Message msg) {
                    try {
                        System.out.println("\nloan reply :"+msg);
                        TreatmentCostsReply treatmentCostsReply = treatmentSerializer.replyFromString(((TextMessage) msg).getText());
                        TreatmentCostsRequest treatmentCostsRequest = (hmap.get(msg.getJMSCorrelationID()));
                        onCostReplyArrived(treatmentCostsRequest,treatmentCostsReply);


                    } catch (JMSException e) {
                        e.printStackTrace();


                    }
                }});
            messageReceiverGatewayBroker.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void applyForCost(TreatmentCostsRequest treatmentCostsRequest) throws JMSException {
        Message msg = messageSenderGatewayBroker.createTextMessage(treatmentSerializer.RequestToString(treatmentCostsRequest));
        // print all message attributes; but JMSDestination is null
        // session makes the message via MctiveMQ. AtiveMQ assigns unique JMSMessageID
        // to each message.
        System.out.println(msg);

        messageSenderGatewayBroker.send(msg);

        //System.out.println("id :" + msg.getJMSMessageID() + "\n" + "body:" + body + "\n");
        hmap.put(msg.getJMSMessageID(),treatmentCostsRequest);
    }

    public abstract void onCostReplyArrived(TreatmentCostsRequest treatmentCostsRequest, TreatmentCostsReply treatmentCostsReply);
}
