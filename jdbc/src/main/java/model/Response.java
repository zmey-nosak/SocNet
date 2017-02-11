package model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Created by Echetik on 11.02.2017.
 */
@Getter
@Setter
public class Response<T> {
    int limit;
    int offset;
    int total;
    ArrayList<T> objects;
}
