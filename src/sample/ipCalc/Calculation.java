package sample.ipCalc;

import sample.objects.Network;
import sample.objects.StructIp;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.pow;

public class Calculation {

    private StructIp initialIp;
    private int initialMask10;
    private int[] initiaCountAndSizeUnderNetwork;
    private StructIp initialNetwork;
    private String binMask;
    private String binInversionMask;
    private String binIp;
    private String binBroadcast;
    private StructIp broadcast;
    private ArrayList<Network> ListIp;
    private StructIp inintiNetwork;

    public ArrayList<Network> getSpaceNetwork() {
        return spaceNetwork;
    }

    ArrayList<Network> spaceNetwork = new ArrayList<>();

    public Calculation() {
    }

    public Calculation(StructIp initialIp, int initialMask, int[] initiaCountAndSizeUnderNetwork) {
        this.initialIp = initialIp;
        this.initialMask10 = initialMask;
        this.initiaCountAndSizeUnderNetwork = initiaCountAndSizeUnderNetwork;
        ListIp = new ArrayList<>(initiaCountAndSizeUnderNetwork.length);

        qSortIpSizeAddress(initiaCountAndSizeUnderNetwork, 0, initiaCountAndSizeUnderNetwork.length - 1);
        voidmirarr(initiaCountAndSizeUnderNetwork);

        //ListIp = new Network[initiaCountAndSizeUnderNetwork.length];


        binMask = maskToBin(initialMask10);
        binInversionMask = inversionMask(binMask);
        binIp = ipToBinIp(this.initialIp);
        binBroadcast = getMetBroadcast(binInversionMask, binIp);
        broadcast = binIpToIp(binBroadcast, initialMask10);

        getIntNet();

        inintiNetwork = initialNetwork;


        for (int i = 0; i < initiaCountAndSizeUnderNetwork.length; i++)
            ListIp.add(i, new Network());

        for (int i = 0; i < initiaCountAndSizeUnderNetwork.length; i++) {
            ListIp.get(i).setName("Подсеть " + (i + 1));
            ListIp.get(i).setSizeAddress(initiaCountAndSizeUnderNetwork[i]);
        }




        System.out.println(initialNetwork.getOne());
        System.out.println(initialNetwork.getTwo());
        System.out.println(initialNetwork.getThree());
        System.out.println(initialNetwork.getFour());

        startCalculate();

    }

    private void startCalculate() {

        int temp = 0;
        int needSizeAll = 0;

        StructIp newNetwork;

        int count = 0;

        for (int i = 0; i < ListIp.size(); i++){
            for (int j = 31; j >= 1; j--){
                if (ListIp.get(i).getSizeAddress() <= pow(2, 32 - j) - 2){
                    needSizeAll += (int)pow(2, 32 - j) -2;
                    break;
                }
            }


        }




        for (int i = initialMask10 + 1; needSizeAll < (int)pow(2, 32 - i) -2; i++){
            binMask = maskToBin(i+1);
            binInversionMask = inversionMask(binMask);
            binIp = ipToBinIp(initialNetwork);
            binBroadcast = getMetBroadcast(binInversionMask, binIp);
            broadcast = binIpToIp(binBroadcast, i+1);
            if (broadcast.getFour() >= 254) {
                newNetwork = new StructIp(broadcast.getOne(), broadcast.getTwo(), broadcast.getThree() + 1, 0, i);
            } else {
                newNetwork = new StructIp(broadcast.getOne(), broadcast.getTwo(), broadcast.getThree(), broadcast.getFour() + 2, i);
            }
            spaceNetwork.add(count, getInformationIp(newNetwork, i + 1 , 0));
        }


        for (int i = 0; i < initiaCountAndSizeUnderNetwork.length; i++) {

            for (int j = 31; j >= 1; j--) {

                if (ListIp.get(i).getSizeAddress() <= pow(2, 32 - j) - 2) {

                    ListIp.set(i, getInformationIp(initialNetwork, j, ListIp.get(i).getSizeAddress()));

                    if (ListIp.get(i).getBroadcast().getFour() >= 254) {
                        initialNetwork = new StructIp(ListIp.get(i).getMaxRangeOfIp().getOne(), ListIp.get(i).getMaxRangeOfIp().getTwo(), ListIp.get(i).getMaxRangeOfIp().getThree() + 1, 0, initialMask10);
                    } else {
                        initialNetwork = new StructIp(ListIp.get(i).getMaxRangeOfIp().getOne(), ListIp.get(i).getMaxRangeOfIp().getTwo(), ListIp.get(i).getMaxRangeOfIp().getThree(), ListIp.get(i).getMaxRangeOfIp().getFour() + 2, initialMask10);
                    }
                    break;
                }

            }
        }

     //   newNetwork = initialNetwork;
         spaceNetwork.add(count, getInformationIp(initialNetwork, ListIp.get(ListIp.size()-1).getMask() , 0));

    }


    public Network getInformationIp(StructIp initialIp, int mask, int temp){
        Network ip = new Network();
        String binMask = maskToBin(mask);
        String binInversionMask = inversionMask(binMask);
        String binIp = ipToBinIp(initialIp);
        String binBroadcast = getMetBroadcast(binInversionMask, binIp);
        StructIp broadcast = binIpToIp(binBroadcast, mask);
        ip.setNetworkAdress(initialIp);
        ip.setMask(mask);
        ip.setSelectedAddress((int) pow(2, 32 - mask) - 2);
        ip.setMinRangeOfIp(new StructIp(initialIp.getOne(), initialIp.getTwo(), initialIp.getThree(), initialIp.getFour() + 1));
        ip.setMaxRangeOfIp(new StructIp(broadcast.getOne(), broadcast.getTwo(), broadcast.getThree(), broadcast.getFour() - 1));
        ip.setBroadcast(new StructIp(broadcast.getOne(), broadcast.getTwo(), broadcast.getThree(), broadcast.getFour()));
        ip.setSizeAddress(temp);
        return ip;
    }


    public void getIntNet() {
        StructIp maskMy = new StructIp();
        maskMy = Mask10to2(initialMask10);

        initialNetwork = new StructIp(
                initialIp.getOne() & maskMy.getOne(),
                initialIp.getTwo() & maskMy.getTwo(),
                initialIp.getThree() & maskMy.getThree(),
                initialIp.getFour() & maskMy.getFour()
        );
    }

    public ArrayList<Network> getResult() {
        return ListIp;
    }

    public StructIp getInitialNetwork() {
        return inintiNetwork;
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

    public String maskToBin(int mask) {
        String bin = "";
        for (int i = 0; i < 32; i++) {
            if (i < mask) {
                bin += "1";
            } else {
                bin += "0";
            }
        }
        return bin;
    }

    private String inversionMask(String mask) {
        String new_mask = "";
        for (int i = 0; i < mask.length(); i++) {
            if (mask.charAt(i) == '0') {
                new_mask += "1";
            } else {
                new_mask += "0";
            }
        }
        return new_mask;
    }

    private String ipToBinIp(StructIp IP) {

        return binar(IP.getOne()) + binar(IP.getTwo()) + binar(IP.getThree()) + binar(IP.getFour());
    }

    private int[] create10() {
        int[] dec = new int[256];
        for (int i = 0; i < 256; i++) {
            dec[i] += i;
        }
        return dec;
    }

    private String[] binCreate() {
        String[] bin = new String[256];
        int check = 256 / 2;
        for (int i = 0; i < 8; i++) {
            int k = 1;
            boolean y = true;
            for (int j = 0; j < 256; j++) {
                if (y) {
                    bin[j] += '0';
                } else {
                    bin[j] += '1';
                }
                k++;
                if (k > check) {
                    k = 1;
                    y = !y;
                }
            }
            check /= 2;
        }
        return bin;
    }

    private String getMetBroadcast(String mask_inversion, String bin_ip) {
        String broadcast = "";

        for (int i = 0; i < mask_inversion.length(); i++) {
            if (mask_inversion.charAt(i) == '0' && bin_ip.charAt(i) == '0') {
                broadcast += '0';
            } else {
                broadcast += '1';
            }

        }

        return broadcast;
    }

    private StructIp binIpToIp(String bin_ip, int mask) {
        int[] dec = create10();
        String[] bin = binCreate();

        String octet1 = "";
        String octet2 = "";
        String octet3 = "";
        String octet4 = "";

        for (int i = 0; i < 8; i++) {
            octet1 += bin_ip.charAt(i);
        }

        for (int i = 8; i < 16; i++) {
            octet2 += bin_ip.charAt(i);
        }

        for (int i = 16; i < 24; i++) {
            octet3 += bin_ip.charAt(i);
        }

        for (int i = 24; i < bin_ip.length(); i++) {
            octet4 += bin_ip.charAt(i);
        }

        int int_octet1 = 0;
        int int_octet2 = 0;
        int int_octet3 = 0;
        int int_octet4 = 0;

        for (int i = 0; i < dec.length; i++) {
            if (bin[i] == octet1) {
                int_octet1 = dec[i];
            }
            if (bin[i] == octet2) {
                int_octet2 = dec[i];
            }
            if (bin[i] == octet3) {
                int_octet3 = dec[i];
            }
            if (bin[i] == octet4) {
                int_octet4 = dec[i];
            }
        }

        return new StructIp(BinToDec(octet1), BinToDec(octet2), BinToDec(octet3), BinToDec(octet4), mask);
    }

    private void qSortIpSizeAddress(int[] array, int l, int r) {
        Random rand = new Random();
        int i = l;
        int j = r;
        int x = array[l + rand.nextInt(r - l + 1)];
        while (i <= j) {
            while (array[i] < x) {
                i++;
            }
            while (array[j] > x) {
                j--;
            }
            if (i <= j) {
                int temp = array[i];
                array[i]= (array[j]);
                array[j] = temp;
                i++;
                j--;
            }
        }
        if (l < j) {
            qSortIpSizeAddress(array, l, j);
        }
        if (i < r) {
            qSortIpSizeAddress(array, i, r);
        }
    }

    private void voidmirarr(int[] x) {
        int a;
        for (int i = 0; i < x.length / 2; i++) {
            a = x[i];
            x[i] = x[x.length - 1 - i];
            x[x.length - 1 - i] = a;

        }
    }

    private String binar(int a) {
        int b;
        String temp = "", reser = "";
        while (a != 0) {
            b = a % 2;
            temp = b + temp;
            a = a / 2;
        }

        if (temp.length() < 8) {
            for (int i = temp.length(); i < 8; i++) {
                reser += "0";
            }
        }

        return reser + temp;
    }

    private int BinToDec(String bin) {
        int res = 0, a = 0, mult = 0;
        char[] symbols = bin.toCharArray();
        for (int len = symbols.length - 1; len >= 0; len--) {
            int temp = 0;
            a = Character.getNumericValue(symbols[len]);
            temp = (int) (a * pow(2, mult));
            mult++;
            res += temp;
        }
        return res;
    }


}