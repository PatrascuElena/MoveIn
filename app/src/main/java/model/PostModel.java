package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;


@Parcel
public class PostModel {

    @SerializedName("postId")
    @Expose
    private String postId;
    @SerializedName("postUserId")
    @Expose
    private String postUserId;
    @SerializedName("post")
    @Expose
    private String post;
    @SerializedName("statusImage")
    @Expose
    private String statusImage;
    @SerializedName("statusTime")
    @Expose
    private String statusTime;
    @SerializedName("likeCount")
    @Expose
    private String likeCount;
    @SerializedName("commentCount")
    @Expose
    private String commentCount;
    @SerializedName("hasComment")
    @Expose
    private String hasComment;
    @SerializedName("privacy")
    @Expose
    private String privacy;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profileUrl")
    @Expose
    private String profileUrl;
    @SerializedName("userToken")
    @Expose
    private String userToken;
    @SerializedName("isLiked")
    @Expose
    private boolean isLiked;
    @SerializedName("userProfile")
    @Expose
    private String userProfile;

    ///

    @SerializedName("suprafata")
    @Expose
    private String suprafata;

    @SerializedName("pret")
    @Expose
    private int pret;
    @SerializedName("adresa")
    @Expose
    private String adresa;
    @SerializedName("descriere")
    @Expose
    private String descriere;
    @SerializedName("oras")
    @Expose
    private String oras;
    @SerializedName("contact")
    @Expose
    private String contact;


    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile= userProfile;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(String postUserId) {
        this.postUserId = postUserId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getStatusImage() {
        return statusImage;
    }

    public void setStatusImage(String statusImage) {
        this.statusImage = statusImage;
    }

    public String getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getHasComment() {
        return hasComment;
    }

    public void setHasComment(String hasComment) {
        this.hasComment = hasComment;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getSuprafata() {
        return suprafata;
    }

    public void setSuprafata(String suprafata) {
        this.suprafata = suprafata;
    }

    public int getPret() {
        return pret;
    }

    public void setPret(int pret) {
        this.pret = pret;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getOras() {
        return oras;
    }

    public void setOras(String oras) {
        this.oras = oras;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}