package nacatamalitosoft.com.cotracosanapps.localDB;


import android.provider.BaseColumns;

public class UserContract {
    public static final class UserEntry implements BaseColumns {
        public static String TABLE_NAME = "AspNetUsers";
        public static String ID = "Id";
        public static String SOCIOID = "SocioId";
        public static String USER = "Usuario";
        public static String EMAIL = "Email";
        public static String AVATAR = "Avatar";
        public static String ISLOGGED = "IsLogged";

    }
}

