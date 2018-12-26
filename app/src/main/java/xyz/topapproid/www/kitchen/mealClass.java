package xyz.topapproid.www.kitchen;

public class mealClass {
    private String mName;
    private String mDes;
    private String mPrice;
    private String mNumberOfPeople;
    private double mRating;
    private int mImage;

    public mealClass(String name, String des, String price, String numberOfPeople, double rating, int image) {
        mName = name;
        mDes = des;
        mPrice = price;
        mNumberOfPeople = numberOfPeople;
        mRating = rating;
        mImage = image;
    }


    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDes() {
        return mDes;
    }

    public void setmDes(String mDes) {
        this.mDes = mDes;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmNumberOfPeople() {
        return mNumberOfPeople;
    }

    public void setmNumberOfPeople(String mNumberOfPeople) {
        this.mNumberOfPeople = mNumberOfPeople;
    }

    public double getmRating() {
        return mRating;
    }

    public void setmRating(double mRating) {
        this.mRating = mRating;
    }

    public int getmImage() {
        return mImage;
    }

    public void setmImage(int mImage) {
        this.mImage = mImage;
    }
}