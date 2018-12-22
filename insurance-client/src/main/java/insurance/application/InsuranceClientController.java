package insurance.application;

import insurance.model.TreatmentCostsReply;
import insurance.model.TreatmentCostsRequest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.jms.JMSException;
import java.net.URL;
import java.util.ResourceBundle;

public class InsuranceClientController implements Initializable {
    @FXML
    private ListView<ClientListLine> lvRequestsReplies;
    @FXML
    private TextField tfSsn;
    @FXML
    private TextField tfAge;
    @FXML
    private TextField tfTreatmentCode;
    @FXML
    private CheckBox cbTransport;
    @FXML
    private TextField tfKilometers;

    private ClientHospitalAppGateway clientHospitalAppGateway;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfSsn.setText("123456");
        tfAge.setText("56");
        tfTreatmentCode.setText("ORT125");
        cbTransport.setSelected(false);
        tfKilometers.setDisable(true);
        clientHospitalAppGateway = new ClientHospitalAppGateway() {
            public void onCostReplyArrived(TreatmentCostsRequest treatmentCostsRequest, TreatmentCostsReply treatmentCostsReply){
                ClientListLine clientListLine = getListLine(treatmentCostsRequest);
                clientListLine.setReply(treatmentCostsReply);
                lvRequestsReplies.refresh();
            }
        };
    }

    private ClientListLine getListLine(TreatmentCostsRequest request) {
        for (ClientListLine clientListLine : lvRequestsReplies.getItems()){
            if (clientListLine.getRequest() == request) {
                return clientListLine;
            }
        }
        return null;
    }

    public void transportChanged(){
        System.out.println(cbTransport.isSelected());
        if (!cbTransport.isSelected()){
            tfKilometers.setText("");
        }
        this.tfKilometers.setDisable(!this.cbTransport.isSelected());
    }

    public void btnSendClicked(){
        int ssn = Integer.parseInt(this.tfSsn.getText());
        String treatmentCode = this.tfTreatmentCode.getText();
        int age = Integer.parseInt(this.tfAge.getText());

        int transportDistance = 0;
        if (this.cbTransport.isSelected()) {
            transportDistance = Integer.parseInt(this.tfKilometers.getText());
        }

        TreatmentCostsRequest request = new TreatmentCostsRequest(ssn, age, treatmentCode, transportDistance);
        //... send request
        ClientListLine listViewLine = new ClientListLine(request,null);
        lvRequestsReplies.getItems().add(listViewLine);
        try {
            clientHospitalAppGateway.applyForCost(request);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
