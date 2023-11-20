package book.api;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import org.constants.FileNameConstants;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class dataDrivenTests {

    @Test(dataProvider = "ExcelTestData")
    public void DataDrivenTest(Map<String, String> testData) {

        System.out.println(testData.get("FirstName"));

    }

    @DataProvider(name = "ExcelTestData")
    public Object[][] getTestData() {
        Object[][] objectArray = null;
        String query = "select * from Sheet1 where Run = 'yes'";

        Map<String, String> testData = null;
        List<Map<String, String>> testDataList = null;

        Fillo fillo = new Fillo();
        Connection connection = null;
        Recordset recordset = null;

        try {
            connection = fillo.getConnection(FileNameConstants.TEST_DATA);
            recordset = connection.executeQuery(query);

            testDataList = new ArrayList<>();

            while (recordset.next()) {
                testData = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

                for (String field : recordset.getFieldNames()) {
                    testData.put(field, recordset.getField(field));
                }

                testDataList.add(testData);
            }

            objectArray = new Object[testDataList.size()][1];

            for (int i = 0; i < testDataList.size(); i++) {
                objectArray[i][0] = testDataList.get(i);
            }

        } catch (FilloException e) {
            throw new RuntimeException(e);
        }

        return objectArray;
    }

}
