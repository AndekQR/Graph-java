package GraphApp.model.entities;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Date;
import java.util.List;

public class Graph {

    private final BooleanProperty directed;
    private final ListProperty<GraphPart> graphParts;
    private final StringProperty name;
    private int id;
    private Date created;

    public Graph(Boolean directed, String name, GraphPart... graphParts) {
        this.directed=new SimpleBooleanProperty(directed);
        this.name=new SimpleStringProperty(name);
        ObservableList<GraphPart> observableList=FXCollections.observableArrayList(graphParts);
        this.graphParts=new SimpleListProperty<>(observableList);
    }

    public Graph(int id, Boolean directed, List<GraphPart> graphParts, String name, Date created) {
        this.id=id;
        this.directed=new SimpleBooleanProperty(directed);
        ObservableList<GraphPart> observableList=FXCollections.observableArrayList(graphParts);
        this.graphParts=new SimpleListProperty<>(observableList);
        this.name=new SimpleStringProperty(name);
        this.created=created;
    }

    public Graph(String name) {
        this.name=new SimpleStringProperty(name);
        ObservableList<GraphPart> observableList=FXCollections.observableArrayList();
        this.graphParts=new SimpleListProperty<>(observableList);
        this.directed=new SimpleBooleanProperty();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public boolean isDirected() {
        return directed.get();
    }

    public void setDirected(boolean directed) {
        this.directed.set(directed);
    }

    public BooleanProperty directedProperty() {
        return directed;
    }

    public ObservableList<GraphPart> getGraphParts() {
        return graphParts.get();
    }

    public void setGraphParts(ObservableList<GraphPart> graphParts) {
        this.graphParts.set(graphParts);
    }

    public ListProperty<GraphPart> graphPartsProperty() {
        return graphParts;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public String toString() {
        return this.name.get();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created=created;
    }
}
