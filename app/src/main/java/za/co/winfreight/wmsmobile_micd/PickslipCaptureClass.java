package za.co.winfreight.wmsmobile_micd;

import java.io.Serializable;
import java.util.ArrayList;

class PickslipCaptureClass implements Serializable {
    private String userName;
    private String containerNumber;
    private ArrayList<String> items;
    private ArrayList<String> bins;
    private ArrayList<String> quantities;
    private byte[] emptyContainerPic;
    private byte[] fullContainerPic;
    private byte[] closedContainerPic;
    private byte[] sealContainerPic;

    PickslipCaptureClass(String userName) {
        this.userName = userName;
        emptyContainerPic = null;
        fullContainerPic = null;
        closedContainerPic = null;
        sealContainerPic = null;
    }

    private byte[] labelContainerPic;

    String getUserName() {
        return userName;
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    String getContainerNumber() {
        return containerNumber;
    }

    void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
    }

    void pushItem(String item){
        if (this.items == null) this.items = new ArrayList<>();
        this.items.add(item);
    }
    void pushBin(String item){
        if (this.bins == null) this.bins = new ArrayList<>();
        this.bins.add(item);
    }
    void pushQty(String item){
        if (this.quantities == null) this.quantities = new ArrayList<>();
        this.quantities.add(item);
    }

    ArrayList<String> getItems() {
        return items;
    }
    ArrayList<String> getBins() {
        return bins;
    }
    ArrayList<String> getQuantities() {
        return quantities;
    }

    void setItems(ArrayList<String> items) {
        this.items = items;
    }
    void setBins(ArrayList<String> bins) {
        this.bins = bins;
    }
    void setQuantities(ArrayList<String> quantities) {
        this.quantities = quantities;
    }

    byte[] getEmptyContainerPic() {
        return emptyContainerPic;
    }

    void setEmptyContainerPic(byte[] emptyContainerPic) {
        this.emptyContainerPic = emptyContainerPic;
    }

    byte[] getFullContainerPic() {
        return fullContainerPic;
    }

    void setFullContainerPic(byte[] fullContainerPic) {
        this.fullContainerPic = fullContainerPic;
    }

    byte[] getClosedContainerPic() {
        return closedContainerPic;
    }

    void setClosedContainerPic(byte[] closedContainerPic) {
        this.closedContainerPic = closedContainerPic;
    }

    byte[] getSealContainerPic() {
        return sealContainerPic;
    }

    void setSealContainerPic(byte[] sealContainerPic) {
        this.sealContainerPic = sealContainerPic;
    }

    byte[] getLabelContainerPic() {
        return labelContainerPic;
    }

    void setLabelContainerPic(byte[] labelContainerPic) {
        this.labelContainerPic = labelContainerPic;
    }
}
