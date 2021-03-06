# Health-Insurance-System-using-JMS

In this assignment integrated a system of several applications for requesting of costs a medical treatment in a hospital. Health Insurance Client application is a Java desktop application. It is used by a Health Insurance company (e.g., VGZ, CZ, etc.) for requesting the costs approximation for a specific medical treatment of a patient (TreatmentCostsRequest) from several hospitals. The user must provide the social security number (ssn), the age of the patient and the treatment code. In addition, it is possible to indicate that the transport of the patient from his/her home address to the hospital is necessary by entering how many kilometres the transport should be (field transportDistance in TreatmentCostsRequest).

![routes](https://raw.githubusercontent.com/tomlicha/Health-Insurance-System-using-JMS/master/images/routes.png)

The TreatmentCostsRequest is then forwarded to three Hospital applications as an HospitalCostsRequest. The HospitalCostsRequest contains the same ssn, age and treatment code from the original TreatmentCostsRequest. Each HospitalCostsRequest is forwarded to the three hospital applications according to the routing rules shown in the table below. Figure 1 shows an example where the HospitalCostsRequest is forwarded to three hospitals based on these routing rules, and Figure 2 shows an example where the HospitalCostsRequest is forwarded to two hospitals based on these routing rules.

![routes](https://raw.githubusercontent.com/tomlicha/Health-Insurance-System-using-JMS/master/images/integration.png)


Each hospital application sends back HospitalCostsReply containing the estimated treatment costs, hospital name and hospital address. For each TreatmentCostsRequest, the Health Insurance Client application finally receives a TreatmentCostsReply, which contains the offer of the hospital which offered the lowest treatment price. In addition, if the original TreatmentCostsRequest included transport from the patient’s home address to the hospital (i.e., the field transportDistance > 0), then TreatmentCostsReply also contains the transport price. This transport price is calculated by multiplying the transport price per kilometre (acquired from the Transport Price web service) with the distance between the patient’s home address and the hospital (contained in original TreatmentCostsRequest). Figure 1 shows an example where the transport is included, and Figure 2 shows an example where the transport is not included.

![routes](https://raw.githubusercontent.com/tomlicha/Health-Insurance-System-using-JMS/master/images/integration2.png)

