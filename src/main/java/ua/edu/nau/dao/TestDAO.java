package ua.edu.nau.dao;

import ua.edu.nau.model.Test;
import ua.edu.nau.model.User;

import java.util.ArrayList;

public interface TestDAO extends GenericDAO<Test> {
    ArrayList<Test> getUserTests(User owner);

    Boolean isTestBelongsToUser(Integer testId, Integer userId);
}
