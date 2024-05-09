/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import Model.DatabaseAccess;

@ManagedBean
public class LoginController {
    private String username;
    private String password;
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    
    public void setPassword(String password) {
        this.password = password;
    }    
 
    
    public void login() throws IOException {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect("error.xhtml");          
        }
        try (Connection conn = DatabaseAccess.getConnection()) {
            String query = "SELECT * FROM user WHERE username = ? AND password = ? ";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, username);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                        externalContext.getSessionMap().put("username", username);
                        externalContext.redirect("success.xhtml");
                    } else {
                        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                        externalContext.redirect("error.xhtml");
                    }
                }
            }
        } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    
}

