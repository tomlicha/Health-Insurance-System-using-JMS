package gateways.client;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import model.TreatmentCostsReply;
import model.TreatmentCostsRequest;
import model.TreatmentSerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.HashMap;

public abstract class ClientAppGateway {

    public static String myReplyQueueClient="BrokerToClient";
    public static String myRequestQueueClient="ClientToBroker";

    MessageSenderGateway messageSenderGatewayClient = new MessageSenderGateway(myReplyQueueClient);
    MessageReceiverGateway messageReceiverGatewayClient = new MessageReceiverGateway(myRequestQueueClient);

    HashMap<TreatmentCostsRequest, Message> hmap = new HashMap<>();

    TreatmentSerializer treatmentSerializer = new TreatmentSerializer();

    public ClientAppGateway() {
        try {

            messageReceiverGatewayClient.setListener(new MessageListener() {
                @Override
                public void onMessage(Message msg) {
                    try {
                        System.out.println("loan request created"+((TextMessage) msg).getText());

                        TreatmentCostsRequest treatmentCostsRequest = treatmentSerializer.requestFromString(((TextMessage) msg).getText());

                        System.out.println("treatment created = "+treatmentCostsRequest);
                        onTreatmentRequestArrived(treatmentCostsRequest);

                        hmap.put(treatmentCostsRequest,msg);

                    } catch (JMSException e) {
                        e.printStackTrace();

                    }
                }});
            messageReceiverGatewayClient.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void sendTreatmentReply(TreatmentCostsReply treatmentCostsReply, TreatmentCostsRequest treatmentCostsRequest) throws JMSException {



        String jsonString = treatmentSerializer.replyToString(treatmentCostsReply);
        // create a text message
        Message clientMessage = messageSenderGatewayClient.createTextMessage(jsonString);

        clientMessage.setJMSCorrelationID(hmap.get(treatmentCostsRequest).getJMSMessageID());
        messageSenderGatewayClient.send(clientMessage);
        hmap.remove(treatmentCostsRequest);
        System.out.println("\nmessage send back to client: "+clientMessage);
    }

    public abstract void onTreatmentRequestArrived(TreatmentCostsRequest treatmentCostsRequest);
}
