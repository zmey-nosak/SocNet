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

    default Optional<BookShop> getById(long shop_id) {
        return getAll().stream().filter(bookShop -> bookShop.getShop_id() == shop_id).findAny();
    }
}
