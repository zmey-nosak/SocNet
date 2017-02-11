package dao;

/**
 * Created by Echetik on 10.02.2017.
 */
public interface ColumnsDB {
    String USER_SURNAME_COLUMN_DB = "f_name";
    String USER_ID_COLUMN_DB = "user_id";
    String USER_NAME_COLUMN_DB = "i_name";
    String USER_EMAIL_COLUMN_DB = "email";
    String USER_PHOTO_SRC_COLUMN_DB = "photo_src";
    String USER_DATE_OF_BIRTH_COLUMN_DB = "dob";

    String BOOK_ID_COLUMN_DB = "book_id";
    String BOOK_NAME_COLUMN_DB = "book_name";
    String BOOK_IMG_COLUMN_DB = "image_src";

    String COMMUNICATION_USER_ID_FROM_COLUMN_DB = "user_from";
    String COMMUNICATION_MESSAGE_COLUMN_DB = "message";
    String ACTIVE_COLUMN_DB = "active";
    String COMMUNICATION_ID_COLUMN_DB = "communication_id";
    String DATE_COLUMN_DB = "date";
    String COMMUNICATION_OWNER_PHOTO_COLUMN_DB = "own_photo_src";
    String COMMUNICATION_PARTNER_ID_COLUMN_DB = "partner";

    String MESSAGE_ID_COLUMN_DB = "id";

}
