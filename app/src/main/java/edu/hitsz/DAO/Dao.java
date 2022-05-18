package edu.hitsz.DAO;

import edu.hitsz.valueobject.Objectvalue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface Dao {
    void read() throws FileNotFoundException;
    void write(Objectvalue objectvalue) throws IOException;
}
