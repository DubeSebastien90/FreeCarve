package UI.Listeners;

import Common.Interfaces.IPanelObserver;

import java.util.ArrayList;
import java.util.List;

public class PanelObservers {
    private List<IPanelObserver> observers = new ArrayList<>();

    public void addObserver(IPanelObserver observer){
        observers.add(observer);
    }

    public void removeObserver(IPanelObserver observer){
        observers.remove(observer);
    }

    public void notifyObservers(){
        for(IPanelObserver observer : observers){
            observer.update();
        }
    }
}
