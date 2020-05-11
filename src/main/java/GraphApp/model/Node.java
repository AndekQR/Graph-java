package GraphApp.model;

import java.util.UUID;

public class Node {

    private final String uid;
    // label jes tylko do wyświetlania, dla nas uid bo nazwy mogą się potwtarzać
    private String label;

    public Node(String label) {
        this.label=label;
        uid=UUID.randomUUID().toString();
    }

    public Node() {
        uid=UUID.randomUUID().toString();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label=label;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return "Node{" +
                "label='" + label + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
