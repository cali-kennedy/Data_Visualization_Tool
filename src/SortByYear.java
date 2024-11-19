import java.util.ArrayList;
import java.util.Comparator;

public class SortByYear implements DataSorter {
    @Override
    public void sort(ArrayList<DataModel> data) {
        data.sort(Comparator.comparingDouble(DataModel::getYear));
    }
}
