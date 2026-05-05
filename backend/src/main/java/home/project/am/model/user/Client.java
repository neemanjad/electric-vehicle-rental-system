package home.project.am.model.user;

import jakarta.persistence.*;

@Entity
@Table(name = "CLIENT")
public class Client {

    @Id
    @Column(name = "USER_userName", nullable = false, length = 45)
    private String userName;

    @Column(name = "documentId", length = 20)
    private String documentId;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "telephone", length = 15)
    private String telephone;

    @Lob
    @Column(name = "picture", columnDefinition = "MEDIUMBLOB")
    private byte[] picture;

    @OneToOne
    @JoinColumn(name = "USER_userName", referencedColumnName = "userName")
    private User user; 

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
