package package1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotr on 2015-01-12.
 */
public class GraphInstance extends Graph<String> {

    public GraphInstance(String FileName){
        super();
        List<Integer> list2 = ReadFile(FileName);
        int nodeNumber = list2.get(0);
        for (int i = 1; i <= nodeNumber; i++) {
            this.addVertex(String.valueOf(i));
        }
        for (int i= 1; i<list2.size(); i= i+2)
        {
            this.addEdge(String.valueOf(list2.get(i)), String.valueOf(list2.get(i+1)));
        }
    }

    private List<Integer> ReadFile(String FileName) {
        List<Integer> list = new ArrayList<Integer>();

        BufferedReader br = null;


        try {
            br = new BufferedReader(new FileReader(FileName));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains((" "))){
                String [] numbers = line.split(" ");
                for ( String number : numbers ) {
                    list.add(Integer.parseInt(number));
                }
            }
            else
            {
                list.add(Integer.parseInt(line));
            }
        }
        br.close();}
        catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
