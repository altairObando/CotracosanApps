package nacatamalitosoft.com.cotracosanapps.localDB;


import android.provider.BaseColumns;

class UserContract {
    static final class UserEntry implements BaseColumns {
        static String TABLE_NAME = "AspNetUsers";
        static String ID = "Id";
        static String SOCIOID = "SocioId";
        static String USER = "Usuario";
        static String EMAIL = "Email";
        static String ROL = "Rol";
        static String AVATAR = "Avatar";
        static String ISLOGGED = "IsLogged";
    }
}

