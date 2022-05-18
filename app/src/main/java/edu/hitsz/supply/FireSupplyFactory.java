package edu.hitsz.supply;

public class FireSupplyFactory extends AbstractSupplyFactory{
    @Override
    public AbstractSupply create(int locationX,int locationY){
        return new FireSupply(locationX, locationY, 0, 10);
    }
}
