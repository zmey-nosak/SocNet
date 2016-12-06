package model;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Created by Echetik on 26.10.2016.
 */
@Value
@AllArgsConstructor
public class BookShop {
    private final int shop_id;
    private final String shop_name;
    private final String urls;
}
