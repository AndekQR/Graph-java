package GraphApp.model.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Node {

    private int id;
    //label jest unikalny w obrębie grafu
    private StringProperty label;

    public Node(String label) {
        this.label = new SimpleStringProperty(label);
    }

    public Node(int id, String label) {
        this.id= id;
        this.label= new SimpleStringProperty(label);
    }

    public Node() {
        this.label = new SimpleStringProperty();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getLabel() {
        return label.get();
    }

    public StringProperty labelProperty() {
        return label;
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", label=" + label.get() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node node=(Node) o;

        return label.equals(node.label);
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }
}
