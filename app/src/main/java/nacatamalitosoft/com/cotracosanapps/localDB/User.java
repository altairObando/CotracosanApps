package nacatamalitosoft.com.cotracosanapps.localDB;

import android.content.ContentValues;

import static nacatamalitosoft.com.cotracosanapps.localDB.UserContract.UserEntry.*;

public class User {
    private String id;
    private String socioId;
    private String usuario;
    private String email;
    private String isLogged;
    private String avatar;

    public User(String id, String socioId, String usuario, String email, String isLogged, String avatar) {
        this.id = id;
        this.socioId = socioId;
        this.usuario = usuario;
        this.email = email;
        this.isLogged = isLogged;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSocioId() {
        return socioId;
    }

    public void setSocioId(String socioId) {
        this.socioId = socioId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsLogged() {
        return isLogged;
    }

    public void setIsLogged(String isLogged) {
        this.isLogged = isLogged;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put(ID, this.id);
        values.put(SOCIOID, this.socioId);
        values.put(USER, this.usuario);
        values.put(EMAIL, this.email);
        values.put(ISLOGGED, this.isLogged);
        values.put(AVATAR, this.avatar);
        return values;
    }
}

