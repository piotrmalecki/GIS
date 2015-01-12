package package1;

/**
 * Created by Piotr on 2015-01-12.
 */
public class State<V> {
    private V info;
    private int color;
    private static int cant = 0;
    private int num;

    public State(V info, int color) {
        this.info = info;
        this.color = color;
        this.num = cant;
        cant++;
    }

    public int getColor(){
        return color;
    }

    public V getInfo(){
        return info;
    }


    @Override
    public String toString() {
        if(info == null )
            return "Start";
        return info.toString() + color + num;
    }
}
