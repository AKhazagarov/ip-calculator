package sample.objects;

/**
 * --Структура информации об Network сети--
 * Включает:
 * Необходимое количество узлов;
 * Выделенное количество узлов;
 * Не занятое количество узлов;
 * Маску сети;
 * Адрес сети;
 * Первый Network адрес;
 * Последний Network арес;
 * Адрес широковещателя;
 **/

public class Network {

    private StructIp networkAdress = new StructIp();    //адрес сети
    private int mask;                                   //маска
    private StructIp minRangeOfIp = new StructIp();     //минимальный выделенный ip адрес
    private StructIp maxRangeOfIp = new StructIp();     //максимальный выделенный ip адрес
    private int sizeAddress;                            //размер
    private int selectedAddress;                        //выделенный размер
    private StructIp broadcast = new StructIp();        //широковещатель

    public Network(StructIp networkAdress, int mask, StructIp minRangeOfIp, StructIp maxRangeOfIp, int sizeAddress, int selectedAddress, StructIp broadcast) {
        this.networkAdress = networkAdress;
        this.mask = mask;
        this.minRangeOfIp = minRangeOfIp;
        this.maxRangeOfIp = maxRangeOfIp;
        this.sizeAddress = sizeAddress;
        this.selectedAddress = selectedAddress;
        this.broadcast = broadcast;
      //  this.name = name;
    }

    @Override
    public String toString() {
        return "Network{" +
                "networkAdress=" + networkAdress +
                ", mask=" + mask +
                ", minRangeOfIp=" + minRangeOfIp +
                ", maxRangeOfIp=" + maxRangeOfIp +
                ", sizeAddress=" + sizeAddress +
                ", selectedAddress=" + selectedAddress +
                ", broadcast=" + broadcast +
                '}';
    }

    public int getSizeAddress() {
        return sizeAddress;
    }

    public void setSizeAddress(int sizeAddress) {
        this.sizeAddress = sizeAddress;
    }

    public int getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(int selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public StructIp getNetworkAdress() {
        return networkAdress;
    }

    public void setNetworkAdress(StructIp networkAdress) {
        this.networkAdress = networkAdress;
    }

    public StructIp getMinRangeOfIp() {
        return minRangeOfIp;
    }

    public void setMinRangeOfIp(StructIp minRangeOfIp) {
        this.minRangeOfIp = minRangeOfIp;
    }

    public StructIp getMaxRangeOfIp() {
        return maxRangeOfIp;
    }

    public void setMaxRangeOfIp(StructIp maxRangeOfIp) {
        this.maxRangeOfIp = maxRangeOfIp;
    }

    public StructIp getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(StructIp broadcast) {
        this.broadcast = broadcast;
    }

    private String name;

    public Network() {
        this.name = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
