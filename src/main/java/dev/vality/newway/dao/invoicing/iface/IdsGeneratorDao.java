package dev.vality.newway.dao.invoicing.iface;

import dev.vality.newway.exception.DaoException;

import java.util.List;

public interface IdsGeneratorDao {
    List<Long> get(int size) throws DaoException;
}
