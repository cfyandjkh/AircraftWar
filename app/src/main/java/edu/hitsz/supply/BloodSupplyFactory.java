package edu.hitsz.supply;

public class BloodSupplyFactory extends AbstractSupplyFactory{
    @Override
    public AbstractSupply create(int locationX,int locationY){
        return new BloodSupply(locationX, locationY, 0, 10);
    }
}
