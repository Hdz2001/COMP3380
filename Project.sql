


-- Route Table
CREATE TABLE Route (
    routeNum INT PRIMARY KEY,
    routeName VARCHAR(255) NOT NULL
);

-- Bus Table
CREATE TABLE Bus (
    busNum INT PRIMARY KEY,
    destination VARCHAR(255) NOT NULL
);

-- BusStop Table
CREATE TABLE BusStop (
    stopNum INT PRIMARY KEY,
    location VARCHAR(255) NOT NULL
);

-- Schedule Table
CREATE TABLE Schedule (
    scheName VARCHAR(255) PRIMARY KEY,
    routeNum INT,
    startDate DATE,
    endDate DATE,
    FOREIGN KEY (routeNum) REFERENCES Route(routeNum)
);

-- RouteBus Table
CREATE TABLE RouteBus(
    busNum INT,
    routeNum INT,
    PRIMARY KEY (routeNum, busNum),
    FOREIGN KEY (busNum) REFERENCES Bus(busNum),
    FOREIGN KEY (routeNum) REFERENCES Route(routeNum)
);

-- Activity Table
CREATE TABLE Activity (
    routeNum INT,
    stopNum INT,
    boardingNum INT,
    alightingNum INT,
    dayType VARCHAR(50),
    timePeriod VARCHAR(50),
    PRIMARY KEY (routeNum, stopNum),
    FOREIGN KEY (routeNum) REFERENCES Route(routeNum),
    FOREIGN KEY (stopNum) REFERENCES BusStop(stopNum)
);

-- Arrive Table
CREATE TABLE Arrive (
    busNum INT,
    stopNum INT,
    scheTime DATETIME,
    deviation INT,
    PRIMARY KEY (busNum, stopNum),
    FOREIGN KEY (busNum) REFERENCES Bus(busNum),
    FOREIGN KEY (stopNum) REFERENCES BusStop(stopNum),
    FOREIGN KEY (busNum) REFERENCES Schedule(routeNum)
);

-- PassUp Table
CREATE TABLE PassUp (
    busNum INT,
    stopNum INT,
    time DATETIME,
    type VARCHAR(50),
    PRIMARY KEY (busNum, stopNum),
    FOREIGN KEY (busNum) REFERENCES Bus(busNum),
    FOREIGN KEY (stopNum) REFERENCES BusStop(stopNum)
);
