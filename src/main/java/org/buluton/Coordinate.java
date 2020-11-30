package org.buluton;

public class Coordinate {
    Integer _x;
    Integer _y;
    String _symbol;

    Coordinate(Integer x, Integer y, String symbol){
        _x = x;
        _y = y;
        _symbol = symbol;
    }

    public void showData(){
        System.out.print("X=" + _x + "  " + " Y=" + _y + " Symbol="+_symbol);
        System.out.println();
    }
}
