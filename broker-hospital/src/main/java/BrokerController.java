import gateways.client.ClientAppGateway;
import gateways.hospital.HospitalAppGateway;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.*;
import net.sourceforge.jeval.EvaluationException;

import javax.jms.JMSException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;


public class BrokerController implements Initializable {


    public ListView<ListViewLine> lvBrokerRequestReply;

    ClientAppGateway clientAppGateway;
    HospitalAppGateway hospitalAppGateway;
    HashMap<HospitalCostsRequest, TreatmentCostsRequest> hmap2 = new HashMap<>();

    public BrokerController() {


        }
    /**
     * This method returns the line of lvMessages which contains the given loan request.
     * @param request BankInterestRequest for which the line of lvMessages should be found and returned
     * @return The ListView line of lvMessages which contains the given request
     */
    private ListViewLine getRequestReply(TreatmentCostsRequest request) {

        for (int i = 0; i < lvBrokerRequestReply.getItems().size(); i++) {
            ListViewLine rr =  lvBrokerRequestReply.getItems().get(i);
            if (rr.getTreatmentCostsRequest() != null && rr.getTreatmentCostsRequest() == request) {
                return rr;
            }
        }

        return null;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientAppGateway = new ClientAppGateway() {
            @Override
            public void onTreatmentRequestArrived(TreatmentCostsRequest treatmentCostsRequest) {

                ListViewLine listViewLine = new ListViewLine(treatmentCostsRequest);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        lvBrokerRequestReply.getItems().add(listViewLine);
                        lvBrokerRequestReply.refresh();
                    }
                });
                //call the credit agency to get credit and history
                HospitalCostsRequest hospitalCostsRequest = new HospitalCostsRequest(treatmentCostsRequest.getSsn(),treatmentCostsRequest.getTreatmentCode(),treatmentCostsRequest.getAge(),treatmentCostsRequest.getTransportDistance());
                hmap2.put(hospitalCostsRequest,treatmentCostsRequest);
                try {
                    hospitalAppGateway.sendTreatmentRequest(hospitalCostsRequest);
                } catch (JMSException e) {
                    e.printStackTrace();
                } catch (EvaluationException e) {
                    e.printStackTrace();
                }

            }
        };
        hospitalAppGateway = new HospitalAppGateway() {
            @Override
            public void onHospitalCostsReplyArrived(HospitalCostsReply hospitalCostsReply, HospitalCostsRequest hospitalCostsRequest) {

                ListViewLine listViewLine = getRequestReply(hmap2.get(hospitalCostsRequest));
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        listViewLine.setHospitalCostsReply(hospitalCostsReply);
                        lvBrokerRequestReply.refresh();

                    }

                });
                String result="0";
                if (hospitalCostsRequest.getTransportDistance() != 0){
                    ContentEnricher contentEnricher = new ContentEnricher();
                    result= contentEnricher.GetRequest();
                }


                TreatmentCostsReply treatmentCostsReply = new TreatmentCostsReply(hospitalCostsReply.getPrice(),Double.valueOf(result)*hospitalCostsRequest.getTransportDistance(),hospitalCostsReply.getHospitalName());
                System.out.println("treatment to be sent back: "+treatmentCostsReply);


                try {
                    clientAppGateway.sendTreatmentReply(treatmentCostsReply,hmap2.get(hospitalCostsRequest));
                } catch (JMSException e) {
                    e.printStackTrace();
                }


            }
        };
    }
}
