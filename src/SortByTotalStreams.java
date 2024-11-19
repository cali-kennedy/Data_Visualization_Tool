import java.util.ArrayList;
import java.util.Comparator;

public class SortByTotalStreams implements DataSorter {
    @Override
    public void sort(ArrayList<DataModel> data) {
        data.sort(Comparator.comparingDouble(DataModel::getTotal_streams).reversed());
    }
}
