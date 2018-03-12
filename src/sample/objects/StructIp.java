package sample.objects;


public class StructIp {

    private int one;
    private int two;
    private int three;
    private int four;

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    private int mask;


    public StructIp() {
    }


    public int toInt(){
        return (one + two + three+ four);
    }

    public StructIp(int one, int two, int three, int four) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
    }

    public StructIp(int one, int two, int three, int four, int mask) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
        this.mask = mask;
    }

    @Override
    public String toString() {
        return  one + "." +
                two + "." +
                three + "." +
                four;
    }

    public String getAllString(){
        return one +"." + two + "." + three + "." + four;
    }


    public void setOne(int one) {
        this.one = one;
    }

    public void setTwo(int two) {
        this.two = two;
    }

    public void setThree(int three) {
        this.three = three;
    }

    public void setFour(int four) {
        this.four = four;
    }

    public int getOne() {
        return one;
    }

    public int getTwo() {
        return two;
    }

    public int getThree() {
        return three;
    }

    public int getFour() {
        return four;
    }


}
