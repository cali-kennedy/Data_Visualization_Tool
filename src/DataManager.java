
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private final ArrayList<DataModel> data;
    private final List<DataObserver> observers = new ArrayList<>();
    private DataModel selectedData; // The currently selected DataModel

    public DataManager(ArrayList<DataModel> data) {
        this.data = data;
    }

    // Attach an observer
    public void attach(DataObserver observer) {
        observers.add(observer);
    }

    // Detach an observer
    public void detach(DataObserver observer) {
        observers.remove(observer);
    }

    // Notify all observers of a change in the selected data
    private void notifyObservers() {
        for (DataObserver observer : observers) {
            observer.update();
        }
    }

    // Get all data
    public ArrayList<DataModel> getData() {
        return data;
    }

    // Set the currently selected DataModel and notify observers
    public void setSelectedData(DataModel selectedData) {
        this.selectedData = selectedData;
        notifyObservers();
    }

    // Get the currently selected DataModel
    public DataModel getSelectedData() {
        return selectedData;
    }
}
