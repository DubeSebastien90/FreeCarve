package UI.Widgets;

/**
 * The {@code ComboBitItem} class encapsulates the {@code JComboBox} item object of the {@code BitChoiceBox}
 * It is used to store the index and the name of the item
 *
 * @author Antoine Morin
 * @version 0.1
 * @since 2024-10-30
 */
public class ComboBitItem {
    private int index;
    private String name;

    /**
     * Constructor of the {@code ComboBitItem} class
     * @param index the position of the bit in the bit list
     * @param name the name of the bit
     */
    public ComboBitItem(int index, String name){
        this.index = index;
        this.name = name;
    }

    public int getIndex(){return this.index;}

    public String getName(){return this.name;}

    public String toString(){return this.name;}
}
