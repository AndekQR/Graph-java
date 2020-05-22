CREATE TABLE NODE(
    id int NOT NULL AUTO_INCREMENT,
    label char(30) NOT NULL ,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE GRAPH(
    id int NOT NULL AUTO_INCREMENT,
    directed boolean NOT NULL,
    name char(30) NOT NULL,
    created timestamp default now(),
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE GRAPHPART(
    id int NOT NULL AUTO_INCREMENT,
    nodeId int NOT NULL,
    graphId int NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (graphId) REFERENCES GRAPH(id),
    FOREIGN KEY (nodeId) REFERENCES NODE(id)
) ENGINE = InnoDB;

CREATE TABLE EDGE(
    id int NOT NULL AUTO_INCREMENT,
    nodeId int NOT NULL,
    graphPartId int NOT NULL,
    weight double NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (nodeId) REFERENCES NODE(id),
    FOREIGN KEY (graphPartId) REFERENCES GRAPHPART(id)
) ENGINE = InnoDB;


