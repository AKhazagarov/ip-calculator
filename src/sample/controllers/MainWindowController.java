package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import sample.ipCalc.Calculation;
import sample.objects.Network;
import sample.objects.StructIp;

import java.util.ArrayList;

import static java.lang.Math.pow;

public class MainWindowController {

    private ObservableList<Network> logTable = FXCollections.observableArrayList();
    private ObservableList<Network> logTableSpace = FXCollections.observableArrayList();

    @FXML
    private TableView<Network> tv_log, tv_logSpace;
    @FXML
    private TableColumn<Network,StructIp> tc_network, tc_networkSpace;
    @FXML
    private TableColumn<Network,Integer> tc_mask, tc_maskSpace;
    @FXML
    private TableColumn<Network,Integer> tc_size, tc_sizeSpace;
    @FXML
    private TableColumn<Network,Integer> tc_selecSize;
    @FXML
    private TableColumn<Network,StructIp> tc_bc, tc_bcSpace;
    @FXML
    private TableColumn<Network,StructIp> tc_ipF, tc_ipFSpace;
    @FXML
    private TableColumn<Network,StructIp> tc_ipL, tc_ipLSpace;

    @FXML
    private Button btn_divide, btn_addNetwork,btn_aggregation;

    @FXML
    private TextField tf_IpOctet1, tf_IpOctet2, tf_IpOctet3, tf_IpOctet4, tf_IpMask, tf_networksSize;

    @FXML
    private Text text_initialNetwork, text_mainSize, text_selectedNetwork, text_selectedFIp, text_selectedLIp, text_selectedSize, text_selectedBC, text_ipSpace;

    @FXML
    private ProgressBar pb_main;

    @FXML
    private VBox vbox_ledtDock;



    private StructIp initialNetwork;
    private int initialMask;
    ArrayList<Network> arrayIpResult = new ArrayList<>();
    ArrayList<Network> spaceNetwork;

    int macpojnt;

    boolean mainErrorEnter = false;

    @FXML
    private void initialize() {
        tc_network.setCellValueFactory(new PropertyValueFactory<Network, StructIp>("networkAdress"));
        tc_mask.setCellValueFactory(new PropertyValueFactory<Network, Integer>("mask"));
        tc_size.setCellValueFactory(new PropertyValueFactory<Network, Integer>("sizeAddress"));
        tc_selecSize.setCellValueFactory(new PropertyValueFactory<Network, Integer>("selectedAddress"));
        tc_bc.setCellValueFactory(new PropertyValueFactory<Network, StructIp>("broadcast"));
        tc_ipF.setCellValueFactory(new PropertyValueFactory<Network, StructIp>("minRangeOfIp"));
        tc_ipL.setCellValueFactory(new PropertyValueFactory<Network, StructIp>("maxRangeOfIp"));

        tc_networkSpace.setCellValueFactory(new PropertyValueFactory<Network, StructIp>("networkAdress"));
        tc_maskSpace.setCellValueFactory(new PropertyValueFactory<Network, Integer>("mask"));
        tc_bcSpace.setCellValueFactory(new PropertyValueFactory<Network, StructIp>("broadcast"));
        tc_sizeSpace.setCellValueFactory(new PropertyValueFactory<Network, Integer>("selectedAddress"));
        tc_ipFSpace.setCellValueFactory(new PropertyValueFactory<Network, StructIp>("minRangeOfIp"));
        tc_ipLSpace.setCellValueFactory(new PropertyValueFactory<Network, StructIp>("maxRangeOfIp"));

        tv_log.setDisable(true);
        tv_logSpace.setDisable(true);
        vbox_ledtDock.setDisable(true);
    }




    public void onAction_btn_divide(ActionEvent actionEvent) {

        tv_log.setDisable(true);
        tv_logSpace.setDisable(true);
        vbox_ledtDock.setDisable(true);

        mainErrorEnter = false;

        String[] inputStrung = tf_networksSize.getText().trim().split("\\s+");
        int[] initialSizeNetwork = arrayStringToInt(inputStrung);
        StructIp divParentIP  = inspectionIp(tf_IpOctet1, tf_IpOctet2, tf_IpOctet3, tf_IpOctet4, tf_IpMask);



        if (mainErrorEnter == false){
            inspectionIp(divParentIP);
            if (mainErrorEnter == false){
                prepareDiv(divParentIP, initialSizeNetwork);
                tv_log.setDisable(false);
                tv_logSpace.setDisable(false);
                vbox_ledtDock.setDisable(false);
            }
        } else{
            showError("Ошибка", " Ошибка ввода", "Входные данные имели неверный формат, проверьте правильность данных и повторите попытку ");
        }



    }



    private void prepareDiv(StructIp ip, int[] UnderNetwork){
        initialMask = ip.getMask();

        Calculation calculate = new Calculation(ip, initialMask, UnderNetwork);
        arrayIpResult = calculate.getResult();
        initialNetwork = calculate.getInitialNetwork();
        macpojnt = (int)(pow(2, 32 - initialMask) - 2);

        text_initialNetwork.setText(calculate.getInitialNetwork() + "/" + ip.getMask());
       // text_ipSpace.setText(calculate.getInformationIp(initialNetwork, initialMask,0).getBroadcast() + "");
        spaceNetwork = calculate.getSpaceNetwork();
        text_ipSpace.setText(spaceNetwork.get(spaceNetwork.size() - 1).getNetworkAdress() + "-" + calculate.getInformationIp(initialNetwork, initialMask,0).getBroadcast());

        enterTVandInfo();

    }


    private void inspectionIp(StructIp ip) {
        if ((ip.getOne() == 0)) {
            showError("Ошибка", "Запрещенный ip адрес", ip.getAllString() + " this host on this network");
            mainErrorEnter = true;
        }
        else if ((ip.getOne() == 10) & (ip.getTwo() == 0) & (ip.getThree() == 0) & (ip.getFour() == 0)) {
            showWarning("Ошибка", "Запрещенный ip адрес", ip.getAllString() + " private-Use Networks");

        }
        else if ((ip.getOne() == 100) & (ip.getTwo() == 64)) {
            showWarning("Ошибка", "Запрещенный ip адрес", ip.getAllString() + " shared Address Space");

        }
        else if ((ip.getOne() == 127)) {
            showWarning("Ошибка", "Запрещенный ip адрес", ip.getAllString() + " loopback \n Several protocols have been granted exceptions to this\n" +
                    "                  rule.  For examples, see [RFC4379] and [RFC5884].");

        }
        else if ((ip.getOne() == 192) & (ip.getTwo() == 254)) {
            showWarning("Ошибка", "Запрещенный ip адрес", ip.getAllString() + " link Local");

        }
        else if ((ip.getOne() == 192)) {
            showWarning("Ошибка", "Запрещенный ip адрес", ip.getAllString() + " IETF Protocol Assignments \n Not usable unless by virtue of a more specific reservation");

        }
        else if ((ip.getOne() == 255) & (ip.getTwo() == 255) & (ip.getThree() == 255) & (ip.getFour() == 255)) {
            showError("Ошибка", "Запрещенный ip адрес", ip.getAllString() + " Limited Broadcast");
            mainErrorEnter = true;

        }




    }




    private int[] arrayStringToInt(String[] arraySource){
        int[] arrayR = new int[arraySource.length];
        for (int i = 0; i < arraySource.length; i++){
            try {
                arrayR[i] = Integer.parseInt(arraySource[i]);
            } catch (Throwable  s) {
                mainErrorEnter = true;
            }

        }
        return arrayR;
    }


    // Проверяем ip адрес
    private StructIp inspectionIp(TextField IpOctet1, TextField IpOctet2, TextField IpOctet3, TextField IpOctet4, TextField Mask ){
        boolean flag = false;
        StructIp ip = new StructIp();

        try{
            if ((Integer.parseInt(IpOctet1.getText()) > 0) && (Integer.parseInt(IpOctet1.getText()) <= 255)){
                ip.setOne(Integer.parseInt(IpOctet1.getText()));
            }else {
                flag = true;
            }

            if ((Integer.parseInt(IpOctet2.getText()) >= 0) && (Integer.parseInt(IpOctet2.getText()) <= 255)){
                ip.setTwo(Integer.parseInt(IpOctet2.getText()));
            }else {
                flag = true;
            }

            if ((Integer.parseInt(IpOctet3.getText()) >= 0) && (Integer.parseInt(IpOctet3.getText()) <= 255)){
                ip.setThree(Integer.parseInt(IpOctet3.getText()));
            }else {
                flag = true;
            }

            if ((Integer.parseInt(IpOctet4.getText()) >= 0) && (Integer.parseInt(IpOctet4.getText()) <= 255)){
                ip.setFour(Integer.parseInt(IpOctet4.getText()));
            }else {
                flag = true;
            }

            if ((Integer.parseInt(Mask.getText()) > 0) && (Integer.parseInt(Mask.getText()) < 30)){
                ip.setMask(Integer.parseInt(Mask.getText()));
            }else {
                flag = true;
            }
        } catch (Exception e){
            mainErrorEnter = true;
        }

        if (flag == true){
            mainErrorEnter = true;
        }

        return ip;

    }

    public void onActoin_aggregation(ActionEvent actionEvent) {

        Network agrAdress = (Network)tv_log.getSelectionModel().getSelectedItem();
       // agrAdress.setMask(agrAdress.getMask() - 1);

        int mask = agrAdress.getMask() - 1;

        StructIp qwe = getIntNet(agrAdress.getNetworkAdress(), mask);

        int size = arrayIpResult.size();

        ArrayList<Network> NewArrayIpResult = new ArrayList<>();

        int count = 0;

        for (int i = 0; i < size; i++){
            StructIp tempp = getIntNet(arrayIpResult.get(i).getNetworkAdress(), mask);
            if (qwe.toInt() != tempp.toInt() ){
                NewArrayIpResult.add(count, arrayIpResult.get(i));
            }
        }

        arrayIpResult = NewArrayIpResult;

        Calculation calc = new Calculation();
        arrayIpResult.add(arrayIpResult.size(), calc.getInformationIp(qwe, mask,0));
        enterTVandInfo();

    }

    public void seeInformSelectTv_log(MouseEvent mouseEvent) {
        Network seeInform = (Network)tv_log.getSelectionModel().getSelectedItem();
        text_selectedNetwork.setText("N: " + seeInform.getNetworkAdress().toString() + "/" + seeInform.getMask());
        text_selectedFIp.setText("F: " + seeInform.getMinRangeOfIp().toString());
        text_selectedLIp.setText("L: " + seeInform.getMaxRangeOfIp().toString());
        text_selectedBC.setText("BC: " + seeInform.getBroadcast().toString());
        text_selectedSize.setText("S: " + seeInform.getSelectedAddress());
    }

    public void seeInformSelectTv_logSpace(MouseEvent mouseEvent) {
        Network seeInform = (Network)tv_logSpace.getSelectionModel().getSelectedItem();
        text_selectedNetwork.setText("N: " + seeInform.getNetworkAdress().toString() + "/" + seeInform.getMask());
        text_selectedFIp.setText("F: " + seeInform.getMinRangeOfIp().toString());
        text_selectedLIp.setText("L: " + seeInform.getMaxRangeOfIp().toString());
        text_selectedBC.setText("BC: " + seeInform.getBroadcast().toString());
        text_selectedSize.setText("S: " + seeInform.getSelectedAddress());
    }

    private void enterTVandInfo(){
        int need = 0;

        for (int i = 0; i < arrayIpResult.size(); i++){
            need += arrayIpResult.get(i).getSelectedAddress();
        }

        text_mainSize.setText("Свободно " + (macpojnt - need)  + " из" + macpojnt);
        pb_main.setProgress(((double)need / macpojnt)  );
        System.out.println(((double)need / macpojnt) );

        tv_log.getItems().clear();
        tv_logSpace.getItems().clear();


        for (int i = 0; i < arrayIpResult.size(); i++){
            logTable.addAll(arrayIpResult.get(i));
        }
        for (int i = 0; i < spaceNetwork.size(); i++){
            logTableSpace.addAll(spaceNetwork.get(i));
        }


        tv_log.setItems(logTable);
        tv_logSpace.setItems(logTableSpace);

    }









    public StructIp getIntNet(StructIp initialNetwork, int initialMask10) {
        StructIp maskMy = new StructIp();
        maskMy = Mask10to2(initialMask10);

        initialNetwork = new StructIp(
                initialNetwork.getOne() & maskMy.getOne(),
                initialNetwork.getTwo() & maskMy.getTwo(),
                initialNetwork.getThree() & maskMy.getThree(),
                initialNetwork.getFour() & maskMy.getFour()
        );

        return initialNetwork;
    }

    public StructIp Mask10to2(int Mask10) {
        String act1 = "", act2 = "", act3 = "", act4 = "";

        for (int i = 0; i < 32; i++) {

            if ((i >= 0) & (i < 8)) {
                if (Mask10 > 0) {
                    act1 += '1';
                    Mask10--;
                } else act1 += '0';
            }

            if ((i >= 8) & (i < 16)) {
                if (Mask10 > 0) {
                    act2 += '1';
                    Mask10--;
                } else act2 += '0';
            }

            if ((i >= 16) & (i < 24)) {
                if (Mask10 > 0) {
                    act3 += '1';
                    Mask10--;
                } else act3 += '0';
            }

            if ((i >= 24) & (i < 32)) {
                if (Mask10 > 0) {
                    act4 += '1';
                    Mask10--;
                } else act4 += '0';
            }

        }

        StructIp mask2 = new StructIp(Integer.parseInt(act1, 2), Integer.parseInt(act2, 2), Integer.parseInt(act3, 2), Integer.parseInt(act4, 2));

        return mask2;
    }

    private void showError(String title, String headrText, String textContent){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(textContent);
        alert.setHeaderText(headrText);
        alert.showAndWait();
    }

    private void showWarning(String title, String headrText, String textContent){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(textContent);
        alert.setHeaderText(headrText);
        alert.showAndWait();
    }









}
