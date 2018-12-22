package hospital.application;


import hospital.model.HospitalCostsReply;
import hospital.model.HospitalCostsRequest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.HashMap;

public abstract class HospitalAppGateway {

    public static String myRequestQueueHospital;
    public static String myReplyQueueHospital ="HospitalToBroker";
    MessageSenderGateway messageSenderGatewayBroker;
    MessageReceiverGateway messageReceiverGatewayBroker ;

    HospitalSerializer hospitalSerializer = new HospitalSerializer();

    HashMap<HospitalListLine,Message> hmap = new HashMap<>();

    public HospitalAppGateway(String hospitalName) {
        myRequestQueueHospital ="BrokerTo"+hospitalName;
        messageSenderGatewayBroker = new MessageSenderGateway(myReplyQueueHospital);
        messageReceiverGatewayBroker = new MessageReceiverGateway(myRequestQueueHospital);
        try {

            messageReceiverGatewayBroker.setListener(new MessageListener() {
                @Override
                public void onMessage(Message msg) {
                 try {
                    HospitalCostsRequest hospitalCostsRequest = hospitalSerializer.requestFromString(((TextMessage) msg).getText());
                    HospitalListLine hospitalListLine = new HospitalListLine(hospitalCostsRequest,null);
                    hmap.put(hospitalListLine,msg);
                     onClientRequestArrived(hospitalCostsRequest,hospitalListLine);

                } catch (JMSException e) {
                    e.printStackTrace();


                }
                }});
            messageReceiverGatewayBroker.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void applyForHospital(HospitalCostsReply hospitalCostsReply, HospitalListLine selected) throws JMSException {
        Message requestMsg = hmap.get(selected);

        System.out.println("request message :" + requestMsg);
        String jsonString =hospitalSerializer.replyToString(hospitalCostsReply);
        // create a text message
        Message banktobrokermessage = messageSenderGatewayBroker.createTextMessage(jsonString);
        banktobrokermessage.setJMSCorrelationID(requestMsg.getJMSMessageID());
        banktobrokermessage.setIntProperty("aggregationID",requestMsg.getIntProperty("aggregationID"));
        messageSenderGatewayBroker.send(banktobrokermessage);
        System.out.println("\nmessage sent:\n"+banktobrokermessage);

    }

    public abstract void onClientRequestArrived(HospitalCostsRequest bankInterestRequest, HospitalListLine listViewLine);
}
