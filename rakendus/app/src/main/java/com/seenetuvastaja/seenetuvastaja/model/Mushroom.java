package com.seenetuvastaja.seenetuvastaja.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mushroom implements Parcelable {

    /* ID on tehisnärvivõrgu mudeli sildi number. */
    private int ID;
    private String estonianName;
    private String binomialName;
    /*
        poisonClass 0 - söödav
        poisonClass 1 - kupatatult söödav
        poisonClass 2 - mürgine
        poisonClass 3 - alkoholiga mürgine
    */
    private int poisonClass;

    public Mushroom(String estonianName, String binomialName, int poisonClass) {
        this.ID = -1;
        this.estonianName = estonianName;
        this.binomialName = binomialName;
        this.poisonClass = poisonClass;
    }

    protected Mushroom(Parcel in) {
        String[] data = new String[4];
        in.readStringArray(data);
        this.ID = Integer.valueOf(data[0]);
        this.estonianName = data[1];
        this.binomialName = data[2];
        this.poisonClass = Integer.valueOf(data[3]);
    }

    public static final Creator<Mushroom> CREATOR = new Creator<Mushroom>() {
        @Override
        public Mushroom createFromParcel(Parcel in) {
            return new Mushroom(in);
        }

        @Override
        public Mushroom[] newArray(int size) {
            return new Mushroom[size];
        }
    };

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEstonianName() {
        return estonianName;
    }

    public void setEstonianName(String estonianName) {
        this.estonianName = estonianName;
    }

    public String getBinomialName() {
        return binomialName;
    }

    public void setVinomialName(String binomialName) {
        this.binomialName= binomialName;
    }

    public int getPoisonClass() {
        return poisonClass;
    }

    public void setPoisonClass(int poisonClass) {
        this.poisonClass = poisonClass;
    }

    public String getFormattedBinomialName() {
        return binomialName.substring(0, 1).toUpperCase() +
                binomialName.replace("_", " ").substring(1);
    }

    public String getFormattedPoisonClass() {
        switch (poisonClass) {
            case 0: return "Söödav";
            case 1: return "Kupatatult söödav, muidu mürgine";
            case 2: return "Mürgine";
            case 3: return "Koos alkoholiga mürgine";
        }
        return "Teave mürgisuse kohta puudub";
    }

    @Override
    public String toString() {
        return getFormattedBinomialName() + "\n" +
                getEstonianName() + "\n" +
                getFormattedPoisonClass();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                Integer.toString(this.ID),
                this.estonianName,
                this.binomialName,
                Integer.toString(this.poisonClass),
        });
    }
}
