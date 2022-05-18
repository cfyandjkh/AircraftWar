package edu.hitsz.supply;

public class BombSupplyFactory extends AbstractSupplyFactory{
    @Override
    public AbstractSupply create(int locationX,int locationY){
        return new BombSupply(locationX, locationY, 0, 10);
    }
}
