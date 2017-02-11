package dao;

import model.BookShop;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Echetik on 26.10.2016.
 */
@FunctionalInterface
public interface BookShopDao {
    Collection<BookShop> getAll();
}
